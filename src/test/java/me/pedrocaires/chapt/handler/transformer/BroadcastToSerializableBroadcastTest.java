package me.pedrocaires.chapt.handler.transformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.pedrocaires.chapt.handler.BaseMessageDTO;
import me.pedrocaires.chapt.handler.Broadcast;
import me.pedrocaires.chapt.springconfig.BeanConfig;
import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BroadcastToSerializableBroadcastTest {

    BeanConfig beanConfig = new BeanConfig();


    BroadcastToSerializableBroadcast broadcastToSerializableBroadcast = new BroadcastToSerializableBroadcast(beanConfig.objectMapper());

    @Mock
    WebSocket client;

    @Test
    void shouldReturnEmptyIfNoBroadcastInfo() throws JsonProcessingException {
        var serializedBroadcast = broadcastToSerializableBroadcast.transform(Optional.empty());

        assertTrue(serializedBroadcast.isEmpty());
    }

    @Test
    void shouldReturnSameClients() throws JsonProcessingException {
        var baseMessage = new BaseMessageDTO("testHandler");
        var clients = Collections.singletonList(client);

        var serializedBroadcast = broadcastToSerializableBroadcast.transform(Optional.of(new Broadcast<BaseMessageDTO>(baseMessage, clients))).get();

        assertEquals(clients, serializedBroadcast.getClients());
    }

    @Test
    void shouldReturnSerializedMessage() throws JsonProcessingException {
        var baseMessage = new BaseMessageDTO("testHandler");
        var serializedValueOfMessage = beanConfig.objectMapper().writeValueAsString(baseMessage);
        var clients = Collections.singletonList(client);

        var serializedBroadcast = broadcastToSerializableBroadcast.transform(Optional.of(new Broadcast<BaseMessageDTO>(baseMessage, clients))).get();

        assertEquals(serializedValueOfMessage, serializedBroadcast.getMessage());
    }
}