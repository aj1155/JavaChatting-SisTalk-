package ����;

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
	
	
	// �̸� ���� �޼ҵ� 
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
		
		
	// num ���ְ� �ٷ� vc�� ����� �����ش�  
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
		// ������� ��ü�� ã�Ƽ� ���� �ϱ�
		
		user.remove(u);
		System.out.println("����ڸ� ���� �Ŀ� ������ : "+user.size());
		
	}
	public void other_User_Sender(String nick , String str)
	{
		for(int i = 0 ; i <user.size(); i++ )
		{
			User u = user.get(i);
			
			if(!u.nickName.equals(nick)) // ���� ���� �̸��� �ٸ� ����� �濡 �ٸ� ����
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
