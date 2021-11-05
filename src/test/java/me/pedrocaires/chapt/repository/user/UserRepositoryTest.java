package me.pedrocaires.chapt.repository.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

	@Mock
	JdbcTemplate jdbcTemplate;

	@Mock
	UserRowMapper userRowMapper;

	@InjectMocks
	UserRepository userRepository;

	@Test
	void shouldReturnOptionalUserWhenFound() {
		var username = "username";
		var password = "password";
		var expectedUser = new User();
		when(jdbcTemplate.queryForObject(UserRepository.FIND_USER_BY_USERNAME_AND_PASSWORD_QUERY, userRowMapper,
				username, password)).thenReturn(expectedUser);

		var foundUser = userRepository.findUserByUsernameAndPassword(username, password).get();

		assertEquals(expectedUser, foundUser);
	}

	@Test
	void shouldReturnEmptyOptionalOnEmptyResultSet() {
		var username = "username";
		var password = "password";
		when(jdbcTemplate.queryForObject(UserRepository.FIND_USER_BY_USERNAME_AND_PASSWORD_QUERY, userRowMapper,
				username, password)).thenThrow(EmptyResultDataAccessException.class);

		var foundUser = userRepository.findUserByUsernameAndPassword(username, password);

		assertTrue(foundUser.isEmpty());
	}

	@Test
	void shouldInsertUser() {
		var username = "username";
		var password = "password";

		userRepository.createUser(username, password);

		verify(jdbcTemplate).update(UserRepository.CREATE_USER_QUERY, username, password);
	}

	@Test
	void shouldSearchUsers() {
		var username = "";
		var page = 1;
		var size = 1;
		List<User> queryResponse = Collections.emptyList();
		when(jdbcTemplate.query(UserRepository.USER_SEARCH_QUERY, userRowMapper, username, page, size))
				.thenReturn(queryResponse);

		var users = userRepository.searchUsers(username, page, size);

		assertEquals(queryResponse, users);
	}

}