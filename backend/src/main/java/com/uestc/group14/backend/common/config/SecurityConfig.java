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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // 启用 CORS 支持

                .authorizeHttpRequests(auth -> auth
                        // 白名单路径 - 使用 antMatchers 风格
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api/v1/resources",        // 资源列表（前台）
                                "/api/v1/resources/**",     // 资源详情及子路径（评分除外，评分需要认证）
                                "/api/v1/news/**",
                                "/api/v1/notices/**",
                                "/api/v1/laboratories/**",  // 实验室列表/详情（前台）
                                "/api/v1/devices/**",       // 设备列表/详情（前台）
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/doc.html",
                                "/favicon.ico",
                                "/files/**",          // 你的资源目录
                                "/images/**",
                                "/reports/**",
                                "/**/*.jpg",
                                "/static/**"          // Spring Boot 默认的静态资源路径（可选）
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}