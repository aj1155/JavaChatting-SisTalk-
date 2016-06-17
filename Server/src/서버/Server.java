package 서버;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Server extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextArea textArea = new JTextArea();
	private JButton start_btn = new JButton("서버 실행");
	private JButton stop_btn = new JButton("서버 중지");

	// Network 자원
	private Image img4;
	private ServerSocket server_socket;
	private Socket socket; // 사용자 소켓
	private int port;
	private Vector<User> user_vc = new Vector();
	public static Vector<Room> room_vc = new Vector();
	private StringTokenizer st;
	public final int pt = 6202; // 고정된 포트번호
	private boolean roomCheck = true;

	public Server() // 생성자 생성
	{
		init(); // 화면 생성 메소드
		start(); // 리스너 설정 메소드

	}
	
	public void start() {
		start_btn.addActionListener(this);
		stop_btn.addActionListener(this);
	}
	public Vector<Room> getRoom()
	{
		return room_vc;
	}

	private void init() // 화면구성
	{

		Toolkit kit4=Toolkit.getDefaultToolkit();
		img4=kit4.getImage("res/ico.png");
		this.setIconImage(img4);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 346, 252);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    contentPane.setBackground(new Color(255, 228, 225));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 306, 120);
		contentPane.add(scrollPane);

		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);

		start_btn.setBounds(12, 169, 153, 23);
		contentPane.add(start_btn);

		stop_btn.setBounds(166, 169, 152, 23);
		contentPane.add(stop_btn);
		stop_btn.setEnabled(false);

		this.setVisible(true); // true 화면에 보이게 false 안보이게
	}

	private void Server_start() {
		try {
			server_socket = new ServerSocket(pt); // 12345포트사용
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "이미 사용중인 포트", "알림",
					JOptionPane.ERROR_MESSAGE);
		}

		if (server_socket != null) // 정상적으로 포트가 열렸을 경우
		{
			Connection();

		}
	}

	private void Connection() 
	{
		// 1가지의 스레드에서는 1가지의 일만 처리할 수 있다.

		Thread th = new Thread(new Runnable() 
		{
			
			public void run() // 스레드 내에서 처리할 일을 기재
			{

				while (true) 
				{
					try 
					{
						textArea.append("사용자 접속 대기중\n");
						socket = server_socket.accept(); // 사용자 접속 대기 무한대기
						textArea.append("사용자 접속!!!\n");
						
						
										
						User user = new User(socket);
						user.start();
						
						System.out.println("진짜찐짜밥:"+user.userWant);
						System.out.println("닉네임:"+user.nickName);
						
						// 접속한 사용자에 대한 처리 
						
						find_Room(user);
						
					}catch (IOException e) {}
				} // while문 끝
			}

		});

		th.start();

	}
	
	
		
	
		

	
	
	public void find_Room(User user) 
	{
		System.out.println("현재의 방 사이즈 : "+room_vc.size());
		
		// 방조회 들어가기 전에 false로 초기화 시킨다.
		roomCheck = false;
		
		if(room_vc.size() == 0) 	// 처음 초기에 방이아무것도 없을때 
		{
			//방을 만든다
			Room room = new Room();
			
			// 랜덤하게 방이름을 지정
			Random ran = new Random();
			
			String random_room = ""+ran.nextInt(999)+1;
			
			// 방이름을 추가 해준다 
			room.setName(random_room);
			
			//방 벡터에 추가
			room_vc.add(room);
			
			// 방에 사용자 추가 
			room.inUser(user);
			
			// 사용자에게 방 할당
			user.set_Room(room);
			room.setName(random_room);
			// 사용자에게 대기 알리기
			user.send_Message("Wait/ ");
			
			// 사용자에게 지정된 방을 알려준다.
			user.send_Message("RoomName/"+random_room);
			System.out.println(user.userWant);
		}
		else // 초기 방이 아닐때 , 즉 서버 처음 실행이 아닌 경우
		{
			textArea.append("방 조회 중\n");
			System.out.println("초기 방이 아닌 경우 방의 사이즈 : "+room_vc.size());
			
			for(int i = 0; i < room_vc.size(); i++)
			{
				System.out.println("방조회중 ");
				Room room = room_vc.get(i);
				
				// 방안에 들어있는 사람 찾기 
				int count = room.get_UserCount();
				
				
				if(count == 1) // 사람이 1명이 들어 있는 경우 
				{
					System.out.println("나:"+user.userWant);
					System.out.println("상대방:"+room.getVector().get(0).userWant);
					
					String my 	= user.userWant;  // 나의 취미 
					String you 	=  room.getVector().get(0).userWant; // 상대방의 취미
					
					// 내 취미와 방에 있는 사람의 취미가 같은 경우
					if(my.equals(you))
					{
						
						room.inUser(user);
						
						// 사용자에게 방 할당
						user.set_Room(room);
						user.send_Message("RoomName/"+room.getName());
						
						room.broad_cast("Find/ ");
						roomCheck = true;
						
						// 방 매칭 성공 
						roomCheck = true;
						
						System.out.println("방매칭 성공");
					}
					
				}
				else
				{
					System.out.println("방에 사용자가 꽉 찼거나, 0명으로 비어진 방");
				}
			}
			
			
			if(!roomCheck) // 방 벡터를 다 돌고도 찾은 방이 없는 경우 새로운 방으로 설정
			{
				//방을 만든다
				Room room = new Room();
				
				// 랜덤하게 방이름을 지정
				Random ran = new Random();
				
				String random_room = ""+ran.nextInt(999)+1;
				
				// 방이름을 추가 해준다 
				room.setName(random_room);
				
				//방 벡터에 추가
				room_vc.add(room);
				
				// 방에 사용자 추가 
				room.inUser(user);
				
				// 사용자에게 방 할당
				user.set_Room(room);
				room.setName(random_room);
				// 사용자에게 대기 알리기
				user.send_Message("Wait/ ");
				
				// 사용자에게 지정된 방을 알려준다.
				user.send_Message("RoomName/"+random_room);
				System.out.println(user.userWant);
			}
		}
		
	}

	public static void main(String[] args) 
	{
		new Server();

	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start_btn) {
			System.out.println("스타트 버튼 클릭");
			Server_start(); // 소켓 생성 및 사용자 접속 대기

			start_btn.setEnabled(false);
			stop_btn.setEnabled(true);
		} else if (e.getSource() == stop_btn) {
			stop_btn.setEnabled(false);
			start_btn.setEnabled(true);

			try {
				server_socket.close();
				user_vc.removeAllElements();

			} catch (IOException e1) {

			}
			System.out.println("서버 스탑 버튼 클릭");
		}

	} // 액션 이벤트 끝




}
