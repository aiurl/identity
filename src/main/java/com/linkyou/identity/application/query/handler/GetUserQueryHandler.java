package com.linkyou.identity.application.query.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.ListUsersQuery;
import com.linkyou.identity.application.query.dto.UserDto;
import com.linkyou.identity.domain.model.aggregate.User;
import com.linkyou.identity.domain.repository.UserRepository;
import com.linkyou.identity.domain.model.valueobject.UserId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GetUserQueryHandler implements Command.Handler<ListUsersQuery, List<UserDto>> {

    private final UserRepository userRepository;

    public GetUserQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserDto> findById(String id) {
        return userRepository.findById(new UserId(id)).map(this::toView);
    }

    @Override
    public List<UserDto> handle(ListUsersQuery query) {
        return userRepository.findAll().stream().map(this::toView).toList();
    }

    public List<UserDto> findAll() {
        return handle(new ListUsersQuery());
    }

    private UserDto toView(User user) {
        return new UserDto(
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
