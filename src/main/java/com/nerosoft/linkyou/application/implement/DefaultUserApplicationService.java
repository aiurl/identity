package com.nerosoft.linkyou.application.implement;

import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.nerosoft.linkyou.application.contract.UserApplicationService;
import com.nerosoft.linkyou.application.dto.UserCreateDto;
import com.nerosoft.linkyou.application.dto.UserDetailDto;
import com.nerosoft.linkyou.domain.aggregate.User;
import com.nerosoft.linkyou.domain.repository.UserRepository;
import com.nerosoft.linkyou.domain.service.UserRegistrationService;
import com.nerosoft.linkyou.seedwork.BaseApplicationService;

import reactor.core.publisher.Mono;

@Service
public class DefaultUserApplicationService extends BaseApplicationService implements UserApplicationService {
    private final UserRepository userRepository;
    private final UserRegistrationService userRegistrationService;
    private final ModelMapper modelMapper;

    public DefaultUserApplicationService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userRegistrationService = new UserRegistrationService(userRepository);
        this.modelMapper = modelMapper;
    }

    @Override
    public Mono<String> createAsync(UserCreateDto data) {
        return Mono.fromCallable(() -> userRegistrationService.register(
                data.username(),
                data.password(),
                data.email(),
                data.phone(),
                data.nickname()).getId());
    }

    @Override
    public Mono<Void> updateAsync(String id, HashMap<String, Object> data) {
        return Mono.fromRunnable(() -> {
            User user = requireUser(id);
            if (data.containsKey("email")) {
                user.changeEmail((String) data.get("email"));
            }
            if (data.containsKey("phone")) {
                user.changePhone((String) data.get("phone"));
            }
            userRepository.save(user);
        });
    }

    @Override
    public Mono<UserDetailDto> getAsync(String id) {
        return Mono.fromSupplier(() -> toDetailDto(requireUser(id)));
    }

    private User requireUser(String id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        return user;
    }

    private UserDetailDto toDetailDto(User user) {
        UserDetailDto dto = modelMapper.map(user, UserDetailDto.class);
        dto.setRoles(List.of());
        dto.setAuthorities(List.of());
        return dto;
    }
}
