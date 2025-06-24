package br.edu.infnet.pageflow.entities;

import br.edu.infnet.pageflow.utils.AdministratorRoles;
import br.edu.infnet.pageflow.utils.BlogUserRoles;
import net.jqwik.api.*;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BlogUserTests {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Provide
    public Arbitrary<Integer> validUserId() {
        return Arbitraries.integers()
                .between(1, 2147483647);
    }

    @Provide
    public Arbitrary<String> validUsername() {
        return Arbitraries.strings()
                .withCharRange('a', 'z')
                .ofMinLength(5)
                .ofMaxLength(16)
                .filter(username -> !username.isBlank());
    }

    @Provide
    public Arbitrary<String> validEmails() {
        Arbitrary<String> localPart = Arbitraries.strings()
                .withChars("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789._%+-")
                .ofMinLength(1)
                .ofMaxLength(20);

        Arbitrary<String> domain = Arbitraries.strings()
                .withChars("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-")
                .ofMinLength(1)
                .ofMaxLength(10);

        Arbitrary<String> tld = Arbitraries.of("com", "org", "net", "edu", "gov", "br", "us", "uk");

        return Combinators.combine(localPart, domain, tld)
                .as((l, d, t) -> l + "@" + d + "." + t)
                .filter(email -> EMAIL_PATTERN.matcher(email).matches());
    }

    @Provide
    public Arbitrary<String> validPasswords() {
        Arbitrary<String> letters = Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1);
        Arbitrary<String> numbers = Arbitraries.strings().withCharRange('0', '9').ofMinLength(1);
        Arbitrary<String> mixed = Arbitraries.strings()
                .withChars("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")
                .ofMinLength(8)
                .ofMaxLength(16);

        return Combinators.combine(letters, numbers, mixed)
                .as((l, n, m) -> l + n + m)
                .filter(pwd -> pwd.length() >= 8 && pwd.length() <= 16);
    }

    @Provide
    public Arbitrary<BlogUser> blogUserProvider() {
        Arbitrary<Integer> userId = validUserId();
        Arbitrary<String> name = Arbitraries.strings().withCharRange('a', 'z').ofMinLength(3).ofMaxLength(20);
        Arbitrary<String> username = validUsername();
        Arbitrary<String> email = validEmails();
        Arbitrary<String> password = validPasswords();
        Arbitrary<BlogUserRoles> role = Arbitraries.of(BlogUserRoles.class);
        Arbitrary<LocalDateTime> createdAt = Arbitraries.just(LocalDateTime.now());
        Arbitrary<LocalDateTime> updatedAt = Arbitraries.just(null);

        return Combinators.combine(userId, name, username, email, password, role, createdAt, updatedAt)
                .as((id, n, u, e, p, r, c, uA) -> {
                    BlogUser user = new BlogUser();
                    user.setId(id);
                    user.setName(n);
                    user.setUsername(u);
                    user.setEmail(e);
                    user.setPassword(p);
                    user.setRole(r);
                    user.setCreatedAt(null);
                    user.setUpdatedAt(uA);
                    return user;
                });
    }

    @Provide
    Arbitrary<BlogUserRoles> blogUserRoles() {
        return Arbitraries.of(BlogUserRoles.values());
    }

    @Provide
    Arbitrary<AdministratorRoles> administratorRoles() {
        return Arbitraries.of(AdministratorRoles.values());
    }

    @Property
    void testBlogUserCreation(
            @ForAll("validUserId") Integer userId,
            @ForAll("validUsername") String username,
            @ForAll("validEmails") String email,
            @ForAll("validPasswords") String password,
            @ForAll("blogUserProvider") BlogUser generatedUser
    ) {
        BlogUser blogUser = new BlogUser("Test User", username, email, password);
        blogUser.setId(userId);
        blogUser.setRole(generatedUser.getRole());
        blogUser.setCreatedAt(generatedUser.getCreatedAt());
        blogUser.setUpdatedAt(LocalDateTime.now());

        assertThat(generatedUser.getId()).isInstanceOf(Integer.class);
        assertThat(generatedUser.getName()).isNotBlank();
        assertThat(generatedUser.getPassword().length()).isBetween(8, 16);
        assertThat(generatedUser.getRole()).isNotNull();
        assertThat(blogUser.getUsername()).isEqualTo(username);
        assertThat(blogUser.getEmail()).isEqualTo(email);
        assertThat(blogUser.getEmail()).contains("@");
        assertThat(blogUser.getPassword()).isEqualTo(password);
        assertThat(blogUser.getUpdatedAt()).isNotNull();
    }

    @Property
    void testBlogUserMustBeNotNull (
            @ForAll("validUserId") Integer userId,
            @ForAll("validUsername") String username,
            @ForAll("validEmails") String email,
            @ForAll("validPasswords") String password
    ) {

        BlogUser user = new BlogUser(email, password);
        user.setId(userId);
        user.setUsername(username);

        assertNotNull(user);
        assertThat(user).isExactlyInstanceOf(BlogUser.class);

    }

    @Property
    void testAuthorMustBeNotNull (
            @ForAll("validUserId") Integer userId,
            @ForAll("validUsername") String username,
            @ForAll("validEmails") String email,
            @ForAll("validPasswords") String password
    ) {

        Author author = new Author("Author User", username, email, password);
        author.setId(userId);
        author.setRole(BlogUserRoles.AUTHOR);
        author.setBio("Lorem ipsum dolor sit amet");

        assertNotNull(author);
        assertThat(author).isExactlyInstanceOf(Author.class);

    }

    @Property
    void testAuthorMustBeInstantiateWithToParams (
            @ForAll("validUserId") Integer userId,
            @ForAll("validUsername") String username,
            @ForAll("validEmails") String email,
            @ForAll("validPasswords") String password
    ) {

        Author author = new Author(email, password);
        author.setId(userId);
        author.setRole(BlogUserRoles.AUTHOR);
        author.setBio("Lorem ipsum dolor sit amet");

        assertNotNull(author);
        assertThat(author).isExactlyInstanceOf(Author.class);

    }

    @Property
    void testAuthorMustBeValidBlogUser (
            @ForAll("validUserId") Integer userId,
            @ForAll("validUsername") String username,
            @ForAll("validEmails") String email,
            @ForAll("validPasswords") String password
    ) {

        Author author = new Author();
        author.setId(userId);
        author.setName("Author User");
        author.setUsername(username);
        author.setEmail(email);
        author.setPassword(password);
        author.setRole(BlogUserRoles.AUTHOR);
        author.setBio("Lorem ipsum dolor sit amet");

        assertThat(author.getRole()).isEqualTo(BlogUserRoles.AUTHOR);
        assertThat(author.getUsername()).isEqualTo(username);
        assertThat(author.getEmail()).isEqualTo(email);
        assertThat(author.getEmail()).contains("@");
        assertThat(author.getPassword()).isEqualTo(password);
        assertThat(author.getBio().length()).isGreaterThan(25);

    }

    @Property
    void testBlogAdministratorMustBeNotNull (
            @ForAll("blogUserRoles") BlogUserRoles userRoles,
            @ForAll("administratorRoles") AdministratorRoles adminRoles
    ) {

        BlogAdministrator blogAdministrator = new BlogAdministrator();
        blogAdministrator.setRole(userRoles);
        blogAdministrator.setAdminRole(adminRoles);

        assertNotNull(blogAdministrator);
        assertThat(blogAdministrator).isExactlyInstanceOf(BlogAdministrator.class);

    }

    @Property
    void testBlogAdministratorMustBeValidBlogUser (
            @ForAll("validUserId") Integer userId,
            @ForAll("validUsername") String username,
            @ForAll("validEmails") String email,
            @ForAll("validPasswords") String password
    ) {

        BlogAdministrator blogAdministrator = new BlogAdministrator();
        blogAdministrator.setId(userId);
        blogAdministrator.setName("Administrator User");
        blogAdministrator.setUsername(username);
        blogAdministrator.setEmail(email);
        blogAdministrator.setPassword(password);
        blogAdministrator.setRole(BlogUserRoles.ADMINISTRATOR);
        blogAdministrator.setAdminRole(AdministratorRoles.SUPER_ADMIN);

        assertThat(blogAdministrator.getRole()).isEqualTo(BlogUserRoles.ADMINISTRATOR);
        assertThat(blogAdministrator.getAdminRole()).isEqualTo(AdministratorRoles.SUPER_ADMIN);
        assertThat(blogAdministrator.getUsername()).isEqualTo(username);
        assertThat(blogAdministrator.getEmail()).isEqualTo(email);
        assertThat(blogAdministrator.getEmail()).contains("@");
        assertThat(blogAdministrator.getPassword()).isEqualTo(password);

    }

    @Property
    void testBlogVisitorMustBeValidBlogUser (
            @ForAll("validUserId") Integer userId,
            @ForAll("validUsername") String username,
            @ForAll("validEmails") String email,
            @ForAll("validPasswords") String password
    ) {

        Visitor visitor = new Visitor();
        visitor.setId(userId);
        visitor.setName("Administrator User");
        visitor.setUsername(username);
        visitor.setEmail(email);
        visitor.setPassword(password);
        visitor.setRole(BlogUserRoles.VISITOR);
        visitor.setCanComment(Boolean.TRUE);


        assertThat(visitor.getRole()).isEqualTo(BlogUserRoles.VISITOR);
        assertThat(visitor.getUsername()).isEqualTo(username);
        assertThat(visitor.getEmail()).isEqualTo(email);
        assertThat(visitor.getEmail()).contains("@");
        assertThat(visitor.getPassword()).isEqualTo(password);
        assertTrue(visitor.getCanComment());

    }
}
