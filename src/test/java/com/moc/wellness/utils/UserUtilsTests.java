package com.moc.wellness.utils;

import com.moc.wellness.enums.Role;
import com.moc.wellness.exception.notFound.PrivateRouteException;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.UserRepository;
import com.moc.wellness.setup.UserSetup;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserUtilsTests extends UserSetup {
    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private UserUtils userUtils;


    @Test
    @DisplayName("Get id by email success")
    public void getIdByEmailSuccess() {
        when(userRepository.findByEmail(userUser.getEmail()))
                .thenReturn(Optional.ofNullable(userUser));
        Long id = userUtils.getIdByEmail(userUser.getEmail());

        Assertions.assertEquals(id, userUser.getId());

        verify(userRepository).findByEmail(userUser.getEmail());
    }

    @Test
    @DisplayName("Get id by email not found")
    public void getIdByEmailNotFound() {
        when(userRepository.findByEmail(userUser.getEmail()))
                .thenThrow(new UsernameNotFoundException("User not found"));

        UsernameNotFoundException exp = Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userUtils.getIdByEmail(userUser.getEmail()));

        Assertions.assertEquals(exp.getMessage(), "User not found");

        verify(userRepository).findByEmail(userUser.getEmail());
    }

    @Test
    @DisplayName("Has permission owner")
    public void hasPermissionOwner() {
        Assertions.assertTrue(
                userUtils.hasPermissionToModifyEntity(userUser, userUser.getId())
        );
    }

    @Test
    @DisplayName("Has permission ADMIN")
    public void hasPermissionAdmin() {
        Assertions.assertTrue(
                userUtils.hasPermissionToModifyEntity(userAdmin, userUser.getId())
        );
    }

    @Test
    @DisplayName("No permission")
    public void noPermission() {
        Assertions.assertFalse(
                userUtils.hasPermissionToModifyEntity(userTrainer, userUser.getId())
        );
    }

    @Test
    @DisplayName("Exists trainer or admin not throw")
    public void existsNotThrow() {
        when(userRepository.existsRouteForEntity(trainerId, List.of(Role.ADMIN, Role.TRAINER)))
                .thenReturn(true);

        Assertions.assertDoesNotThrow(() ->
                userUtils.existsTrainerOrAdmin(trainerId));

        verify(userRepository).existsRouteForEntity(trainerId, List.of(Role.ADMIN, Role.TRAINER));
    }

    @Test
    @DisplayName("Does not exists trainer or admin not throw")
    public void notExistsNotThrow() {
        when(userRepository.existsRouteForEntity(trainerId, List.of(Role.ADMIN, Role.TRAINER)))
                .thenReturn(false);

        PrivateRouteException exp = Assertions.assertThrows(PrivateRouteException.class,
                () -> userUtils.existsTrainerOrAdmin(trainerId));

        Assertions.assertEquals(exp.getMessage(), "Not allowed!");

        verify(userRepository).existsRouteForEntity(trainerId, List.of(Role.ADMIN, Role.TRAINER));
    }

    @Test
    @DisplayName("Get principal success")
    public void getPrincipalSuccess() {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userUser, null, userUser.getAuthorities());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UserCustom exp = userUtils.getPrincipal();

        Assertions.assertEquals(exp, userUser);

        verify(securityContext).getAuthentication();

    }

    @Test
    @DisplayName("Get principal null")
    public void getPrincipalNull() {
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        PrivateRouteException ex = Assertions.assertThrows(PrivateRouteException.class,
                () -> userUtils.getPrincipal());

        Assertions.assertEquals(ex.getMessage(), "Not allowed!");

        verify(securityContext).getAuthentication();
    }
}
