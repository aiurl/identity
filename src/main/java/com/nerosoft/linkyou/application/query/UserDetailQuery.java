package com.nerosoft.linkyou.application.query;

import com.nerosoft.linkyou.persistence.model.UserEntity;
import com.nerosoft.linkyou.seedwork.BaseQuery;
import lombok.Getter;

@Getter
public class UserDetailQuery extends BaseQuery<UserEntity> {

    /**
     *  用户Id
     */
    private final String id;

    /**
     *  用户名
     */
    private final String username;

    public UserDetailQuery(String id, String username) {
        this.id = id;
        this.username = username;
    }

}
