package me.pedrocaires.chapt.handler.transformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.handler.Broadcast;
import me.pedrocaires.chapt.handler.SerializableBroadcast;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BroadcastToSerializableBroadcast {

    private final ObjectMapper objectMapper;

    public BroadcastToSerializableBroadcast(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <O> Optional<SerializableBroadcast> transform(Optional<Broadcast<O>> optionalBroadcast)
            throws JsonProcessingException {
        if (optionalBroadcast.isPresent()) {
            var broadcast = optionalBroadcast.get();
            var message = objectMapper.writeValueAsString(broadcast.getMessage());
            return Optional.of(new SerializableBroadcast(message, broadcast.getClients()));
        }
        return Optional.empty();
    }

}
