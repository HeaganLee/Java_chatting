package client;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import java.awt.Button;
import java.awt.Label;
import java.awt.Font;

public class ChattingClient extends JFrame {

	private JPanel contentPane;
	private Image kakao;
	private int w;
	private int h;
	private JTextField textField;
	private JTextField textField_1;
	
	
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 800);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 235, 59));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel loginpanel = new JPanel();
		loginpanel.setBackground(new Color(255, 235, 59));
		contentPane.add(loginpanel, "name_999600029531900");
		loginpanel.setLayout(null);
		
		ImageIcon imgTest = new ImageIcon("images\\kakao2.png"); 
		Image img = imgTest.getImage();
		Image changeImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		
		JLabel lblNewLabel = new JLabel(changeIcon);
		lblNewLabel.setBackground(new Color(242, 242, 0));
		lblNewLabel.setBounds(177, 251, 100, 66);
		loginpanel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(95, 344, 251, 32);
		loginpanel.add(textField);
		textField.setColumns(10);
		
		ImageIcon startimg = new ImageIcon("C:\\Users\\qusei\\OneDrive\\바탕 화면\\kakaostart.png"); 
		Image img2 = startimg.getImage();
		Image changeImg2 = img2.getScaledInstance(251, 50, Image.SCALE_SMOOTH);
		ImageIcon changeIcon2 = new ImageIcon(changeImg2);
		
		JButton btnNewButton = new JButton(changeIcon2);
		btnNewButton.setBounds(95, 396, 251, 32);
		loginpanel.add(btnNewButton);
		
		JScrollPane chatListScroll = new JScrollPane();
		contentPane.add(chatListScroll, "name_1000424702831000");
		
		JList chatList = new JList();
		chatListScroll.setViewportView(chatList);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 235, 59));
		panel.setPreferredSize(new Dimension(90, 10));
		chatListScroll.setRowHeaderView(panel);
		panel.setLayout(null);
		
		
		ImageIcon kakaoimg = new ImageIcon("C:\\Users\\qusei\\OneDrive\\바탕 화면\\kakao2.png"); 
		Image img3 = kakaoimg.getImage();
		Image listkakao = img3.getScaledInstance(90, 72, Image.SCALE_SMOOTH);
		ImageIcon listkakaoc = new ImageIcon(listkakao);
		
		JLabel kakaolabel = new JLabel(listkakaoc);
		kakaolabel.setBounds(0, 0, 90, 72);
		panel.add(kakaolabel);
			
		ImageIcon kakaop = new ImageIcon("C:\\Users\\qusei\\OneDrive\\바탕 화면\\kakaoplus.png"); 
		Image img4 = kakaop.getImage();
		Image kakaopl = img4.getScaledInstance(90, 72, Image.SCALE_SMOOTH);
		ImageIcon kakaoplus = new ImageIcon(kakaopl);
		
		
		JLabel addLabel = new JLabel(kakaoplus);
		addLabel.setBounds(25, 82, 37, 29);
		panel.add(addLabel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 235, 59));
		contentPane.add(panel_1, "name_1002260299991800");
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 83, 456, 606);
		panel_1.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		textField_1 = new JTextField();
		textField_1.setBounds(0, 689, 388, 64);
		panel_1.add(textField_1);
		textField_1.setColumns(10);
		
		
		
		ImageIcon kakaoimg2 = new ImageIcon("C:\\Users\\qusei\\OneDrive\\바탕 화면\\kakao2.png"); 
		Image img5 = kakaoimg.getImage();
		Image listkakao2 = img5.getScaledInstance(94, 84, Image.SCALE_SMOOTH);
		ImageIcon kakao2 = new ImageIcon(listkakao2);
		JLabel label = new JLabel(kakao2);
		label.setBounds(0, 0, 94, 84);
		panel_1.add(label);
		
		ImageIcon out = new ImageIcon("C:\\Users\\qusei\\OneDrive\\바탕 화면\\out.png"); 
		Image out2 = out.getImage();
		Image outL = out2.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon outIcon = new ImageIcon(outL);
		JLabel outLabel = new JLabel(outIcon);
		outLabel.setBounds(377, 10, 67, 63);
		panel_1.add(outLabel);
		
		ImageIcon input = new ImageIcon("C:\\Users\\qusei\\OneDrive\\바탕 화면\\enter.png"); 
		Image input1 = input.getImage();
		Image input2 = input1.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon enterIcon = new ImageIcon(input2);
		JButton btnNewButton_1 = new JButton(enterIcon);
		btnNewButton_1.setBounds(387, 689, 69, 64);
		panel_1.add(btnNewButton_1);
		
		JLabel lblNewLabel_1 = new JLabel("김동민님의 방입니다");
		lblNewLabel_1.setFont(new Font("굴림", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(106, 26, 236, 32);
		panel_1.add(lblNewLabel_1);
		
		
	}
}
