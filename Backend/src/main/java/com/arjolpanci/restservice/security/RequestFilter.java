package com.arjolpanci.restservice.security;

import com.arjolpanci.restservice.controllers.AuthenticationController;
import com.arjolpanci.restservice.dbmodels.User;
import com.arjolpanci.restservice.services.JWTUserDetailsService;
import com.arjolpanci.restservice.services.UsersService;
import com.arjolpanci.restservice.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestFilter extends OncePerRequestFilter {

    private static final Logger LOG = LogManager.getLogger(RequestFilter.class);

    @Autowired
    private JWTUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;

        if(authHeader != null){
            try{
                token = authHeader.split(" ")[1];
                username = jwtUtils.getUserFromToken(token);
            } catch (IllegalArgumentException e) {
                LOG.warn("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                LOG.warn("JWT Token has expired");
            } catch(Exception e) {
                LOG.warn("Wrong auth header format");
            }
        }else {
            LOG.warn("No auth header set");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            if(jwtUtils.isValid(token, userDetails)){
                UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                userPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
