package config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Desabilitando CSRF para facilitar os testes
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios/**").permitAll() // Permitindo acesso a rotas de usuário
                        .anyRequest().authenticated() // Bloqueando todas as outras rotas
                )
                .httpBasic(); // Usando autenticação básica para testes
        return http.build();
    }
}
