# EmiyaOJ 前端 API 接口文档

> 适用于 Vue 3.5 前端开发
> 
> 文档版本：v1.0
> 
> 最后更新：2025-10-21

## 目录

- [1. 基础说明](#1-基础说明)
- [2. 认证模块](#2-认证模块)
- [3. 用户管理](#3-用户管理)
- [4. 角色管理](#4-角色管理)
- [5. 权限管理](#5-权限管理)
- [6. 数据结构说明](#6-数据结构说明)

---

## 1. 基础说明

### 1.1 接口基础地址

```
开发环境: http://localhost:8080
生产环境: https://api.emiyaoj.com
```

### 1.2 统一响应格式

所有接口均返回统一的 JSON 格式：

```typescript
interface ResponseResult<T> {
  code: number;      // 状态码：200-成功，500-失败
  msg: string;       // 响应消息
  data: T;           // 响应数据
}
```

**成功响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": { ... }
}
```

**失败响应示例：**
```json
{
  "code": 500,
  "msg": "操作失败",
  "data": null
}
```

### 1.3 分页参数

**请求参数（PageDTO）：**
```typescript
interface PageDTO {
  pageNo: number;      // 页码，从1开始
  pageSize: number;    // 每页大小
  sortBy?: string;     // 排序字段
  isAsc?: boolean;     // 是否升序
}
```

**响应数据（PageVO）：**
```typescript
interface PageVO<T> {
  total: number;       // 总记录数
  pages: number;       // 总页数
  list: T[];          // 数据列表
}
```

### 1.4 请求头

需要认证的接口需携带以下请求头：

```
Authorization: <token>
```

---

## 2. 认证模块

### 2.1 用户登录

**接口地址：** `POST /auth/login`

**是否需要认证：** 否

**请求参数：**

```typescript
interface UserLoginDTO {
  username: string;  // 用户名，5-20位字母数字下划线
  password: string;  // 密码，5-16位字母数字下划线
}
```

**请求示例：**
```json
{
  "username": "admin",
  "password": "123456"
}
```

**响应数据：**
```typescript
interface UserLoginVO {
  id: number;        // 用户ID
  username: string;  // 用户名
  name: string;      // 姓名
  token: string;     // JWT令牌
}
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "id": 1,
    "username": "admin",
    "name": "管理员",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

**Vue 3.5 调用示例：**
```typescript
import { ref } from 'vue';
import axios from 'axios';

const login = async (username: string, password: string) => {
  try {
    const response = await axios.post('/auth/login', {
      username,
      password
    });
    
    if (response.data.code === 200) {
      // 保存 token
      localStorage.setItem('token', response.data.data.token);
      return response.data.data;
    }
  } catch (error) {
    console.error('登录失败:', error);
    throw error;
  }
};
```

---

### 2.2 退出登录

**接口地址：** `POST /auth/logout`

**是否需要认证：** 是

**请求参数：** 无

**响应数据：** 无

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

**Vue 3.5 调用示例：**
```typescript
const logout = async () => {
  try {
    const response = await axios.post('/auth/logout', {}, {
      headers: {
        'Authorization': localStorage.getItem('token')
      }
    });
    
    if (response.data.code === 200) {
      // 清除本地 token
      localStorage.removeItem('token');
      // 跳转到登录页
      router.push('/login');
    }
  } catch (error) {
    console.error('退出登录失败:', error);
  }
};
```

---

## 3. 用户管理

### 3.1 分页查询用户列表

**接口地址：** `POST /user/page`

**是否需要认证：** 是

**所需权限：** `USER.LIST`

**请求参数：**
```typescript
interface UserPageDTO extends PageDTO {
  // 可扩展查询条件
}
```

**请求示例：**
```json
{
  "pageNo": 1,
  "pageSize": 10,
  "sortBy": "create_time",
  "isAsc": false
}
```

**响应数据：**
```typescript
interface UserVO {
  id: number;              // 用户ID
  username: string;        // 用户名
  nickname: string;        // 昵称
  email: string;           // 邮箱
  phone: string;           // 手机号
  avatar: string;          // 头像URL
  status: number;          // 状态：0-禁用，1-启用
  statusDesc: string;      // 状态描述
  createTime: string;      // 创建时间
  updateTime: string;      // 更新时间
}

type Response = ResponseResult<PageVO<UserVO>>;
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "total": 100,
    "pages": 10,
    "list": [
      {
        "id": 1,
        "username": "admin",
        "nickname": "管理员",
        "email": "admin@example.com",
        "phone": "13800138000",
        "avatar": "https://example.com/avatar.jpg",
        "status": 1,
        "statusDesc": "启用",
        "createTime": "2025-01-01 10:00:00",
        "updateTime": "2025-01-10 15:30:00"
      }
    ]
  }
}
```

---

### 3.2 根据ID查询用户

**接口地址：** `GET /user/{id}`

**是否需要认证：** 是

**所需权限：** `USER.LIST`

**路径参数：**
- `id`: 用户ID

**响应数据：** `ResponseResult<UserVO>`

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "管理员",
    "email": "admin@example.com",
    "phone": "13800138000",
    "avatar": "https://example.com/avatar.jpg",
    "status": 1
  }
}
```

---

### 3.3 新增用户

**接口地址：** `POST /user`

**是否需要认证：** 是

**所需权限：** `USER.ADD`

**请求参数：**
```typescript
interface UserSaveDTO {
  username: string;        // 用户名，3-50个字符，必填
  password: string;        // 密码，6-50个字符，新增时必填
  nickname?: string;       // 昵称，最多50个字符
  email?: string;          // 邮箱，需符合邮箱格式
  phone?: string;          // 手机号
  avatar?: string;         // 头像URL
  status?: number;         // 状态：0-禁用，1-启用
}
```

**请求示例：**
```json
{
  "username": "zhangsan",
  "password": "123456",
  "nickname": "张三",
  "email": "zhangsan@example.com",
  "phone": "13800138001",
  "status": 1
}
```

**响应数据：** `ResponseResult<void>`

---

### 3.4 修改用户

**接口地址：** `PUT /user`

**是否需要认证：** 是

**所需权限：** `USER.EDIT`

**请求参数：**
```typescript
interface UserSaveDTO {
  id: number;              // 用户ID，修改时必填
  username: string;        // 用户名
  password?: string;       // 密码，不修改时可不传
  nickname?: string;       // 昵称
  email?: string;          // 邮箱
  phone?: string;          // 手机号
  avatar?: string;         // 头像URL
  status?: number;         // 状态
}
```

**请求示例：**
```json
{
  "id": 1,
  "username": "zhangsan",
  "nickname": "张三（修改）",
  "email": "zhangsan_new@example.com"
}
```

---

### 3.5 删除用户

**接口地址：** `DELETE /user/{id}`

**是否需要认证：** 是

**所需权限：** `USER.DELETE`

**路径参数：**
- `id`: 用户ID

**响应数据：** `ResponseResult<void>`

---

### 3.6 批量删除用户

**接口地址：** `DELETE /user/batch`

**是否需要认证：** 是

**所需权限：** `USER.DELETE`

**请求参数：**
```typescript
type Request = number[];  // 用户ID数组
```

**请求示例：**
```json
[1, 2, 3, 4, 5]
```

---

### 3.7 重置用户密码

**接口地址：** `PUT /user/{id}/reset-password`

**是否需要认证：** 是

**所需权限：** `USER.RESET.PASSWORD`

**路径参数：**
- `id`: 用户ID

**查询参数：**
- `newPassword`: 新密码

**请求示例：**
```
PUT /user/1/reset-password?newPassword=newpass123
```

---

### 3.8 修改用户状态

**接口地址：** `PUT /user/{id}/status`

**是否需要认证：** 是

**所需权限：** `USER.EDIT`

**路径参数：**
- `id`: 用户ID

**查询参数：**
- `status`: 状态值（0-禁用，1-启用）

**请求示例：**
```
PUT /user/1/status?status=0
```

---

### 3.9 为用户分配角色

**接口地址：** `PUT /user/{id}/roles`

**是否需要认证：** 是

**所需权限：** `USER.EDIT`

**路径参数：**
- `id`: 用户ID

**请求参数：**
```typescript
type Request = number[];  // 角色ID数组
```

**请求示例：**
```json
[1, 2, 3]
```

---

### 3.10 获取用户权限列表

**接口地址：** `GET /user/{id}/permissions`

**是否需要认证：** 是

**所需权限：** `USER.LIST`

**路径参数：**
- `id`: 用户ID

**响应数据：** `ResponseResult<string[]>`

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": ["USER.LIST", "USER.ADD", "USER.EDIT", "ROLE.LIST"]
}
```

---

### 3.11 检查用户是否拥有指定权限

**接口地址：** `GET /user/{id}/has-permission`

**是否需要认证：** 是

**所需权限：** `USER.LIST`

**路径参数：**
- `id`: 用户ID

**查询参数：**
- `permissionCode`: 权限编码

**请求示例：**
```
GET /user/1/has-permission?permissionCode=USER.ADD
```

**响应数据：** `ResponseResult<boolean>`

---

### 3.12 检查用户是否拥有指定角色

**接口地址：** `GET /user/{id}/has-role`

**是否需要认证：** 是

**所需权限：** `USER.LIST`

**路径参数：**
- `id`: 用户ID

**查询参数：**
- `roleCode`: 角色编码

**请求示例：**
```
GET /user/1/has-role?roleCode=ADMIN
```

**响应数据：** `ResponseResult<boolean>`

---

## 4. 角色管理

### 4.1 分页查询角色列表

**接口地址：** `GET /role/page`

**是否需要认证：** 是

**所需权限：** `ROLE_LIST`

**请求参数：**
```typescript
interface RoleQueryDTO {
  roleCode?: string;         // 角色编码
  roleName?: string;         // 角色名称
  status?: number;           // 状态：0-禁用，1-启用
  createTimeStart?: string;  // 创建时间开始
  createTimeEnd?: string;    // 创建时间结束
  pageNum?: number;          // 页码，默认1
  pageSize?: number;         // 每页大小，默认10
}
```

**查询参数示例：**
```
GET /role/page?roleName=管理员&status=1&pageNum=1&pageSize=10
```

**响应数据：**
```typescript
interface RoleVO {
  id: number;              // 角色ID
  roleCode: string;        // 角色编码
  roleName: string;        // 角色名称
  description: string;     // 角色描述
  status: number;          // 状态：0-禁用，1-启用
  statusDesc: string;      // 状态描述
  createTime: string;      // 创建时间
  updateTime: string;      // 更新时间
}

// 注意：此接口使用 MyBatis-Plus 的 Page，不是 PageVO
interface Page<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

type Response = ResponseResult<Page<RoleVO>>;
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "records": [
      {
        "id": 1,
        "roleCode": "ADMIN",
        "roleName": "管理员",
        "description": "系统管理员角色",
        "status": 1,
        "statusDesc": "启用",
        "createTime": "2025-01-01 10:00:00",
        "updateTime": "2025-01-10 15:30:00"
      }
    ],
    "total": 50,
    "size": 10,
    "current": 1,
    "pages": 5
  }
}
```

---

### 4.2 查询所有角色列表

**接口地址：** `GET /role/list`

**是否需要认证：** 是

**所需权限：** `ROLE_LIST`

**响应数据：** `ResponseResult<RoleVO[]>`

---

### 4.3 根据ID查询角色

**接口地址：** `GET /role/{id}`

**是否需要认证：** 是

**所需权限：** `ROLE_LIST`

**路径参数：**
- `id`: 角色ID

**响应数据：** `ResponseResult<RoleVO>`

---

### 4.4 新增角色

**接口地址：** `POST /role`

**是否需要认证：** 是

**所需权限：** `ROLE_ADD`

**请求参数：**
```typescript
interface RoleSaveDTO {
  roleCode: string;         // 角色编码，最多50个字符，必填
  roleName: string;         // 角色名称，最多50个字符，必填
  description?: string;     // 角色描述，最多255个字符
  status?: number;          // 状态：0-禁用，1-启用
  permissionIds?: number[]; // 权限ID列表
}
```

**请求示例：**
```json
{
  "roleCode": "MANAGER",
  "roleName": "经理",
  "description": "部门经理角色",
  "status": 1,
  "permissionIds": [1, 2, 3, 4]
}
```

---

### 4.5 修改角色

**接口地址：** `PUT /role`

**是否需要认证：** 是

**所需权限：** `ROLE_EDIT`

**请求参数：**
```typescript
interface RoleSaveDTO {
  id: number;               // 角色ID，修改时必填
  roleCode: string;         // 角色编码
  roleName: string;         // 角色名称
  description?: string;     // 角色描述
  status?: number;          // 状态
  permissionIds?: number[]; // 权限ID列表
}
```

---

### 4.6 删除角色

**接口地址：** `DELETE /role/{id}`

**是否需要认证：** 是

**所需权限：** `ROLE_DELETE`

**路径参数：**
- `id`: 角色ID

---

### 4.7 批量删除角色

**接口地址：** `DELETE /role/batch`

**是否需要认证：** 是

**所需权限：** `ROLE_DELETE`

**请求参数：** `number[]` (角色ID数组)

---

### 4.8 修改角色状态

**接口地址：** `PUT /role/{id}/status`

**是否需要认证：** 是

**所需权限：** `ROLE_EDIT`

**路径参数：**
- `id`: 角色ID

**查询参数：**
- `status`: 状态值（0-禁用，1-启用）

---

### 4.9 为角色分配权限

**接口地址：** `PUT /role/{id}/permissions`

**是否需要认证：** 是

**所需权限：** `ROLE_ASSIGN`

**路径参数：**
- `id`: 角色ID

**请求参数：** `number[]` (权限ID数组)

**请求示例：**
```json
[1, 2, 3, 4, 5]
```

---

### 4.10 获取角色权限列表

**接口地址：** `GET /role/{id}/permissions`

**是否需要认证：** 是

**所需权限：** `ROLE_LIST`

**路径参数：**
- `id`: 角色ID

**响应数据：** `ResponseResult<string[]>` (权限编码列表)

---

### 4.11 检查角色编码是否存在

**接口地址：** `GET /role/exists`

**是否需要认证：** 是

**所需权限：** `ROLE_LIST`

**查询参数：**
- `roleCode`: 角色编码
- `excludeId`: 排除的角色ID（可选，用于编辑时排除自身）

**请求示例：**
```
GET /role/exists?roleCode=ADMIN&excludeId=1
```

**响应数据：** `ResponseResult<boolean>`

---

### 4.12 根据用户ID查询角色列表

**接口地址：** `GET /role/user/{userId}`

**是否需要认证：** 是

**所需权限：** `ROLE_LIST`

**路径参数：**
- `userId`: 用户ID

**响应数据：** `ResponseResult<RoleVO[]>`

---

## 5. 权限管理

### 5.1 查询权限列表

**接口地址：** `GET /permission/list`

**是否需要认证：** 是

**所需权限：** `PERMISSION_LIST`

**请求参数：**
```typescript
interface PermissionQueryDTO {
  permissionCode?: string;   // 权限编码
  permissionName?: string;   // 权限名称
  permissionType?: number;   // 权限类型：1-菜单，2-按钮，3-接口
  status?: number;           // 状态：0-禁用，1-启用
  parentId?: number;         // 父权限ID
}
```

**查询参数示例：**
```
GET /permission/list?permissionType=1&status=1
```

**响应数据：**
```typescript
interface PermissionVO {
  id: number;                  // 权限ID
  parentId: number;            // 父权限ID
  permissionCode: string;      // 权限编码
  permissionName: string;      // 权限名称
  permissionType: number;      // 权限类型：1-菜单，2-按钮，3-接口
  permissionTypeDesc: string;  // 权限类型描述
  path: string;                // 路由路径
  component: string;           // 组件路径
  icon: string;                // 图标
  sortOrder: number;           // 排序
  status: number;              // 状态：0-禁用，1-启用
  statusDesc: string;          // 状态描述
  createTime: string;          // 创建时间
  updateTime: string;          // 更新时间
  children?: PermissionVO[];   // 子权限列表（树形结构时使用）
}

type Response = ResponseResult<PermissionVO[]>;
```

---

### 5.2 查询权限树

**接口地址：** `GET /permission/tree`

**是否需要认证：** 是

**所需权限：** `PERMISSION_LIST`

**请求参数：** 同权限列表查询参数

**响应数据：** `ResponseResult<PermissionVO[]>` (树形结构)

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": [
    {
      "id": 1,
      "parentId": 0,
      "permissionCode": "SYSTEM",
      "permissionName": "系统管理",
      "permissionType": 1,
      "path": "/system",
      "icon": "system",
      "sortOrder": 1,
      "status": 1,
      "children": [
        {
          "id": 2,
          "parentId": 1,
          "permissionCode": "USER_MANAGE",
          "permissionName": "用户管理",
          "permissionType": 1,
          "path": "/system/user",
          "component": "system/user/index",
          "icon": "user",
          "sortOrder": 1,
          "status": 1,
          "children": [
            {
              "id": 3,
              "parentId": 2,
              "permissionCode": "USER.LIST",
              "permissionName": "用户列表",
              "permissionType": 2,
              "sortOrder": 1,
              "status": 1
            }
          ]
        }
      ]
    }
  ]
}
```

---

### 5.3 根据ID查询权限

**接口地址：** `GET /permission/{id}`

**是否需要认证：** 是

**所需权限：** `PERMISSION_LIST`

**路径参数：**
- `id`: 权限ID

**响应数据：** `ResponseResult<PermissionVO>`

---

### 5.4 新增权限

**接口地址：** `POST /permission`

**是否需要认证：** 是

**所需权限：** `PERMISSION_ADD`

**请求参数：**
```typescript
interface PermissionSaveDTO {
  parentId?: number;         // 父权限ID
  permissionCode: string;    // 权限编码，最多100个字符，必填
  permissionName: string;    // 权限名称，最多50个字符，必填
  permissionType: number;    // 权限类型：1-菜单，2-按钮，3-接口，必填
  path?: string;             // 路由路径
  component?: string;        // 组件路径
  icon?: string;             // 图标
  sortOrder?: number;        // 排序
  status?: number;           // 状态：0-禁用，1-启用
}
```

**请求示例：**
```json
{
  "parentId": 1,
  "permissionCode": "USER.ADD",
  "permissionName": "新增用户",
  "permissionType": 2,
  "sortOrder": 1,
  "status": 1
}
```

---

### 5.5 修改权限

**接口地址：** `PUT /permission`

**是否需要认证：** 是

**所需权限：** `PERMISSION_EDIT`

**请求参数：**
```typescript
interface PermissionSaveDTO {
  id: number;                // 权限ID，修改时必填
  parentId?: number;         // 父权限ID
  permissionCode: string;    // 权限编码
  permissionName: string;    // 权限名称
  permissionType: number;    // 权限类型
  path?: string;             // 路由路径
  component?: string;        // 组件路径
  icon?: string;             // 图标
  sortOrder?: number;        // 排序
  status?: number;           // 状态
}
```

---

### 5.6 删除权限

**接口地址：** `DELETE /permission/{id}`

**是否需要认证：** 是

**所需权限：** `PERMISSION_DELETE`

**路径参数：**
- `id`: 权限ID

---

### 5.7 批量删除权限

**接口地址：** `DELETE /permission/batch`

**是否需要认证：** 是

**所需权限：** `PERMISSION_DELETE`

**请求参数：** `number[]` (权限ID数组)

---

### 5.8 修改权限状态

**接口地址：** `PUT /permission/{id}/status`

**是否需要认证：** 是

**所需权限：** `PERMISSION_EDIT`

**路径参数：**
- `id`: 权限ID

**查询参数：**
- `status`: 状态值（0-禁用，1-启用）

---

### 5.9 检查权限编码是否存在

**接口地址：** `GET /permission/exists`

**是否需要认证：** 是

**所需权限：** `PERMISSION_LIST`

**查询参数：**
- `permissionCode`: 权限编码
- `excludeId`: 排除的权限ID（可选）

**响应数据：** `ResponseResult<boolean>`

---

### 5.10 根据角色ID查询权限列表

**接口地址：** `GET /permission/role/{roleId}`

**是否需要认证：** 是

**所需权限：** `PERMISSION_LIST`

**路径参数：**
- `roleId`: 角色ID

**响应数据：** `ResponseResult<PermissionVO[]>`

---

### 5.11 根据用户ID查询权限列表

**接口地址：** `GET /permission/user/{userId}`

**是否需要认证：** 是

**所需权限：** `PERMISSION_LIST`

**路径参数：**
- `userId`: 用户ID

**响应数据：** `ResponseResult<PermissionVO[]>`

---

### 5.12 获取用户菜单权限树

**接口地址：** `GET /permission/menu/{userId}`

**是否需要认证：** 是

**所需权限：** 无（用户只能查询自己的菜单）

**路径参数：**
- `userId`: 用户ID

**响应数据：** `ResponseResult<PermissionVO[]>` (树形结构，仅包含菜单类型的权限)

**说明：** 此接口用于生成前端动态路由菜单

---

### 5.13 获取用户按钮权限列表

**接口地址：** `GET /permission/button/{userId}`

**是否需要认证：** 是

**所需权限：** 无

**路径参数：**
- `userId`: 用户ID

**响应数据：** `ResponseResult<string[]>` (按钮权限编码列表)

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": ["USER.ADD", "USER.EDIT", "USER.DELETE", "ROLE.ADD"]
}
```

**说明：** 此接口用于前端按钮权限控制

---

## 6. 数据结构说明

### 6.1 TypeScript 类型定义文件

建议在 Vue 3.5 项目中创建以下类型定义文件：

**`src/types/api.ts`**

```typescript
// ========== 通用类型 ==========

export interface ResponseResult<T = any> {
  code: number;
  msg: string;
  data: T;
}

export interface PageDTO {
  pageNo: number;
  pageSize: number;
  sortBy?: string;
  isAsc?: boolean;
}

export interface PageVO<T> {
  total: number;
  pages: number;
  list: T[];
}

export interface Page<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

// ========== 用户相关 ==========

export interface UserLoginDTO {
  username: string;
  password: string;
}

export interface UserLoginVO {
  id: number;
  username: string;
  name: string;
  token: string;
}

export interface UserSaveDTO {
  id?: number;
  username: string;
  password?: string;
  nickname?: string;
  email?: string;
  phone?: string;
  avatar?: string;
  status?: number;
}

export interface UserVO {
  id: number;
  username: string;
  nickname: string;
  email: string;
  phone: string;
  avatar: string;
  status: number;
  statusDesc: string;
  createTime: string;
  updateTime: string;
}

// ========== 角色相关 ==========

export interface RoleQueryDTO {
  roleCode?: string;
  roleName?: string;
  status?: number;
  createTimeStart?: string;
  createTimeEnd?: string;
  pageNum?: number;
  pageSize?: number;
}

export interface RoleSaveDTO {
  id?: number;
  roleCode: string;
  roleName: string;
  description?: string;
  status?: number;
  permissionIds?: number[];
}

export interface RoleVO {
  id: number;
  roleCode: string;
  roleName: string;
  description: string;
  status: number;
  statusDesc: string;
  createTime: string;
  updateTime: string;
}

// ========== 权限相关 ==========

export interface PermissionQueryDTO {
  permissionCode?: string;
  permissionName?: string;
  permissionType?: number;
  status?: number;
  parentId?: number;
}

export interface PermissionSaveDTO {
  id?: number;
  parentId?: number;
  permissionCode: string;
  permissionName: string;
  permissionType: number;
  path?: string;
  component?: string;
  icon?: string;
  sortOrder?: number;
  status?: number;
}

export interface PermissionVO {
  id: number;
  parentId: number;
  permissionCode: string;
  permissionName: string;
  permissionType: number;
  permissionTypeDesc: string;
  path: string;
  component: string;
  icon: string;
  sortOrder: number;
  status: number;
  statusDesc: string;
  createTime: string;
  updateTime: string;
  children?: PermissionVO[];
}

// ========== 枚举类型 ==========

export enum UserStatus {
  DISABLED = 0,
  ENABLED = 1
}

export enum PermissionType {
  MENU = 1,
  BUTTON = 2,
  API = 3
}
```

---

### 6.2 Axios 封装示例

**`src/utils/request.ts`**

```typescript
import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { ElMessage } from 'element-plus';
import router from '@/router';

// 创建 axios 实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 15000
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 添加 token
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = token;
    }
    return config;
  },
  (error) => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data;
    
    // 如果状态码不是 200，判断为错误
    if (res.code !== 200) {
      ElMessage.error(res.msg || '请求失败');
      
      // 401: 未授权，跳转到登录页
      if (res.code === 401) {
        localStorage.removeItem('token');
        router.push('/login');
      }
      
      return Promise.reject(new Error(res.msg || '请求失败'));
    }
    
    return res;
  },
  (error) => {
    console.error('响应错误:', error);
    
    if (error.response) {
      switch (error.response.status) {
        case 401:
          ElMessage.error('未授权，请重新登录');
          localStorage.removeItem('token');
          router.push('/login');
          break;
        case 403:
          ElMessage.error('权限不足');
          break;
        case 404:
          ElMessage.error('请求的资源不存在');
          break;
        case 500:
          ElMessage.error('服务器错误');
          break;
        default:
          ElMessage.error(error.response.data.msg || '请求失败');
      }
    } else {
      ElMessage.error('网络错误，请检查网络连接');
    }
    
    return Promise.reject(error);
  }
);

export default service;
```

---

### 6.3 API 调用示例

**`src/api/auth.ts`**

```typescript
import request from '@/utils/request';
import type { ResponseResult, UserLoginDTO, UserLoginVO } from '@/types/api';

/**
 * 用户登录
 */
export function login(data: UserLoginDTO) {
  return request<ResponseResult<UserLoginVO>>({
    url: '/auth/login',
    method: 'post',
    data
  });
}

/**
 * 退出登录
 */
export function logout() {
  return request<ResponseResult<void>>({
    url: '/auth/logout',
    method: 'post'
  });
}
```

**`src/api/user.ts`**

```typescript
import request from '@/utils/request';
import type { 
  ResponseResult, 
  PageDTO, 
  PageVO, 
  UserVO, 
  UserSaveDTO 
} from '@/types/api';

/**
 * 分页查询用户列表
 */
export function getUserPage(data: PageDTO) {
  return request<ResponseResult<PageVO<UserVO>>>({
    url: '/user/page',
    method: 'post',
    data
  });
}

/**
 * 根据ID查询用户
 */
export function getUserById(id: number) {
  return request<ResponseResult<UserVO>>({
    url: `/user/${id}`,
    method: 'get'
  });
}

/**
 * 新增用户
 */
export function addUser(data: UserSaveDTO) {
  return request<ResponseResult<void>>({
    url: '/user',
    method: 'post',
    data
  });
}

/**
 * 修改用户
 */
export function updateUser(data: UserSaveDTO) {
  return request<ResponseResult<void>>({
    url: '/user',
    method: 'put',
    data
  });
}

/**
 * 删除用户
 */
export function deleteUser(id: number) {
  return request<ResponseResult<void>>({
    url: `/user/${id}`,
    method: 'delete'
  });
}

/**
 * 批量删除用户
 */
export function batchDeleteUsers(ids: number[]) {
  return request<ResponseResult<void>>({
    url: '/user/batch',
    method: 'delete',
    data: ids
  });
}

/**
 * 修改用户状态
 */
export function updateUserStatus(id: number, status: number) {
  return request<ResponseResult<void>>({
    url: `/user/${id}/status`,
    method: 'put',
    params: { status }
  });
}

/**
 * 为用户分配角色
 */
export function assignRoles(id: number, roleIds: number[]) {
  return request<ResponseResult<void>>({
    url: `/user/${id}/roles`,
    method: 'put',
    data: roleIds
  });
}

/**
 * 获取用户权限列表
 */
export function getUserPermissions(id: number) {
  return request<ResponseResult<string[]>>({
    url: `/user/${id}/permissions`,
    method: 'get'
  });
}
```

**`src/api/role.ts`**

```typescript
import request from '@/utils/request';
import type { 
  ResponseResult, 
  Page,
  RoleQueryDTO,
  RoleVO, 
  RoleSaveDTO 
} from '@/types/api';

/**
 * 分页查询角色列表
 */
export function getRolePage(params: RoleQueryDTO) {
  return request<ResponseResult<Page<RoleVO>>>({
    url: '/role/page',
    method: 'get',
    params
  });
}

/**
 * 查询所有角色列表
 */
export function getAllRoles() {
  return request<ResponseResult<RoleVO[]>>({
    url: '/role/list',
    method: 'get'
  });
}

/**
 * 根据ID查询角色
 */
export function getRoleById(id: number) {
  return request<ResponseResult<RoleVO>>({
    url: `/role/${id}`,
    method: 'get'
  });
}

/**
 * 新增角色
 */
export function addRole(data: RoleSaveDTO) {
  return request<ResponseResult<void>>({
    url: '/role',
    method: 'post',
    data
  });
}

/**
 * 修改角色
 */
export function updateRole(data: RoleSaveDTO) {
  return request<ResponseResult<void>>({
    url: '/role',
    method: 'put',
    data
  });
}

/**
 * 删除角色
 */
export function deleteRole(id: number) {
  return request<ResponseResult<void>>({
    url: `/role/${id}`,
    method: 'delete'
  });
}

/**
 * 为角色分配权限
 */
export function assignPermissions(id: number, permissionIds: number[]) {
  return request<ResponseResult<void>>({
    url: `/role/${id}/permissions`,
    method: 'put',
    data: permissionIds
  });
}

/**
 * 获取角色权限列表
 */
export function getRolePermissions(id: number) {
  return request<ResponseResult<string[]>>({
    url: `/role/${id}/permissions`,
    method: 'get'
  });
}
```

**`src/api/permission.ts`**

```typescript
import request from '@/utils/request';
import type { 
  ResponseResult, 
  PermissionQueryDTO,
  PermissionVO, 
  PermissionSaveDTO 
} from '@/types/api';

/**
 * 查询权限列表
 */
export function getPermissionList(params: PermissionQueryDTO) {
  return request<ResponseResult<PermissionVO[]>>({
    url: '/permission/list',
    method: 'get',
    params
  });
}

/**
 * 查询权限树
 */
export function getPermissionTree(params: PermissionQueryDTO) {
  return request<ResponseResult<PermissionVO[]>>({
    url: '/permission/tree',
    method: 'get',
    params
  });
}

/**
 * 根据ID查询权限
 */
export function getPermissionById(id: number) {
  return request<ResponseResult<PermissionVO>>({
    url: `/permission/${id}`,
    method: 'get'
  });
}

/**
 * 新增权限
 */
export function addPermission(data: PermissionSaveDTO) {
  return request<ResponseResult<void>>({
    url: '/permission',
    method: 'post',
    data
  });
}

/**
 * 修改权限
 */
export function updatePermission(data: PermissionSaveDTO) {
  return request<ResponseResult<void>>({
    url: '/permission',
    method: 'put',
    data
  });
}

/**
 * 删除权限
 */
export function deletePermission(id: number) {
  return request<ResponseResult<void>>({
    url: `/permission/${id}`,
    method: 'delete'
  });
}

/**
 * 获取用户菜单权限树
 */
export function getUserMenuTree(userId: number) {
  return request<ResponseResult<PermissionVO[]>>({
    url: `/permission/menu/${userId}`,
    method: 'get'
  });
}

/**
 * 获取用户按钮权限列表
 */
export function getUserButtonPermissions(userId: number) {
  return request<ResponseResult<string[]>>({
    url: `/permission/button/${userId}`,
    method: 'get'
  });
}
```

---

### 6.4 Vue 组件使用示例

**用户登录组件示例：**

```vue
<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { login } from '@/api/auth';
import type { UserLoginDTO } from '@/types/api';

const router = useRouter();
const loading = ref(false);

const loginForm = reactive<UserLoginDTO>({
  username: '',
  password: ''
});

const handleLogin = async () => {
  loading.value = true;
  try {
    const res = await login(loginForm);
    
    // 保存 token 和用户信息
    localStorage.setItem('token', res.data.token);
    localStorage.setItem('userInfo', JSON.stringify(res.data));
    
    ElMessage.success('登录成功');
    router.push('/dashboard');
  } catch (error) {
    console.error('登录失败:', error);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <el-form :model="loginForm" @submit.prevent="handleLogin">
    <el-form-item label="用户名">
      <el-input v-model="loginForm.username" placeholder="请输入用户名" />
    </el-form-item>
    <el-form-item label="密码">
      <el-input 
        v-model="loginForm.password" 
        type="password" 
        placeholder="请输入密码" 
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" :loading="loading" @click="handleLogin">
        登录
      </el-button>
    </el-form-item>
  </el-form>
</template>
```

**用户列表组件示例：**

```vue
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getUserPage, deleteUser, updateUserStatus } from '@/api/user';
import type { UserVO, PageDTO } from '@/types/api';

const loading = ref(false);
const tableData = ref<UserVO[]>([]);
const total = ref(0);

const queryParams = reactive<PageDTO>({
  pageNo: 1,
  pageSize: 10
});

// 查询用户列表
const fetchUserList = async () => {
  loading.value = true;
  try {
    const res = await getUserPage(queryParams);
    tableData.value = res.data.list;
    total.value = res.data.total;
  } catch (error) {
    console.error('查询失败:', error);
  } finally {
    loading.value = false;
  }
};

// 修改用户状态
const handleStatusChange = async (row: UserVO) => {
  try {
    await updateUserStatus(row.id, row.status);
    ElMessage.success('状态修改成功');
  } catch (error) {
    // 恢复原状态
    row.status = row.status === 1 ? 0 : 1;
  }
};

// 删除用户
const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确认删除该用户吗？', '提示', {
      type: 'warning'
    });
    
    await deleteUser(id);
    ElMessage.success('删除成功');
    fetchUserList();
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error);
    }
  }
};

// 页码改变
const handlePageChange = (page: number) => {
  queryParams.pageNo = page;
  fetchUserList();
};

onMounted(() => {
  fetchUserList();
});
</script>

<template>
  <div>
    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="nickname" label="昵称" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column label="状态">
        <template #default="{ row }">
          <el-switch 
            v-model="row.status" 
            :active-value="1" 
            :inactive-value="0"
            @change="handleStatusChange(row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button type="primary" size="small">编辑</el-button>
          <el-button 
            type="danger" 
            size="small" 
            @click="handleDelete(row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <el-pagination
      v-model:current-page="queryParams.pageNo"
      v-model:page-size="queryParams.pageSize"
      :total="total"
      @current-change="handlePageChange"
    />
  </div>
</template>
```

---

## 7. 权限控制指南

### 7.1 路由权限控制

```typescript
// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router';
import { getUserMenuTree } from '@/api/permission';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    // 静态路由
    {
      path: '/login',
      component: () => import('@/views/login/index.vue')
    }
  ]
});

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('token');
  
  if (to.path === '/login') {
    next();
    return;
  }
  
  if (!token) {
    next('/login');
    return;
  }
  
  // 动态加载用户菜单权限
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
  if (userInfo.id) {
    try {
      const res = await getUserMenuTree(userInfo.id);
      // 根据权限动态生成路由
      // ...
      next();
    } catch (error) {
      next('/login');
    }
  }
});

export default router;
```

### 7.2 按钮权限控制

**自定义指令方式：**

```typescript
// src/directives/permission.ts
import { Directive } from 'vue';

export const permission: Directive = {
  mounted(el, binding) {
    const { value } = binding;
    const permissions = JSON.parse(localStorage.getItem('permissions') || '[]');
    
    if (value && !permissions.includes(value)) {
      el.parentNode?.removeChild(el);
    }
  }
};

// 在 main.ts 中注册
import { permission } from '@/directives/permission';
app.directive('permission', permission);
```

**使用示例：**

```vue
<template>
  <el-button v-permission="'USER.ADD'" type="primary">新增用户</el-button>
  <el-button v-permission="'USER.EDIT'" type="warning">编辑</el-button>
  <el-button v-permission="'USER.DELETE'" type="danger">删除</el-button>
</template>
```

---

## 8. 常见问题

### 8.1 跨域问题

如果遇到跨域问题，可以在 Vue 项目中配置代理：

```typescript
// vite.config.ts
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
});
```

### 8.2 Token 刷新

建议实现 Token 自动刷新机制，在响应拦截器中处理 Token 过期情况。

### 8.3 权限缓存

建议在用户登录后，将用户的按钮权限列表缓存到 localStorage 或 Pinia store 中，避免频繁请求。

---

## 9. 更新日志

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| v1.0 | 2025-10-21 | 初始版本，包含所有基础接口文档 |

---

**文档维护者：** 后端开发团队

**联系方式：** dev@emiyaoj.com

**备注：** 本文档将随着系统迭代持续更新，请以最新版本为准。

