package com.ecnu;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;




class ServerReceive extends Thread
{
	Text serverStatusText = null;
	
	UserNode client = null;
	
	UserLinkList userLinkList = null;
	
	
	public ServerReceive(Text serverStatusText, UserNode client, UserLinkList userLinkList)
	{
		this.serverStatusText = serverStatusText;
		this.client = client;
		this.userLinkList = userLinkList;
							
	}
	
	
	public void run()
	{
		//向所有人发送用户的列表
		sendUserList();
		
		while(!client.socket.isClosed())
		{
			try
			{								
				String type = client.input.readUTF();
				
				if(type.equals("message"))
				{					
					String message = client.input.readUTF();
					
					String[] messageArray = message.split(":");
					
					if (messageArray.length>1)
					{
						String msg = client.username + "  said: "+ messageArray[1]+ "\n";	
						sendToSomePeople(msg, type, messageArray[0]);
					}
					else
					{
						String msg = client.username + " said: "+ message+ "\n";				
						sendToAll(msg,type,true);	
					}
								
				}
				else if (type.equals("operation"))
				{
					sendToAll(client.input.readUTF(),type, false);
				}
				else if(type.equals("userleft"))
				{
					UserNode node = userLinkList.findUser(client.username);
					userLinkList.delUser(node);
										
					Display.getDefault().syncExec(new Runnable()
					{
					    public void run() 
					    {
					    	serverStatusText.append(client.username + " left this session\n");
					    }
					 }); 
					
										
					sendToAll(client.username + " left this session\n","message", true);//向所有人发送消息
					sendUserList();//重新发送用户列表,刷新
					
					
					node.locationInput.close();
					node.locationOutput.close();
					node.locationSocket.close();
					
					node.input.close();
					node.output.close();
					node.socket.close();
				
					break;
				}
			}
			catch (Exception e)
			{
				System.out.println(e);
			}
		}
	}
	
	
	
	public void sendToAll(String msg, String type, boolean includeSelf)
	{
		int count = userLinkList.getCount();
		
		int i = 0;
		while(i < count)
		{
			UserNode node = userLinkList.findUser(i);
			if(node == null) 
			{
				i ++;
				continue;
			}
			
			if (!includeSelf && client.username.equals(node.username))
			{
				i++;
				continue;
			}
			
			try
			{
				node.output.writeUTF(type);
				node.output.flush();
				node.output.writeUTF(msg);
				node.output.flush();
			}
			catch (Exception e)
			{
				System.out.println(e);
			}
			
			i++;
		}
	}
	
	public void sendToSomePeople(String msg, String type, String peopleList)
	{
		if(peopleList.indexOf(client.username)== -1) 
		{
			peopleList = peopleList+","+client.username;
		}
		
		String[] peopleArray = peopleList.split(",");
		
		for (int i = 0; i < peopleArray.length; i++)
		{
			UserNode node = userLinkList.findUser(peopleArray[i]);
			if(node == null) 
			{				
				continue;
			}
			try
			{
				node.output.writeUTF(type);
				node.output.flush();
				node.output.writeUTF(msg);
				node.output.flush();
			}
			catch (Exception e)
			{
				System.out.println(e);
			}
		}	
	}
	
	
	public void sendUserList()
	{
		String userlist = "";
		
		int count = userLinkList.getCount();

		int i = 0;
		while(i < count)
		{
			UserNode node = userLinkList.findUser(i);
			if(node == null) 
			{
				i ++;
				continue;
			}
			
			userlist += node.username;
			if (node.isLeader)
			{
				userlist += "(Leader)";
			}
			userlist += "\n";
			i++;
		}
		
		i = 0;
		while(i < count)
		{
			UserNode node = userLinkList.findUser(i);
			if(node == null) 
			{
				i ++;
				continue;
			} 
			
			try
			{
				node.output.writeUTF("userlist");
				node.output.flush();
				node.output.writeUTF(userlist);
				node.output.flush();
			}
			catch (Exception e)
			{
				System.out.println(e);
			}
			i++;
		}
	}
	

}
