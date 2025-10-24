# Fastjson2 Long类型与String类型自动转换说明

## 配置完成情况

✅ **双向转换已支持**

### 1. 后端返回给前端：Long → String
通过在VO类的id字段上添加 `@JSONField` 注解实现：
```java
@JSONField(serializeFeatures = com.alibaba.fastjson2.JSONWriter.Feature.WriteLongAsString)
private Long id;
```

**效果**：
- 后端：`id = 1234567890123456789L`
- 前端接收：`"id": "1234567890123456789"`（字符串格式）

### 2. 前端传入后端：String → Long
通过Fastjson2配置的反序列化特性自动实现：
```java
config.setReaderFeatures(
    JSONReader.Feature.SupportArrayToBean,
    JSONReader.Feature.SupportSmartMatch  // 智能匹配，支持类型自动转换
);
```

**效果**：
- 前端发送：`"id": "1234567890123456789"`（字符串格式）
- 后端接收：`id = 1234567890123456789L`（Long类型）

## 测试示例

### 测试用例1：前端传入字符串ID
```json
// 前端请求体
{
  "id": "1234567890123456789",
  "username": "testuser",
  "nickname": "测试用户"
}
```

```java
// 后端DTO接收（自动转换）
@Data
public class UserSaveDTO {
    private Long id;  // 会自动将 "1234567890123456789" 转换为 Long 类型
    private String username;
    private String nickname;
}
```

### 测试用例2：后端返回Long类型ID
```java
// 后端VO
@Data
public class UserVO {
    @JSONField(serializeFeatures = JSONWriter.Feature.WriteLongAsString)
    private Long id;  // 1234567890123456789L
    private String username;
}
```

```json
// 前端接收到的JSON（自动转换为字符串）
{
  "id": "1234567890123456789",
  "username": "testuser"
}
```

## 支持的转换场景

✅ **支持以下所有场景**：

1. **前端传入字符串 → 后端Long**
   - `"123"` → `123L` ✓
   - `"1234567890123456789"` → `1234567890123456789L` ✓

2. **前端传入数字 → 后端Long**
   - `123` → `123L` ✓
   - `1234567890` → `1234567890L` ✓

3. **后端Long → 前端字符串**（带@JSONField注解的字段）
   - `123L` → `"123"` ✓
   - `1234567890123456789L` → `"1234567890123456789"` ✓

4. **null值处理**
   - `null` → `null` ✓

## 验证方法

### 方法1：使用Swagger/Knife4j测试
1. 启动应用
2. 访问 http://localhost:8080/doc.html
3. 测试用户新增接口，传入：
```json
{
  "id": "1234567890123456789",
  "username": "testuser",
  "password": "123456"
}
```
4. 查看后端日志，确认id被正确转换为Long类型

### 方法2：使用curl测试
```bash
# 测试新增用户（字符串ID）
curl -X POST http://localhost:8080/user/save \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "id": "1234567890123456789",
    "username": "testuser",
    "password": "123456"
  }'

# 测试查询用户（返回字符串ID）
curl -X GET http://localhost:8080/user/1234567890123456789 \
  -H "Authorization: Bearer {token}"
```

## 注意事项

1. **字符串格式要求**：前端传入的字符串必须是有效的数字字符串，否则会报错
   - ✓ 正确：`"123"`, `"1234567890123456789"`
   - ✗ 错误：`"abc"`, `"12.34"`, `""`

2. **精度保护**：只有VO类中带 `@JSONField` 注解的Long字段才会序列化为字符串
   - UserVO.id → 字符串 ✓
   - UserSaveDTO.id → 接收字符串或数字，转换为Long ✓

3. **兼容性**：配置同时支持前端传入数字或字符串，都能正确转换为Long类型

## 配置文件位置

- **Fastjson2配置**：`oj-common/src/main/java/com/emiyaoj/common/config/MyFastJsonConfig.java`
- **VO类注解**：`oj-service/src/main/java/com/emiyaoj/service/domain/vo/*.java`

## 总结

✅ **当前配置完全支持双向自动转换**：
- 前端String → 后端Long：自动转换 ✓
- 后端Long → 前端String：通过@JSONField注解转换 ✓
- 兼容前端传入数字或字符串 ✓

无需额外配置，开箱即用！

