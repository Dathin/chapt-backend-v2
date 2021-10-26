package me.pedrocaires.chapt.springconfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BeanConfigTest {

    @InjectMocks
    BeanConfig beanConfig;

    @Test
    void shouldReturnObjectMapper() {
        assertNotNull(beanConfig.objectMapper());
    }

    @Test
    void shouldReturnInetSocketAddress() {
        assertNotNull(beanConfig.inetSocketAddress());
    }
}