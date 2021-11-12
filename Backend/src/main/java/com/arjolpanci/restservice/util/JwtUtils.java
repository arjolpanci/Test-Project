package com.arjolpanci.restservice.util;

import com.arjolpanci.restservice.dbmodels.User;
import com.arjolpanci.restservice.security.JWTUserDetails;
import com.arjolpanci.restservice.services.UsersService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils implements Serializable {

    public static final long TOKEN_LIFESPAN = 2 * 60 * 60;

    @Autowired
    private UsersService usersService;

    @Value("${jwt.secret}")
    private String secretKey;

    private Claims getTokenClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public String generateToken(User user, String role){
        Map<String, Object> claims = new HashMap<>();
        String data = user.getUsername() + ":" + role;
        return Jwts.builder().setClaims(claims).setSubject(data).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_LIFESPAN * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public String getUserFromToken(String token){
        Claims claims = getTokenClaims(token);
        String data = claims.getSubject();
        return data.split(":")[0];
    }

    public String getRoleFromToken(String token){
        Claims claims = getTokenClaims(token);
        String data = claims.getSubject();
        return data.split(":")[1];
    }

    public void verifyUserAuthority(User user, String token, String compareAgainstRole, Long compareAgainstId) throws InsufficientAccessException, Exception {
        String username = getUserFromToken(token);
        if(user == null) {
            user = usersService.findUserByUsername(username);
        }
        String role = getRoleFromToken(token);

        //In this case, I am giving the supervisor full access.
        if(!role.equals(User.ROLE_SUPERVISOR)) {
            if(!user.getRole().equals(role) || !user.getUsername().equals(username)) {
                throw new InsufficientAccessException("Your authentication token has been tampered, please refresh session!");
            }
            if(compareAgainstRole != null) {
                if(!user.getRole().equals(compareAgainstRole)) {
                    throw new InsufficientAccessException("You do not have access to perform this action!");
                }
            }
            if(compareAgainstId != null) {
                if(!user.getId().equals(compareAgainstId)) {
                    throw new InsufficientAccessException("You do not have access to the requested user's data!");
                }
            }
        }
    }

    private Boolean isExpired(String token){
        Claims claims = getTokenClaims(token);
        Date expirationDate = claims.getExpiration();
        return expirationDate.before(new Date());
    }

    public Boolean isValid(String token, UserDetails userDetails){
        String username = getUserFromToken(token);
        return (!isExpired(token) && username.equals(userDetails.getUsername()));
    }

}
