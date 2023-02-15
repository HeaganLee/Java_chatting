package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RespDto {
	// 서버에서 제공하는 리소스
	private String resource;
	// HTTP의 응답 코드
	private String status;
	// 응답 본문 데이터
	private String body;
}
