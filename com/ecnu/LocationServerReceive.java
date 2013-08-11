package com.ecnu;


class LocationServerReceive extends Thread
{
	   	
	UserNode client = null;
	
	UserLinkList userLinkList = null;
	

	public LocationServerReceive(UserNode client, UserLinkList userLinkList)
	{		
		this.client = client;
		this.userLinkList = userLinkList;		
	}
	
	
	public void run()
	{				
		while(!client.locationSocket.isClosed())
		{
			try
			{								
				String location = client.locationInput.readUTF();
				sendToAll(location);							
			}
			catch (Exception e)
			{
				System.out.println(e);

			}
		}
	}	
	
	
	public void sendToAll(String location)
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
			
			if (client.username.equals(node.username))
			{
				i++;
				continue;
			}
					
			try
			{
				node.locationOutput.writeUTF(location);
				node.locationOutput.flush();
			}
			catch (Exception e)
			{
				System.out.println(e);																										
			}
			
			i++;
		}
	}
}
