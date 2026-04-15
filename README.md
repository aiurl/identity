# identity

身份认证与用户体系服务，采用 **Spring Boot + DDD + CQRS** 的分层设计，便于后续扩展用户、角色、权限、认证、授权等核心能力。

## 架构说明

项目按照经典领域驱动设计（DDD）与命令查询职责分离（CQRS）的思路进行组织：

- **Interfaces 层**：负责接收外部请求，例如 REST API。
- **Application 层**：负责命令、查询、用例编排与 Handler。
- **Domain 层**：负责核心业务规则、聚合、实体、值对象与仓储接口。
- **Infrastructure 层**：负责持久化、配置、安全、消息等技术实现。
- **Common 层**：放置公共常量、异常与工具类。

## 项目目录结构

```text
src/main/java/com/linkyou/identity
├── common                      # 公共能力层
│   ├── constants               # 常量定义
│   ├── exception               # 通用异常
│   └── util                    # 工具类
├── domain                      # 领域层
│   ├── event                   # 领域事件
│   ├── model                   # 领域模型
│   │   ├── aggregate           # 聚合根
│   │   ├── entity              # 实体
│   │   └── valueobject         # 值对象
│   ├── repository              # 仓储接口
│   └── service                 # 领域服务
├── application                 # 应用层（CQRS）
│   ├── command                 # 写操作模型
│   │   ├── dto                 # Command DTO
│   │   └── handler             # Command Handler
│   ├── query                   # 读操作模型
│   │   ├── dto                 # Query DTO / View Object
│   │   └── handler             # Query Handler
│   └── service                 # 应用服务
├── interfaces                  # 接口层
│   ├── assembler               # DTO / DO / VO 转换
│   └── rest                    # REST 控制器
└── infrastructure              # 基础设施层
    ├── config                  # 配置类
    ├── messaging               # 消息集成
    ├── persistence             # 持久化实现
    │   ├── entity              # 数据库实体
    │   ├── mapper              # Mapper / JPA / MyBatis 映射
    │   └── repository          # 仓储实现
    └── security                # 安全相关实现
```

## 分层依赖建议

推荐遵循以下依赖方向：

- Interfaces → Application
- Application → Domain
- Infrastructure → Domain
- Domain 不依赖具体基础设施实现

这样可以保证核心业务模型稳定，同时便于测试、重构与后续模块扩展。

## 用户-角色-权限关联表设计

当前已补充完整的 RBAC 关系表设计，包含以下核心表：

- `sys_users`：用户表
- `sys_roles`：角色表
- `sys_permissions`：权限表
- `sys_user_roles`：用户-角色关联表
- `sys_role_permissions`：角色-权限关联表

### 关系说明

- 一个用户可以拥有多个角色
- 一个角色可以分配给多个用户
- 一个角色可以拥有多个权限
- 一个权限也可以被多个角色复用

即：

- User ↔ Role：多对多
- Role ↔ Permission：多对多

对应初始化脚本位于 [src/main/resources/schema.sql](src/main/resources/schema.sql)。

## 当前已实现的示例能力

当前项目已经补充了基础的身份认证示例：

- 用户注册
- 用户登录并签发 JWT
- 基于角色的访问控制（RBAC）
- 用户接口鉴权访问
- 角色与权限接口的管理员限制访问

### 示例接口

- POST /api/auth/register：注册用户
- POST /api/auth/login：登录并获取 JWT
- GET /api/users：需要携带有效 Token
- GET /api/roles：仅 ADMIN 可访问
- GET /api/permissions：仅 ADMIN 可访问

## 后续扩展示例

后续可以在该结构下继续新增：

- JWT 刷新令牌
- OAuth2 / OpenID Connect
- 更完整的角色与权限管理
- 多租户身份体系
- 审计日志与领域事件发布
