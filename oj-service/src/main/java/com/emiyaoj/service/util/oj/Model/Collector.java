// Collector.java
package com.emiyaoj.service.util.oj.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Collector extends BaseFile {
    // Getters and Setters
    private String name;
    private Integer max;
    private Boolean pipe;

    public Collector() {
        setType("collector");
    }

    public Collector(String name, Integer max) {
        this();
        this.name = name;
        this.max = max;
    }

    public Collector(String name, Integer max, Boolean pipe) {
        this(name, max);
        this.pipe = pipe;
    }

}