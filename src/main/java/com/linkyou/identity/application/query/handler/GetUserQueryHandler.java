package com.linkyou.identity.application.query.handler;

import com.linkyou.identity.application.query.dto.UserView;
import com.linkyou.identity.domain.model.aggregate.User;
import com.linkyou.identity.domain.repository.UserRepository;
import com.linkyou.identity.domain.model.valueobject.UserId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GetUserQueryHandler {

    private final UserRepository userRepository;

    public GetUserQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserView> findById(String id) {
        return userRepository.findById(new UserId(id)).map(this::toView);
    }

    public List<UserView> findAll() {
        return userRepository.findAll().stream().map(this::toView).toList();
    }

    private UserView toView(User user) {
        return new UserView(
                user.getId().value(),
                user.getUsername(),
                user.getNickname(),
                user.getPhone(),
                user.getEmail().value(),
                user.getAccessFailedCount(),
                user.getLockoutEnd(),
                user.getCreatedAt(),
                user.getPasswordChangedTime(),
                user.isActive(),
                user.getRoles().stream().map(role -> role.getName()).toList()
        );
    }
}
