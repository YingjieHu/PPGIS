package com.ecnu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;


class ServerListen extends Thread
{
	ServerSocket serverSocket= null;
	
	ServerSocket locationServerSocket = null;
	
	Text serverStatusText = null;
	
	UserLinkList userLinkList = null;
	
	UserNode client = null;
	
    ServerReceive serverReceive = null;
    
    LocationServerReceive locationServerReceive = null;
    
    public boolean isStop = false;
    
    boolean isFirst = true;
    
    public ServerListen(ServerSocket serverSocket, ServerSocket locationServerSocket, Text serverStatusText, UserLinkList userLinkList)
    {
    	this.serverSocket = serverSocket;
    	this.locationServerSocket = locationServerSocket;
    	this.serverStatusText = serverStatusText;
    	this.userLinkList = userLinkList;
    	
    	isStop = false;
    }
    
    
    public void run()
    {
		while(!isStop && !serverSocket.isClosed())
		{
			try
			{
				client = new UserNode();
				
				client.socket = serverSocket.accept();
				client.locationSocket = locationServerSocket.accept();
				
				//System.out.println("ok");
				client.output = new DataOutputStream(client.socket.getOutputStream());
				client.output.flush();
				client.input  = new DataInputStream(client.socket.getInputStream());
				client.username = client.input.readUTF();
				
								
				client.locationOutput = new DataOutputStream(client.locationSocket.getOutputStream());
				client.locationOutput.flush();
				client.locationInput = new DataInputStream(client.locationSocket.getInputStream());
				//System.out.println(client.username);
				//显示提示信息
				
				//默认将第一个加入会话的设为leader
				if (isFirst)
				{
					client.isLeader = true;
					isFirst = false;
				}
				
				userLinkList.addUser(client);
				
				
				
				//System.out.println("ss");
								
				serverReceive = new ServerReceive(serverStatusText,client,userLinkList);
				serverReceive.start();
				
				locationServerReceive = new LocationServerReceive(client,userLinkList);
				locationServerReceive.start();
				
				Display.getDefault().syncExec(new Runnable()
				{
				    public void run() 
				    {
				    	serverStatusText.append( client.username + " joined this session" + "\n");
				    }
				 }); 
				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}
	

}
