package com.emiyaoj.service.domain.dto;

import com.emiyaoj.common.domain.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProblemQueryDTO extends PageDTO {
    private String title;
    private Integer difficulty;
    private String tags;
}
