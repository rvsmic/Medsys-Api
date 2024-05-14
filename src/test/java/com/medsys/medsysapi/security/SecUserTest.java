package com.medsys.medsysapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

import static org.mockito.Mockito.*;

@SpringBootTest
class SecUserTest {
    @Mock
    SecUserToken token;
    @Mock
    Set<GrantedAuthority> authorities;
    @Mock
    SecUserDetails userDetails;
    @InjectMocks
    SecUser secUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
    TODO: testy
    do ustalenia czy potrzebne
    */
}