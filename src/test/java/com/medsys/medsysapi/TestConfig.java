package com.medsys.medsysapi;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.medsys.medsysapi.*"})
public class TestConfig {

}
