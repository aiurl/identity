package com.nerosoft.linkyou.seedwork;

/**
 * 基础查询类
 * 
 * 所有查询对象都应该继承自这个基类，以便于统一管理和处理。
 * 
 * @author nerosoft
 * @version 1.0
 */
public abstract class BaseQuery {
    private final String queryId = java.util.UUID.randomUUID().toString();
    private final java.time.Instant issuedAt = java.time.Instant.now();

    /**
     * 获取查询的唯一标识
     * @return 查询的唯一标识
     */
    public String getQueryId() {
        return queryId;
    }

    /**
     * 获取查询的发出时间
     * @return 查询的发出时间
     */
    public java.time.Instant getIssuedAt() {
        return issuedAt;
    }

}
