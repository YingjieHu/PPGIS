package com.ecnu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.Timer;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class ClientReceive extends Thread 
{
	List peopleInRoomList = null;	
	Text connectionStatusText = null;
	Text messageTextArea = null;
	Browser browser = null;
	Combo targetPlanCombo = null;
	Button isFollowTheLeaderCheck = null;
	String currentUserName = "";
    Timer broadCastLocationTimer = null;


	
	Socket socket = null;
	DataOutputStream output = null;
	DataInputStream input = null;
	
	Socket locationSocket = null;
	DataOutputStream locationOutput = null;
	DataInputStream locationInput = null;
	
	
	
	public ClientReceive(ClientMain clientMain, Timer broadCastLocationTimer)
	{
		this.socket = clientMain.clientSocket;
		this.output = clientMain.clientOutput;
		this.input = clientMain.clientInput;
		
        this.locationSocket = clientMain.locationClientSocket;
        this.locationOutput = clientMain.locationClientOutput;
        this.locationInput = clientMain.locationClientInput;
		
		this.peopleInRoomList = clientMain.peopleInRoomList;
		this.connectionStatusText = clientMain.connectionStatusText;
		this.messageTextArea = clientMain.messageTextArea;
		this.browser = clientMain.browser;
		this.targetPlanCombo = clientMain.targetPlanCombo;
		this.isFollowTheLeaderCheck = clientMain.isFollowTheLeaderCheck;
		this.currentUserName = clientMain.currentUserName;
		
		this.broadCastLocationTimer = broadCastLocationTimer;
			
	}
	
	public void run()
	{
		while(!socket.isClosed())
		{
			try
			{								
				String type = input.readUTF();
								
				if(type.equals("serverstop"))
				{
					locationOutput.close();
					locationInput.close();
					locationSocket.close();
					
					output.close();
					input.close();
					socket.close();
					
					Display.getDefault().syncExec(new Runnable()
					{
					    public void run() 
					    {
					    	connectionStatusText.append("Server has stopped！\n");
							messageTextArea.append("Server has stopped！\n");
					    }
					 }); 
							
					break;
					
				}
				else if(type.equals("message"))
				{					
					Display.getDefault().syncExec(new Runnable()
					{
					    public void run() 
					    {
					    	try
							{
					    		String message = input.readUTF();
					    		messageTextArea.append(message);
							} 
					    	catch (Exception e)
							{
								// TODO: handle exception
							}						
					    }
					 }); 				
				}
				else if (type.equals("operation"))
				{
					Display.getDefault().syncExec(new Runnable()
					{
					    public void run() 
					    {
					    	try
							{
					    		doTheSameOperation(input.readUTF());					    	
							} 
					    	catch (Exception e)
							{
								// TODO: handle exception
							}						
					    }
					 }); 	
				}
				else if(type.equals("userlist"))
				{					
					Display.getDefault().syncExec(new Runnable()
					{
					    public void run() 
					    {
					    	try
							{
					    		String userlist = input.readUTF();
								String usernames[] = userlist.split("\n");
								peopleInRoomList.removeAll();
								
								int i =0;
								
								while(i < usernames.length)
								{
									peopleInRoomList.add(usernames[i]);	
									
									if (usernames[i].indexOf(currentUserName)!= -1)
									{
										if (usernames[i].indexOf("Leader")==-1)
										{
											isFollowTheLeaderCheck.setVisible(true);
											broadCastLocationTimer.stop();
										}
										else
										{
											isFollowTheLeaderCheck.setVisible(false);
											broadCastLocationTimer.start();
										}
									}
									
									i ++;
								}
							} 
					    	catch (Exception e)
							{
								// TODO: handle exception
							}							
					    }
					 }); 									
				}
			}
			catch (Exception e ){
				System.out.println(e);
			}
		}
	}
	
	public void doTheSameOperation(String operationString)
	{
		String[] operationDetails = operationString.split(",");
		
		if (operationDetails[0].equals("createPlan"))
		{
			if (operationDetails[1].equals(targetPlanCombo.getText()))
				return;
			
			browser.execute("IInformationTree.CreateGroup('"+operationDetails[1]+"',0)");
			
			if (targetPlanCombo.getText().equals("None") )
			{
				targetPlanCombo.removeAll();				
			}
			targetPlanCombo.add(operationDetails[1]);
			targetPlanCombo.select(targetPlanCombo.getItemCount()-1);
			
			browser.execute("planManager.setPlanVisibility('"+targetPlanCombo.getText()+"')");
									
		}
		else if (operationDetails[0].equals("focusPlan")) 
		{
			for(int i=0;i< targetPlanCombo.getItemCount();i++)
			{
				if (targetPlanCombo.getItem(i).equals(operationDetails[1]))
				{
					targetPlanCombo.select(i);
					break;
				}
			}
			
			browser.execute("planManager.setPlanVisibility('"+targetPlanCombo.getText()+"')");			
			
		}
		else if (operationDetails[0].equals("deletePlan")) 
		{
			browser.execute("planManager.deletePlan('"+operationDetails[1]+"')");				
			targetPlanCombo.remove(operationDetails[1]);
			if (targetPlanCombo.getItemCount()==0)
			{
				targetPlanCombo.add("None");
			}
			targetPlanCombo.select(0);
			
			browser.execute("planManager.setPlanVisibility('"+targetPlanCombo.getText()+"')");	
		}
		else if (operationDetails[0].equals("savePlan")) 
		{
			browser.execute("planManager.savePlan('"+operationDetails[1]+"')");
		}
		else if (operationDetails[0].equals("createRoad")) 
		{
			int coordStart = operationString.indexOf("[");
			int coordEnd = operationString.indexOf("]");
			
			String coordString = operationString.substring(coordStart, coordEnd+1);
			int count = operationDetails.length;
			browser.execute("roadBuilder.buildTheSameRoad('"+coordString+"','"+operationDetails[count-3] +"','"+operationDetails[count-2]+"','"+operationDetails[count-1]+"')");
		}
		else if (operationDetails[0].equals("createTree"))
		{
			int coordStart = operationString.indexOf("[");
			int coordEnd = operationString.indexOf("]");
			
			String coordString = operationString.substring(coordStart, coordEnd+1);
			int count = operationDetails.length;
			browser.execute("treeBuilder.buildTheSameTree('"+coordString+"','"+operationDetails[count-3] +"','"+operationDetails[count-2]+"','"+operationDetails[count-1]+"')");
		}
		else if (operationDetails[0].equals("createLight"))
		{
			int coordStart = operationString.indexOf("[");
			int coordEnd = operationString.indexOf("]");
			
			String coordString = operationString.substring(coordStart, coordEnd+1);
			int count = operationDetails.length;
			browser.execute("lightBuilder.buildTheSameLight('"+coordString+"','"+operationDetails[count-3] +"','"+operationDetails[count-2]+"','"+operationDetails[count-1]+"')");
		}
		else if (operationDetails[0].equals("createTile"))
		{
			int coordStart = operationString.indexOf("[");
			int coordEnd = operationString.indexOf("]");
			
			String coordString = operationString.substring(coordStart, coordEnd+1);
			int count = operationDetails.length;
			browser.execute("tileBuilder.buildTheSameTile('"+coordString+"','"+operationDetails[count-2]+"','"+operationDetails[count-1]+"')");
		}
		else if (operationDetails[0].equals("createBuilding"))
		{
			int coordStart = operationString.indexOf("[");
			int coordEnd = operationString.indexOf("]");
			
			String coordString = operationString.substring(coordStart, coordEnd+1);
			int count = operationDetails.length;
			browser.execute("buildingBuilder.buildTheSameBuilding('"+coordString+"','"+operationDetails[count-2]+"','"+operationDetails[count-1]+"')");
		}
		else if (operationDetails[0].equals("createOtherModel"))
		{
			int coordStart = operationString.indexOf("[");
			int coordEnd = operationString.indexOf("]");
			
			String coordString = operationString.substring(coordStart, coordEnd+1);
			int count = operationDetails.length;
			browser.execute("otherModelBuilder.buildTheSameOtherModel('"+coordString+"','"+operationDetails[count-3]+"','"+operationDetails[count-2]+"','"+operationDetails[count-1]+"')");
		}
		else if (operationDetails[0].equals("createPolyline"))
		{
			int coordStart = operationString.indexOf("[");
			int coordEnd = operationString.indexOf("]");
			
			String coordString = operationString.substring(coordStart, coordEnd+1);
			int count = operationDetails.length;
			browser.execute("labelBuilder.buildTheSamePolyline('"+coordString+"','"+operationDetails[count-1]+"')");
		}
		else if (operationDetails[0].equals("createPolygon"))
		{
			int coordStart = operationString.indexOf("[");
			int coordEnd = operationString.indexOf("]");
			
			String coordString = operationString.substring(coordStart, coordEnd+1);
			int count = operationDetails.length;
			browser.execute("labelBuilder.buildTheSamePolygon('"+coordString+"','"+operationDetails[count-1]+"')");
		}
		else if (operationDetails[0].equals("createArrow"))
		{
			int coordStart = operationString.indexOf("[");
			int coordEnd = operationString.indexOf("]");
			
			String coordString = operationString.substring(coordStart, coordEnd+1);
			int count = operationDetails.length;
			browser.execute("labelBuilder.buildTheSameArrow('"+coordString+"','"+operationDetails[count-1]+"')");
		}
		else if (operationDetails[0].equals("createCircle"))
		{
			int coordStart = operationString.indexOf("[");
			int coordEnd = operationString.indexOf("]");
			
			String coordString = operationString.substring(coordStart, coordEnd+1);
			int count = operationDetails.length;
			browser.execute("labelBuilder.buildTheSameCircle('"+coordString+"','"+operationDetails[count-1]+"')");
		}
		else if (operationDetails[0].equals("createText"))
		{
			int coordStart = operationString.indexOf("[");
			int coordEnd = operationString.indexOf("]");
			
			String coordString = operationString.substring(coordStart, coordEnd+1);
			int count = operationDetails.length;
			browser.execute("labelBuilder.buildTheSameText('"+coordString+"','"+operationDetails[count-2]+"','"+operationDetails[count-1]+"')");
		}
		else if (operationDetails[0].equals("deleteModel"))
		{
			browser.execute("modifyModel.deleteTheSameModel('"+operationDetails[1]+"')");
		}
		else if (operationDetails[0].equals("changeYaw"))
		{
			browser.execute("modifyModel.changeTheModelYaw('"+operationDetails[1]+"',"+operationDetails[2]+")");
		}
		else if (operationDetails[0].equals("changeText"))
		{
			browser.execute("objectEditor.changeTheSameText('"+operationDetails[1]+"','"+operationDetails[2]+"')");
		}
		else if (operationDetails[0].equals("changeFont"))
		{
			browser.execute("objectEditor.changeTheSameFont('"+operationDetails[1]+"',"+operationDetails[2]+")");
		}
		else if (operationDetails[0].equals("changeFillColor"))
		{
			browser.execute("objectEditor.changeTheSameFillColor('"+operationDetails[1]+"',"+operationDetails[2]+")");
		}
		else if (operationDetails[0].equals("changeFillAlpha"))
		{
			browser.execute("objectEditor.changeTheSameFillAlpha('"+operationDetails[1]+"',"+operationDetails[2]+")");
		}
		else if (operationDetails[0].equals("changeOutLineAlpha"))
		{
			browser.execute("objectEditor.changeTheSameOutLineAlpha('"+operationDetails[1]+"',"+operationDetails[2]+")");
		}
		else if (operationDetails[0].equals("changeOutLineColor"))
		{
			browser.execute("objectEditor.changeTheSameOutLineColor('"+operationDetails[1]+"',"+operationDetails[2]+")");
		}
		else if (operationDetails[0].equals("changeOutLineWidth"))
		{
			browser.execute("objectEditor.changeTheSameOutLineWidth('"+operationDetails[1]+"',"+operationDetails[2]+")");
		}
		else if (operationDetails[0].equals("changeLineStyle"))
		{
			browser.execute("objectEditor.changeTheSameLineStyle('"+operationDetails[1]+"',"+operationDetails[2]+")");
		}
		
		
		
	}
	
	
	

}
