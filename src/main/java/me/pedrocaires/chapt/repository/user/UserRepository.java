package me.pedrocaires.chapt.repository.user;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

	private final JdbcTemplate jdbcTemplate;

	private final UserRowMapper userRowMapper;

	protected static final String FIND_USER_BY_USERNAME_AND_PASSWORD_QUERY = String.format(
			"SELECT %s, %s FROM USERS WHERE %s=? AND %s=?", UserConstants.ID, UserConstants.USERNAME,
			UserConstants.USERNAME, UserConstants.PASSWORD);

	protected static final String CREATE_USER_QUERY = String.format("INSERT INTO USERS (%s, %s) VALUES (?, ?)",
			UserConstants.USERNAME, UserConstants.PASSWORD);

	protected static final String USER_SEARCH_QUERY = String.format(
			"SELECT %s, %s FROM USERS WHERE %s=? OFFSET ? LIMIT ?", UserConstants.ID, UserConstants.USERNAME,
			UserConstants.USERNAME);

	public UserRepository(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.userRowMapper = userRowMapper;
	}

	public void createUser(String username, String password) {
		jdbcTemplate.update(CREATE_USER_QUERY, username, password);
	}

	public Optional<User> findUserByUsernameAndPassword(String username, String password) {
		try {
			return Optional.of(jdbcTemplate.queryForObject(FIND_USER_BY_USERNAME_AND_PASSWORD_QUERY, userRowMapper,
					username, password));
		}
		catch (EmptyResultDataAccessException ex) {
			return Optional.empty();
		}
	}

	public List<User> searchUsers(String username, int page, int size) {
		return jdbcTemplate.query(USER_SEARCH_QUERY, userRowMapper, username, page, size);
	}

}
