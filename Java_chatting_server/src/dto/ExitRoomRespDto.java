package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
// 여기서는 방에서 나갔을 때 메세지를 띄움
public class ExitRoomRespDto {
	private String outMessage;
}
