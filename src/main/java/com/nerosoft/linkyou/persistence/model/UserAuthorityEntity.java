package com.nerosoft.linkyou.persistence.model;

import com.nerosoft.linkyou.seedwork.Persistable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * UserAuthorityEntity类表示用户权限的实体，包含用户与权限之间的关系
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
}
