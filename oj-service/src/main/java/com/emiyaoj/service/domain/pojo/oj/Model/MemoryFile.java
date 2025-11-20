// MemoryFile.java
package com.emiyaoj.service.domain.pojo.oj.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.nio.charset.StandardCharsets;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemoryFile extends BaseFile {
    private String content;
    private byte[] contentBytes;

    public MemoryFile() {
        setType("memory");
    }

    public MemoryFile(String content) {
        this();
        this.content = content;
    }

    public MemoryFile(byte[] contentBytes) {
        this();
        this.contentBytes = contentBytes;
    }

    public String getContent() { 
        if (content != null) {
            return content;
        } else if (contentBytes != null) {
            return new String(contentBytes, StandardCharsets.UTF_8);
        }
        return null;
    }
    
    public void setContent(String content) { 
        this.content = content;
        this.contentBytes = null;
    }

    public byte[] getContentBytes() { 
        if (contentBytes != null) {
            return contentBytes;
        } else if (content != null) {
            return content.getBytes(StandardCharsets.UTF_8);
        }
        return null;
    }
    
    public void setContentBytes(byte[] contentBytes) { 
        this.contentBytes = contentBytes;
        this.content = null;
    }
}