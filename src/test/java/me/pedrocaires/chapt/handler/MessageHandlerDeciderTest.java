package me.pedrocaires.chapt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.pedrocaires.chapt.authentication.AuthenticationFilter;
import me.pedrocaires.chapt.exception.UnexpectedException;
import me.pedrocaires.chapt.handler.message.auth.AuthMessage;
import me.pedrocaires.chapt.handler.message.direct.DirectMessage;
import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageHandlerDeciderTest {

    @Mock
    AuthMessage authMessage;

    @Mock
    DirectMessage directMessage;

    @Mock
    AuthenticationFilter authenticationFilter;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    ObjectNode objectNode;

    @Mock
    WebSocket client;

    @Mock
    JsonNode jsonNode;

    @InjectMocks
    MessageHandlerDecider messageHandlerDecider;


    @Test
    void shouldThrowUnexpectedExceptionWhenHandleIsNull() throws JsonProcessingException {
        var message = "";
        when(objectMapper.readValue(message, ObjectNode.class)).thenReturn(objectNode);
        when(objectNode.get("handler")).thenReturn(null);

        assertThrows(UnexpectedException.class, () -> messageHandlerDecider.decide(message, client, Collections.singletonMap("user", client)));
    }

    @Test
    void shouldFilterRequests() throws JsonProcessingException {
        var message = "";
        when(objectMapper.readValue(message, ObjectNode.class)).thenReturn(objectNode);
        when(objectNode.get("handler")).thenReturn(jsonNode);
        when(jsonNode.toString()).thenReturn("\"AUTH\"");

        messageHandlerDecider.decide(message, client, Collections.singletonMap("user", client));

        verify(authenticationFilter).doFilter(any(), any());
    }

    @Test
    void shouldThrowUnexpectedExceptionWhenUnknownHandle() throws JsonProcessingException {
        var message = "";
        when(objectMapper.readValue(message, ObjectNode.class)).thenReturn(objectNode);
        when(objectNode.get("handler")).thenReturn(jsonNode);
        when(jsonNode.toString()).thenReturn("\"NOTSURE\"");


        assertThrows(UnexpectedException.class, () -> messageHandlerDecider.decide(message, client, Collections.singletonMap("user", client)));
    }

    //TODO: test AUTH AND DIRECT BEING CALLED

}