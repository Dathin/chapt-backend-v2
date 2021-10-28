package me.pedrocaires.chapt.repository.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRowMapperTest {

	@Mock
	ResultSet resultSet;

	@InjectMocks
	UserRowMapper userRowMapper;

	@Test
	void shouldMapUser() throws SQLException {
		var id = 1;
		var username = "username";
		when(resultSet.getInt(UserConstants.ID)).thenReturn(id);
		when(resultSet.getString(UserConstants.USERNAME)).thenReturn(username);

		var user = userRowMapper.mapRow(resultSet, 0);

		assertEquals(id, user.getId());
		assertEquals(username, user.getUsername());
	}

}