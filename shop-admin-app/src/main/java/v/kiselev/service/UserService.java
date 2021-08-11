package v.kiselev.service;


import org.springframework.data.domain.Page;
import v.kiselev.controller.DTO.UserDto;
import v.kiselev.controller.UserListParams;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAll();

    Page<UserDto> findWithFilter(UserListParams userListParams);

    Optional<UserDto> findById(Long id);

    void save(UserDto user);

    void deleteById(Long id);
}