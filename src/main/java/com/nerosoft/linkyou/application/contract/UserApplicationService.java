package com.nerosoft.linkyou.application.contract;

import java.util.HashMap;

import com.nerosoft.linkyou.application.dto.UserCreateDto;
import com.nerosoft.linkyou.application.dto.UserDetailDto;
import com.nerosoft.linkyou.application.dto.UserProfileDto;
import com.nerosoft.linkyou.seedwork.ApplicationService;

import reactor.core.publisher.Mono;

/**
 * 用户应用服务接口
 */
public interface UserApplicationService extends ApplicationService {
    /**
     * 创建用户
     * @param data 包含创建用户所需的信息，必须包含 username 和 password 字段
     * @return 创建的用户 ID
     */
    Mono<String> createAsync(UserCreateDto data);

    /**
     * 更新用户信息
     * 只允许更新用户的 email 和 phone 字段，其他字段不允许更新
     * @param id 用户 ID
     * @param data 包含要更新的字段，必须包含 email 和 phone 字段
     * @return void
     */
    Mono<Void> updateAsync(String id, HashMap<String, Object> data);

    /**
     * 获取用户详细信息
     * @param id 用户 ID
     * @return 用户详细信息
     */
    Mono<UserDetailDto> getAsync(String id);

    /**
     * 获取当前用户的个人信息
     * @return 当前用户的个人信息
     */
    Mono<UserProfileDto> getProfileAsync();

    /**
     * 修改当前用户的密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return void
     */
    Mono<Void> changePasswordAsync(String oldPassword, String newPassword);

    /**
     * 重置用户密码
     * @param username 用户名
     * @param password 密码
     * @param verifyCode 验证码
     * @return void
     */
    Mono<Void> resetPasswordAsync(String username, String password, String verifyCode);
}
