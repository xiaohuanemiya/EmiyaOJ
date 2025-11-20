// Symlink.java
package com.emiyaoj.service.domain.pojo.oj.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Symlink extends BaseFile {
    private String symlink;

    public Symlink() {
        setType("symlink");
    }

    public Symlink(String symlink) {
        this();
        this.symlink = symlink;
    }

}