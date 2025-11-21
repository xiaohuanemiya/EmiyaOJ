package com.emiyaoj.service.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.pojo.Language;
import com.emiyaoj.service.service.ILanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 语言Controller
 */
@Tag(name = "语言管理")
@RestController
@RequestMapping("/language")
public class LanguageController {

    @Autowired
    private ILanguageService languageService;

    @Operation(summary = "获取所有可用语言")
    @GetMapping("/list")
    public ResponseResult<List<Language>> listLanguages() {
        LambdaQueryWrapper<Language> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Language::getStatus, 1);
        wrapper.orderByAsc(Language::getId);
        List<Language> languages = languageService.list(wrapper);
        return ResponseResult.success(languages);
    }

    @Operation(summary = "获取语言详情")
    @GetMapping("/{id}")
    public ResponseResult<Language> getLanguageDetail(@PathVariable Long id) {
        Language language = languageService.getById(id);
        if (language == null) {
            return ResponseResult.fail("语言不存在");
        }
        return ResponseResult.success(language);
    }
}
