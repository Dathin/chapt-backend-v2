package me.pedrocaires.chapt.repository.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		var user = new User();
		user.setId(rs.getInt(UserConstants.ID));
		user.setUsername(rs.getString(UserConstants.USERNAME));
		return user;
	}

}
