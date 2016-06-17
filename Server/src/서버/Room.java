package 서버;

import java.io.IOException;
import java.util.Vector;

public class Room {


	private String name;
	private Vector<User> user = new Vector<User>();
	private int num=0;
	
	public void setName(String s)
	{
		
		name = s;
	}
	
	
	// 이름 리턴 메소드 
	public String getName()
	{
		return this.name;
	}
	
	
	
	public void inUser(User u)
	{
		user.add(u);
		u.setUserNum(num);
		num++;
	}
	
	
	public void outUser()
	{
		user.remove(num);
		user.get(num-1).send_Message("OutPerson");
		num--;
	}
		
		
	// num 없애고 바로 vc의 사이즈를 돌려준다  
	public int get_UserCount() 
	{
		return user.size();
	}
	
	public Vector<User> getVector()
	{
		return user;
	}
	
	
	public void broad_cast(String str)
	{
		for(int i = 0 ; i <user.size(); i++ )
		{
			User u = user.get(i);
			
			u.send_Message(str);
		}
	}
	public void remove_User(User u) {
		// 사용자의 객체를 찾아서 삭제 하기
		
		user.remove(u);
		System.out.println("사용자를 지운 후에 사이즈 : "+user.size());
		
	}
	public void other_User_Sender(String nick , String str)
	{
		for(int i = 0 ; i <user.size(); i++ )
		{
			User u = user.get(i);
			
			if(!u.nickName.equals(nick)) // 현재 나와 이름이 다른 사람은 방에 다른 유저
			{
				try 
				{
					u.dos.writeUTF(str);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}

}
