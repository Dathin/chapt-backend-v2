package me.pedrocaires.chapt.springconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class BeanConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress("localhost", 8087);
    }

}
