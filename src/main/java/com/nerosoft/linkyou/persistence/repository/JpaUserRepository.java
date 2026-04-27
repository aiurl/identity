package com.nerosoft.linkyou.persistence.repository;

import java.util.function.Predicate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nerosoft.linkyou.domain.aggregate.User;
import com.nerosoft.linkyou.domain.repository.UserRepository;
import com.nerosoft.linkyou.persistence.model.UserEntity;

import jakarta.persistence.NoResultException;

@Repository
@ConditionalOnExpression("'${repository.type}'.equals('jpa')") // 当 repository.type 配置为 jpa 时启用
public class JpaUserRepository implements UserRepository {

    // @Autowired
    // EntityManager entityManager;

    @Autowired
    private ModelMapper mapper;

    private final JpaRepository<UserEntity, String> repository;

    public JpaUserRepository(JpaRepository<UserEntity, String> jpaRepository) {
        this.repository = jpaRepository;
    }

    @Override
    public void save(User user) {
        UserEntity entity = mapper.map(user, UserEntity.class);
        repository.save(entity);
    }

    @Override
    public User findById(String id) {
        return repository.findById(id).map(entity -> mapper.map(entity, User.class)).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        try {
            return repository.findAll().stream()
                    .filter(entity -> entity.getUsername() != null && entity.getUsername().equalsIgnoreCase(username))
                    .findFirst()
                    .map(entity -> mapper.map(entity, User.class))
                    .orElse(null);
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.findAll().stream()
                .anyMatch(entity -> entity.getUsername() != null && entity.getUsername().equalsIgnoreCase(username));
    }

    @Override
    public void delete(User user) {
        repository.deleteById(user.getId());
    }

    @Override
    public User findByAnyOf(String username, String email, String phone, String excludeId) {
        try {
            // 判断传入参数，如果前三个条件都无效，则抛出异常
            if (username == null && email == null && phone == null) {
                throw new IllegalArgumentException("At least one of username, email, or phone must be provided");
            }

            // 构建一个动态的过滤条件，根据传入的参数来过滤用户实体
            Predicate<UserEntity> predicate = entity -> false; // 初始为一个永远返回 false 的谓词

            if (username != null) {
                predicate = predicate
                        .or(entity -> entity.getUsername() != null && entity.getUsername().equalsIgnoreCase(username));
            }
            if (email != null) {
                predicate = predicate
                        .or(entity -> entity.getEmail() != null && entity.getEmail().equalsIgnoreCase(email));
            }
            if (phone != null) {
                predicate = predicate
                        .or(entity -> entity.getPhone() != null && entity.getPhone().equalsIgnoreCase(phone));
            }

            return repository.findAll().stream()
                    .filter(predicate.and(entity -> excludeId == null || !entity.getId().equals(excludeId))) // 排除指定 ID
                                                                                                             // 的用户
                    .findFirst()
                    .map(entity -> mapper.map(entity, User.class))
                    .orElse(null);
        } catch (NoResultException ex) {
            return null;
        }
    }
}
