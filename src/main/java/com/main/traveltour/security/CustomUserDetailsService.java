package com.main.traveltour.security;

import com.main.traveltour.entity.Roles;
import com.main.traveltour.entity.Users;
import com.main.traveltour.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersRepository.findUserByEmail(email);

        if (user != null) {
            if (!user.getIsActive()) {
                throw new UsernameNotFoundException("User not found !");
            }
            return new User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
        } else {
            throw new UsernameNotFoundException("Email and password wrong !");
        }
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Roles> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNameRole())).collect(Collectors.toList());
    }
}
