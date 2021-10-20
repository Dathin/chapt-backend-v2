package me.pedrocaires.chapt;

import me.pedrocaires.chapt.handler.MessageHandlerDecider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleServerTest {

    @Mock
    MessageHandlerDecider messageHandlerDecider;

    @InjectMocks
    SimpleServer simpleServer;

    @Test
    void a(){
        simpleServer.onStart();
    }
}