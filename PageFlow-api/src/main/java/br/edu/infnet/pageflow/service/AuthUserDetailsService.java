package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.repository.UserRepository;
import br.edu.infnet.pageflow.utils.BlogUserRoles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        BlogUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this email: " + email));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + getUserRole(user)));

        return new User(user.getEmail(), user.getPassword(), authorities);
    }

    private BlogUserRoles getUserRole(BlogUser user) {
        return switch (String.valueOf(user.getRole())) {
            case "ADMINISTRATOR" -> BlogUserRoles.ADMINISTRATOR;
            case "AUTHOR" -> BlogUserRoles.AUTHOR;
            case "VISITOR" -> BlogUserRoles.VISITOR;
            default -> null;
        };
    }
}
