package com.ecnu;

import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.widgets.Label;

class AdvancedStatusTextListener implements StatusTextListener
{
	private Label status;
	private ClientMain clientMain;
	
	
	
	public AdvancedStatusTextListener(Label status, ClientMain clientMain)
	{
		this.status = status;
		this.clientMain = clientMain;
	}

	public void changed(StatusTextEvent event)
	{
		String[] info = event.text.split(";");
		
		clientMain.positionInfoString = info[0];
		
		if (info.length>1)
		{
			clientMain.operationInfoString = info[1];
			clientMain.afterPlanningOperation(info[1]);
		}
		
						
		String position = info[0];
		
		String coords[] = position.split(",");
		
		if (coords.length == 8)
		{
			status.setText("X:"+coords[0].split(":")[1]+",Y:"+coords[1].split(":")[1]+",Elevation:"+coords[2].split(":")[1]);
		}
	    
	}

}
