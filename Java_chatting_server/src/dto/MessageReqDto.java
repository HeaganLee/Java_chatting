package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageReqDto {
	private String fromUser;
	private String roomname;
	private String messageValue;
}
