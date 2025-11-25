# EmiyaOJ

## 项目结构表

```
EmiyaOJ/
 |--pom.xml
 |--doc/
 |   |--project.md
 |--database/
 |   |--rbac_init_data.sql
 |   |--rbac_schema.sql
 |--oj-common/  # 模块
 |   |--pom.xml
 |   |--src/main/
 |       |--java/  # 源代码
 |       |   |--com/emiyaoj/common/
 |       |       |--config/
 |       |       |   |--CommonConfig.java
 |       |       |   |--RedisConfig.java
 |       |       |--constant/
 |       |       |   |--JwtClaimsConstant.java
 |       |       |   |--ResultEnum.java
 |       |       |--domain/
 |       |       |   |--ResponseResult.java
 |       |       |--exception/
 |       |       |   |--BadRequestException.java
 |       |       |   |--BaseException.java
 |       |       |--handler/
 |       |       |   |--AnonymousAuthenticationHandler.java
 |       |       |   |--CustomerAccessDeniedHandler.java
 |       |       |   |--GlobalExceptionHandler.java
 |       |       |   |--LoginFailureHandler.java
 |       |       |--properties/
 |       |       |   |--JwtPropertis.java
 |       |       |--utils/
 |       |           |--BaseContext.java
 |       |           |--JwtUtil.java
 |       |           |--ObjectUtil.java
 |       |           |--RedisUtil.java
 |       |--resources/
 |           |--META-INF/
 |               |--spring/
 |                   |--org.springframework.boot.autoconfigure.AutoConfiguration.imports
 |--oj-service/  # 模块
     |--src/
         |--pom.xml
         |--main/
         |   |--java/
         |   |   |--com/emiyaoj/service/
         |   |       |--OjApplication.java
         |   |       |--config/
         |   |       |   |--JwtTokenOncePerRequestFilter.java
         |   |       |   |--SecurityConfig.java
         |   |       |--controller/
         |   |       |   |--AuthController.java
         |   |       |   |--HelloController.java
         |   |       |   |--OperationLogController.java
         |   |       |   |--PermissionController.java
         |   |       |   |--ProblemController.java
         |   |       |   |--RoleController.java
         |   |       |   |--TestCaseController.java
         |   |       |   |--UserController.java
         |   |       |--domain/
         |   |       |   |--dto/
         |   |       |   |   |--PermissionQueryDTO.java
         |   |       |   |   |--PermissionSaveDTO.java
         |   |       |   |   |--ProblemSaveDTO.java
         |   |       |   |   |--RoleQueryDTO.java
         |   |       |   |   |--RoleSaveDTO.java
         |   |       |   |   |--TestCaseSaveDTO.java
         |   |       |   |   |--UserLoginDTO.java
         |   |       |   |   |--UserQueryDTO.java
         |   |       |   |   |--UserSaveDTO.java
         |   |       |   |--pojo/
         |   |       |   |   |--OperationLog.java
         |   |       |   |   |--Permission.java
         |   |       |   |   |--Role.java
         |   |       |   |   |--RolePermission.java
         |   |       |   |   |--User.java
         |   |       |   |   |--UserLogin.java
         |   |       |   |   |--UserPermission.java
         |   |       |   |   |--UserRole.java
         |   |       |   |--vo/
         |   |       |       |--PermissionVO.java
         |   |       |       |--ProblemVO.java
         |   |       |       |--RoleVO.java
         |   |       |       |--TestCaseVO.java
         |   |       |       |--UserLoginVO.java
         |   |       |       |--UserVO.java
         |   |       |--mapper/
         |   |       |   |--OperationLogMapper.java
         |   |       |   |--PermissionMapper.java
         |   |       |   |--ProblemMapper.java
         |   |       |   |--RoleMapper.java
         |   |       |   |--RolePermissionMapper.java
         |   |       |   |--TestCaseMapper.java
         |   |       |   |--UserMapper.java
         |   |       |   |--UserPermissionMapper.java
         |   |       |   |--UserRoleMapper.java
         |   |       |--service/
         |   |           |--IOperationLogService.java
         |   |           |--IPermissionService.java
         |   |           |--IProblemService.java
         |   |           |--IRoleService.java
         |   |           |--ITestCaseService.java
         |   |           |--IUserService.java
         |   |           |--impl/
         |   |               |--OperationLogServiceImpl.java
         |   |               |--PermissionServiceImpl.java
         |   |               |--ProblemServiceImpl.java
         |   |               |--RoleServiceImpl.java
         |   |               |--TestCaseServiceImpl.java
         |   |               |--UserDetailsServiceImpl.java
         |   |               |--UserServiceImpl.java
         |   |--resource/
         |       |--application.yaml
         |       |--mapper/
         |           |--OperationLogMapper.xml
         |           |--PermissionMapper.xml
         |           |--RoleMapper.xml
         |           |--RolePermissionMapper.xml
         |           |--UserMapper.xml
         |           |--UserPermissionMapper.xml
         |           |--UserRoleMapper.xml
         |--test/
             |--java/
                 |--UserServiceTest.java
```

## 路由表

| 路由                          | 请求方法 | 描述 |
| ----------------------------- | -------- | ---- |
| `/hello`                      | GET      |      |
| `/user`                       | PUT      |      |
| `/user`                       | POST     |      |
| `/user/batch`                 | DELETE   |      |
| `/user/page`                  | GET      |      |
| `/user/{id}`                  | GET      |      |
| `/user/{id}`                  | DELETE   |      |
| `/user/{id}/has-permission`   | GET      |      |
| `/user/{id}/has-role`         | GET      |      |
| `/user/{id}/permissions`      | GET      |      |
| `/user/{id}/reset-password`   | PUT      |      |
| `/user/{id}/roles`            | PUT      |      |
| `/user/{id}/status`           | PUT      |      |
| `/role`                       | PUT      |      |
| `/role`                       | POST     |      |
| `/role/batch`                 | DELETE   |      |
| `/role/exists`                | GET      |      |
| `/role/list`                  | GET      |      |
| `/role/page`                  | GET      |      |
| `/role/user/{userId}`         | GET      |      |
| `/role/{id}`                  | GET      |      |
| `/role/{id}`                  | DELETE   |      |
| `/role/{id}/permissions`      | GET      |      |
| `/role/{id}/permissions`      | PUT      |      |
| `/role/{id}/status`           | PUT      |      |
| `/auth/login`                 | POST     |      |
| `/auth/logout`                | POST     |      |
| `/permission`                 | PUT      |      |
| `/permission`                 | POST     |      |
| `/permission/batch`           | DELETE   |      |
| `/permission/button/{userId}` | GET      |      |
| `/permission/exists`          | GET      |      |
| `/permission/list`            | GET      |      |
| `/permission/menu/{userId}`   | GET      |      |
| `/permission/role/{roleId}`   | GET      |      |
| `/permission/tree`            | GET      |      |
| `/permission/user/{userId}`   | GET      |      |
| `/permission/{id}`            | GET      |      |
| `/permission/{id}`            | DELETE   |      |
| `/permission/{id}/status`     | PUT      |      |
| `/problem`                    | POST     | 新增题目 |
| `/problem`                    | PUT      | 修改题目 |
| `/problem/batch`              | DELETE   | 批量删除题目 |
| `/problem/page`               | GET      | 分页查询题目 |
| `/problem/{id}`               | GET      | 获取题目详情 |
| `/problem/{id}`               | DELETE   | 删除题目 |
| `/problem/{id}/status`        | PUT      | 修改题目状态 |
| `/testcase`                   | POST     | 新增测试用例 |
| `/testcase`                   | PUT      | 修改测试用例 |
| `/testcase/batch`             | DELETE   | 批量删除测试用例 |
| `/testcase/problem/{problemId}` | GET    | 根据题目ID获取测试用例列表 |
| `/testcase/problem/{problemId}` | DELETE | 根据题目ID删除所有测试用例 |
| `/testcase/{id}`              | GET      | 获取测试用例详情 |
| `/testcase/{id}`              | DELETE   | 删除测试用例 |


