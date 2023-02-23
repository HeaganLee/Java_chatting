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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import dto.CreateRoomReqDto;
import dto.CreateRoomRespDto;
import dto.ExitRoomReqDto;
import dto.ExitRoomRespDto;
import dto.JoinReqDto;
import dto.JoinRespDto;
import dto.JoinRoomReqDto;
import dto.JoinRoomRespDto;
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
	private String kinguser;
	private String joineduser;
	private static List<String> createdRooms = new ArrayList<>();
	private static List<Room> roomList = new ArrayList<>();
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
			
			Set<String> createdRoomsSet;
			while(true) {
				String reuest = in.readLine();
				ReqDto reqDto = gson.fromJson(reuest, ReqDto.class);
				
				switch(reqDto.getResource()) {
					case "join":
						JoinReqDto joinReqDto = gson.fromJson(reqDto.getBody(), JoinReqDto.class);
						username = joinReqDto.getUsername();
						List<String> connectedUser = new ArrayList<>();
						System.out.println(username);
						
						for(ConnectedSocket connectedSocket : socketList) {
							connectedUser.add(connectedSocket.getUsername());
						}
						
						createdRooms.clear();
						for(Room r : roomList) {
							createdRooms.add(r.getRoomName());
						}
						System.out.println("조인할때" + createdRooms);
						
						createdRoomsSet = new LinkedHashSet<>(createdRooms); // LinkedHashSet은 순서를 유지합니다.
						createdRooms.clear();
						createdRooms.addAll(createdRoomsSet);
						
						JoinRespDto joinRespDto = new JoinRespDto(username + "환영", connectedUser, createdRooms);
						System.out.println(connectedUser);
						sendToAll(reqDto.getResource(), "ok", gson.toJson(joinRespDto));
						break;
						
					case "createroom":
						CreateRoomReqDto createRoomReqDto = gson.fromJson(reqDto.getBody(),CreateRoomReqDto.class);
						
						roomname = createRoomReqDto.getRoomname();
						System.out.println(roomname);
						
						kinguser = createRoomReqDto.getKinguser();
						System.out.println(kinguser);
						
						Room room = new Room();
						room.setKingUser(kinguser);
						room.setRoomName(roomname);
						room.addUser(kinguser);
						
						roomList.add(room);
						
						createdRooms.clear();
						for(Room r: roomList) {
							createdRooms.add(r.getRoomName());
						}
						System.out.println("방만들때" + createdRooms);
						createdRoomsSet = new LinkedHashSet<>(createdRooms); // LinkedHashSet은 순서를 유지합니다.
						createdRooms.clear();
						createdRooms.addAll(createdRoomsSet);						
			
						CreateRoomRespDto createRoomRespDto = new CreateRoomRespDto(roomname + "생성됨",kinguser, createdRooms);
						
						sendToAll(reqDto.getResource(), "ok", gson.toJson(createRoomRespDto));
						
					
						break;
						
					case "joinroom":
						JoinRoomReqDto joinRoomReqDto = gson.fromJson(reqDto.getBody(), JoinRoomReqDto.class);
						roomname = joinRoomReqDto.getRoomname();
						joineduser = joinRoomReqDto.getUsername();
						
						System.out.println(roomname);
						for(Room r : roomList) {
							if(r.getRoomName().equals(roomname)) {
								r.addUser(joineduser);
								System.out.println("감방들어간 " +   r.getConnecteUsers());
								}
								
							}
						System.out.println(roomList);
						
						JoinRoomRespDto joinRoomRespDto = new JoinRoomRespDto(joineduser +"님이 방에 들어옴", joineduser, roomname);
						sendToRoom(reqDto.getResource(), "ok", gson.toJson(joinRoomRespDto),joinRoomRespDto.getRoomname());
						break;
						
					case "sendMessage":
						MessageReqDto messageReqDto = gson.fromJson(reqDto.getBody(), MessageReqDto.class);
						String message = messageReqDto.getFromUser() + " > " + messageReqDto.getMessageValue();
						MessageRespDto messageRespDto = new MessageRespDto(message);
						sendToRoom(reqDto.getResource(), "ok", gson.toJson(messageRespDto),messageReqDto.getRoomname());
						break;
					
					case "exit":
						ExitRoomReqDto exitRoomReqDto = gson.fromJson(reqDto.getBody(), ExitRoomReqDto.class);
						String exituser = exitRoomReqDto.getUsername();
						String exitroom = exitRoomReqDto.getRoomname();
						
						System.out.println("삭제전 룸리스트" + roomList);
						
						for (Room r : roomList) {
							if(r.getRoomName().equals(exitroom)) {
								if(r.getKingUser().equals(exituser)) {
									roomList.remove(r);
									System.out.println(createdRooms);
									System.out.println("삭제후 룸리스트" + roomList);
									break;
								}
								else {
									r.removeUser(exituser);
									System.out.println("삭제후 룸리스트" + roomList);
									break;
								}
							}
						}
						
						createdRooms.clear();
						for(Room r : roomList) {
							createdRooms.add(r.getRoomName());
						}
						
						createdRoomsSet = new LinkedHashSet<>(createdRooms); // LinkedHashSet은 순서를 유지합니다.
						createdRooms.clear();
						createdRooms.addAll(createdRoomsSet);
						
						System.out.println("삭제후 룸 리스트" + createdRooms);
						
						ExitRoomRespDto exitRoomRespDto = new ExitRoomRespDto(exituser + "님이 나감", exitroom, createdRooms);
						sendToAll(reqDto.getResource(), "ok", gson.toJson(exitRoomRespDto));
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
	
	
	
	private void sendToAll(String resource, String status, String body, String roomname) throws IOException {
		RespDto respDto = new RespDto(resource, status, body);
		
		for(ConnectedSocket connectedSocket : socketList) {
			OutputStream outputStream = connectedSocket.getSocket().getOutputStream();
			PrintWriter out = new PrintWriter(outputStream, true);
			out.println(gson.toJson(respDto));
		}
		
		for(ConnectedSocket connectedSocket : socketList) {
			if(connectedSocket.getRoomname().equals(roomname)) {
				OutputStream outputStream = connectedSocket.getSocket().getOutputStream();
				PrintWriter out = new PrintWriter(outputStream, true);
				out.println(gson.toJson(respDto));
			}
			
		}
		
		
		
	}
	
	private void sendToRoom(String resource, String status, String body, String roomnmae) throws IOException {
		RespDto respDto = new RespDto(resource, status, body);
		for(ConnectedSocket connectedSocket : socketList) {
			if(connectedSocket.getRoomname().equals(roomname)) {
				OutputStream outputStream = connectedSocket.getSocket().getOutputStream();
				PrintWriter out = new PrintWriter(outputStream, true);
				
				out.println(gson.toJson(respDto));
			}
			
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
