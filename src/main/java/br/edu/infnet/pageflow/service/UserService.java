package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.model.Author;
import br.edu.infnet.pageflow.model.BlogAdministrator;
import br.edu.infnet.pageflow.model.User;
import br.edu.infnet.pageflow.model.Visitor;
import br.edu.infnet.pageflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Collection<User> getUsers() {
        return userRepository.getAllUsers();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    // TODO fix code smell - repeated code - start//
    public User createBlogAdministrator(BlogAdministrator admin) {
        return userRepository.save(admin);
    }

    public User createAuthor(Author author) {
        return userRepository.save(author);
    }

    public User createVisitor(Visitor visitor) {
        return userRepository.save(visitor);
    }
    // TODO fix code smell - repeated code - end//

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado!");
        }
        userRepository.deleteById(id);
    }

}
