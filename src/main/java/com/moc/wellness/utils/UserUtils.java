package com.moc.wellness.utils;

import com.moc.wellness.exception.notFound.PrivateRouteException;
import com.moc.wellness.enums.Role;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;


    public Long getIdByEmail(String email) {
        UserCustom userCustom = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userCustom.getId();
    }

    public boolean hasPermissionToModifyEntity(UserCustom authUser, Long entityUserId) {
        return authUser.getRole() == Role.ADMIN || Objects.equals(authUser.getId(), entityUserId);
    }

    public void existsTrainerOrAdmin(Long trainerId) {
        if (!userRepository.existsRouteForEntity(trainerId, List.of(Role.ADMIN, Role.TRAINER))) {
            throw new PrivateRouteException();
        }
    }

    public UserCustom getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new PrivateRouteException();
        }
        return (UserCustom) authentication.getPrincipal();
    }
}
