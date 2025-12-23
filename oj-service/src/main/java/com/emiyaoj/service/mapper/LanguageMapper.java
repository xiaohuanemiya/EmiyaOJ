package com.emiyaoj.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emiyaoj.service.domain.pojo.Language;
import org.apache.ibatis.annotations.Mapper;

/**
 * 编程语言Mapper
 */
@Mapper
public interface LanguageMapper extends BaseMapper<Language> {
}
