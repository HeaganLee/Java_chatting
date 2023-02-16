package client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

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

import dto.JoinReqDto;
import dto.ReqDto;

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
	
	private CardLayout mainCard;
	private JPanel contentPane;
	private JTextField usernameField;
	private JTextField inputChatting;
	private JList<String> roomList;
	private DefaultListModel<String> roomListModel;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					
					ChattingClient frame = new ChattingClient();
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
					
					// 유저이름과 join 요청
					username = usernameField.getText();
					JoinReqDto joinReqDto = new JoinReqDto(username);
					String joinReqDtoJson = gson.toJson(joinReqDto);
					ReqDto reqDto = new ReqDto("join", joinReqDtoJson);
					String reqDtoJson = gson.toJson(reqDto);
					
					OutputStream outputStream = socket.getOutputStream();
					PrintWriter out = new PrintWriter(outputStream, true);
					out.println(reqDtoJson);
					
					mainCard.show(contentPane, "chatListScroll");
					
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
		
		JList chatList = new JList();
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
		addLabel.setBounds(25, 82, 37, 29);
		panel.add(addLabel);
		
		JPanel chattingPanel = new JPanel();
		chattingPanel.setBackground(new Color(255, 235, 59));
		contentPane.add(chattingPanel, "name_1002260299991800");
		chattingPanel.setLayout(null);
		
		JScrollPane chatScroll = new JScrollPane();
		chatScroll.setBounds(0, 83, 456, 606);
		chattingPanel.add(chatScroll);
		
		JTextArea textArea = new JTextArea();
		chatScroll.setViewportView(textArea);
		
		inputChatting = new JTextField();
		inputChatting.setBounds(0, 689, 388, 64);
		chattingPanel.add(inputChatting);
		inputChatting.setColumns(10);
		
		
		
		ImageIcon kakaoimg2 = new ImageIcon("images\\kakao2.png"); 
		Image img5 = kakaoimg.getImage();
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
		chatOut.setBounds(377, 10, 67, 63);
		chattingPanel.add(chatOut);
		
		ImageIcon input = new ImageIcon("images\\enter.png"); 
		Image input1 = input.getImage();
		Image input2 = input1.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon enterIcon = new ImageIcon(input2);
		JButton trsanportButton = new JButton(enterIcon);
		trsanportButton.setBounds(387, 689, 69, 64);
		chattingPanel.add(trsanportButton);
		
		JLabel chattingRoomName = new JLabel("김동민님의 방입니다");
		chattingRoomName.setFont(new Font("굴림", Font.PLAIN, 15));
		chattingRoomName.setBounds(106, 26, 236, 32);
		chattingPanel.add(chattingRoomName);
		
		
	}
}
