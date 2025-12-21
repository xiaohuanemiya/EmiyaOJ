package com.emiyaoj.service.service;

import com.emiyaoj.service.domain.dto.ProblemSaveDTO;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.vo.ProblemVO;
import com.emiyaoj.service.mapper.ProblemMapper;
import com.emiyaoj.service.service.impl.ProblemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Problem Service Unit Tests
 */
@ExtendWith(MockitoExtension.class)
public class ProblemServiceTest {

    @Mock
    private ProblemMapper problemMapper;

    private ProblemServiceImpl problemService;

    private Problem sampleProblem;

    @BeforeEach
    void setUp() {
        problemService = new ProblemServiceImpl();
        // Set the baseMapper through reflection for ServiceImpl
        ReflectionTestUtils.setField(problemService, "baseMapper", problemMapper);
        
        sampleProblem = new Problem();
        sampleProblem.setId(1L);
        sampleProblem.setTitle("A+B Problem");
        sampleProblem.setDescription("Calculate the sum of two integers.");
        sampleProblem.setInputDescription("Two integers a and b");
        sampleProblem.setOutputDescription("Output a+b");
        sampleProblem.setSampleInput("1 2");
        sampleProblem.setSampleOutput("3");
        sampleProblem.setDifficulty(1);
        sampleProblem.setTimeLimit(1000);
        sampleProblem.setMemoryLimit(256);
        sampleProblem.setStackLimit(128);
        sampleProblem.setStatus(1);
        sampleProblem.setDeleted(0);
        sampleProblem.setAcceptCount(0);
        sampleProblem.setSubmitCount(0);
        sampleProblem.setCreateTime(LocalDateTime.now());
        sampleProblem.setUpdateTime(LocalDateTime.now());
    }

    @Test
    void testSelectProblemById_Found() {
        // Arrange
        when(problemMapper.selectById(1L)).thenReturn(sampleProblem);

        // Act
        ProblemVO result = problemService.selectProblemById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("A+B Problem", result.getTitle());
        assertEquals("Calculate the sum of two integers.", result.getDescription());
        verify(problemMapper, times(1)).selectById(1L);
    }

    @Test
    void testSelectProblemById_NotFound() {
        // Arrange
        when(problemMapper.selectById(999L)).thenReturn(null);

        // Act
        ProblemVO result = problemService.selectProblemById(999L);

        // Assert
        assertNull(result);
        verify(problemMapper, times(1)).selectById(999L);
    }

    @Test
    void testSelectProblemById_Deleted() {
        // Arrange
        Problem deletedProblem = new Problem();
        deletedProblem.setId(2L);
        deletedProblem.setDeleted(1);
        when(problemMapper.selectById(2L)).thenReturn(deletedProblem);

        // Act
        ProblemVO result = problemService.selectProblemById(2L);

        // Assert
        assertNull(result);
        verify(problemMapper, times(1)).selectById(2L);
    }

    @Test
    void testSaveProblem_ValidDTO() {
        // Arrange
        ProblemSaveDTO saveDTO = new ProblemSaveDTO();
        saveDTO.setTitle("Test Problem");
        saveDTO.setDescription("Test Description");
        saveDTO.setTimeLimit(1000);
        saveDTO.setMemoryLimit(256);
        
        when(problemMapper.insert(any(Problem.class))).thenReturn(1);

        // Act
        boolean result = problemService.saveProblem(saveDTO);

        // Assert
        assertTrue(result);
        verify(problemMapper, times(1)).insert(any(Problem.class));
    }

    @Test
    void testUpdateProblem_NullId() {
        // Arrange
        ProblemSaveDTO saveDTO = new ProblemSaveDTO();
        saveDTO.setTitle("Test Problem");
        saveDTO.setDescription("Test Description");
        saveDTO.setTimeLimit(1000);
        saveDTO.setMemoryLimit(256);
        // saveDTO.setId is not set

        // Act
        boolean result = problemService.updateProblem(saveDTO);

        // Assert
        assertFalse(result);
        verify(problemMapper, never()).updateById(any(Problem.class));
    }

    @Test
    void testUpdateProblem_NotFound() {
        // Arrange
        ProblemSaveDTO saveDTO = new ProblemSaveDTO();
        saveDTO.setId(999L);
        saveDTO.setTitle("Test Problem");
        saveDTO.setDescription("Test Description");
        saveDTO.setTimeLimit(1000);
        saveDTO.setMemoryLimit(256);
        
        when(problemMapper.selectById(999L)).thenReturn(null);

        // Act
        boolean result = problemService.updateProblem(saveDTO);

        // Assert
        assertFalse(result);
        verify(problemMapper, never()).updateById(any(Problem.class));
    }

    @Test
    void testIncrementAcceptCount() {
        // Arrange
        when(problemMapper.selectById(1L)).thenReturn(sampleProblem);
        when(problemMapper.updateById(any(Problem.class))).thenReturn(1);

        // Act
        problemService.incrementAcceptCount(1L);

        // Assert
        assertEquals(1, sampleProblem.getAcceptCount());
        verify(problemMapper, times(1)).updateById(sampleProblem);
    }

    @Test
    void testIncrementSubmitCount() {
        // Arrange
        when(problemMapper.selectById(1L)).thenReturn(sampleProblem);
        when(problemMapper.updateById(any(Problem.class))).thenReturn(1);

        // Act
        problemService.incrementSubmitCount(1L);

        // Assert
        assertEquals(1, sampleProblem.getSubmitCount());
        verify(problemMapper, times(1)).updateById(sampleProblem);
    }

    @Test
    void testIncrementAcceptCount_NotFound() {
        // Arrange
        when(problemMapper.selectById(999L)).thenReturn(null);

        // Act
        problemService.incrementAcceptCount(999L);

        // Assert
        verify(problemMapper, never()).updateById(any(Problem.class));
    }
}
