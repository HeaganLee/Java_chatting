package client;

import java.awt.CardLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JPanel;

import com.google.gson.Gson;

import dto.CreateRoomRespDto;
import dto.JoinRespDto;
import dto.MessageRespDto;
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
				CreateRoomRespDto createRoomRespDto;
				JoinRespDto joinRespDto;
				System.out.println("이거>> " + respDto);
				switch(respDto.getResource()) {	
					case "createroom" :
						createRoomRespDto = gson.fromJson(respDto.getBody(), CreateRoomRespDto.class);
						ChattingClient.getInstance().getChatListModel().clear();
						ChattingClient.getInstance().getChatListModel().addElement("--- 방 목록 ---");
						ChattingClient.getInstance().getChatListModel().addAll(createRoomRespDto.getCreatedRooms());
						ChattingClient.getInstance().getChatList().setSelectedIndex(0);
						System.out.println("리시브" + ChattingClient.getInstance().getChatListModel());
						break;
						
	
					case "join":
						joinRespDto = gson.fromJson(respDto.getBody(), JoinRespDto.class);
							if(joinRespDto.getCreatedRooms1() != null) {
								ChattingClient.getInstance().getChatListModel().clear();
								ChattingClient.getInstance().getChatListModel().addElement("--- 방 목록 ---");
								ChattingClient.getInstance().getChatListModel().addAll(joinRespDto.getCreatedRooms1());
								System.out.println("조인했을때 " + joinRespDto.getCreatedRooms1());
								ChattingClient.getInstance().getChatList().setSelectedIndex(0);
								System.out.println("리시브" + ChattingClient.getInstance().getChatListModel());
						}
						break;
						
					case "sendMessage":
						MessageRespDto messageRespDto = 
								gson.fromJson(respDto.getBody(), MessageRespDto.class);
						ChattingClient.getInstance().getContentView().append(messageRespDto.getMessageValue() + "\n");
						break;
						
					
				}
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
