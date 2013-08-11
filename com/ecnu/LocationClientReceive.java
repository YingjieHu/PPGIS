package com.ecnu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.Timer;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

public class LocationClientReceive extends Thread implements ActionListener
{

	Button isFollowTheLeaderCheck = null;
	Browser browser = null;
	ClientMain clientMain = null;
	
	Socket socket = null;
	DataOutputStream output = null;
	DataInputStream input = null;
	
	public Timer broadCastLocationTimer = null;
	
	String location = null;
	
	
	public LocationClientReceive(ClientMain clientMain)
	{
		this.clientMain = clientMain;
		this.socket = clientMain.locationClientSocket;
		this.output = clientMain.locationClientOutput;
		this.input = clientMain.locationClientInput;
		this.isFollowTheLeaderCheck = clientMain.isFollowTheLeaderCheck;
		this.browser = clientMain.browser;
		
		
		broadCastLocationTimer = new Timer(10, this);
			
	}
	
	public void run()
	{
		while(!socket.isClosed())
		{	
			try
			{
				location = input.readUTF();
			} 
			catch (Exception e)
			{
				// TODO: handle exception
			}
			
			
			Display.getDefault().syncExec(new Runnable()
			{
			    public void run() 
			    {
			    	try
					{				    		
			    		if (isFollowTheLeaderCheck.getVisible() && isFollowTheLeaderCheck.getSelection())
						{			    			
				    		String coords[] = location.split(",");

				    		String script = "IPlane.MovePosition("+ coords[0].split(":")[1]+","+ coords[1].split(":")[1]+","+ coords[2].split(":")[1]+","
				    		               +coords[3].split(":")[1]+","+coords[4].split(":")[1]+","+coords[5].split(":")[1]+","+coords[6].split(":")[1]+","+coords[7].split(":")[1]+",0)";
				    		
				    		//System.out.println(script);
				    		browser.execute(script);
						}
					} 
			    	catch (Exception e)
					{
						// TODO: handle exception
					}						    	
			    }
			 }); 					    	
	
		}
		broadCastLocationTimer.stop();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == broadCastLocationTimer)
		{
			String location = clientMain.positionInfoString;
			
			if(socket.isClosed())
			{
				broadCastLocationTimer.stop();
				return ;
			}
			
			try
			{				
				output.writeUTF(location);
				output.flush();
			}
			catch (Exception exception)
			{
				broadCastLocationTimer.stop();				
			}		
		}
		
	}

}
