package com.uestc.group14.backend.common.config;

import com.uestc.group14.backend.common.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        // 白名单路径 - 使用 antMatchers 风格
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api/v1/resources",        // 资源列表（前台）
                                "/api/v1/resources/**",     // 资源详情及子路径（评分除外，评分需要认证）
                                "/api/v1/news/**",
                                "/api/v1/notices/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/doc.html",
                                "/favicon.ico"
                        ).permitAll()

                        // 静态资源放行
                        .requestMatchers(
                                "/",
                                "/error",
                                "/*.html",
                                "/**/*.html",
                                "/**/*.css",
                                "/**/*.js"
                        ).permitAll()

                        // 其他请求需要认证
                        .anyRequest().authenticated()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 添加 JWT 过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}