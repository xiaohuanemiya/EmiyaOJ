package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.service.domain.pojo.Language;
import com.emiyaoj.service.mapper.LanguageMapper;
import com.emiyaoj.service.service.ILanguageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 语言服务实现类
 */
@Service
public class LanguageServiceImpl extends ServiceImpl<LanguageMapper, Language> implements ILanguageService {
    
    @Override
    public List<Language> listEnabled() {
        LambdaQueryWrapper<Language> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Language::getStatus, 1);
        return list(wrapper);
    }
    
    @Override
    public Language getByNameAndVersion(String name, String version) {
        LambdaQueryWrapper<Language> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Language::getName, name)
                .eq(Language::getVersion, version)
                .eq(Language::getStatus, 1);
        return getOne(wrapper);
    }
}
