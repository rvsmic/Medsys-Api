package com.medsys.medsysapi.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@Getter
public class SecUser {

    private final SecUserToken token;
    private final Set<GrantedAuthority> authorities;
    private final SecUserDetails userDetails;

    public SecUser(SecUserDetails userDetails, Collection<? extends GrantedAuthority> authorities) {
        this.userDetails = userDetails;
        this.token = new SecUserToken(SecUserRoles.getId((Set<GrantedAuthority>) authorities), null);
        this.authorities = (Set<GrantedAuthority>) authorities;
    }

    public boolean hasAuthority(String authority) {
        Set<GrantedAuthority> authorities = getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            if (authority.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }

        return false;
    }

}
