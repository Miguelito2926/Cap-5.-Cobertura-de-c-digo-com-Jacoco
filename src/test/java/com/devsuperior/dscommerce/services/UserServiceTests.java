package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.UserDTO;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.projections.UserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;
import com.devsuperior.dscommerce.tests.UserFactory;
import com.devsuperior.dscommerce.util.CustomUserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomUserUtil customUserUtil;

    private String existingUsername, nonExistUsername;
    private User user;
    private List<UserDetailsProjection> userDetails;

    @BeforeEach
    void setUp() throws Exception{

        existingUsername = "maria@gmail.com";
        nonExistUsername = "user@gmail.com";
        user = UserFactory.createCustomClientUser(1L, existingUsername);
        userDetails = UserDetailsFactory.createCustomAdminUser(existingUsername);

        Mockito.when(userRepository.searchUserAndRolesByEmail(existingUsername)).thenReturn(userDetails);
        Mockito.when(userRepository.searchUserAndRolesByEmail(nonExistUsername)).thenReturn(new ArrayList<>());
        Mockito.when(userRepository.findByEmail(existingUsername)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByEmail(nonExistUsername)).thenReturn(Optional.empty());
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
        UserDetails result = userService.loadUserByUsername(existingUsername);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingUsername, result.getUsername());
    }

    @Test
    public void loadUserByUsernameShouldThrowReturnUsernameNotFoundExceptionWhenUserDoesNotExists() {

        Assertions.assertThrows(
             UsernameNotFoundException.class,   () -> {
                    userService.loadUserByUsername(nonExistUsername);;
                });
    }

    @Test
    public void authenticatedShouldReturnUserWhenUserExists(){
        Mockito.when(customUserUtil.getLoggedUsername()).thenReturn(existingUsername);

        User authenticated = userService.authenticated();
        Assertions.assertNotNull(authenticated);
        Assertions.assertEquals(authenticated.getUsername(), existingUsername);
    }

    @Test
    public void authenticatedShouldThrowReturnUsernameNotFoundExceptionWhenUserDoesNotExists() {

        Mockito.doThrow(ClassCastException.class).when(customUserUtil).getLoggedUsername();
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.authenticated();
        });
    }

    @Test
    public void getMeShouldReturnUserDTOWhenLogged() {
        Mockito.when(customUserUtil.getLoggedUsername()).thenReturn(existingUsername);
        UserDTO userDTO = userService.getMe();
        Assertions.assertNotNull(userDTO);
        Assertions.assertEquals(existingUsername, userDTO.getName());
    }
}
