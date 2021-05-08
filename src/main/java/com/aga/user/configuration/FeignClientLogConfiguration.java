package com.aga.user.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientLogConfiguration {

    @Bean
    Logger.Level feignLoggerLever(){
        return Logger.Level.FULL;
    }

}
