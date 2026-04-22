package com.nerosoft.linkyou.domain.repository;

import com.nerosoft.linkyou.domain.aggregate.User;

/* 用户仓储接口 */
public interface UserRepository {
    /**
     * 保存用户对象到仓储中，如果用户已经存在则更新其信息
     * 
     * @param user 要保存的用户对象，不能为空且必须包含有效的ID
     */
    void  save(User user);

    /**
     * 根据用户ID从仓储中查找用户对象
     * 
     * @param id 用户ID，不能为空
     * @return 查找到的用户对象，如果不存在则返回null
     */
    User findById(String id);

    /**
     * 根据用户名从仓储中查找用户对象
     * 
     * @param username 用户名，不能为空
     * @return 查找到的用户对象，如果不存在则返回null
     */
    User findByUsername(String username);

    /**
     * 检查仓储中是否存在指定用户名的用户
     * 
     * @param username 用户名，不能为空
     * @return 如果存在指定用户名的用户则返回true，否则返回false
     */
    boolean existsByUsername(String username);

    /**
     * 根据用户名、邮箱或手机号从仓储中查找用户对象
     * 
     * @param username 用户名，可以为null
     * @param email    邮箱，可以为null
     * @param phone    手机号，可以为null
     * @param excludeId 要排除的用户ID，可以为null，如果不为null则查找时会排除该ID对应的用户
     * @return 查找到的用户对象，如果不存在则返回null
     */
    User findByAnyOf(String username, String email, String phone, String excludeId);

    /**
     * 从仓储中删除指定的用户对象
     * 
     * @param user 要删除的用户对象，不能为空
     */
    void delete(User user);
}
