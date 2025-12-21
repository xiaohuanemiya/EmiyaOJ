package com.emiyaoj.service.service;

import com.emiyaoj.service.domain.dto.TestCaseSaveDTO;
import com.emiyaoj.service.domain.pojo.TestCase;
import com.emiyaoj.service.domain.vo.TestCaseVO;
import com.emiyaoj.service.mapper.TestCaseMapper;
import com.emiyaoj.service.service.impl.TestCaseServiceImpl;
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
 * TestCase Service Unit Tests
 */
@ExtendWith(MockitoExtension.class)
public class TestCaseServiceTest {

    @Mock
    private TestCaseMapper testCaseMapper;

    private TestCaseServiceImpl testCaseService;

    private TestCase sampleTestCase;

    @BeforeEach
    void setUp() {
        testCaseService = new TestCaseServiceImpl();
        // Set the baseMapper through reflection for ServiceImpl
        ReflectionTestUtils.setField(testCaseService, "baseMapper", testCaseMapper);
        
        sampleTestCase = new TestCase();
        sampleTestCase.setId(1L);
        sampleTestCase.setProblemId(1L);
        sampleTestCase.setInput("1 2");
        sampleTestCase.setOutput("3");
        sampleTestCase.setIsSample(1);
        sampleTestCase.setScore(10);
        sampleTestCase.setSortOrder(1);
        sampleTestCase.setDeleted(0);
        sampleTestCase.setCreateTime(LocalDateTime.now());
        sampleTestCase.setUpdateTime(LocalDateTime.now());
    }

    @Test
    void testSelectTestCaseById_Found() {
        // Arrange
        when(testCaseMapper.selectById(1L)).thenReturn(sampleTestCase);

        // Act
        TestCaseVO result = testCaseService.selectTestCaseById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getProblemId());
        assertEquals("1 2", result.getInput());
        assertEquals("3", result.getOutput());
        assertEquals(1, result.getIsSample());
        verify(testCaseMapper, times(1)).selectById(1L);
    }

    @Test
    void testSelectTestCaseById_NotFound() {
        // Arrange
        when(testCaseMapper.selectById(999L)).thenReturn(null);

        // Act
        TestCaseVO result = testCaseService.selectTestCaseById(999L);

        // Assert
        assertNull(result);
        verify(testCaseMapper, times(1)).selectById(999L);
    }

    @Test
    void testSelectTestCaseById_Deleted() {
        // Arrange
        TestCase deletedTestCase = new TestCase();
        deletedTestCase.setId(2L);
        deletedTestCase.setDeleted(1);
        when(testCaseMapper.selectById(2L)).thenReturn(deletedTestCase);

        // Act
        TestCaseVO result = testCaseService.selectTestCaseById(2L);

        // Assert
        assertNull(result);
        verify(testCaseMapper, times(1)).selectById(2L);
    }

    @Test
    void testSaveTestCase_ValidDTO() {
        // Arrange
        TestCaseSaveDTO saveDTO = new TestCaseSaveDTO();
        saveDTO.setProblemId(1L);
        saveDTO.setInput("5 10");
        saveDTO.setOutput("15");
        saveDTO.setIsSample(0);
        saveDTO.setScore(10);
        saveDTO.setSortOrder(2);
        
        when(testCaseMapper.insert(any(TestCase.class))).thenReturn(1);

        // Act
        boolean result = testCaseService.saveTestCase(saveDTO);

        // Assert
        assertTrue(result);
        verify(testCaseMapper, times(1)).insert(any(TestCase.class));
    }

    @Test
    void testSaveTestCase_WithDefaults() {
        // Arrange
        TestCaseSaveDTO saveDTO = new TestCaseSaveDTO();
        saveDTO.setProblemId(1L);
        saveDTO.setInput("5 10");
        saveDTO.setOutput("15");
        // Not setting optional fields to test defaults
        
        when(testCaseMapper.insert(any(TestCase.class))).thenReturn(1);

        // Act
        boolean result = testCaseService.saveTestCase(saveDTO);

        // Assert
        assertTrue(result);
        verify(testCaseMapper, times(1)).insert(any(TestCase.class));
    }

    @Test
    void testUpdateTestCase_NullId() {
        // Arrange
        TestCaseSaveDTO saveDTO = new TestCaseSaveDTO();
        saveDTO.setProblemId(1L);
        saveDTO.setInput("5 10");
        saveDTO.setOutput("15");
        // saveDTO.setId is not set

        // Act
        boolean result = testCaseService.updateTestCase(saveDTO);

        // Assert
        assertFalse(result);
        verify(testCaseMapper, never()).updateById(any(TestCase.class));
    }

    @Test
    void testUpdateTestCase_NotFound() {
        // Arrange
        TestCaseSaveDTO saveDTO = new TestCaseSaveDTO();
        saveDTO.setId(999L);
        saveDTO.setProblemId(1L);
        saveDTO.setInput("5 10");
        saveDTO.setOutput("15");
        
        when(testCaseMapper.selectById(999L)).thenReturn(null);

        // Act
        boolean result = testCaseService.updateTestCase(saveDTO);

        // Assert
        assertFalse(result);
        verify(testCaseMapper, never()).updateById(any(TestCase.class));
    }

    @Test
    void testUpdateTestCase_Deleted() {
        // Arrange
        TestCaseSaveDTO saveDTO = new TestCaseSaveDTO();
        saveDTO.setId(2L);
        saveDTO.setProblemId(1L);
        saveDTO.setInput("5 10");
        saveDTO.setOutput("15");
        
        TestCase deletedTestCase = new TestCase();
        deletedTestCase.setId(2L);
        deletedTestCase.setDeleted(1);
        when(testCaseMapper.selectById(2L)).thenReturn(deletedTestCase);

        // Act
        boolean result = testCaseService.updateTestCase(saveDTO);

        // Assert
        assertFalse(result);
        verify(testCaseMapper, never()).updateById(any(TestCase.class));
    }

    @Test
    void testUpdateTestCase_Valid() {
        // Arrange
        TestCaseSaveDTO saveDTO = new TestCaseSaveDTO();
        saveDTO.setId(1L);
        saveDTO.setProblemId(1L);
        saveDTO.setInput("5 10");
        saveDTO.setOutput("15");
        
        when(testCaseMapper.selectById(1L)).thenReturn(sampleTestCase);
        when(testCaseMapper.updateById(any(TestCase.class))).thenReturn(1);

        // Act
        boolean result = testCaseService.updateTestCase(saveDTO);

        // Assert
        assertTrue(result);
        verify(testCaseMapper, times(1)).updateById(any(TestCase.class));
    }
}
