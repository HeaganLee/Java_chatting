package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.google.gson.Gson;

import dto.CreateRoomRespDto;
import dto.ExitRoomRespDto;
import dto.JoinRespDto;
import dto.JoinRoomRespDto;
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
				ExitRoomRespDto exitRoomRespDto;
				JoinRoomRespDto joinRoomRespDto;
				System.out.println("이거>> " + respDto);
				switch(respDto.getResource()) {	
				
					case "join":
						joinRespDto = gson.fromJson(respDto.getBody(), JoinRespDto.class);
							if(joinRespDto.getCreatedRooms1() != null) {
								ChattingClient.getInstance().getChatListModel().clear();
								ChattingClient.getInstance().getChatListModel().addElement("--- 방 목록 ---");
								ChattingClient.getInstance().getChatListModel().addAll(joinRespDto.getCreatedRooms1());
								System.out.println("조인했을때 " + joinRespDto.getCreatedRooms1());
								ChattingClient.getInstance().getChatList().setSelectedIndex(0);
							
							}
							break;
							
					case "joinroom":
						joinRoomRespDto = gson.fromJson(respDto.getBody(), JoinRoomRespDto.class);
						ChattingClient.getInstance().getContentView().append(joinRoomRespDto.getWelcomeRoomMessage() + "\n");
						
						break;
							
					case "createroom" :
						createRoomRespDto = gson.fromJson(respDto.getBody(), CreateRoomRespDto.class);
						ChattingClient.getInstance().getChatListModel().clear();
						ChattingClient.getInstance().getChatListModel().addElement("--- 방 목록 ---");
						ChattingClient.getInstance().getChatListModel().addAll(createRoomRespDto.getCreatedRooms());
						ChattingClient.getInstance().getChatList().setSelectedIndex(0);
						
//						ChattingClient.getInstance().getContentView().append(createRoomRespDto.getWelcomMessage() + "\n");
//						ChattingClient.getInstance().getContentView().append(createRoomRespDto.getKinguser() + "님이 들어옴\n");
//						System.out.println("룸 크리에이트" + ChattingClient.getInstance().getChatListModel());
						break;
								
					case "exit":
						
						exitRoomRespDto = gson.fromJson(respDto.getBody(), ExitRoomRespDto.class);
						
							ChattingClient.getInstance().getChatListModel().clear();
							ChattingClient.getInstance().getChatListModel().addElement("--- 방 목록 ---");
							ChattingClient.getInstance().getChatListModel().addAll(exitRoomRespDto.getRoomlist());
							ChattingClient.getInstance().getChatList().setSelectedIndex(0);
							System.out.println("exit리시브" + ChattingClient.getInstance().getChatListModel());
							
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
