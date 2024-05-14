package com.medsys.medsysapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.mockito.Mockito.*;

@SpringBootTest
class SecUserDetailsTest {
    @Mock
    Date date_of_birth;
    @InjectMocks
    SecUserDetails secUserDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
    TODO:
     */
}
