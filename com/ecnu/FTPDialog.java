package com.ecnu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FTPDialog extends Dialog implements SelectionListener
{
	Object result;
	Shell parent;
	Shell ftpShell;
	
	Font generalFont = null;
	
	Text usernameInputText = null;
	Text passwordInputText = null;
	
	Button confirmButton = null;
	
	
	
		
	public FTPDialog (Shell parent, int style) 
	{
		super (parent, style);
		this.parent = parent;
	}
	public FTPDialog (Shell parent) 
	{
		this (parent, 0); // your default style bits go here (not the Shell's style bits)
	}
	
	public void createContent(Shell shell, Display display)
	{
		shell.setLayout(new FormLayout());
		
		generalFont = new Font(display,"Arial",15, SWT.NONE);
		
		Label usernameLabel = new Label(shell,SWT.NONE);
		usernameLabel.setText("Username: ");
		FormData usernameLabelFormData = new FormData();
		usernameLabelFormData.top = new FormAttachment(10);
		usernameLabelFormData.left = new FormAttachment(5);
		usernameLabelFormData.right = new FormAttachment(25);
		usernameLabel.setLayoutData(usernameLabelFormData);
		
		usernameInputText = new Text(shell,SWT.BORDER);
		FormData usernameInputTextFormData = new FormData();
		usernameInputTextFormData.top = new FormAttachment(10);
		usernameInputTextFormData.left = new FormAttachment(30);
		usernameInputTextFormData.right = new FormAttachment(90);
		usernameInputText.setLayoutData(usernameInputTextFormData);
		usernameInputText.setFont(generalFont);
		
		Label passwordLabel = new Label(shell,SWT.NONE);
		passwordLabel.setText("Password: ");
		FormData passwordLabelFormData = new FormData();
		passwordLabelFormData.top = new FormAttachment(40);
		passwordLabelFormData.left = new FormAttachment(5);
		passwordLabelFormData.right = new FormAttachment(25);
		passwordLabel.setLayoutData(passwordLabelFormData); 
		
		passwordInputText = new Text(shell, SWT.BORDER|SWT.PASSWORD);
		FormData passwordInputTextFormData = new FormData();
		passwordInputTextFormData.top = new FormAttachment(40);
		passwordInputTextFormData.left = new FormAttachment(30);
		passwordInputTextFormData.right = new FormAttachment(90);
		passwordInputText.setLayoutData(passwordInputTextFormData);
		passwordInputText.setFont(generalFont);
		
		confirmButton = new Button(shell, SWT.NONE);
		confirmButton.setText("OK");
		FormData confirmButtonFormData = new FormData();
		confirmButtonFormData.top = new FormAttachment(80);
		confirmButtonFormData.left = new FormAttachment(30);
		confirmButtonFormData.right = new FormAttachment(60);
		confirmButton.setLayoutData(confirmButtonFormData);
		confirmButton.addSelectionListener(this);
			
	}
	
	
	
	public Object open () 
	{
		
		ftpShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		ftpShell.setText("Please input the username and password for FTP");
		Display display = parent.getDisplay();
		
		createContent(ftpShell,display);
		ftpShell.setSize(300, 300);
		ftpShell.open();
		
		while (!ftpShell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		return result;
	}
	
	public Object getResult()
	{
		return result;
	}
	
	public void widgetSelected(SelectionEvent e)
	{
		if (e.getSource() == confirmButton)
		{
			if (usernameInputText.getText().equals("") || passwordInputText.getText().equals(""))
			{
				MessageBox messageBox = new MessageBox(ftpShell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please input username and password");
				messageBox.open();								
				return;
			}
			
			result = usernameInputText.getText()+";"+passwordInputText.getText();
			
			ftpShell.dispose();
		}
		
	}
	
	public void widgetDefaultSelected(SelectionEvent e)
	{
		// TODO Auto-generated method stub
		
	}
 }

