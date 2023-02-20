package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExitRoomReqDto {
	private String username;
	private String roomname;
}
