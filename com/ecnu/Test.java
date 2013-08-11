package com.ecnu;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

public class Test
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		try
		{
			SimpleFTP simpleFTP = new SimpleFTP();
			simpleFTP.connect("localhost", 21, "gis", "sa");
			//simpleFTP.bin();
			simpleFTP.cwd("PPGISProj");
			simpleFTP.stor(new File("c:\\","client.jar"));
			simpleFTP.disconnect();
		
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		
	// TODO Auto-generated method stub

	}

}
