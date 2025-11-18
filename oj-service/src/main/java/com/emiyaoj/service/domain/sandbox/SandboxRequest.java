package com.emiyaoj.service.domain.sandbox;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SandboxRequest {
    private String requestId;
    private List<Cmd> cmd;
    private List<PipeMap> pipeMapping;
}
