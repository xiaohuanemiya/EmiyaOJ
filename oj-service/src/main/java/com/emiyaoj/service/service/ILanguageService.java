package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.service.domain.pojo.Language;
import com.emiyaoj.service.domain.vo.oj.LanguageVO;

import java.util.List;

/**
 * 语言服务接口
 */
public interface ILanguageService extends IService<Language> {
    
    /**
     * 获取所有启用的语言
     * 
     * @return 语言列表
     */
    List<Language> listEnabled();
    
    /**
     * 获取所有启用的语言VO列表
     * 
     * @return 语言VO列表
     */
    List<LanguageVO> listEnabledVO();
    
    /**
     * 根据名称和版本查询语言
     * 
     * @param name 语言名称
     * @param version 版本
     * @return 语言信息
     */
    Language getByNameAndVersion(String name, String version);
}
