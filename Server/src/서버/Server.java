package ����;

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
	private JButton start_btn = new JButton("���� ����");
	private JButton stop_btn = new JButton("���� ����");

	// Network �ڿ�
	private Image img4;
	private ServerSocket server_socket;
	private Socket socket; // ����� ����
	private int port;
	private Vector<User> user_vc = new Vector();
	public static Vector<Room> room_vc = new Vector();
	private StringTokenizer st;
	public final int pt = 6202; // ������ ��Ʈ��ȣ
	private boolean roomCheck = true;

	public Server() // ������ ����
	{
		init(); // ȭ�� ���� �޼ҵ�
		start(); // ������ ���� �޼ҵ�

	}
	
	public void start() {
		start_btn.addActionListener(this);
		stop_btn.addActionListener(this);
	}
	public Vector<Room> getRoom()
	{
		return room_vc;
	}

	private void init() // ȭ�鱸��
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

		this.setVisible(true); // true ȭ�鿡 ���̰� false �Ⱥ��̰�
	}

	private void Server_start() {
		try {
			server_socket = new ServerSocket(pt); // 12345��Ʈ���
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "�̹� ������� ��Ʈ", "�˸�",
					JOptionPane.ERROR_MESSAGE);
		}

		if (server_socket != null) // ���������� ��Ʈ�� ������ ���
		{
			Connection();

		}
	}

	private void Connection() 
	{
		// 1������ �����忡���� 1������ �ϸ� ó���� �� �ִ�.

		Thread th = new Thread(new Runnable() 
		{
			
			public void run() // ������ ������ ó���� ���� ����
			{

				while (true) 
				{
					try 
					{
						textArea.append("����� ���� �����\n");
						socket = server_socket.accept(); // ����� ���� ��� ���Ѵ��
						textArea.append("����� ����!!!\n");
						
						
										
						User user = new User(socket);
						user.start();
						
						System.out.println("��¥��¥��:"+user.userWant);
						System.out.println("�г���:"+user.nickName);
						
						// ������ ����ڿ� ���� ó�� 
						
						find_Room(user);
						
					}catch (IOException e) {}
				} // while�� ��
			}

		});

		th.start();

	}
	
	
		
	
		

	
	
	public void find_Room(User user) 
	{
		System.out.println("������ �� ������ : "+room_vc.size());
		
		// ����ȸ ���� ���� false�� �ʱ�ȭ ��Ų��.
		roomCheck = false;
		
		if(room_vc.size() == 0) 	// ó�� �ʱ⿡ ���̾ƹ��͵� ������ 
		{
			//���� �����
			Room room = new Room();
			
			// �����ϰ� ���̸��� ����
			Random ran = new Random();
			
			String random_room = ""+ran.nextInt(999)+1;
			
			// ���̸��� �߰� ���ش� 
			room.setName(random_room);
			
			//�� ���Ϳ� �߰�
			room_vc.add(room);
			
			// �濡 ����� �߰� 
			room.inUser(user);
			
			// ����ڿ��� �� �Ҵ�
			user.set_Room(room);
			room.setName(random_room);
			// ����ڿ��� ��� �˸���
			user.send_Message("Wait/ ");
			
			// ����ڿ��� ������ ���� �˷��ش�.
			user.send_Message("RoomName/"+random_room);
			System.out.println(user.userWant);
		}
		else // �ʱ� ���� �ƴҶ� , �� ���� ó�� ������ �ƴ� ���
		{
			textArea.append("�� ��ȸ ��\n");
			System.out.println("�ʱ� ���� �ƴ� ��� ���� ������ : "+room_vc.size());
			
			for(int i = 0; i < room_vc.size(); i++)
			{
				System.out.println("����ȸ�� ");
				Room room = room_vc.get(i);
				
				// ��ȿ� ����ִ� ��� ã�� 
				int count = room.get_UserCount();
				
				
				if(count == 1) // ����� 1���� ��� �ִ� ��� 
				{
					System.out.println("��:"+user.userWant);
					System.out.println("����:"+room.getVector().get(0).userWant);
					
					String my 	= user.userWant;  // ���� ��� 
					String you 	=  room.getVector().get(0).userWant; // ������ ���
					
					// �� ��̿� �濡 �ִ� ����� ��̰� ���� ���
					if(my.equals(you))
					{
						
						room.inUser(user);
						
						// ����ڿ��� �� �Ҵ�
						user.set_Room(room);
						user.send_Message("RoomName/"+room.getName());
						
						room.broad_cast("Find/ ");
						roomCheck = true;
						
						// �� ��Ī ���� 
						roomCheck = true;
						
						System.out.println("���Ī ����");
					}
					
				}
				else
				{
					System.out.println("�濡 ����ڰ� �� á�ų�, 0������ ����� ��");
				}
			}
			
			
			if(!roomCheck) // �� ���͸� �� ���� ã�� ���� ���� ��� ���ο� ������ ����
			{
				//���� �����
				Room room = new Room();
				
				// �����ϰ� ���̸��� ����
				Random ran = new Random();
				
				String random_room = ""+ran.nextInt(999)+1;
				
				// ���̸��� �߰� ���ش� 
				room.setName(random_room);
				
				//�� ���Ϳ� �߰�
				room_vc.add(room);
				
				// �濡 ����� �߰� 
				room.inUser(user);
				
				// ����ڿ��� �� �Ҵ�
				user.set_Room(room);
				room.setName(random_room);
				// ����ڿ��� ��� �˸���
				user.send_Message("Wait/ ");
				
				// ����ڿ��� ������ ���� �˷��ش�.
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
			System.out.println("��ŸƮ ��ư Ŭ��");
			Server_start(); // ���� ���� �� ����� ���� ���

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
			System.out.println("���� ��ž ��ư Ŭ��");
		}

	} // �׼� �̺�Ʈ ��




}
