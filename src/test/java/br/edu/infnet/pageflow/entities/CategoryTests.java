package br.edu.infnet.pageflow.entities;

import net.jqwik.api.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class CategoryTests {

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

    @Property
    void testIdMustBeInteger(@ForAll("validUserId") Integer userId) {

        Category category = new Category();
        category.setId(userId);

        assertNotNull(category);
        assertSame(category.getId(), userId);
    }

    @Property
    void testNameMustBeString(@ForAll("validUsername") String name) {
        Category category = new Category();
        category.setName(name);

        assertNotNull(category);
        assertSame(category.getName(), name);
    }
}
