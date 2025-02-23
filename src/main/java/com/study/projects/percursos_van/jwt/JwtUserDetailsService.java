package com.study.projects.percursos_van.jwt;

import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.model.enums.Role;
import com.study.projects.percursos_van.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    public final JwtUtils jwtUtils;
    public final UserService userService;

    public JwtToken generateToken(String username){
        Role role = userService.findRoleFromEmail(username);
        return jwtUtils.generateToken(username, role.name());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByEmail(username);
        return new JwtUserDetails(user);
    }
}
