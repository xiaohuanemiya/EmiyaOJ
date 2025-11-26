// PipeIndex.java
package com.emiyaoj.service.util.oj.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipeIndex {
    // Getters and Setters
    private Integer index;
    private Integer fd;

    // Constructors
    public PipeIndex() {}

    public PipeIndex(Integer index, Integer fd) {
        this.index = index;
        this.fd = fd;
    }

}