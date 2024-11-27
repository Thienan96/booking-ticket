package com.example.booking.base.security;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

import com.example.booking.entity.ClientEntity;
import com.example.booking.entity.RoleEntity;
import com.example.booking.repository.ClientRepos;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtUserDetailsService implements UserDetailsService {
    private final ClientRepos clientRepos;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ClientEntity> optClientEntity = clientRepos.findByEmail(username);
        if (optClientEntity.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        } else {
            ClientEntity clientEntity = optClientEntity.get();
            Collection<RoleEntity> grantList = clientEntity.getRoles();
            List<String> grantListStr = grantList.stream()
                    .map(role -> role.getCode())
                    .collect(Collectors.toList());
            UserBuilder user = User.withUsername(clientEntity.getEmail()).password(clientEntity.getPassword())
                    .roles(String.join(", ", grantListStr));
            return user.build();
        }
    }

}
