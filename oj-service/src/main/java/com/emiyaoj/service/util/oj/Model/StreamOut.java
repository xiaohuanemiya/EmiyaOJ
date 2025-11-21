// StreamOut.java
package com.emiyaoj.service.util.oj.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StreamOut extends BaseFile {
    private Boolean streamOut;

    public StreamOut() {
        setType("streamOut");
        this.streamOut = true;
    }

}