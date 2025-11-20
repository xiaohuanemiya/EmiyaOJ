// PreparedFile.java
package com.emiyaoj.service.domain.pojo.oj.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreparedFile extends BaseFile {
    private String fileId;

    public PreparedFile() {
        setType("prepared");
    }

    public PreparedFile(String fileId) {
        this();
        this.fileId = fileId;
    }

}