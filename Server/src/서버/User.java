package ����;

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
	public String nickName = ""; // ����� �����ϱ� ���� ���̵�
	private StringTokenizer st;
	private int userNum =0;
	private String sender;
	private String senderRoom;
	public String userWant;
	private boolean Roomch = false;

	
	

	private Room myRoom = null;
	

	public User(Socket soc) // ������ �޼ҵ�
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
			
			// ó���� ����� ��Ī�� �޴´�. // �׷��� you �� me�� �����Ѵ�
			nickName = dis.readUTF();
			System.out.println("������ ����� �г��� : "+nickName);
			userWant = dis.readUTF();
			System.out.println("������ ����� ���ϴ°� : "+userWant);
		} catch (IOException e) {
			
			
			}
			
			
		}
	

	public void run() // Thread ���� ó���� ����
	{
		while (true) {
			try {

				String msg = dis.readUTF();
				InMessage(msg);
			} catch (IOException e) {
				System.out.println("����� ���� ����");
				
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
	} // run �޼ҵ� �P

	public void InMessage(String str) // Ŭ���̾�Ʈ�κ��� ������ �޼��� ó��
	{
		st = new StringTokenizer(str,"/");
		
		String protocol = st.nextToken();
		String Message = st.nextToken();
		
		System.out.println("��������:"+protocol);
		System.out.println("����:"+Message);
		
		
		if(protocol.equals("Me"))
		{
			send_Message("PartnerMessage/"+Message);
		}
		else if(protocol.equals("Chat")) //ä��+�����+���ȣ+�޼���  3���� ������. ���������� 2���� �ȴ�.
		{
			String user = Message; // ����� �̸�
			String msg = st.nextToken(); // ���� �޼��� 
			
			
			// ����ڰ� ���� �濡 �޼����� ���� ���ش�.
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
			myRoom.remove_User(this);							// �濡�� �ڽ��� ����
		}
		else if(protocol.equals("inter"))
		{
			userWant = Message;
				
			
		}
		else if(protocol.equals("FindAnother"))						// newchat��ư�� ������ �ٽ� ó������ ���� ã��
		{
			
			if(Server.room_vc.size() == 0) 	// ó�� �ʱ⿡ ���̾ƹ��͵� ������ 
			{
				//���� �����
				Room room = new Room();
				
				// �����ϰ� ���̸��� ����
				Random ran = new Random();
				
				String random_room = ""+ran.nextInt(999)+1;
				
				// ���̸��� �߰� ���ش� 
				room.setName(random_room);
				
				//�� ���Ϳ� �߰�
				Server.room_vc.add(room);
				
				// �濡 ����� �߰� 
				room.inUser(this);
				
				// ����ڿ��� �� �Ҵ�
				this.set_Room(room);
				room.setName(random_room);
				// ����ڿ��� ��� �˸���
				this.send_Message("Wait/ ");
				
				// ����ڿ��� ������ ���� �˷��ش�.
				this.send_Message("RoomName/"+random_room);
			}
			else // �ʱ� ���� �ƴҶ� , �� ���� ó�� ������ �ƴ� ���
			{
				
				
				for(int i = 0; i <Server.room_vc.size(); i++)
				{
					// ��� �ִ� �� ã��
					Room room = Server.room_vc.get(i);
					
					// ��ȿ� ����ִ� ��� ã�� 
					int count = room.get_UserCount();
					
					
					if(count==1) // �濡 1���� ��� ���ְ� ��� ���϶� 
					{
						String my 	= this.userWant;  // ���� ��� 
						String you 	=  room.getVector().get(0).userWant; // ������ ���
						
						if(my.equals(you))
						{
							//������ ����� 1�� ���� 
							room.inUser(this);
							
							
							// ����ڿ��� �� �Ҵ�
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
					
					// �����ϰ� ���̸��� ����
					Random ran = new Random();
					
					String random_room = ""+ran.nextInt(999)+1;
					
					// ���̸��� �߰� ���ش� 
					room2.setName(random_room);
					
					//�� ���Ϳ� �߰�
					Server.room_vc.add(room2);
					
					// �濡 ����� �߰� 
					room2.inUser(this);
					
					// ����ڿ��� �� �Ҵ�
					this.set_Room(room2);
					room2.setName(random_room);
					// ����ڿ��� ��� �˸���
					this.send_Message("Wait/ ");
					
					// ����ڿ��� ������ ���� �˷��ش�.
					this.send_Message("RoomName/"+random_room);
					
					Roomch = true;
				}
				}
			}
			
		}	
	
		
	public void send_Message(String str) // ���ڿ��� �޾Ƽ� ����
	{
		try {
			dos.writeUTF(str);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	private void remove_myRoom(Room myRoom) 
	{
		
		// ����ڰ� ��û�� ���� ���� �Ѵ�.
		Server.room_vc.remove(myRoom);
		
	}
	
	// �ڽ��� ���� ���� ���� �Ѵ�.
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
