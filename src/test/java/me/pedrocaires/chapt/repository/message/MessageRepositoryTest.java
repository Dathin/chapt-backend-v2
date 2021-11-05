package me.pedrocaires.chapt.repository.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageRepositoryTest {

	@Mock
	JdbcTemplate jdbcTemplate;

	@Mock
	MessageIdResultSetExtractor messageIdResultSetExtractor;

	@Mock
	MessageRowMapper messageRowMapper;

	@InjectMocks
	MessageRepository messageRepository;

	@Test
	void shouldInsertMessage() {
		var to = 1;
		var from = 1;
		var message = "";

		messageRepository.insertMessage(to, from, message);

		verify(jdbcTemplate).query(MessageRepository.INSERT_MESSAGE_QUERY, messageIdResultSetExtractor, to, from,
				message);
	}

	@Test
	void shouldGetMessageHistory() {
		var to = 1;
		var from = 1;
		var previousThanId = 1L;
		var size = 1;

		messageRepository.getMessageHistory(to, from, previousThanId, size);

		verify(jdbcTemplate).query(MessageRepository.MESSAGE_HISTORY_QUERY, messageRowMapper, to, from, previousThanId, size);
	}
}