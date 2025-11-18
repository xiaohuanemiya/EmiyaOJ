package com.emiyaoj.service.domain.sandbox;

import lombok.Data;

@Data
public class PipeMap {
    private PipeIndex in;
    private PipeIndex out;
    private Boolean proxy;
    private String name;
    private Long max;
}
