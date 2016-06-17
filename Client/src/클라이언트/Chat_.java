package 클라이언트;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Chat_ extends JFrame implements ActionListener,KeyListener,WindowListener {
	
	//Browser 객체생성
	private BrowserControl b=new BrowserControl();
	
	//Interest 화면 구성요소
	private JFrame Interest_GUI = new JFrame();
	private JPanel Interest_Panel;
	private JButton bob_btn;
	private JButton sul_btn;
	private JButton gomin_btn;
	
	private JLabel bob_label = new JLabel("      #\uBC25");
	private JLabel sul_label = new JLabel("      #\uC220");
	private JLabel gomin_label = new JLabel("     #\uACE0\uBBFC");
	private Image img2;

	
	//Start 화면 구성요소
	private JFrame Start_GUI = new JFrame();
	private JPanel Start_Panel;
	private JButton Start_btn;
	private JButton Interest_btn;
	private JLabel Start_label;
	private Image img1;
	
	//Main 화면 구성요소
	private JPanel contentPane;
	private JTextField textField;
	private JButton send_btn = new JButton("");
	private JTextArea textArea = new JTextArea();
	private JScrollPane scroller = new JScrollPane(textArea);
	private JButton newChat_btn = new JButton("");

	private JButton facebook_btn = new JButton("");
	private JButton skhu_btn = new JButton("");
	private Image img3;
	
	//통신 구성요소
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private StringTokenizer st;
	private String ip ="127.0.0.1";
	private int port = 6202;
	private String nickName;
	private String path;
	private String itr = "default";
	
	
	private String MyRoom;

	public void Start_init()
	{
		Toolkit kit1=Toolkit.getDefaultToolkit();
	    img1=kit1.getImage("res/ico.png");
	    Start_GUI.setIconImage(img1);
		
		Start_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Start_GUI.setBounds(100, 100, 358, 567);
		Start_Panel = new JPanel();
		Start_Panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		Start_GUI.setContentPane(Start_Panel);
		Start_Panel.setLayout(null);
		
		Start_label = new JLabel("");
		Start_label.setIcon(new ImageIcon("res/sistalk.jpg"));
		Start_label.setBounds(0, 0, 342, 444);
		Start_Panel.add(Start_label);
		
		Start_btn = new JButton("");
		Start_btn.setIcon(new ImageIcon("res/start.png"));
		Start_btn.setBackground(UIManager.getColor("Button.disabledShadow"));
		Start_btn.setForeground(new Color(0, 0, 0));
		Start_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		Start_btn.setFont(new Font("굴림", Font.BOLD, 30));
		Start_btn.setBounds(0, 444, 171, 85);
		Start_Panel.add(Start_btn);
		
		Interest_btn = new JButton("");
		Interest_btn.setIcon(new ImageIcon("res/interest.png"));
		Interest_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		Interest_btn.setBackground(Color.WHITE);
		Interest_btn.setForeground(Color.WHITE);
		Interest_btn.setFont(new Font("굴림", Font.BOLD, 30));
		Interest_btn.setBounds(171, 444, 171, 85);
		Start_Panel.add(Interest_btn);
		Start_GUI.setVisible(true);
		
	}
	public void Interest_init()
	{
		Toolkit kit2=Toolkit.getDefaultToolkit();
		img2=kit2.getImage("res/ico.png");
		Interest_GUI.setIconImage(img2);	 
	    
		Interest_GUI.setBounds(100, 100, 466, 318);
		Interest_Panel = new JPanel();
		Interest_Panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		Interest_GUI.setContentPane(Interest_Panel);
		Interest_Panel.setLayout(null);
		
		bob_btn = new JButton("");
		bob_btn.setIcon(new ImageIcon("res/bob.png"));
		bob_btn.setBounds(0, 0, 150, 234);
		Interest_Panel.add(bob_btn);
		
		sul_btn = new JButton("");
		sul_btn.setIcon(new ImageIcon("res/sul.png"));
		sul_btn.setBounds(150, 0, 150, 234);
		Interest_Panel.add(sul_btn);
		
		gomin_btn = new JButton("");
		gomin_btn.setIcon(new ImageIcon("res/gomin.png"));
		gomin_btn.setBounds(300, 0, 150, 234);
		Interest_Panel.add(gomin_btn);
		
		bob_label.setForeground(Color.RED);
		bob_label.setOpaque(true);
		bob_label.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		bob_label.setBackground(new Color(255, 228, 225));
		bob_label.setFont(new Font("HY동녘B", Font.BOLD, 22));
		bob_label.setBounds(0, 234, 150, 46);
		Interest_Panel.add(bob_label);
		
		sul_label.setForeground(Color.BLUE);
		sul_label.setOpaque(true);
		sul_label.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		sul_label.setBackground(new Color(255, 228, 225));
		sul_label.setFont(new Font("HY동녘B", Font.BOLD, 22));
		sul_label.setBounds(150, 234, 150, 46);
		Interest_Panel.add(sul_label);
		
		gomin_label.setForeground(new Color(0, 102, 51));
		gomin_label.setOpaque(true);	
		gomin_label.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		gomin_label.setBackground(new Color(255, 228, 225));
		gomin_label.setFont(new Font("HY동녘B", Font.BOLD, 22));
		gomin_label.setBounds(300, 234, 150, 46);
		Interest_Panel.add(gomin_label);

		
		
		Interest_GUI.setVisible(false);
	}
	
	public void Main_init()
	{
		Toolkit kit3=Toolkit.getDefaultToolkit();
		img3=kit3.getImage("res/ico.png");
		this.setIconImage(img3);		
		
		setBounds(100, 100, 614, 436);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBackground(new Color(255, 228, 225));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		send_btn.setIcon(new ImageIcon("res/send.png"));
		send_btn.setBackground(Color.WHITE);
		
		
		send_btn.setBounds(489, 314, 97, 39);
		contentPane.add(send_btn);
		
		textField = new JTextField();
		textField.setBounds(12, 314, 478, 39);
		contentPane.add(textField);
		textField.setColumns(10);
		
		
		textArea.setBounds(12, 10, 574, 294);
		//contentPane.add(textArea);
		scroller.setBounds(12, 10, 574, 294);
		newChat_btn.setIcon(new ImageIcon("res/research.png"));
		newChat_btn.setBackground(Color.WHITE);
		
		
		
		newChat_btn.setBounds(489, 363, 97, 24);
		contentPane.add(newChat_btn);
		newChat_btn.setEnabled(false);
		
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scroller);
		
		facebook_btn.setIcon(new ImageIcon("res/facebook2.png"));
		facebook_btn.setBounds(12, 363, 26, 24);
		contentPane.add(facebook_btn);
		
		
		skhu_btn.setIcon(new ImageIcon("res/skhu3.png"));
		skhu_btn.setBounds(50, 363, 27, 25);
		contentPane.add(skhu_btn);
		
		this.addWindowListener(this);
		setVisible(false);		
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Chat_();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	

	public Chat_() {
		Main_init();
		Start_init();
		Interest_init();
		start();
	}
	public void start()
	{
		Start_btn.addActionListener(this);
		Interest_btn.addActionListener(this);
		send_btn.addActionListener(this);
		textField.addKeyListener(this);
		newChat_btn.addActionListener(this);
		bob_btn.addActionListener(this);
		sul_btn.addActionListener(this);
		gomin_btn.addActionListener(this);
		facebook_btn.addActionListener(this);
		skhu_btn.addActionListener(this);
	}
	
	private void Connection()	// 실질적인 메소드 연결부분 ,서버와 주고 받음
	{
		try{
		is = socket.getInputStream();
		dis = new DataInputStream(is);
		
		os = socket.getOutputStream();
		dos = new DataOutputStream(os);
		
		
		// 랜덤하게 닉네임을 설정, 한 컴퓨터에서 할경우 아이피는 동일 아이피라서 구별이 불가능
		Random r = new Random();
		nickName = ""+r.nextInt(59999)+1;
		send_message(nickName);
		System.out.println(itr);
		send_message(itr);
		
		}
		catch(IOException e)	// 에러처리부분
		{
			JOptionPane.showMessageDialog(null,"연결 실패","알림",JOptionPane.ERROR_MESSAGE);
		}	// Stream 설정 끝
		
		
		this.setVisible(true); //연결이 정상적으로 됬다면 채팅창 나옴
		Start_GUI.setVisible(false); // 스타트 화면 안보임.
		Interest_GUI.setVisible(false);
		
		Thread th = new Thread(new Runnable(){
			
			public void run()
			{
				while(true)
				{
					try {
						String msg = dis.readUTF();	//메세지 수신
						
						System.out.println("서버로부터 수신된 메세지"+msg);
						
						inmessage(msg);
					} catch (IOException e) {
						
						try{
						os.close();
						is.close();
						socket.close();
						JOptionPane.showMessageDialog(null,"서버와 접속 끊어짐","알림",JOptionPane.ERROR_MESSAGE);
						}
						catch(IOException e1){}
						
						break;
						
						
						
					} 
			}
			}
		});
		
		th.start();
	}

	private void inmessage(String str)	// 서버로부터 들어오는 모든 메세지
	{
		System.out.println(str);
		
		st = new StringTokenizer(str,"/");
		
		
		
		String protocol = st.nextToken();
		String Message = st.nextToken();
		
		System.out.println("프로토콜:"+protocol);
		System.out.println("내용:"+Message);
		
		if(protocol.equals("Wait"))
		{
			textArea.setText("현재 성공회인을 찾고있습니다 잠시만 기다려주세요~\n");
			
		}
		else if(protocol.equals("Find"))
		{
			
			textArea.setText("멋진 성공회인을 찾았습니다. 즐거운 대화 나누세요~\n");
			
		}
		else if(protocol.equals("NewChat"))		
		{
			
			String roomName = Message;
			// 이전에 채팅하던 사람에게 남기는 메세지
			
			if(MyRoom.equals(roomName))	// 방번호가 같으면
			{
				textArea.setText("상대방이 채팅방을 나갔습니다 또 다른 성공회인을 만나고 싶다면 다시찾기 버튼을 누르세요~\n");
				newChat_btn.setEnabled(true);
				send_message("AnotherChat/"+MyRoom);
				
			}
		}
		else if(protocol.equals("PartnerMessage"))
		{
			textArea.append("Stranger:"+Message);
		}
		else if(protocol.equals("RoomName")) 
		{
			
			MyRoom = Message;
			
		}
		else if(protocol.equals("Chat")) // 채팅의 경우 / 프로토콜 / 사용자 / 메세지로 온다. 
		{
			String user = Message;
			String msg = st.nextToken();
			
			/** 내이름하고 같지 않는 경우 **/ 
			if(!user.equals(nickName))
			{
				textArea.append("Stranger : "+msg+"\n");
			}
		}
		else if(protocol.equals("Error"))
		{
			String roomName = Message;
			
			if(MyRoom.equals(roomName))
			{
				textArea.setText("상대방이 연결문제로 인해 접속이 끊겼습니다 새로운 채팅을 원하시면 버튼을 눌러주세요");
				
			}
		}
				
	}
	
	private void send_message(String str)	// 서버에게 메세지를 보내는 부분
	{
		try {
			dos.writeUTF(str);
		} catch (IOException e) 	// 에러처리부분
		{
			
			e.printStackTrace();
		}
	}	
			
	private void Network()
	{
		try {
			socket = new Socket(ip,port);
			
			if(socket != null)	// 정상적으로 소켓이 연결되었을 경우
			{
				Connection();
			}
		} catch (UnknownHostException e) {
			
			JOptionPane.showMessageDialog(null,"연결 실패","알림",JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			
			JOptionPane.showMessageDialog(null,"연결 실패","알림",JOptionPane.ERROR_MESSAGE);
		}
	}


	
	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() == e.VK_ENTER)
		{
			
			String s = textField.getText().trim();   
			textArea.append("Me : "+s+"\n");
			
			// 메세지를 보낼때는 채팅+사용자+메세지  3개로 보낸다. 프로토콜은 2개가 된다. 
			send_message("Chat/"+nickName+"/ "+s);
			textField.setText("");
		}
		
		
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==Start_btn){  //스타트 버튼 클릭시 네트워크 호출
		
		
			
			Network();
			
			
		}
		else if(e.getSource()==Interest_btn) // 인터레스트 버튼 클릭시 버튼3개 나온다.
		{
			Interest_GUI.setVisible(true);
		}
		else if(e.getSource()==bob_btn)  
		{	
			itr="밥";
			Network();
		
		}
		else if(e.getSource()==sul_btn)
		{
			itr="술";
			Network();
			
		}
		else if(e.getSource()==gomin_btn)
		{
			itr="고민";
			Network();
			
		}
		else if(e.getSource()==send_btn)
		{
			String s = textField.getText().trim();
			textArea.append("Me : "+s+"\n");
			
			
			// 메세지를 보낼때는 채팅+사용자+메세지  3개로 보낸다. 프로토콜은 2개가 된다. 
			send_message("Chat/"+nickName+"/ "+s);
			textField.setText("");
		
			
		}
		else if(e.getSource() == newChat_btn)
		{
			send_message("FindAnother/ ");
			newChat_btn.setEnabled(false);
			
		}
		else if(e.getSource()==facebook_btn)
		{
			b.openUrl("https://www.facebook.com/sistalk6202/");
		}
		else if(e.getSource()==skhu_btn)
		{
			b.openUrl("http://www.skhu.ac.kr/intro3.aspx");
		}
	}



	@Override
	public void windowClosing(WindowEvent e) {
		send_message("OutRoom/"+MyRoom);
		
		System.exit(0);
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	

}
