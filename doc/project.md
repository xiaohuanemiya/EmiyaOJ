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
         |   |       |   |--RoleController.java
         |   |       |   |--UserController.java
         |   |       |--domain/
         |   |       |   |--dto/
         |   |       |   |   |--PermissionQueryDTO.java
         |   |       |   |   |--PermissionSaveDTO.java
         |   |       |   |   |--RoleQueryDTO.java
         |   |       |   |   |--RoleSaveDTO.java
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
         |   |       |       |--RoleVO.java
         |   |       |       |--UserLoginVO.java
         |   |       |       |--UserVO.java
         |   |       |--mapper/
         |   |       |   |--OperationLogMapper.java
         |   |       |   |--PermissionMapper.java
         |   |       |   |--RoleMapper.java
         |   |       |   |--RolePermissionMapper.java
         |   |       |   |--UserMapper.java
         |   |       |   |--UserPermissionMapper.java
         |   |       |   |--UserRoleMapper.java
         |   |       |--service/
         |   |           |--IOperationLogService.java
         |   |           |--IPermissionService.java
         |   |           |--IRoleService.java
         |   |           |--IUserService.java
         |   |           |--impl/
         |   |               |--OperationLogServiceImpl.java
         |   |               |--PermissionServiceImpl.java
         |   |               |--RoleServiceImpl.java
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

