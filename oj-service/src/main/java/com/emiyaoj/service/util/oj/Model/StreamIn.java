// StreamIn.java
package com.emiyaoj.service.util.oj.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StreamIn extends BaseFile {
    private Boolean streamIn;

    public StreamIn() {
        setType("streamIn");
        this.streamIn = true;
    }

}