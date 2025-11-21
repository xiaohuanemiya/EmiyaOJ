import com.emiyaoj.service.OjApplication;
import com.emiyaoj.service.util.oj.Model.Request;
import com.emiyaoj.service.util.oj.Model.Result;
import com.emiyaoj.service.util.GoJudgeRequestBuilder;
import com.emiyaoj.service.util.GoJudgeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = OjApplication.class)

public class OJUtilTest {
    @Autowired
    private GoJudgeService goJudgeService;



    @Test
    public void goJudgeTest(){
        // 测试GoJudgeService的judge方法 使用c++
        //源代码
        String sourceCode = "#include <iostream>\n" +
                "int main() {\n" +
                "    int a, b;\n" +
                "    std::cin >> a >> b;\n" +
                "    std::cout << a + b << std::endl;\n" +
                "    return 0;\n" +
                "}\n";
        // 测试输入
        String testInput = "100 3\n";
        // 文件名
        String compileOutputName="main";

        Request compileRequest = GoJudgeRequestBuilder.buildCppCompileRequest(sourceCode, compileOutputName);
        List<Result> compileResults = goJudgeService.execute(compileRequest);
        Result compileResult = compileResults.get(0);
        if (!"Accepted".equals(compileResult.getStatus().getValue())) {
            return;
        }
        // 2. 获取编译后的文件ID
        String executableFileId = compileResult.getFileIds().get(compileOutputName);
        // 设置运行请求参数
        Long cpuLimit = 1L; // CPU时间限制，单位秒
        Long memoryLimit = 256L; // 内存限制，单位MB
        // 3. 运行程序
        Request runRequest = GoJudgeRequestBuilder.buildRunRequest(
                executableFileId, testInput, compileOutputName, cpuLimit, memoryLimit);
        List<Result> runResults = goJudgeService.execute(runRequest);

        // 4. 清理缓存文件
        goJudgeService.deleteFile(executableFileId);

        // 5. 验证结果
        System.out.println(runResults.getFirst().getFiles().get("stdout"));

    }

    //测试死循环
    @Test
    public void goJudgeInfiniteLoopTest(){
        String sourceCode = "#include <iostream>\n" +
                "int main() {\n" +
                "    while(true) {}\n" +
                "    return 0;\n" +
                "}\n";
        String compileOutputName="main";

        Request compileRequest = GoJudgeRequestBuilder.buildCppCompileRequest(sourceCode, compileOutputName);
        List<Result> compileResults = goJudgeService.execute(compileRequest);
        Result compileResult = compileResults.getFirst();
        if (!"Accepted".equals(compileResult.getStatus().getValue())) {
            return;
        }
        String executableFileId = compileResult.getFileIds().get(compileOutputName);
        Long cpuLimit = 1L; // CPU时间限制，单位秒
        Long memoryLimit = 256L; // 内存限制，单位MB
        Request runRequest = GoJudgeRequestBuilder.buildRunRequest(
                executableFileId, "", compileOutputName, cpuLimit, memoryLimit);
        List<Result> runResults = goJudgeService.execute(runRequest);
        goJudgeService.deleteFile(executableFileId);
        System.out.println(runResults.getFirst().getStatus().getValue());
    }
}
