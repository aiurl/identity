package com.nerosoft.linkyou.seedwork;

/**
 * Persistable接口定义了一个具有持久化能力的实体对象，包含获取和设置ID的方法
 * @param <T> ID的类型，可以是String、Long等
 */
public interface Persistable<T> {
    T getId();

    void setId(T id);
}
