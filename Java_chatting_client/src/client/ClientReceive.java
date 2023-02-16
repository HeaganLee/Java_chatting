package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.google.gson.Gson;

import dto.CreateRoomRespDto;
import dto.JoinRespDto;
import dto.RespDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientReceive extends Thread {
	private final Socket socket;
	private InputStream inputStream;
	private Gson gson;
	
	@Override
	public void run() {
		try {
			inputStream = socket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			gson = new Gson();
			
			while(true) {
			String request = in.readLine();
			RespDto respDto = gson.fromJson(request, RespDto.class);
			switch(respDto.getResource()) {
				case "join" :
					JoinRespDto joinRespDto = gson.fromJson(respDto.getBody(), JoinRespDto.class);
					ChattingClient.getInstance().getRoomListModel().addAll(null);
			}
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
