# 博客模块接口文档

> 基础路径: `/client/blog`
> 
> 需要认证: 是（Bearer Token）
> 
> Content-Type: `application/json`

---

## 通用响应格式

所有接口均返回以下格式：

```json
{
  "code": 200,
  "msg": "Success",
  "data": <具体数据>
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |
| data | T | 响应数据，具体类型视接口而定 |

### 常见错误码

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 401 | 权限不足 |
| 404 | 资源未找到 |
| 500 | 服务器错误 |

---

## 分页响应格式

```json
{
  "total": 100,
  "pages": 10,
  "list": [...]
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| total | Long | 总记录数 |
| pages | Long | 总页数 |
| list | Array | 数据列表 |

---

## 1. 查询所有博客

### 请求

- **方法**: `GET`
- **路径**: `/client/blog`
- **权限**: `BLOG.LIST`

### 响应

```json
{
  "code": 200,
  "msg": "Success",
  "data": [
    {
      "id": "1234567890",
      "userId": "9876543210",
      "title": "博客标题",
      "content": "博客内容",
      "createTime": "2025-12-21T10:30:00",
      "updateTime": "2025-12-21T10:30:00",
      "tags": [
        {
          "id": "1",
          "name": "Java",
          "desc": "Java编程语言"
        }
      ]
    }
  ]
}
```

---

## 2. 发布博客

### 请求

- **方法**: `POST`
- **路径**: `/client/blog`
- **权限**: `BLOG.ADD`

### 请求体

```json
{
  "title": "博客标题",
  "content": "博客内容",
  "tagIds": [1, 2, 3]
}
```

| 字段 | 类型 | 必填 | 说明 | 校验规则 |
|------|------|------|------|----------|
| title | String | 是 | 博客标题 | 不能为空，最大50字符 |
| content | String | 是 | 博客内容 | 不能为空，最大1000字符 |
| tagIds | Array\<Long\> | 是 | 标签ID列表 | 至少选择一个标签 |

### 响应

```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

---

## 3. 分页条件查询博客

### 请求

- **方法**: `POST`
- **路径**: `/client/blog/query`
- **权限**: `BLOG.LIST`

### 请求体

```json
{
  "title": "搜索关键词",
  "createTime": "2025-12-21T00:00:00",
  "pageNo": 1,
  "pageSize": 10
}
```

| 字段 | 类型 | 必填 | 说明 | 校验规则 |
|------|------|------|------|----------|
| title | String | 否 | 标题模糊搜索，为空查全部 | 最大50字符 |
| createTime | String (ISO 8601) | 否 | 查当天数据，为空查全部 | - |
| pageNo | Integer | 是 | 页码 | 不能为空 |
| pageSize | Integer | 是 | 每页大小 | 不能为空 |

### 响应

```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "total": 100,
    "pages": 10,
    "list": [
      {
        "id": "1234567890",
        "userId": "9876543210",
        "title": "博客标题",
        "content": "博客内容",
        "createTime": "2025-12-21T10:30:00",
        "updateTime": "2025-12-21T10:30:00",
        "tags": [
          {
            "id": "1",
            "name": "Java",
            "desc": "Java编程语言"
          }
        ]
      }
    ]
  }
}
```

---

## 4. 获取指定博客

### 请求

- **方法**: `GET`
- **路径**: `/client/blog/{bid}`
- **权限**: `BLOG.LIST`

### 路径参数

| 参数 | 类型 | 说明 |
|------|------|------|
| bid | Long | 博客ID |

### 响应

**成功**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "id": "1234567890",
    "userId": "9876543210",
    "title": "博客标题",
    "content": "博客内容",
    "createTime": "2025-12-21T10:30:00",
    "updateTime": "2025-12-21T10:30:00",
    "tags": [
      {
        "id": "1",
        "name": "Java",
        "desc": "Java编程语言"
      }
    ]
  }
}
```

**失败 (404)**:
```json
{
  "code": 404,
  "msg": "未找到该博客",
  "data": null
}
```

---

## 5. 删除博客（逻辑删除）

### 请求

- **方法**: `DELETE`
- **路径**: `/client/blog/{bid}`
- **权限**: `BLOG.DELETE`

### 路径参数

| 参数 | 类型 | 说明 |
|------|------|------|
| bid | Long | 博客ID |

### 响应

```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

---

## 6. 修改博客

### 请求

- **方法**: `PUT`
- **路径**: `/client/blog/{bid}`
- **权限**: `BLOG.EDIT`

### 路径参数

| 参数 | 类型 | 说明 |
|------|------|------|
| bid | Long | 博客ID |

### 请求体

```json
{
  "title": "新标题",
  "content": "新内容"
}
```

| 字段 | 类型 | 必填 | 说明 | 校验规则 |
|------|------|------|------|----------|
| title | String | 是 | 博客标题 | 不能为空，最大50字符 |
| content | String | 是 | 博客内容 | 不能为空，最大10000字符 |

> **注意**: 如果 `title` 或 `content` 为空字符串，后端会将其设为 `null`

### 响应

```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

---

## 7. 分页查询博客评论

### 请求

- **方法**: `POST`
- **路径**: `/client/blog/{bid}/comments/query`
- **权限**: `COMMENT.LIST`

### 路径参数

| 参数 | 类型 | 说明 |
|------|------|------|
| bid | Long | 博客ID |

### 请求体

```json
{
  "pageNo": 1,
  "pageSize": 10,
  "sortBy": "create_time",
  "isAsc": false
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNo | Integer | 否 | 页码 |
| pageSize | Integer | 否 | 每页大小 |
| sortBy | String | 否 | 排序字段 |
| isAsc | Boolean | 否 | 是否升序 |

### 响应

**成功**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "total": 50,
    "pages": 5,
    "list": [
      {
        "id": "123",
        "userId": "456",
        "username": "user1",
        "nickname": "用户昵称",
        "content": "这是评论内容",
        "createTime": "2025-12-21T10:30:00",
        "updateTime": "2025-12-21T10:30:00"
      }
    ]
  }
}
```

**失败 (404)**:
```json
{
  "code": 404,
  "msg": "未找到该博客",
  "data": null
}
```

---

## 8. 发表评论

### 请求

- **方法**: `POST`
- **路径**: `/client/blog/{bid}/comments`
- **权限**: `COMMENT.ADD`

### 路径参数

| 参数 | 类型 | 说明 |
|------|------|------|
| bid | Long | 博客ID |

### 请求体

```json
{
  "content": "评论内容"
}
```

| 字段 | 类型 | 必填 | 说明 | 校验规则 |
|------|------|------|------|----------|
| content | String | 是 | 评论内容 | 不能为空，最大200字符 |

### 响应

```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

---

## 9. 收藏博客

### 请求

- **方法**: `POST`
- **路径**: `/client/blog/{bid}/star`
- **权限**: `BLOG.STAR`

### 路径参数

| 参数 | 类型 | 说明 |
|------|------|------|
| bid | Long | 博客ID |

### 响应

```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

---

## 10. 取消收藏博客

### 请求

- **方法**: `DELETE`
- **路径**: `/client/blog/{bid}/star`
- **权限**: `BLOG.STAR`

### 路径参数

| 参数 | 类型 | 说明 |
|------|------|------|
| bid | Long | 博客ID |

### 响应

```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

---

## 11. 查询博客模块用户信息

### 请求

- **方法**: `GET`
- **路径**: `/client/blog/user/{uid}`
- **权限**: `USER.LIST`

### 路径参数

| 参数 | 类型 | 说明 |
|------|------|------|
| uid | Long | 用户ID |

### 响应

**成功**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "userId": "123456",
    "username": "user1",
    "nickname": "用户昵称",
    "blogCount": 10,
    "starCount": 5
  }
}
```

**失败 (404)**:
```json
{
  "code": 404,
  "msg": "未找到该用户",
  "data": null
}
```

---

## 12. 分页查询用户发表的博客

### 请求

- **方法**: `POST`
- **路径**: `/client/blog/user/{uid}/blogs/query`
- **权限**: `BLOG.LIST`

### 路径参数

| 参数 | 类型 | 说明 |
|------|------|------|
| uid | Long | 用户ID |

### 请求体

```json
{
  "pageNo": 1,
  "pageSize": 10
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNo | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页大小 |

> **注意**: `userId` 由路径参数提供，无需在请求体中传递

### 响应

**成功**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "total": 100,
    "pages": 10,
    "list": [
      {
        "id": "1234567890",
        "userId": "9876543210",
        "title": "博客标题",
        "content": "博客内容",
        "createTime": "2025-12-21T10:30:00",
        "updateTime": "2025-12-21T10:30:00",
        "tags": []
      }
    ]
  }
}
```

**失败 (404)**:
```json
{
  "code": 404,
  "msg": "未找到该用户",
  "data": null
}
```

---

## 13. 分页查询用户收藏的博客

### 请求

- **方法**: `POST`
- **路径**: `/client/blog/user/{uid}/stars/query`
- **权限**: `BLOG.LIST`

### 路径参数

| 参数 | 类型 | 说明 |
|------|------|------|
| uid | Long | 用户ID |

### 请求体

```json
{
  "pageNo": 1,
  "pageSize": 10
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNo | Integer | 否 | 页码 |
| pageSize | Integer | 否 | 每页大小 |

> **注意**: `userId` 由路径参数提供，无需在请求体中传递

### 响应

**成功**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "total": 50,
    "pages": 5,
    "list": [
      {
        "id": "1234567890",
        "userId": "9876543210",
        "title": "博客标题",
        "content": "博客内容",
        "createTime": "2025-12-21T10:30:00",
        "updateTime": "2025-12-21T10:30:00",
        "tags": []
      }
    ]
  }
}
```

**失败 (404)**:
```json
{
  "code": 404,
  "msg": "未找到该用户",
  "data": null
}
```

---

## 14. 查询所有标签

### 请求

- **方法**: `GET`
- **路径**: `/client/blog/tags`
- **权限**: `BLOG.LIST`

### 响应

```json
{
  "code": 200,
  "msg": "Success",
  "data": [
    {
      "id": "1",
      "name": "Java",
      "desc": "Java编程语言"
    },
    {
      "id": "2",
      "name": "Python",
      "desc": "Python编程语言"
    }
  ]
}
```

---

## 15. 条件查询评论

### 请求

- **方法**: `POST`
- **路径**: `/client/blog/comments/query`
- **权限**: `COMMENT.LIST`

### 请求体

```json
{
  "blogId": 123,
  "fromDay": "2025-12-01T00:00:00",
  "toDay": "2025-12-31T23:59:59"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| blogId | Long | 否 | 博客ID |
| fromDay | String (ISO 8601) | 否 | 起始日期 |
| toDay | String (ISO 8601) | 否 | 结束日期 |

### 响应

**成功**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": [
    {
      "id": "123",
      "userId": "456",
      "username": "user1",
      "nickname": "用户昵称",
      "content": "这是评论内容",
      "createTime": "2025-12-21T10:30:00",
      "updateTime": "2025-12-21T10:30:00"
    }
  ]
}
```

**失败 (404)**:
```json
{
  "code": 404,
  "msg": "未找到该用户",
  "data": null
}
```

---

## 16. 获取指定评论

### 请求

- **方法**: `GET`
- **路径**: `/client/blog/comments/{cid}`
- **权限**: `COMMENT.LIST`

### 路径参数

| 参数 | 类型 | 说明 |
|------|------|------|
| cid | Long | 评论ID |

### 响应

**成功**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "id": "123",
    "userId": "456",
    "username": "user1",
    "nickname": "用户昵称",
    "content": "这是评论内容",
    "createTime": "2025-12-21T10:30:00",
    "updateTime": "2025-12-21T10:30:00"
  }
}
```

**失败 (404)**:
```json
{
  "code": 404,
  "msg": "未找到该评论",
  "data": null
}
```

---

## 17. 删除评论

### 请求

- **方法**: `DELETE`
- **路径**: `/client/blog/comments/{cid}`
- **权限**: `COMMENT.DELETE`

### 路径参数

| 参数 | 类型 | 说明 |
|------|------|------|
| cid | Long | 评论ID |

### 响应

**成功 (200)**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

**失败 (404)**:
```json
{
  "code": 404,
  "msg": "未找到该评论",
  "data": null
}
```

**失败 (401)**:
```json
{
  "code": 401,
  "msg": "权限不足",
  "data": null
}
```

---

## 数据模型

### BlogVO

| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | 博客ID（Long序列化为String） |
| userId | String | 用户ID（Long序列化为String） |
| title | String | 博客标题 |
| content | String | 博客内容 |
| createTime | String (ISO 8601) | 创建时间 |
| updateTime | String (ISO 8601) | 更新时间 |
| tags | Array\<BlogTagVO\> | 标签列表 |

### BlogTagVO

| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | 标签ID（Long序列化为String） |
| name | String | 标签名称 |
| desc | String | 标签描述 |

### CommentVO

| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | 评论ID（Long序列化为String） |
| userId | String | 用户ID（Long序列化为String） |
| username | String | 用户名 |
| nickname | String | 用户昵称 |
| createTime | String (ISO 8601) | 创建时间 |
| updateTime | String (ISO 8601) | 更新时间 |

### UserBlogVO

| 字段 | 类型 | 说明 |
|------|------|------|
| userId | String | 用户ID（Long序列化为String） |
| username | String | 用户名 |
| nickname | String | 用户昵称 |
| blogCount | Integer | 发表博客数量 |
| starCount | Integer | 收藏博客数量 |

---

## 权限说明

| 权限标识 | 说明 |
|----------|------|
| BLOG.LIST | 查看博客列表 |
| BLOG.ADD | 发布博客 |
| BLOG.EDIT | 编辑博客 |
| BLOG.DELETE | 删除博客 |
| BLOG.STAR | 收藏/取消收藏博客 |
| COMMENT.LIST | 查看评论 |
| COMMENT.ADD | 发表评论 |
| COMMENT.DELETE | 删除评论 |
| USER.LIST | 查看用户信息 |

