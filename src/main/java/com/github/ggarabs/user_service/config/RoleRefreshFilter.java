package com.github.ggarabs.user_service.config;

import com.github.ggarabs.user_service.adapters.outbound.UserRepository;
import com.github.ggarabs.user_service.adapters.outbound.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class RoleRefreshFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public RoleRefreshFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getName() != null && !"anonymousUser".equals(authentication.getName())) {
            userRepository.findByEmailIgnoreCase(authentication.getName()).ifPresentOrElse(user -> {
                UserRole role = user.getRole() == null ? UserRole.CANDIDATE : user.getRole();
                var refreshed = UsernamePasswordAuthenticationToken.authenticated(
                        user.getEmail(), null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role.name())));
                refreshed.setDetails(authentication.getDetails());
                SecurityContextHolder.getContext().setAuthentication(refreshed);
            }, () -> SecurityContextHolder.clearContext());
        }
        filterChain.doFilter(request, response);
    }
}
