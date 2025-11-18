package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.pojo.Language;
import com.emiyaoj.service.domain.vo.oj.LanguageVO;
import com.emiyaoj.service.service.ILanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 语言控制器
 */
@Tag(name = "语言管理")
@RestController
@RequestMapping("/language")
@RequiredArgsConstructor
public class LanguageController {
    
    private final ILanguageService languageService;
    
    @Operation(summary = "获取所有支持的语言")
    @GetMapping("/list")
    public ResponseResult<List<LanguageVO>> listLanguages() {
        List<Language> languages = languageService.listEnabled();
        List<LanguageVO> languageVOS = languages.stream()
                .map(lang -> LanguageVO.builder()
                        .id(lang.getId())
                        .name(lang.getName())
                        .version(lang.getVersion())
                        .sourceFileExt(lang.getSourceFileExt())
                        .isCompiled(lang.getIsCompiled())
                        .build())
                .collect(Collectors.toList());
        return ResponseResult.success(languageVOS);
    }
}
