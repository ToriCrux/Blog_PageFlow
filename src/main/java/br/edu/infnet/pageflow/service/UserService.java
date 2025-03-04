package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.dto.SignupRequest;
import br.edu.infnet.pageflow.entities.*;
import br.edu.infnet.pageflow.repository.PasswordTokenRepository;
import br.edu.infnet.pageflow.repository.UserRepository;
import br.edu.infnet.pageflow.utils.BlogUserRoles;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordTokenRepository passwordTokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Collection<BlogUser> getUsers() {
        return userRepository.getAllUsers();
    }

    public Optional<BlogUser> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public BlogUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this email: " + email));
    }

    public BlogUser createUser(SignupRequest signupRequest) {

        if(userRepository.existsByEmail(signupRequest.getEmail())) {
            return null;
        }

        BlogUser newBlogUser = instantiateUserByRole(signupRequest.getRole());
        BeanUtils.copyProperties(signupRequest, newBlogUser);

        String encodedPassword = passwordEncoder.encode(newBlogUser.getPassword());
        newBlogUser.setPassword(encodedPassword);

        userRepository.save(newBlogUser);
        return newBlogUser;
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado!");
        }
        userRepository.deleteById(id);
    }

    private BlogUser instantiateUserByRole(BlogUserRoles role) {
        return switch (role) {
            case BlogUserRoles.ADMINISTRATOR -> new BlogAdministrator();
            case BlogUserRoles.VISITOR -> new Visitor();
            case BlogUserRoles.AUTHOR -> new Author();
        };
    }

    public void createPasswordResetTokenForUser(BlogUser user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(passwordResetToken);
    }

    public BlogUser getUserByPasswordResetToken(String token) {
        return passwordTokenRepository.findByToken(token).getBlogUser();
    }

    public void changeUserPassword(BlogUser user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
