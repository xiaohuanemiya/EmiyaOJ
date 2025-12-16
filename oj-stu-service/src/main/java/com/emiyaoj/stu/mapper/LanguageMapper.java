package com.emiyaoj.stu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emiyaoj.stu.domain.pojo.Language;
import org.apache.ibatis.annotations.Mapper;

/**
 * 编程语言Mapper
 */
@Mapper
public interface LanguageMapper extends BaseMapper<Language> {
}

