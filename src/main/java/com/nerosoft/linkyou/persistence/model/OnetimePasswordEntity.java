package com.nerosoft.linkyou.persistence.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

/**
 * 一次性密码实体
 */
@Entity
@Table(name = "onetime_password")
@Data
public class OnetimePasswordEntity implements Persistable<Long> {

    /**
     * 主键ID，雪花算法生成
     */
    @Id
    private Long id;

    /**
     * 请求ID，通常是一个唯一的字符串，用于标识一次性密码的生成请求，长度不超过36个字符，且不可修改
     */
    @Column(name = "request_id", nullable = false, unique = true, length = 36)
    private String requestId;

    /**
     * OTP代码，通常是一个随机生成的字符串，长度不超过12个字符，且不可修改
     */
    @Column(name = "code", nullable = false, length = 12, updatable = false)
    private String code;

    /**
     * OTP接收者，可以是用户的邮箱地址或手机号，长度不超过255个字符，且不可修改
     */
    @Column(name = "recipient", nullable = false, length = 255)
    private String recipient;

    /**
     * 失效时间
     */
    @Column(name = "expiration")
    private LocalDateTime expiration;

    /**
     * 检查时间，记录OTP被验证的时间，初始值为null，表示尚未被验证
     */
    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    /**
     * 持续时间，单位为秒，表示OTP的有效期长度，初始值为null，表示没有设置持续时间
     */
    @Column(name = "duration")
    private Integer duration;

    /**
     * OTP用途，通常是一个整数值，用于区分不同类型的OTP，例如登录验证、密码重置等，且不可修改
     */
    @Column(name = "usage", nullable = false)
    private Integer usage;

    /**
     * 创建时间，记录OTP生成的时间，且不可修改
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Override
    public boolean isNew() {
        return false;
    }
}
