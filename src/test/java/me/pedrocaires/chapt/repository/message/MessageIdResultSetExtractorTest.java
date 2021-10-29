package me.pedrocaires.chapt.repository.message;

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
class MessageIdResultSetExtractorTest {

	@Mock
	ResultSet resultSet;

	@InjectMocks
	MessageIdResultSetExtractor messageIdResultSetExtractor;

	@Test
	void shouldMapMessageId() throws SQLException {
		var expectedMessageId = 1;
		when(resultSet.getInt(MessageConstants.ID)).thenReturn(expectedMessageId);

		var providedMessageId = messageIdResultSetExtractor.extractData(resultSet);

		assertEquals(expectedMessageId, providedMessageId);
	}

}