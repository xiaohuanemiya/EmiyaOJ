package com.emiyaoj.service.domain.sandbox;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Collector {
    private String name;
    private Long max;
    private Boolean pipe;
}
