// PipeMap.java
package com.emiyaoj.service.domain.pojo.oj.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipeMap {
    // Getters and Setters
    private PipeIndex in;
    private PipeIndex out;
    private Boolean proxy;
    private String name;
    private Integer max;

    // Constructors
    public PipeMap() {}

    public PipeMap(PipeIndex in, PipeIndex out) {
        this.in = in;
        this.out = out;
    }

}