package com.example.demo.jwt;

import com.example.demo.entity.UrlApiRole;
import com.example.demo.entity.UserRole;
import com.example.demo.repo.UrlApiRepo;
import com.example.demo.security.CustomUserDetail;
import com.example.demo.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.*;


@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private UrlApiRepo urlApiRepo;

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validationToken(jwt)) {
                String username = jwtTokenProvider.getUserNameFromJwt(jwt);
                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                    List<String> roleNames = authorities.stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList());
                    List<String> allowedUrls = urlApiRepo.findNameUrlByRoles(roleNames);
                    log.info("Các path có quyền trong role " +allowedUrls.toString());
                    String requestUrl = request.getRequestURI();
                    if (!isUrlAllowed(requestUrl, allowedUrls)) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write(String.valueOf(of("message", "Không có quyền truy cập")));
                        return;
                    }
                }
            }

        } catch (Exception ex) {
            log.error("fail on set user Authentication", ex);
        }
        filterChain.doFilter(request, response);
    }


//    private List<String> getAllowedUrlsForRoles(Collection<? extends GrantedAuthority> roles) {
//        return urlApiRepo.findNameUrlByRoles(roles);
//        // Implement this method to fetch allowed URLs from the database based on the roles
//    }

    private boolean isUrlAllowed(String requestUrl, List<String> allowedUrls) {
        for (String url : allowedUrls) {
            if (requestUrl.equals(url) || requestUrl.startsWith(url)) {
                return true;
            }
        }
        return false;
    }
}
