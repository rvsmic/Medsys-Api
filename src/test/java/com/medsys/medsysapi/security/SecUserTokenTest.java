package com.medsys.medsysapi.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import static org.mockito.Mockito.*;

@SpringBootTest
class SecUserTokenTest {
    @Mock
    Date expirationDate;
    @Mock
    SecureRandom secureRandom;
    @Mock
    Base64.Encoder base64Encoder;
    @InjectMocks
    SecUserToken secUserToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsTokenValid() {
        when(expirationDate.before(any(Date.class))).thenReturn(false);
        String value = secUserToken.getValue();
        boolean result = secUserToken.isTokenValid(value);
        verify(expirationDate).before(any(Date.class));
        Assertions.assertEquals(true, result);

        value = "invalid";
        result = secUserToken.isTokenValid(value);
        verify(expirationDate, times(2)).before(any(Date.class));
        Assertions.assertEquals(false, result);

        when(expirationDate.before(any(Date.class))).thenReturn(true);
        value = secUserToken.getValue();
        result = secUserToken.isTokenValid(value);
        verify(expirationDate, times(3)).before(any(Date.class));
        Assertions.assertEquals(false, result);

        value = "invalid";
        result = secUserToken.isTokenValid(value);
        verify(expirationDate, times(4)).before(any(Date.class));
        Assertions.assertEquals(false, result);
    }

    @Test
    void testIsExpired() {
        when(expirationDate.before(any(Date.class))).thenReturn(true);

        boolean result = secUserToken.isExpired();
        verify(expirationDate).before(any(Date.class));
        Assertions.assertEquals(true, result);

        when(expirationDate.before(any(Date.class))).thenReturn(false);

        result = secUserToken.isExpired();
        verify(expirationDate, times(2)).before(any(Date.class));
        Assertions.assertEquals(false, result);
    }
}