package dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
// 여기서는 방에서 나갔을 때 메세지를 띄움
public class ExitRoomRespDto {
	private String outMessage;
	private String roomname;
	private List<String> roomlist;
	

	
}
