package me.pedrocaires.chapt.repository.message;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {

	protected static final String INSERT_MESSAGE_QUERY = String.format(
			"INSERT INTO MESSAGES (%s, %s, %s) VALUES (?, ?, ?)", MessageConstants.TO_USER_ID,
			MessageConstants.FROM_USER_ID, MessageConstants.MESSAGE);

	private final JdbcTemplate jdbcTemplate;

	private final MessageIdResultSetExtractor messageIdResultSetExtractor;

	public MessageRepository(JdbcTemplate jdbcTemplate, MessageIdResultSetExtractor messageIdResultSetExtractor) {
		this.jdbcTemplate = jdbcTemplate;
		this.messageIdResultSetExtractor = messageIdResultSetExtractor;
	}

	public Integer insertMessage(int to, int from, String message) {
		return jdbcTemplate.query(INSERT_MESSAGE_QUERY, messageIdResultSetExtractor, to, from, message);
	}

}
