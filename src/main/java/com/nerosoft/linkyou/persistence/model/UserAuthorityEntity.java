package com.nerosoft.linkyou.persistence.model;

import org.springframework.data.domain.Persistable;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户关联的第三方授权信息实体类，表示用户与第三方授权平台之间的关联关系
 * 包含用户ID、授权平台名称、第三方账号ID等信息，用于实现用户通过第三方平台进行授权登录的功能
 */
@Data
@Entity
@Table(name = "user_authority")
public class UserAuthorityEntity implements Persistable<Long> {
    /**
     * 主键ID，通过雪花算法生成，确保全局唯一性和有序性
     */
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 关联的用户ID
     */
    @Column(name = "user_id", nullable = false, length = 32)
    private String userId;

    /**
     * 第三方授权平台的名称，例如Google、Facebook、GitHub等，用于标识用户是通过哪个第三方平台进行授权登录的
     */
    @Column(name = "provider", nullable = false, length = 64)
    private String provider;

    /**
     * 第三方授权账号ID，通常是第三方平台提供的唯一标识符，用于关联用户与其在第三方平台上的账户信息
     */
    @Column(name = "open_id", nullable = false, length = 128)
    private String openId;

    /**
     * 第三方授权账号名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 创建时间
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Override
    public boolean isNew() {
        return id == null || id <= 0;
    }
}
