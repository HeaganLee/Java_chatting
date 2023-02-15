package server;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import dto.CreateRoomReqDto;
import dto.CreateRoomRespDto;
import dto.JoinReqDto;
import dto.JoinRespDto;
import dto.JoinRoomReqDto;
import dto.MessageReqDto;
import dto.MessageRespDto;
import dto.ReqDto;
import dto.RespDto;
import lombok.Data;


@Data
class ConnectedSocket extends Thread{
	private static List<ConnectedSocket> socketList = new ArrayList<>();
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private Gson gson;
	
	private String roomname;
	private String username;
	
	public ConnectedSocket(Socket socket) {
		this.socket = socket;
		gson = new Gson();
		socketList.add(this);
	}
	
	// 유저가입리스, 방생성리스트, 메세지 보내기 용도
	@Override
	public void run() {
		try {
			inputStream = socket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			
			while(true) {
				String reuest = in.readLine();
				ReqDto reqDto = gson.fromJson(reuest, ReqDto.class);
				
				switch(reqDto.getResource()) {
					case "join":
						JoinReqDto joinReqDto = gson.fromJson(reqDto.getBody(), JoinReqDto.class);
						username = joinReqDto.getUsername();
						List<String> connectedUser = new ArrayList<>();
					
						for(ConnectedSocket connectedSocket : socketList) {
							connectedUser.add(connectedSocket.getUsername());
						}
						break;
						
					case "createroom":
						CreateRoomReqDto createRoomReqDto = gson.fromJson(reqDto.getBody(),CreateRoomReqDto.class);
						roomname = createRoomReqDto.getRoomname();
						List<String> createdRooms = new ArrayList<>();
						
						for(ConnectedSocket connectedSocket : socketList) {
							createdRooms.add(connectedSocket.getRoomname());
						}
						
						CreateRoomRespDto createRoomRespDto = new CreateRoomRespDto(roomname + "생성됨", createdRooms);
						
						sendToAll(reqDto.getResource(), "ok", gson.toJson(createRoomRespDto));
						break;
						
					case "joinroom":
						JoinRoomReqDto joinRoomReqDto = gson.fromJson(reqDto.getBody(), JoinRoomReqDto.class);
						roomname = joinRoomReqDto.getRoomname();
						username = joinRoomReqDto.getUsername();
						List<String> roomconnectedUsers = new ArrayList<>();
						
						roomconnectedUsers.add(username);
						
						JoinRespDto joinRespDto = new JoinRespDto(username +"님이 방에 들어옴", roomconnectedUsers);
						break;
						
					case "sendMessage":
						MessageReqDto messageReqDto = gson.fromJson(reqDto.getBody(), MessageReqDto.class);
						String message = messageReqDto.getFromUser() + " > " + messageReqDto.getMessageValue();
						MessageRespDto messageRespDto = new MessageRespDto(message);
						sendToAll(reqDto.getResource(), "ok", gson.toJson(messageRespDto));
						
						break;
					}
				}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private void sendToAll(String resource, String status, String body) throws IOException {
		RespDto respDto = new RespDto(resource, status, body);
		for(ConnectedSocket connectedSocket : socketList) {
			OutputStream outputStream = connectedSocket.getSocket().getOutputStream();
			PrintWriter out = new PrintWriter(outputStream, true);
			
			out.println(gson.toJson(respDto));
		}
	}
}

public class ServerApplication {
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(9090);
			System.out.println("=====<< 채팅방 서버 실행 >>=====");
			
			while(true) {
				Socket socket = serverSocket.accept();
				ConnectedSocket connectedSocket = new ConnectedSocket(socket);
				connectedSocket.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(serverSocket != null) {
				try {
					serverSocket.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("=====<< 채팅방 서버 종료 >>=====");
		}
	}
}
