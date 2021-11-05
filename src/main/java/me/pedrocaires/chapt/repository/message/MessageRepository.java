package me.pedrocaires.chapt.repository.message;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageRepository {

	protected static final String INSERT_MESSAGE_QUERY = String.format(
			"INSERT INTO MESSAGES (%s, %s, %s) VALUES (?, ?, ?) RETURNING %s", MessageConstants.TO_USER_ID,
			MessageConstants.FROM_USER_ID, MessageConstants.CONTENT, MessageConstants.ID);

	protected static final String MESSAGE_HISTORY_QUERY = String.format(
			"SELECT %s, %s, %s, %s, %s, %s FROM MESSAGES WHERE TO_USER_ID = ? AND FROM_USER_ID = ? AND ID < ? LIMIT ?",
			MessageConstants.ID, MessageConstants.TO_USER_ID, MessageConstants.FROM_USER_ID, MessageConstants.CONTENT,
			MessageConstants.DELIVERED, MessageConstants.READ);

	private final JdbcTemplate jdbcTemplate;

	private final MessageIdResultSetExtractor messageIdResultSetExtractor;

	private final MessageRowMapper messageRowMapper;

	public MessageRepository(JdbcTemplate jdbcTemplate, MessageIdResultSetExtractor messageIdResultSetExtractor,
			MessageRowMapper messageRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.messageIdResultSetExtractor = messageIdResultSetExtractor;
		this.messageRowMapper = messageRowMapper;
	}

	public Integer insertMessage(int to, int from, String message) {
		return jdbcTemplate.query(INSERT_MESSAGE_QUERY, messageIdResultSetExtractor, to, from, message);
	}

	public List<Message> getMessageHistory(int to, int from, long previousThanId, int size) {
		return jdbcTemplate.query(MESSAGE_HISTORY_QUERY, messageRowMapper, to, from, previousThanId, size);
	}

}
