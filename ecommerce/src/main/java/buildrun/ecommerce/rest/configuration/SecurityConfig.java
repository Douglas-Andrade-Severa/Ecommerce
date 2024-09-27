package buildrun.ecommerce.rest.configuration;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_WHITELIST = {
            "/", "/home", "/produtos/**", "/categoria/**", "/contato", "/politica", "/termos", "/ecommerce/**"  // Rotas públicas
    };

    private static final String[] COLLABORATOR_PORTAL = {
            "/admin/**", "/portal/**", "/gerenciamento/**"  // Portal de colaboradores (administração)
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/css/**", "/js/**").permitAll()
                        .requestMatchers(PUBLIC_WHITELIST).permitAll() // Permite acesso às rotas públicas dos clientes
                        .requestMatchers("/portal/login").permitAll() // Permite acesso à página de login do portal
                        .requestMatchers(COLLABORATOR_PORTAL).permitAll() // Permite acesso às rotas do portal de colaboradores
                        .requestMatchers("/portal/**").hasRole("COLLABORATOR") // Protege as rotas do portal
                        .anyRequest().authenticated() // Qualquer outra rota precisa estar autenticada
                )
                .formLogin(form -> form
                        .loginPage("/ecommerce/login")
                        .loginProcessingUrl("/ecommerce/login")
                        .defaultSuccessUrl("/ecommerce", true) // Redireciona após login para o e-commerce
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/ecommerce/logout")
                        .logoutSuccessUrl("/ecommerce") // Redireciona para a home do e-commerce após logout
                        .permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/access-denied")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/ecommerce/login?sessao")
                        .maximumSessions(1)
                );

        // Configuração específica para o portal
        http.formLogin(form -> form
                .loginPage("/portal/login")
                .loginProcessingUrl("/portal/login")
                .defaultSuccessUrl("/portal", true) // Redireciona após login para o portal
                .permitAll()
        ).logout(logout -> logout
                .logoutUrl("/portal/logout")
                .logoutSuccessUrl("/portal/login") // Redireciona para a página de login do portal após logout
                .permitAll()
        );

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {
                se.getSession().setMaxInactiveInterval(3600); // Tempo máximo de inatividade
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                // Lógica adicional ao destruir sessão
            }
        };
    }

    @Bean
    public SessionInformationExpiredStrategy sessionExpiredStrategy() {
        return event -> event.getResponse().sendRedirect("/login?error=sessionExpired");
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher(); // Suporte à limitação de sessões
    }
}
