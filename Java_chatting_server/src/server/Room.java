package server;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;


@Data
public class Room {
	private String kingUser;
	private String roomName;
	private List<String> connecteUsers = new ArrayList<>();
	
	public void addUser(String user) {
		this.connecteUsers.add(user);
		
	}
	
	public void removeUser(String user) {
		this.connecteUsers.remove(user);
		
	}
}
