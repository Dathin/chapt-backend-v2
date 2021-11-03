package me.pedrocaires.chapt.repository.message;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MessageRowMapper implements RowMapper<Message> {

	@Override
	public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
		var message = new Message();
		message.setId(rs.getInt(MessageConstants.ID));
		message.setToUserId(rs.getInt(MessageConstants.TO_USER_ID));
		message.setFromUserId(rs.getInt(MessageConstants.FROM_USER_ID));
		message.setMessage(rs.getString(MessageConstants.MESSAGE));
		message.setDelivered(rs.getBoolean(MessageConstants.DELIVERED));
		message.setRead(rs.getBoolean(MessageConstants.READ));
		return message;
	}

}
