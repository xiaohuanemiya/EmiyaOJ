// LocalFile.java
package com.emiyaoj.service.util.oj.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocalFile extends BaseFile {
    private String src;

    public LocalFile() {
        setType("local");
    }

    public LocalFile(String src) {
        this();
        this.src = src;
    }

}