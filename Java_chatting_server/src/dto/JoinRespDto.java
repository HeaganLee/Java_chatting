package dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
// 여기는 유저가 서버에 조인을 했을 때 서버가 반응하는 클래스
public class JoinRespDto {
	private String welcomeMessage;
	private List<String> connectedUsers;
	private List<String> createdRooms1;
}
