package com.equipo01.featureflag.featureflag.config;

import com.equipo01.featureflag.featureflag.model.User;
import com.equipo01.featureflag.featureflag.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom service for loading user details. Extends {@link UserDetailsService} and implements the
 * loadUserByUsername method to search for users in the database. -Uses {@link UserRepository} to
 * access user data.
 *
 * @author alex
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
  // Repository to access user data
  private final UserRepository userRepository;

  /**
   * Constructor to initialize service dependencies
   *
   * @param userRepository
   */
  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Loads a user by their username. -1.Searches for the user in the database using userRepository.
   * -2.If the user does not exist, throws a UsernameNotFoundException. -3.If the user exists,
   * converts the User object into a Spring Security UserDetails.
   *
   * @param username the username to search for
   * @throws UsernameNotFoundException if the user does not exist
   * @return a UserDetails object with the user's information
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User userEntity =
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException("User with username " + username + " not found"));

    return org.springframework.security.core.userdetails.User.builder()
        .username(userEntity.getUsername())
        .password(userEntity.getPassword())
        .roles(userEntity.getRole().toString())
        .disabled(!userEntity.isEnabled())
        .build();
  }
}
