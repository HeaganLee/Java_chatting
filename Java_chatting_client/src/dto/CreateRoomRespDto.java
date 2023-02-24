package dto;

import java.util.List
;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
// 생성된 룸을 리스트로 받는 역할을 함
public class CreateRoomRespDto {
	private String roomname;
	private String kinguser;
	private List<String> createdRooms;
	
}
