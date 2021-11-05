package me.pedrocaires.chapt.repository.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageRowMapperTest {

    @Mock
    ResultSet rs;

    @InjectMocks
    MessageRowMapper messageRowMapper;

    @Test
    void shouldMapMessage() throws SQLException {
        var id = 1;
        var toUserId = 1;
        var fromUserId = 1;
        var content = "";
        when(rs.getInt(MessageConstants.ID)).thenReturn(id);
        when(rs.getInt(MessageConstants.TO_USER_ID)).thenReturn(toUserId);
        when(rs.getInt(MessageConstants.FROM_USER_ID)).thenReturn(fromUserId);
        when(rs.getString(MessageConstants.CONTENT)).thenReturn(content);
        when(rs.getBoolean(MessageConstants.DELIVERED)).thenReturn(true);
        when(rs.getBoolean(MessageConstants.READ)).thenReturn(true);

        var message = messageRowMapper.mapRow(rs, 1);

        assertEquals(id, message.getId());
        assertEquals(toUserId, message.getToUserId());
        assertEquals(fromUserId, message.getFromUserId());
        assertEquals(content, message.getContent());
        assertEquals(true, message.getDelivered());
        assertEquals(true, message.getRead());
    }
}