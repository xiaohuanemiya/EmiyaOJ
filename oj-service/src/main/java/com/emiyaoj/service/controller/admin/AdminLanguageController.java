package com.emiyaoj.service.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.admin.LanguageDTO;
import com.emiyaoj.service.domain.pojo.Language;
import com.emiyaoj.service.service.ILanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 语言管理控制器（管理端）
 */
@Tag(name = "语言管理（管理端）")
@RestController
@RequestMapping("/admin/language")
@RequiredArgsConstructor
public class AdminLanguageController {
    
    private final ILanguageService languageService;
    
    @Operation(summary = "创建语言")
    @PostMapping
    public ResponseResult<Long> createLanguage(@RequestBody LanguageDTO languageDTO) {
        Language language = new Language();
        BeanUtils.copyProperties(languageDTO, language);
        
        boolean success = languageService.save(language);
        if (success) {
            return ResponseResult.success(language.getId());
        }
        return ResponseResult.fail("创建失败");
    }
    
    @Operation(summary = "更新语言")
    @PutMapping("/{id}")
    public ResponseResult<Void> updateLanguage(
            @PathVariable Long id,
            @RequestBody LanguageDTO languageDTO) {
        
        Language language = languageService.getById(id);
        if (language == null) {
            return ResponseResult.fail("语言不存在");
        }
        
        BeanUtils.copyProperties(languageDTO, language);
        language.setId(id);
        
        boolean success = languageService.updateById(language);
        if (success) {
            return ResponseResult.success();
        }
        return ResponseResult.fail("更新失败");
    }
    
    @Operation(summary = "删除语言")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteLanguage(@PathVariable Long id) {
        boolean success = languageService.removeById(id);
        if (success) {
            return ResponseResult.success();
        }
        return ResponseResult.fail("删除失败");
    }
    
    @Operation(summary = "获取语言详情")
    @GetMapping("/{id}")
    public ResponseResult<Language> getLanguage(@PathVariable Long id) {
        Language language = languageService.getById(id);
        if (language == null) {
            return ResponseResult.fail("语言不存在");
        }
        return ResponseResult.success(language);
    }
    
    @Operation(summary = "获取所有语言列表")
    @GetMapping("/list")
    public ResponseResult<List<Language>> listLanguages(
            @Parameter(description = "状态（可选，0-禁用，1-启用）") @RequestParam(required = false) Integer status) {
        
        LambdaQueryWrapper<Language> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Language::getStatus, status);
        }
        wrapper.orderByDesc(Language::getCreateTime);
        
        List<Language> languages = languageService.list(wrapper);
        return ResponseResult.success(languages);
    }
}
