# EmiyaOJ 部署指南

## 环境准备

### 1. 安装 Java 17
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# 验证安装
java -version
```

### 2. 安装 MySQL 8.0
```bash
# Ubuntu/Debian
sudo apt install mysql-server

# 启动 MySQL
sudo systemctl start mysql
sudo systemctl enable mysql

# 设置 root 密码
sudo mysql_secure_installation
```

### 3. 安装 Redis
```bash
# Ubuntu/Debian
sudo apt install redis-server

# 启动 Redis
sudo systemctl start redis
sudo systemctl enable redis
```

### 4. 部署 go-judge 沙箱

#### 使用 Docker 部署（推荐）

```bash
# 拉取镜像
docker pull criyle/executorserver

# 运行容器
docker run -d \
  --name go-judge \
  --privileged \
  -p 10000:5050 \
  --restart always \
  criyle/executorserver
```

#### 检查沙箱是否正常运行

```bash
curl http://10.30.22.1:10000/version
```

应该返回类似：
```json
{
  "buildVersion": "...",
  "goVersion": "..."
}
```

## 数据库配置

### 1. 创建数据库
```bash
mysql -u root -p
```

```sql
CREATE DATABASE `emiya-oj` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 导入表结构和初始数据
```bash
# 导入 RBAC 权限表结构
mysql -u root -p emiya-oj < database/rbac_schema.sql

# 导入 RBAC 初始数据
mysql -u root -p emiya-oj < database/rbac_init_data.sql

# 导入 OJ 系统表结构
mysql -u root -p emiya-oj < database/oj_schema.sql
```

## 应用配置

### 1. 修改配置文件

编辑 `oj-service/src/main/resources/application.yaml`：

```yaml
server:
  port: 8080

spring:
  application:
    name: oj-service
  datasource:
    url: jdbc:mysql://localhost:3306/emiya-oj?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai
    username: root
    password: your_mysql_password  # 修改为你的 MySQL 密码
    driver-class-name: com.mysql.cj.jdbc.Driver
  data:
    redis:
      host: localhost
      port: 6379

# 沙箱配置
sandbox:
  url: http://10.30.22.1:10000  # 修改为你的沙箱地址
  timeout: 30000

# JWT配置
jwt:
  secret-key: your-secret-key-at-least-32-characters-long  # 修改为你的密钥
  ttl: 7200000
  token-name: Authorization
```

### 2. 生成安全的 JWT 密钥

```bash
# 使用 openssl 生成随机密钥
openssl rand -base64 32
```

将生成的密钥替换配置文件中的 `jwt.secret-key`。

## 编译和运行

### 1. 编译项目

```bash
# 在项目根目录执行
./mvnw clean package -DskipTests
```

编译成功后会在 `oj-service/target/` 目录生成 jar 文件。

### 2. 运行应用

#### 开发环境
```bash
# 直接运行
./mvnw spring-boot:run -pl oj-service
```

#### 生产环境
```bash
# 使用 nohup 后台运行
nohup java -jar oj-service/target/oj-service-0.0.1-SNAPSHOT.jar > logs/app.log 2>&1 &

# 或使用 systemd 服务
sudo cp emiya-oj.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl start emiya-oj
sudo systemctl enable emiya-oj
```

### 3. Systemd 服务配置示例

创建文件 `/etc/systemd/system/emiya-oj.service`：

```ini
[Unit]
Description=EmiyaOJ Service
After=network.target mysql.service redis.service

[Service]
Type=simple
User=your-user
WorkingDirectory=/path/to/EmiyaOJ
ExecStart=/usr/bin/java -jar /path/to/EmiyaOJ/oj-service/target/oj-service-0.0.1-SNAPSHOT.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

## 验证部署

### 1. 检查服务状态

```bash
# 查看日志
tail -f logs/app.log

# 检查端口
netstat -tlnp | grep 8080
```

### 2. 访问 API 文档

打开浏览器访问：http://localhost:8080/doc.html

### 3. 测试 API

```bash
# 健康检查
curl http://localhost:8080/hello

# 登录获取 token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

## Nginx 反向代理配置（可选）

如果需要使用域名和 HTTPS，可以配置 Nginx：

```nginx
server {
    listen 80;
    server_name oj.example.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

配置 SSL：
```bash
# 使用 certbot 获取证书
sudo certbot --nginx -d oj.example.com
```

## 性能优化

### 1. JVM 参数优化

```bash
java -Xms512m -Xmx2g \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar oj-service-0.0.1-SNAPSHOT.jar
```

### 2. MySQL 优化

编辑 `/etc/mysql/mysql.conf.d/mysqld.cnf`：

```ini
[mysqld]
max_connections = 200
innodb_buffer_pool_size = 1G
innodb_log_file_size = 256M
```

### 3. Redis 优化

编辑 `/etc/redis/redis.conf`：

```ini
maxmemory 512mb
maxmemory-policy allkeys-lru
```

## 监控和日志

### 1. 日志配置

在 `application.yaml` 中配置日志级别：

```yaml
logging:
  level:
    com.emiyaoj: INFO
    org.springframework: WARN
  file:
    name: logs/emiya-oj.log
    max-size: 100MB
    max-history: 30
```

### 2. 监控工具

- Spring Boot Actuator: http://localhost:8080/actuator
- Prometheus + Grafana
- ELK Stack (Elasticsearch + Logstash + Kibana)

## 故障排查

### 常见问题

1. **无法连接数据库**
   - 检查 MySQL 是否运行：`sudo systemctl status mysql`
   - 检查连接配置是否正确
   - 检查防火墙规则

2. **沙箱连接失败**
   - 检查 Docker 容器是否运行：`docker ps`
   - 检查沙箱 URL 配置
   - 测试沙箱连接：`curl http://10.30.22.1:10000/version`

3. **JWT 验证失败**
   - 检查密钥配置
   - 确保密钥长度至少 32 个字符

4. **判题失败**
   - 查看应用日志
   - 检查沙箱日志：`docker logs go-judge`
   - 确认测试用例格式正确

### 日志查看

```bash
# 应用日志
tail -f logs/emiya-oj.log

# 沙箱日志
docker logs -f go-judge

# MySQL 错误日志
sudo tail -f /var/log/mysql/error.log
```

## 备份和恢复

### 数据库备份

```bash
# 备份
mysqldump -u root -p emiya-oj > backup_$(date +%Y%m%d).sql

# 恢复
mysql -u root -p emiya-oj < backup_20240101.sql
```

### 自动备份脚本

```bash
#!/bin/bash
BACKUP_DIR="/backup/mysql"
DATE=$(date +%Y%m%d_%H%M%S)

mkdir -p $BACKUP_DIR
mysqldump -u root -pYOUR_PASSWORD emiya-oj > $BACKUP_DIR/emiya-oj_$DATE.sql
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
```

添加到 crontab：
```bash
# 每天凌晨 2 点备份
0 2 * * * /path/to/backup.sh
```

## 升级指南

1. 备份数据库
2. 停止应用：`sudo systemctl stop emiya-oj`
3. 拉取最新代码：`git pull`
4. 执行数据库迁移脚本（如果有）
5. 重新编译：`./mvnw clean package -DskipTests`
6. 启动应用：`sudo systemctl start emiya-oj`
7. 验证功能

## 安全建议

1. 定期更新依赖包
2. 使用强密码
3. 启用 HTTPS
4. 限制数据库远程访问
5. 配置防火墙规则
6. 定期备份数据
7. 监控异常登录
8. 使用 Redis 密码认证

## 技术支持

如遇问题，请查看：
- 项目文档：`/doc` 目录
- API 示例：`doc/api_examples.md`
- GitHub Issues
