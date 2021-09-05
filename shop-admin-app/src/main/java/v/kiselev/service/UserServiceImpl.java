package v.kiselev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import v.kiselev.controller.DTO.RoleDto;
import v.kiselev.controller.DTO.UserDto;
import v.kiselev.controller.UserListParams;
import v.kiselev.persist.model.Role;
import v.kiselev.persist.model.User;
import v.kiselev.persist.UserRepository;
import v.kiselev.persist.UserSpecifications;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passEncoder) {
        this.userRepository = userRepository;
        this.passEncoder = passEncoder;
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(user ->
                new UserDto(user.getId(),
                        user.getUsername(),
                        user.getAge(),
                        mapRolesDto(user)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDto> findWithFilter(UserListParams userListParams) {
        Specification<User> spec = Specification.where(null);

        if (userListParams.getUsernameFilter() != null && !userListParams.getUsernameFilter().isBlank()) {
            spec = spec.and(UserSpecifications.usernamePrefix(userListParams.getUsernameFilter()));
        }
        if (userListParams.getMinAge() != null) {
            spec = spec.and(UserSpecifications.minAge(userListParams.getMinAge()));
        }
        if (userListParams.getMaxAge() != null) {
            spec = spec.and(UserSpecifications.maxAge(userListParams.getMaxAge()));
        }

        return userRepository.findAll(spec,
                PageRequest.of(
                        Optional.ofNullable(userListParams.getPage()).orElse(1) - 1,
                        Optional.ofNullable(userListParams.getSize()).orElse(3),
                        Sort.by(Optional.ofNullable(userListParams.getSortField())
                                .filter(c -> !c.isBlank())
                                .orElse("id")))).map(user ->
                new UserDto(user.getId(), user.getUsername(), user.getAge(), mapRolesDto(user)));
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id).map(user ->
                new UserDto(user.getId(), user.getUsername(), user.getAge(), mapRolesDto(user)));
    }

    @Override
    public void save(UserDto userDto) {
        User user = new User(userDto.getId(),
                userDto.getUsername(),
                passEncoder.encode(userDto.getPassword()),
                userDto.getAge(),
                userDto.getRoles().stream().
                        map(roleDto -> new Role(roleDto.getId(), roleDto.getName()))
                        .collect(Collectors.toSet()));
        userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private static Set<RoleDto> mapRolesDto(User user) {
        return user.getRoles().stream().
                map(role -> new RoleDto(role.getId(), role.getName()) ).collect(Collectors.toSet());
    }
}
