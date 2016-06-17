package 서버;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.StringTokenizer;

public class User extends Thread {
	private OutputStream os;
	private InputStream is;
	public DataOutputStream dos;
	private DataInputStream dis;

	private Socket user_socket;
	public String nickName = ""; // 사용자 구별하기 위한 아이디
	private StringTokenizer st;
	private int userNum =0;
	private String sender;
	private String senderRoom;
	public String userWant;
	private boolean Roomch = false;

	
	

	private Room myRoom = null;
	

	public User(Socket soc) // 생성자 메소드
	{
		this.user_socket = soc;
		
		UserNetwork();
	}

	private void UserNetwork() {

		try {
			is = user_socket.getInputStream();
			dis = new DataInputStream(is);

			os = user_socket.getOutputStream();
			dos = new DataOutputStream(os);
			
			// 처음에 사용자 명칭을 받는다. // 그래서 you 와 me를 구분한다
			nickName = dis.readUTF();
			System.out.println("접속한 사용자 닉네임 : "+nickName);
			userWant = dis.readUTF();
			System.out.println("접속한 사용자 원하는거 : "+userWant);
		} catch (IOException e) {
			
			
			}
			
			
		}
	

	public void run() // Thread 에서 처리할 내용
	{
		while (true) {
			try {

				String msg = dis.readUTF();
				InMessage(msg);
			} catch (IOException e) {
				System.out.println("사용자 접속 끊음");
				
				try {
					dos.close();
					dis.close();
					user_socket.close();
				} catch (IOException e1) {		
					Roomch = false;
					myRoom.broad_cast("Error/"+myRoom.getName());
				}
				break;
			}
		}
	} // run 메소드 긑

	public void InMessage(String str) // 클라이언트로부터 들어오는 메세지 처리
	{
		st = new StringTokenizer(str,"/");
		
		String protocol = st.nextToken();
		String Message = st.nextToken();
		
		System.out.println("프로토콜:"+protocol);
		System.out.println("내용:"+Message);
		
		
		if(protocol.equals("Me"))
		{
			send_Message("PartnerMessage/"+Message);
		}
		else if(protocol.equals("Chat")) //채팅+사용자+방번호+메세지  3개로 보낸다. 프로토콜은 2개가 된다.
		{
			String user = Message; // 사용자 이름
			String msg = st.nextToken(); // 실제 메세지 
			
			
			// 사용자가 속한 방에 메세지를 전달 해준다.
			String return_msg = "Chat"+"/"+user+"/"+msg;
			myRoom.broad_cast(return_msg);
			
		}
		else if(protocol.equals("OutRoom"))
		{
			
			
			String roomName = Message;
			myRoom.remove_User(this);
			
			String return_msg = "NewChat/"+roomName;
			myRoom.broad_cast(return_msg);
		}
		else if(protocol.equals("AnotherChat"))
		{
			myRoom.remove_User(this);							// 방에서 자신을 지움
		}
		else if(protocol.equals("inter"))
		{
			userWant = Message;
				
			
		}
		else if(protocol.equals("FindAnother"))						// newchat버튼을 누르면 다시 처음부터 상대방 찾음
		{
			
			if(Server.room_vc.size() == 0) 	// 처음 초기에 방이아무것도 없을때 
			{
				//방을 만든다
				Room room = new Room();
				
				// 랜덤하게 방이름을 지정
				Random ran = new Random();
				
				String random_room = ""+ran.nextInt(999)+1;
				
				// 방이름을 추가 해준다 
				room.setName(random_room);
				
				//방 벡터에 추가
				Server.room_vc.add(room);
				
				// 방에 사용자 추가 
				room.inUser(this);
				
				// 사용자에게 방 할당
				this.set_Room(room);
				room.setName(random_room);
				// 사용자에게 대기 알리기
				this.send_Message("Wait/ ");
				
				// 사용자에게 지정된 방을 알려준다.
				this.send_Message("RoomName/"+random_room);
			}
			else // 초기 방이 아닐때 , 즉 서버 처음 실행이 아닌 경우
			{
				
				
				for(int i = 0; i <Server.room_vc.size(); i++)
				{
					// 비어 있는 방 찾기
					Room room = Server.room_vc.get(i);
					
					// 방안에 들어있는 사람 찾기 
					int count = room.get_UserCount();
					
					
					if(count==1) // 방에 1명이 들어 가있고 대기 중일때 
					{
						String my 	= this.userWant;  // 나의 취미 
						String you 	=  room.getVector().get(0).userWant; // 상대방의 취미
						
						if(my.equals(you))
						{
							//나머지 사용자 1명 지정 
							room.inUser(this);
							
							
							// 사용자에게 방 할당
							this.set_Room(room);
							this.send_Message("RoomName/"+room.getName());
							
							room.broad_cast("Find/ ");
							Roomch = true;
						}
					
					}
					else if(count == 0)
					{
						room.inUser(this);
						this.set_Room(room);
						this.send_Message("RoomName/"+room.getName());
						
						room.broad_cast("Wait/ ");
						
						Roomch = true;
					}
					else if(count == 2)
					{
						Roomch = false;
					}
					
					
				}
				if(Roomch == false)
				{
					Room room2 = new Room();
					
					// 랜덤하게 방이름을 지정
					Random ran = new Random();
					
					String random_room = ""+ran.nextInt(999)+1;
					
					// 방이름을 추가 해준다 
					room2.setName(random_room);
					
					//방 벡터에 추가
					Server.room_vc.add(room2);
					
					// 방에 사용자 추가 
					room2.inUser(this);
					
					// 사용자에게 방 할당
					this.set_Room(room2);
					room2.setName(random_room);
					// 사용자에게 대기 알리기
					this.send_Message("Wait/ ");
					
					// 사용자에게 지정된 방을 알려준다.
					this.send_Message("RoomName/"+random_room);
					
					Roomch = true;
				}
				}
			}
			
		}	
	
		
	public void send_Message(String str) // 문자열을 받아서 전송
	{
		try {
			dos.writeUTF(str);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	private void remove_myRoom(Room myRoom) 
	{
		
		// 사용자가 요청한 방을 제거 한다.
		Server.room_vc.remove(myRoom);
		
	}
	
	// 자신이 속한 방을 지정 한다.
	public void set_Room(Room r)
	{
		this.myRoom = r;
	}
	public void set_Room_Name(String s)
	{
		myRoom.setName(s);
	}
	public int getUserNum()
	{
		return userNum;
	}
	public void setUserNum(int n)
	{
		userNum = n;
	}
	public String getInterest()
	{
		return userWant;
	}

	

}
