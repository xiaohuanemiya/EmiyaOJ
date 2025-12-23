// Request.java
package com.emiyaoj.service.util.oj.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request {
    // Getters and Setters
    private String requestId;
    private List<Cmd> cmd;
    private List<PipeMap> pipeMapping;

}