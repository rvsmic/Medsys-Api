package com.medsys.medsysapi.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
class BasicResponseTest {
    @Mock
    Date timeStamp;
    @Mock
    Map data;
    @InjectMocks
    BasicResponse basicResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateResponse() {
        int status = 624;
        String message = "Delight economy safer requested boards critics avi, alike pumps telling unique accompanying ambient details, attractions wider beginner. ";
        when(data.get(anyString())).thenReturn("value");
        basicResponse = new BasicResponse<Map>(status, message, data);

        Assertions.assertEquals(basicResponse.generateResponse().getStatusCodeValue(), status);
        Assertions.assertEquals(((Map<String, Object>) basicResponse.generateResponse().getBody()).get("message").toString(), message);
    }
} 
