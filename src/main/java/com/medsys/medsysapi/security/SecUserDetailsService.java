package com.medsys.medsysapi.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SecUserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(SecUserDetails.class);

    public final Map<String, SecUser> tokens = new HashMap<>();

    public SecUserDetailsService() {
    }

    public SecUserToken createUser(SecUserDetails userDetails) {
        logger.debug("Creating user: " + userDetails.getName());
        checkForDuplicates(userDetails.getId());

        SecUser user = new SecUser(userDetails, resolveAuthorities(userDetails));
        this.tokens.put(user.getToken().getValue(), user);

        logger.info("User " + user.getUserDetails().getUsername() + " logged in.");

        return user.getToken();
    }

    public void deleteUser(String token) {
        this.tokens.remove(token);
    }

    public void deleteUser(SecUser user) {
        this.tokens.remove(user.getToken().getValue());
    }

    public String findUserToken(int id){
        for (Map.Entry<String, SecUser> entry : this.tokens.entrySet()) {
            if (entry.getValue().getUserDetails().getId() == id) {
                return entry.getKey();
            }
        }
        return "";
    }

    public SecUser loadUserByToken(String token) throws UsernameNotFoundException {
        return this.tokens.get(token);
    }

    private Set<SimpleGrantedAuthority> resolveAuthorities(SecUserDetails userDetails){
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(SecUserRoles.ROLE_USER));
        switch(userDetails.getProfession()) {
            case "Lekarz": {
                authorities.add(new SimpleGrantedAuthority(SecUserRoles.ROLE_DOCTOR));
                break;
            }
            case "Personel": {
                authorities.add(new SimpleGrantedAuthority(SecUserRoles.ROLE_PERSONNEL));
                break;
            }
            case "Admin": {
                authorities.add(new SimpleGrantedAuthority(SecUserRoles.ROLE_ADMIN));
                break;
            }
        }

        return authorities;
    }

    public void deleteExpiredTokens() {
        for (Map.Entry<String, SecUser> entry : this.tokens.entrySet()) {
            logger.debug("Checking token of user: " + entry.getValue().getUserDetails().getUsername());
            if (entry.getValue().getToken().isExpired()) {
                deleteUser(entry.getValue());
                logger.info("Deleted expired token of user: " + entry.getValue().getUserDetails().getUsername());
            }
        }
    }

    private void checkForDuplicates(int id){
        String token = findUserToken(id);
        if(!token.isEmpty()){
            deleteUser(token);
            logger.warn("Deleted duplicate user with id: " + id);
        }
    }
}
