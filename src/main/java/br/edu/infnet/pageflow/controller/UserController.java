package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.model.Author;
import br.edu.infnet.pageflow.model.BlogAdministrator;
import br.edu.infnet.pageflow.model.User;
import br.edu.infnet.pageflow.model.Visitor;
import br.edu.infnet.pageflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/v1/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<Collection<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // TODO fix code smell - repeated code - start//
    @PostMapping("/administrator")
    public ResponseEntity<User> createAdministrator(@RequestBody @Valid BlogAdministrator admin) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createBlogAdministrator(admin));
    }

    @PostMapping("/author")
    public ResponseEntity<User> createAuthor(@RequestBody @Valid Author author) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createAuthor(author));
    }

    @PostMapping("/visitor")
    public ResponseEntity<User> createVisitor(@RequestBody @Valid Visitor visitor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createVisitor(visitor));
    }
    // TODO fix code smell - repeated code - end//

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
