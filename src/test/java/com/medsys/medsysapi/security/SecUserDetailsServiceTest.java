package com.medsys.medsysapi.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.*;

class SecUserDetailsServiceTest {

    @Mock
    Map<String, SecUser> tokens;
    @InjectMocks
    SecUserDetailsService secUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        SecUserDetails userDetails = new SecUserDetails(0, "name", new GregorianCalendar(2024, Calendar.MAY, 19, 6, 33).getTime(), "pesel", "gender", "phone_number", "address", "speciality", "username", "password", "profession");
        SecUserToken token = secUserDetailsService.createUser(userDetails);
        Assertions.assertEquals(token.getValue(), secUserDetailsService.tokens.get(token.getValue()).getToken().getValue());
        secUserDetailsService.deleteUser(token.getValue());
        Assertions.assertNull(secUserDetailsService.tokens.get(token.getValue()));
    }
}