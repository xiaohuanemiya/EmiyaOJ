package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.service.domain.pojo.Language;
import com.emiyaoj.service.mapper.LanguageMapper;
import com.emiyaoj.service.service.ILanguageService;
import org.springframework.stereotype.Service;

/**
 * 语言Service实现
 */
@Service
public class LanguageServiceImpl extends ServiceImpl<LanguageMapper, Language> implements ILanguageService {
}
