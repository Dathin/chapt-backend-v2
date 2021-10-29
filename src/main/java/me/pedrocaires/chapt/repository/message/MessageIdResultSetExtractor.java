package me.pedrocaires.chapt.repository.message;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MessageIdResultSetExtractor implements ResultSetExtractor<Integer> {

	@Override
	public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
		return rs.getInt(MessageConstants.ID);
	}

}
