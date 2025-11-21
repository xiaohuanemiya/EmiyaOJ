// GoJudgeRequestBuilder.java
package com.emiyaoj.service.util;

import com.emiyaoj.service.util.oj.Model.*;
import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class GoJudgeRequestBuilder {

    /**
     *
     * @param sourceCode 源代码
     * @param outputName 编译后输出文件名
     * @return Request 交给 GoJudgeService 处理
     */
    public static Request buildCppCompileRequest(String sourceCode, String outputName) {
        Request request = new Request();
        
        Cmd cmd = new Cmd();
        UUID uuid = UUID.randomUUID();
        //通过UUID生成唯一的输出文件名，防止并发时文件名冲突
        String s = uuid.toString() + ".cpp";
        cmd.setArgs(Arrays.asList("/usr/bin/g++", s, "-o", outputName));
        cmd.setEnv(Arrays.asList("PATH=/usr/bin:/bin"));
        
        // 设置文件描述符
        List<BaseFile> files = new ArrayList<>();
        files.add(new MemoryFile("")); // stdin
        files.add(new Collector("stdout", 10240)); // stdout
        files.add(new Collector("stderr", 10240)); // stderr
        cmd.setFiles(files);
        
        // 资源限制
        cmd.setCpuLimit(10_000_000_000L); // 10秒
        cmd.setMemoryLimit(100 * 1024 * 1024L); // 100MB
        cmd.setProcLimit(50);
        
        // 复制输入文件
        Map<String, BaseFile> copyIn = new HashMap<>();
        copyIn.put(s, new MemoryFile(sourceCode));
        cmd.setCopyIn(copyIn);
        
        // 设置输出
        cmd.setCopyOut(Arrays.asList("stdout", "stderr"));
        cmd.setCopyOutCached(Arrays.asList(outputName));
        
        request.setCmd(Collections.singletonList(cmd));
        return request;
    }

    /**
     * 构建C编译请求
     * @param sourceCode 源代码
     * @param outputName 编译后输出文件名
     * @return Request 交给 GoJudgeService 处理
     */
    public static Request buildCCompileRequest(String sourceCode, String outputName) {
        Request request = new Request();
        
        Cmd cmd = new Cmd();
        UUID uuid = UUID.randomUUID();
        //通过UUID生成唯一的输出文件名，防止并发时文件名冲突
        String s = uuid.toString() + ".c";
        cmd.setArgs(Arrays.asList("/usr/bin/gcc", s, "-o", outputName));
        cmd.setEnv(Arrays.asList("PATH=/usr/bin:/bin"));
        
        // 设置文件描述符
        List<BaseFile> files = new ArrayList<>();
        files.add(new MemoryFile("")); // stdin
        files.add(new Collector("stdout", 10240)); // stdout
        files.add(new Collector("stderr", 10240)); // stderr
        cmd.setFiles(files);
        
        // 资源限制
        cmd.setCpuLimit(10_000_000_000L); // 10秒
        cmd.setMemoryLimit(100 * 1024 * 1024L); // 100MB
        cmd.setProcLimit(50);
        
        // 复制输入文件
        Map<String, BaseFile> copyIn = new HashMap<>();
        copyIn.put(s, new MemoryFile(sourceCode));
        cmd.setCopyIn(copyIn);
        
        // 设置输出
        cmd.setCopyOut(Arrays.asList("stdout", "stderr"));
        cmd.setCopyOutCached(Arrays.asList(outputName));
        
        request.setCmd(Collections.singletonList(cmd));
        return request;
    }

    /**
     *
     * @param executableFileId 缓存的可执行文件ID
     * @param input          程序输入
     * @param executableName 可执行文件名
     * @param cpuLimit   CPU限制，单位：秒
     * @param memoryLimit 内存限制，单位：MB
     * @return Request 交给 GoJudgeService 处理
     */
    public static Request buildRunRequest(String executableFileId, String input, String executableName, Long cpuLimit, Long memoryLimit) {
        Request request = new Request();
        
        Cmd cmd = new Cmd();
        cmd.setArgs(Arrays.asList(executableName));
        cmd.setEnv(Arrays.asList("PATH=/usr/bin:/bin"));
        
        // 设置文件描述符
        List<BaseFile> files = new ArrayList<>();
        files.add(new MemoryFile(input)); // stdin
        files.add(new Collector("stdout", 10240)); // stdout
        files.add(new Collector("stderr", 10240)); // stderr
        cmd.setFiles(files);
        
        // 资源限制
        cmd.setCpuLimit(cpuLimit*1000000000L); // 秒
        cmd.setMemoryLimit(memoryLimit * 1024 * 1024L); // MB
        cmd.setProcLimit(50);
        
        // 使用缓存的二进制文件
        Map<String, BaseFile> copyIn = new HashMap<>();
        copyIn.put(executableName, new PreparedFile(executableFileId));
        cmd.setCopyIn(copyIn);
        
        // 设置输出
        cmd.setCopyOut(Arrays.asList("stdout", "stderr"));
        
        request.setCmd(Collections.singletonList(cmd));
        return request;
    }
    
    /**
     * 构建交互式程序请求（多个程序通过管道通信）
     */
    public static Request buildInteractiveRequest(String program1Code, String program2Code) {
        Request request = new Request();
        List<Cmd> cmds = new ArrayList<>();
        
        // 第一个程序（输出程序）
        Cmd cmd1 = new Cmd();
        cmd1.setArgs(Arrays.asList("/bin/cat", "1"));
        cmd1.setEnv(Arrays.asList("PATH=/usr/bin:/bin"));
        
        List<BaseFile> files1 = new ArrayList<>();
        files1.add(new MemoryFile("")); // stdin
        files1.add(new StreamOut()); // stdout - 流式输出到管道
        files1.add(new Collector("stderr", 10240)); // stderr
        cmd1.setFiles(files1);
        
        cmd1.setCpuLimit(1_000_000_000L);
        cmd1.setMemoryLimit(1 * 1024 * 1024L);
        cmd1.setProcLimit(50);
        
        Map<String, BaseFile> copyIn1 = new HashMap<>();
        copyIn1.put("1", new MemoryFile("TEST 1"));
        cmd1.setCopyIn(copyIn1);
        cmd1.setCopyOut(Arrays.asList("stderr"));
        
        cmds.add(cmd1);
        
        // 第二个程序（输入程序）
        Cmd cmd2 = new Cmd();
        cmd2.setArgs(Arrays.asList("/bin/cat"));
        cmd2.setEnv(Arrays.asList("PATH=/usr/bin:/bin"));
        
        List<BaseFile> files2 = new ArrayList<>();
        files2.add(new StreamIn()); // stdin - 从管道输入
        files2.add(new Collector("stdout", 10240)); // stdout
        files2.add(new Collector("stderr", 10240)); // stderr
        cmd2.setFiles(files2);
        
        cmd2.setCpuLimit(1_000_000_000L);
        cmd2.setMemoryLimit(1 * 1024 * 1024L);
        cmd2.setProcLimit(50);
        cmd2.setCopyOut(Arrays.asList("stdout", "stderr"));
        
        cmds.add(cmd2);
        
        // 管道映射
        List<PipeMap> pipeMapping = new ArrayList<>();
        PipeMap pipe = new PipeMap();
        pipe.setIn(new PipeIndex(0, 1)); // 第一个程序的stdout
        pipe.setOut(new PipeIndex(1, 0)); // 第二个程序的stdin
        pipeMapping.add(pipe);
        
        request.setCmd(cmds);
        request.setPipeMapping(pipeMapping);
        return request;
    }
    
    /**
     * 构建带CPU限制的死循环测试请求
     */
    public static Request buildInfiniteLoopRequest() {
        Request request = new Request();
        
        Cmd cmd = new Cmd();
        cmd.setArgs(Arrays.asList("/usr/bin/python3", "1.py"));
        cmd.setEnv(Arrays.asList("PATH=/usr/bin:/bin"));
        
        List<BaseFile> files = new ArrayList<>();
        files.add(new MemoryFile("")); // stdin
        files.add(new Collector("stdout", 10240)); // stdout
        files.add(new Collector("stderr", 10240)); // stderr
        cmd.setFiles(files);
        
        // 资源限制
        cmd.setCpuLimit(3_000_000_000L); // 3秒CPU时间
        cmd.setClockLimit(4_000_000_000L); // 4秒挂钟时间
        cmd.setMemoryLimit(100 * 1024 * 1024L); // 100MB
        cmd.setProcLimit(50);
        cmd.setCpuRateLimit(10); // 10% CPU使用率
        
        // 复制死循环代码
        Map<String, BaseFile> copyIn = new HashMap<>();
        copyIn.put("1.py", new MemoryFile("while True:\n pass"));
        cmd.setCopyIn(copyIn);
        
        // 设置输出
        cmd.setCopyOut(Arrays.asList("stdout", "stderr"));
        
        request.setCmd(Collections.singletonList(cmd));
        return request;
    }
}