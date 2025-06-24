package br.edu.infnet.pageflow.security;

import br.edu.infnet.pageflow.security.configuration.WebSecurityConfig;
import br.edu.infnet.pageflow.security.jwt.JwtRequestFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class WebSecurityConfigTest {

    @Test
    void testSecurityConfigurationLoadsFilterChain() {
        new ApplicationContextRunner()
                .withUserConfiguration(TestSecurityConfig.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(SecurityFilterChain.class);
                    assertThat(context).hasSingleBean(JwtRequestFilter.class);
                });
    }

    @Configuration
    static class TestSecurityConfig extends WebSecurityConfig {
        public TestSecurityConfig() {
            super(mock(JwtRequestFilter.class));
        }

        @Bean
        public JwtRequestFilter jwtRequestFilter() {
            return mock(JwtRequestFilter.class);
        }
    }
}
