package me.pedrocaires.chapt.handler.message.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.authentication.WebSocketAttachment;
import me.pedrocaires.chapt.handler.Broadcast;
import me.pedrocaires.chapt.handler.message.MessageExecutor;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import me.pedrocaires.chapt.repository.message.MessageRepository;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class HistoryMessageExecutor extends MessageExecutor<HistoryRequestDTO, HistoryResponseDTO> {

	private final MessageRepository messageRepository;

	protected HistoryMessageExecutor(ObjectMapper objectMapper,
			BroadcastToSerializableBroadcast broadcastToSerializableBroadcast, MessageRepository messageRepository) {
		super(objectMapper, broadcastToSerializableBroadcast);
		this.messageRepository = messageRepository;
	}

	@Override
	public Optional<Broadcast<HistoryResponseDTO>> handleMessage(HistoryRequestDTO message, WebSocket client,
			Map<Integer, WebSocket> clients) {
		var userId = ((WebSocketAttachment) client.getAttachment()).getAuthentication().getUserId();
		var messageHistory = messageRepository.getMessageHistory(userId, message.getFrom(), message.getPreviousThanId(),
				message.getSize());
		var historyResponse = new HistoryResponseDTO(message.getHandler(), messageHistory);
		return Optional.of(new Broadcast<>(historyResponse, Collections.singletonList(client)));
	}

}
