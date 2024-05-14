package com.medsys.medsysapi.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SecUserDetailsService {

    protected final Log logger = LogFactory.getLog(this.getClass());
    public final Map<String, SecUser> tokens = new HashMap<>();

    public SecUserDetailsService() {
    }

    public SecUserDetailsService(Collection<SecUser> users) {
        for (SecUser user : users) {
            this.createUser(user);
        }
    }

    public SecUserToken createUser(SecUser user) {
        logger.debug("Creating user: " + user.getUserDetails().getName());
        checkForDuplicates(user.getUserDetails().getId());
        this.tokens.put(user.getToken().getValue(), user);
        return user.getToken();
    }

    public SecUserToken createUser(SecUserDetails userDetails) {
        logger.debug("Creating user: " + userDetails.getName());
        checkForDuplicates(userDetails.getId());

        SecUser user = new SecUser(userDetails, resolveAuthorities(userDetails));
        this.tokens.put(user.getToken().getValue(), user);

        logger.debug("User created: " + user.getUserDetails().getName() + "\nUserDetails:\n" + user.getUserDetails().toString());
        logger.info("User " + user.getUserDetails().getUsername() + " logged in.");

        return user.getToken();
    }

    public void deleteUser(SecUser user) {
        this.tokens.remove(user.getToken().getValue());
    }

    public boolean userExists(SecUser user) {
        return this.tokens.containsKey(user.getToken().getValue());
    }

    public boolean userExists(int id) {
        for (Map.Entry<String, SecUser> entry : this.tokens.entrySet()) {
            if (entry.getValue().getUserDetails().getId() == id) {
                return true;
            }
        }
        return false;
    }

    public SecUser loadUserByToken(String token) throws UsernameNotFoundException {
        return this.tokens.get(token);
    }

    private Set<SimpleGrantedAuthority> resolveAuthorities(SecUserDetails userDetails){
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(SecUserRoles.ROLE_USER));
        switch(userDetails.getProfession()) {
            case "Doctor": {
                authorities.add(new SimpleGrantedAuthority(SecUserRoles.ROLE_DOCTOR));
                break;
            }
            case "Personnel": {
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
                logger.debug("Deleted expired token of user: " + entry.getValue().getUserDetails().getUsername());
            }
        }
    }

    private void checkForDuplicates(int id){
        for (Map.Entry<String, SecUser> entry : this.tokens.entrySet()) {
            if (entry.getValue().getUserDetails().getId() == id) {
                deleteUser(entry.getValue());
                logger.warn("Deleted duplicate user with id: " + id);
            }
        }
    }
}
