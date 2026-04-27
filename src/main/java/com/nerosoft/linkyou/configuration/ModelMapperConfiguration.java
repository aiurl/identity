package com.nerosoft.linkyou.configuration;

import java.util.Optional;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nerosoft.linkyou.application.dto.UserDetailDto;
import com.nerosoft.linkyou.domain.aggregate.User;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        var optionalStringConverter = new AbstractConverter<String, Optional<String>>() {
            @Override
            protected Optional<String> convert(String source) {
                return Optional.ofNullable(source);
            }
        };

        modelMapper.typeMap(User.class, UserDetailDto.class).addMappings(mapper -> {
            mapper.using(optionalStringConverter).map(User::getEmail, UserDetailDto::setEmail);
            mapper.using(optionalStringConverter).map(User::getPhone, UserDetailDto::setPhone);
            mapper.using(optionalStringConverter).map(User::getNickname, UserDetailDto::setNickname);
            mapper.using(optionalStringConverter).map(User::getAvatarUrl, UserDetailDto::setAvatarUrl);
            mapper.map(User::getLockoutEnd, UserDetailDto::setLockoutEnd);
        });

        return modelMapper;
    }
}
