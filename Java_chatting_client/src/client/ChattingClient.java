package client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

import dto.CreateRoomReqDto;
import dto.ExitRoomReqDto;
import dto.JoinReqDto;
import dto.JoinRoomReqDto;
import dto.MessageReqDto;
import dto.ReqDto;
import lombok.Getter;

@Getter

public class ChattingClient extends JFrame {
	private static  ChattingClient instance;
	
	public static ChattingClient getInstance() {
		if(instance == null) {
			instance = new ChattingClient();
		}
		return instance;
	}
	
	private Socket socket;
	private Gson gson;
	private String username;
	private String roomname;
	private String joineduser;
	
	private CardLayout mainCard;
	private JPanel contentPane;
	private JTextField usernameField;
	private JTextArea contentView;
	private JTextField inputChatting;
	private JList<String> chatList;
	private DefaultListModel<String> chatListModel;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChattingClient frame = ChattingClient.getInstance();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChattingClient() {
		gson = new Gson();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 800);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 235, 59));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		mainCard = new CardLayout();
		contentPane.setLayout(mainCard);
		
		JPanel loginpanel = new JPanel();
		loginpanel.setBackground(new Color(255, 235, 59));
		contentPane.add(loginpanel, "name_999600029531900");
		loginpanel.setLayout(null);
	
		ImageIcon imgTest = new ImageIcon("images\\kakao2.png"); 
		Image img = imgTest.getImage();
		Image changeImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		
		JLabel KakaoLabel = new JLabel(changeIcon);
		KakaoLabel.setBackground(new Color(242, 242, 0));
		KakaoLabel.setBounds(177, 251, 100, 66);
		loginpanel.add(KakaoLabel);
		
		usernameField = new JTextField();
		usernameField.setBounds(95, 344, 251, 32);
		loginpanel.add(usernameField);
		usernameField.setColumns(10);
		
		ImageIcon startimg = new ImageIcon("images\\kakaostart.png"); 
		Image img2 = startimg.getImage();
		Image changeImg2 = img2.getScaledInstance(251, 50, Image.SCALE_SMOOTH);
		ImageIcon changeIcon2 = new ImageIcon(changeImg2);
		
		JButton inButton = new JButton(changeIcon2);
		inButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String ip = "127.0.0.1";
				int port = 9090;
				
				try {
					socket = new Socket(ip, port);
					
					JOptionPane.showMessageDialog(null,"헬톡에 오신걸 환영합니다.",
							usernameField.getText() + "환영합니다.", JOptionPane.INFORMATION_MESSAGE);
					
					ClientReceive clientReceive = new ClientReceive(socket);
					clientReceive.start();
					// 유저이름과 join 요청
					username = usernameField.getText();
					JoinReqDto joinReqDto = new JoinReqDto(username);
					String joinReqDtoJson = gson.toJson(joinReqDto);
					ReqDto reqDto = new ReqDto("join", joinReqDtoJson);
					String reqDtoJson = gson.toJson(reqDto);
					
					System.out.println(username + "접속");
					OutputStream outputStream = socket.getOutputStream();
					PrintWriter out = new PrintWriter(outputStream, true);
					out.println(reqDtoJson);
					
					mainCard.show(contentPane, "name_1000424702831000");
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
				
				
				
			}
		});
		inButton.setBounds(95, 396, 251, 32);
		loginpanel.add(inButton);
		
		JScrollPane chatListScroll = new JScrollPane();
		contentPane.add(chatListScroll, "name_1000424702831000");
		
		chatListModel = new DefaultListModel<>();
		chatListModel.clear();
		Collections.list(chatListModel.elements()).stream().distinct().forEach(chatListModel::addElement);
		chatList = new JList<String>(chatListModel);
		chatList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					System.out.println("클릭완료");
					roomname = chatList.getSelectedIndex() == 0 ? null : chatList.getSelectedValue();
					joineduser = username;
					System.out.println(roomname + ","+ joineduser);
					
					JoinRoomReqDto joinRoomReqDto =
							new JoinRoomReqDto(roomname, joineduser);
					
					OutputStream outputStream;
					
					try {
						
						createdRoom(roomname);	
						outputStream = socket.getOutputStream();
						PrintWriter out1 = new PrintWriter(outputStream, true);
						
						ReqDto reqDto = new ReqDto("joinroom", gson.toJson(joinRoomReqDto));
						out1.println(gson.toJson(reqDto));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					 mainCard.show(contentPane, roomname);
				}
			}
		});
		
		chatListScroll.setViewportView(chatList);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 235, 59));
		panel.setPreferredSize(new Dimension(90, 10));
		chatListScroll.setRowHeaderView(panel);
		panel.setLayout(null);
		
		
		ImageIcon kakaoimg = new ImageIcon("images\\kakao2.png"); 
		Image img3 = kakaoimg.getImage();
		Image listkakao = img3.getScaledInstance(90, 72, Image.SCALE_SMOOTH);
		ImageIcon listkakaoc = new ImageIcon(listkakao);
		
		JLabel kakaolabel = new JLabel(listkakaoc);
		kakaolabel.setBounds(0, 0, 90, 72);
		panel.add(kakaolabel);
			
		ImageIcon kakaop = new ImageIcon("images\\kakaoplus.png"); 
		Image img4 = kakaop.getImage();
		Image kakaopl = img4.getScaledInstance(90, 72, Image.SCALE_SMOOTH);
		ImageIcon kakaoplus = new ImageIcon(kakaopl);
		
		
		JLabel addLabel = new JLabel(kakaoplus);
		
		addLabel.addMouseListener(new MouseAdapter() {
	         @Override
	         public void mouseClicked(MouseEvent e) {
	            try {
	               
	               ClientReceive clientReceive = new ClientReceive(socket);
	               clientReceive.start();
	               
	               roomname = JOptionPane.showInputDialog(null, "방 이름을 지어주세요", "방 이름 입력", JOptionPane.INFORMATION_MESSAGE);
	               CreateRoomReqDto createRoomReqDto = new CreateRoomReqDto(roomname,username);
	               String createRoomReqDtoJson = gson.toJson(createRoomReqDto);
	               ReqDto reqDto = new ReqDto("createroom", createRoomReqDtoJson);
	               String reqDtoJson = gson.toJson(reqDto);
	                              
	               OutputStream outputStream = socket.getOutputStream();
	               PrintWriter outer = new PrintWriter(outputStream, true);
	               outer.println(reqDtoJson);
	               
	               createdRoom(roomname);
	               
	            } catch (IOException e1) {
	               e1.printStackTrace();
	            }
	            
	            mainCard.show(contentPane, roomname);
	            
	            
	         }
	      });
		
		addLabel.setBounds(25, 82, 37, 29);
		panel.add(addLabel);
		

		
		
	}
	
	private void sendRequest(String resource, String body) {
		OutputStream outputStream;
		try {
			outputStream = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outputStream,true);
			
			ReqDto reqDto = new ReqDto(resource, body);
			
			out.println(gson.toJson(reqDto));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void createdRoom(String roomname) {
		
		JPanel chattingPanel = new JPanel();
        chattingPanel.setBackground(new Color(255, 235, 59));
        contentPane.add(chattingPanel,roomname);
        chattingPanel.setLayout(null);            
        JScrollPane chatScroll = new JScrollPane();
        chatScroll.setBounds(0, 83, 456, 606);
        chattingPanel.add(chatScroll);
        
        JTextArea textArea = new JTextArea();
        chatScroll.setViewportView(textArea);
        
        contentView = new JTextArea();
        chatScroll.setViewportView(contentView);
        contentView.setEditable(false);
 
        inputChatting = new JTextField();
        inputChatting.addKeyListener(new KeyAdapter() {      
           @Override
           public void keyPressed(KeyEvent e) {
              if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                 sendMessage();
              }            
           }         
        });
        
        inputChatting.setBounds(0, 689, 388, 64);
        chattingPanel.add(inputChatting);
        inputChatting.setColumns(10);
        
        ImageIcon kakaoimg2 = new ImageIcon("images\\kakao2.png"); 
        Image img5 = kakaoimg2.getImage();
        Image listkakao2 = img5.getScaledInstance(94, 84, Image.SCALE_SMOOTH);
        ImageIcon kakao2 = new ImageIcon(listkakao2);
        JLabel chattingPannelLabel = new JLabel(kakao2);
        chattingPannelLabel.setBounds(0, 0, 94, 84);
        chattingPanel.add(chattingPannelLabel);
        
        ImageIcon out = new ImageIcon("images\\out.png"); 
        Image out2 = out.getImage();
        Image outL = out2.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon outIcon = new ImageIcon(outL);
        JLabel chatOut = new JLabel(outIcon);
        chatOut.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        	
        		try {
        			ExitRoomReqDto exitRoomReqDto = new ExitRoomReqDto(username, roomname);
            		String exitReqDtoJson = gson.toJson(exitRoomReqDto);
            		ReqDto reqDto = new ReqDto("exit", exitReqDtoJson);
            		String reqDtoJson = gson.toJson(reqDto);
            		
					OutputStream outputStream = socket.getOutputStream();
					PrintWriter out = new PrintWriter(outputStream, true);
					out.println(reqDtoJson);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		mainCard.show(contentPane, "name_1000424702831000");
        	}
		});
        chatOut.setBounds(377, 10, 67, 63);
        chattingPanel.add(chatOut);
        
        ImageIcon input = new ImageIcon("images\\enter.png"); 
        Image input1 = input.getImage();
        Image input2 = input1.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon enterIcon = new ImageIcon(input2);
        
        JButton trsanportButton = new JButton(enterIcon);
        trsanportButton.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
              sendMessage();
           }
        });
        
        trsanportButton.setBounds(387, 689, 69, 64);
        chattingPanel.add(trsanportButton);
        
        JLabel chattingRoomName = new JLabel(roomname);
        chattingRoomName.setFont(new Font("굴림", Font.PLAIN, 15));
        chattingRoomName.setBounds(106, 26, 236, 32);
        chattingPanel.add(chattingRoomName);
		
	}
	
	private void sendMessage() {
		if(!inputChatting.getText().isBlank()) {
			MessageReqDto messageReqDto = 
					new MessageReqDto(username, roomname, inputChatting.getText());
			
			sendRequest("sendMessage", gson.toJson(messageReqDto));
			inputChatting.setText("");
			
		}
	}
}
