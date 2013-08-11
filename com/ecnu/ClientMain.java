package com.ecnu;

import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;








public class ClientMain implements SelectionListener, KeyListener
{
	
	//综合变量===============
	
	Shell shell = null;
	
	TabFolder tabFolder = null;
	
	Browser browser = null;
	
	Label status = null;
	
	Text inputFlyAddressText = null;
	
	Button inputFlyAddressOKButton = null;
	
	Button inputFlyAddressResetButton = null;
	
	Text inputServerPortText = null;
	
	Text inputServerUserNameText = null;
	
	Font generalFont = null;
	
	Button startServerButton = null;
	
	Button stopServerButton = null;
	
	Text serverStatusText = null;
	
	Text inputServerIPClientText = null;
	
	Text inputServerPortClientText = null;
	
	Text inputUserNameClientText = null;
	
	Button startConnectionButton = null;
	
	Button stopConnectionButton = null;
	
	Text connectionStatusText = null;
	
	String currentUrlAddress = "";
	
	String currentUserName = "";
	
	Composite collaborationPanel = null;
	
	List peopleInRoomList = null;
	
	Button setAsLeaderButton = null;
	
	Button isFollowTheLeaderCheck = null;
	
	Text messageTextArea = null;
	
	Text userInputMessageText = null;
	
	Button sendMessageButton = null;
	
	boolean isLeader = false;
	
	public String positionInfoString = "";
	
	public String operationInfoString = "";
	
	Combo targetPlanCombo = null;
	
	Button newPlanButton = null;
	
	Button deletePlanButton = null;
	
	Button savePlanButton = null;
	
	Button uploadPlanButton = null;
	
	Button addRoadButton = null;
	
	Button addTreeButton = null;
	
	Button addLightButton = null;
	
	Button addPedestrainButton = null;
	
	Button addBuildingButton = null;
	
	Button addFacilityButton = null;
	
	Button addPeopleButton = null;
	
	Button editModelButton = null;
	
	Button addPolylineButton = null;
	
	Button addPolygonButton = null;
	
	Button addTextButton = null;
	
	Button addArrowButton = null;
	
	Button addCircleButton = null;
	
	Button editLabelButton = null;
	
	TabFolder planTabFolder = null;
	
	//============================
	
	
	
	
	//服务器端变量================
	
	boolean isServer = false;
	
	int serverPort = 12345;
	
	ServerSocket serverSocket = null;
	ServerSocket locationServerSocket = null;
	UserLinkList userLinkList = null;		
	ServerListen serverListenThread = null;
	
	
	//===========================
	
	
	
	
	//客户端变量====================
	
	boolean isClient = false;
	
	Socket clientSocket = null;
	
	DataOutputStream clientOutput = null;//网络套接字输出流
	DataInputStream clientInput = null;//网络套接字输入流	
	ClientReceive clientReceiveThread = null;
	
	int clientServerPort = 12345;
	
	//专门为定位设计的变量
	Socket locationClientSocket = null;
	
	DataOutputStream locationClientOutput = null;
	DataInputStream locationClientInput = null;
	LocationClientReceive locationClientReceiveThread = null;
	
	//=========================
	
	//==同步定位部分的端口号
	
	int locationPort = 12349;
	
	//================
	
	//

	

	
	
	
	public void createContents(Shell shell, Display display)
	{
		shell.setLayout(new FormLayout());
		
		generalFont = new Font(display,"Arial",15, SWT.NONE);
		
		//状态栏
		status = new Label(shell, SWT.NONE);
        FormData statusFormData = new FormData();
        statusFormData.left = new FormAttachment(0);
        statusFormData.right = new FormAttachment(100);
        statusFormData.bottom = new FormAttachment(100);
        status.setLayoutData(statusFormData);
        status.setText("Program has started;");
		
		
        //左边的总面板
		tabFolder = new TabFolder(shell,SWT.NONE);
		FormData formData1 = new FormData();
		formData1.top = new FormAttachment(0);
		formData1.left = new FormAttachment(0);
		formData1.right = new FormAttachment(30);
		formData1.bottom = new FormAttachment(status);		
		tabFolder.setLayoutData(formData1);
		
		//连接面板
		TabItem tabItem1 =  new TabItem(tabFolder, SWT.NONE);
		tabItem1.setText("Connection");
		
		Composite connectionPanel = new Composite(tabFolder,SWT.NONE);
		FormLayout formLayout = new FormLayout();
		connectionPanel.setLayout(formLayout);		
		tabItem1.setControl(connectionPanel);
		
		//工程连接面板
		Group flyConnectionGroup = new Group(connectionPanel, SWT.NONE);
		flyConnectionGroup.setText("Project connection");
		FormData flyConnectionFormData = new FormData();
		flyConnectionFormData.top = new FormAttachment(2);
		flyConnectionFormData.left = new FormAttachment(2);
		flyConnectionFormData.right = new FormAttachment(98);
		flyConnectionFormData.bottom = new FormAttachment(25);
		flyConnectionGroup.setLayoutData(flyConnectionFormData);
		
		flyConnectionGroup.setLayout(new FormLayout());
		Label inputFlyAddressLabel = new Label(flyConnectionGroup, SWT.NONE);
		inputFlyAddressLabel.setText("Please input the address of the project here: ");
		FormData inputFlyAddressLabelFormData = new FormData();
		inputFlyAddressLabelFormData.top = new FormAttachment(20);
		inputFlyAddressLabelFormData.left = new FormAttachment(2);
		inputFlyAddressLabelFormData.right = new FormAttachment(98);
	    inputFlyAddressLabel.setLayoutData(inputFlyAddressLabelFormData);
	    
	    inputFlyAddressText = new Text(flyConnectionGroup, SWT.BORDER);
	    FormData inputFlyAddressTextFormData = new FormData();
	    inputFlyAddressTextFormData.top = new FormAttachment(40);
	    inputFlyAddressTextFormData.left = new FormAttachment(2);
	    inputFlyAddressTextFormData.right = new FormAttachment(98);
	    inputFlyAddressText.setFont(generalFont);
	    inputFlyAddressText.setLayoutData(inputFlyAddressTextFormData);
	    inputFlyAddressText.addKeyListener(this);
	    
	    inputFlyAddressOKButton = new Button(flyConnectionGroup, SWT.PUSH);
	    inputFlyAddressOKButton.setText("Connect to the Project");
	    FormData inputFlyAddressOKButtonFormData = new FormData();
	    inputFlyAddressOKButtonFormData.top = new FormAttachment(75);
	    inputFlyAddressOKButtonFormData.left = new FormAttachment(15);
	    inputFlyAddressOKButton.setLayoutData(inputFlyAddressOKButtonFormData);
	    inputFlyAddressOKButton.addSelectionListener(this);
	    
	    
	    inputFlyAddressResetButton = new Button(flyConnectionGroup, SWT.PUSH);
	    inputFlyAddressResetButton.setText("Reset the project address");
	    FormData inputFlyAddressResetButtonFormData = new FormData();
	    inputFlyAddressResetButtonFormData.top = new FormAttachment(75);
	    inputFlyAddressResetButtonFormData.left = new FormAttachment(50);
	    inputFlyAddressResetButton.setLayoutData(inputFlyAddressResetButtonFormData);
	    inputFlyAddressResetButton.addSelectionListener(this);
	    
		
		
		
		//协同创建与连接面板
		TabFolder gisConnectionFolder = new TabFolder(connectionPanel, SWT.NONE);
		FormData gisConnectionFormData = new FormData();
		gisConnectionFormData.top = new FormAttachment(27);
		gisConnectionFormData.left = new FormAttachment(2);
		gisConnectionFormData.right = new FormAttachment(98);
		gisConnectionFormData.bottom = new FormAttachment(98);
		gisConnectionFolder.setLayoutData(gisConnectionFormData);
		
		
		//创建协同面板
		TabItem gisConnectionServerItem = new TabItem(gisConnectionFolder, SWT.NONE);
		gisConnectionServerItem.setText("Create collaboration");
		
		Composite createSessionPanel = new Composite(gisConnectionFolder, SWT.NONE);
		gisConnectionServerItem.setControl(createSessionPanel);
		createSessionPanel.setLayout(new FormLayout());
		
		Label inputServerPortLabel = new Label(createSessionPanel,SWT.NONE);
		inputServerPortLabel.setText("Server port: ");
		FormData inputServerPortLabelFormData = new FormData();
		inputServerPortLabelFormData.top = new FormAttachment(5);
		inputServerPortLabelFormData.left = new FormAttachment(2);
		inputServerPortLabel.setLayoutData(inputServerPortLabelFormData);
		
		inputServerPortText = new Text(createSessionPanel, SWT.BORDER);
		inputServerPortText.setText("12345");
		FormData inputServerPortTextFormData = new FormData();
		inputServerPortTextFormData.top = new FormAttachment(3);
		inputServerPortTextFormData.left = new FormAttachment(55);
		inputServerPortTextFormData.right = new FormAttachment(98);
		inputServerPortText.setFont(generalFont);
		inputServerPortText.setLayoutData(inputServerPortTextFormData);
		
		Label inputServerUserNameLabel = new Label(createSessionPanel,SWT.NONE);
		inputServerUserNameLabel.setText("Username: ");
		FormData inputServerUserNameLabelFormData = new FormData();
		inputServerUserNameLabelFormData.top = new FormAttachment(15);
		inputServerUserNameLabelFormData.left = new FormAttachment(2);
		inputServerUserNameLabel.setLayoutData(inputServerUserNameLabelFormData);
		
		inputServerUserNameText = new Text(createSessionPanel, SWT.BORDER);
		FormData inputServerUserNameTextFormData = new FormData();
		inputServerUserNameTextFormData.top = new FormAttachment(13);
		inputServerUserNameTextFormData.left = new FormAttachment(55);
		inputServerUserNameTextFormData.right = new FormAttachment(98);
		inputServerUserNameText.setFont(generalFont);
		inputServerUserNameText.setLayoutData(inputServerUserNameTextFormData);
		
		startServerButton = new Button(createSessionPanel, SWT.PUSH);
		startServerButton.setText("Start server");
		FormData startServerButtonFormData = new FormData();
		startServerButtonFormData.top = new FormAttachment(25);
		startServerButtonFormData.left = new FormAttachment(15);
		startServerButton.setLayoutData(startServerButtonFormData);
		startServerButton.addSelectionListener(this);
					
		stopServerButton = new Button(createSessionPanel, SWT.PUSH);
		stopServerButton.setText("Stop server");
		FormData stopServerButtonFormData = new FormData();
		stopServerButtonFormData.top = new FormAttachment(25);
		stopServerButtonFormData.left = new FormAttachment(50);
		stopServerButton.setLayoutData(stopServerButtonFormData);
		stopServerButton.setEnabled(false);
		stopServerButton.addSelectionListener(this);
		
		
		Label serverStatusLabel = new Label(createSessionPanel,SWT.NONE);
		serverStatusLabel.setText("Server status: ");
		FormData serverStatusLabelFormData = new FormData();
		serverStatusLabelFormData.top = new FormAttachment(35);
		serverStatusLabelFormData.left = new FormAttachment(2);
		serverStatusLabelFormData.right = new FormAttachment(98);
		serverStatusLabel.setLayoutData(serverStatusLabelFormData);
		
		serverStatusText = new Text(createSessionPanel, SWT.BORDER|SWT.MULTI|SWT.V_SCROLL);
		FormData serverStatusTextFormData = new FormData();
		serverStatusTextFormData.top = new FormAttachment(43);
		serverStatusTextFormData.left = new FormAttachment(2);
		serverStatusTextFormData.right = new FormAttachment(98);
		serverStatusTextFormData.bottom = new FormAttachment(98);
		serverStatusText.setLayoutData(serverStatusTextFormData);
		serverStatusText.setEditable(false);
				
		
		TabItem gisConnectionClientItem = new TabItem(gisConnectionFolder, SWT.NONE);
		gisConnectionClientItem.setText("Join collaboration");

		Composite connectSessionPanel = new Composite(gisConnectionFolder, SWT.NONE);
		gisConnectionClientItem.setControl(connectSessionPanel);
		connectSessionPanel.setLayout(new FormLayout());
		
		Label inputServerIPClientLabel = new Label(connectSessionPanel,SWT.NONE);
		inputServerIPClientLabel.setText("Server's IP address:");
		FormData inputServerIPClientLabelFormData = new FormData();
		inputServerIPClientLabelFormData.top = new FormAttachment(5);
		inputServerIPClientLabelFormData.left = new FormAttachment(2);
		inputServerIPClientLabel.setLayoutData(inputServerIPClientLabelFormData);
		
		inputServerIPClientText = new Text(connectSessionPanel, SWT.BORDER);
		inputServerIPClientText.setText("");
		FormData inputServerIPClientTextFormData = new FormData();
		inputServerIPClientTextFormData.top = new FormAttachment(3);
		inputServerIPClientTextFormData.left = new FormAttachment(55);
		inputServerIPClientTextFormData.right = new FormAttachment(98);
		inputServerIPClientText.setFont(generalFont);
		inputServerIPClientText.setLayoutData(inputServerIPClientTextFormData);
		
		Label inputServerPortClientLabel = new Label(connectSessionPanel,SWT.NONE);
		inputServerPortClientLabel.setText("Server port: ");
		FormData inputServerPortClientLabelFormData = new FormData();
		inputServerPortClientLabelFormData.top = new FormAttachment(15);
		inputServerPortClientLabelFormData.left = new FormAttachment(2);
		inputServerPortClientLabel.setLayoutData(inputServerPortClientLabelFormData);
		
		inputServerPortClientText = new Text(connectSessionPanel, SWT.BORDER);
		inputServerPortClientText.setText("12345");
		FormData inputServerPortClientTextFormData = new FormData();
		inputServerPortClientTextFormData.top = new FormAttachment(13);
		inputServerPortClientTextFormData.left = new FormAttachment(55);
		inputServerPortClientTextFormData.right = new FormAttachment(98);
		inputServerPortClientText.setFont(generalFont);
		inputServerPortClientText.setLayoutData(inputServerPortClientTextFormData);
		
		Label inputUserNameClientLabel = new Label(connectSessionPanel,SWT.NONE);
		inputUserNameClientLabel.setText("Username: ");
		FormData inputUserNameClientLabelFormData = new FormData();
		inputUserNameClientLabelFormData.top = new FormAttachment(25);
		inputUserNameClientLabelFormData.left = new FormAttachment(2);
		inputUserNameClientLabel.setLayoutData(inputUserNameClientLabelFormData);
		
		inputUserNameClientText = new Text(connectSessionPanel, SWT.BORDER);
		FormData inputUserNameClientTextFormData = new FormData();
		inputUserNameClientTextFormData.top = new FormAttachment(23);
		inputUserNameClientTextFormData.left = new FormAttachment(55);
		inputUserNameClientTextFormData.right = new FormAttachment(98);
		inputUserNameClientText.setFont(generalFont);
		inputUserNameClientText.setLayoutData(inputUserNameClientTextFormData);
		
		startConnectionButton = new Button(connectSessionPanel, SWT.PUSH);
		startConnectionButton.setText("Connect to server");
		FormData startConnectionButtonFormData = new FormData();
		startConnectionButtonFormData.top = new FormAttachment(35);
		startConnectionButtonFormData.left = new FormAttachment(15);
		startConnectionButton.setLayoutData(startConnectionButtonFormData);
		startConnectionButton.addSelectionListener(this);
					
		stopConnectionButton = new Button(connectSessionPanel, SWT.PUSH);
		stopConnectionButton.setText("Disconnect from server");
		FormData stopConnectionButtonFormData = new FormData();
		stopConnectionButtonFormData.top = new FormAttachment(35);
		stopConnectionButtonFormData.left = new FormAttachment(50);
		stopConnectionButton.setLayoutData(stopConnectionButtonFormData);
		stopConnectionButton.setEnabled(false);
		stopConnectionButton.addSelectionListener(this);
		
		
		Label connectionStatusLabel = new Label(connectSessionPanel,SWT.NONE);
		connectionStatusLabel.setText("Connection status:");
		FormData connectionStatusLabelFormData = new FormData();
		connectionStatusLabelFormData.top = new FormAttachment(45);
		connectionStatusLabelFormData.left = new FormAttachment(2);
		connectionStatusLabelFormData.right = new FormAttachment(98);
		connectionStatusLabel.setLayoutData(connectionStatusLabelFormData);
		
		connectionStatusText = new Text(connectSessionPanel, SWT.BORDER|SWT.MULTI|SWT.V_SCROLL);
		FormData connectionStatusTextFormData = new FormData();
		connectionStatusTextFormData.top = new FormAttachment(53);
		connectionStatusTextFormData.left = new FormAttachment(2);
		connectionStatusTextFormData.right = new FormAttachment(98);
		connectionStatusTextFormData.bottom = new FormAttachment(98);
		connectionStatusText.setLayoutData(connectionStatusTextFormData);
		connectionStatusText.setEditable(false);
		
		
		
		
		//协作面板==========================	
		TabItem tabItem2 =  new TabItem(tabFolder, SWT.NONE);
		tabItem2.setText("Collaboration");
		
		
		collaborationPanel = new Composite(tabFolder, SWT.NONE);
		tabItem2.setControl(collaborationPanel);
		collaborationPanel.setLayout(new FormLayout());
		collaborationPanel.setEnabled(false);
		
		//聊天同步组
		Group chatRoomGroup = new Group(collaborationPanel,SWT.NONE);
		chatRoomGroup.setText("Session Information");
		chatRoomGroup.setLayout(new FormLayout());
		FormData chatRoomGroupFormData = new FormData();
		chatRoomGroupFormData.top = new FormAttachment(2);
		chatRoomGroupFormData.left = new FormAttachment(2);
		chatRoomGroupFormData.right = new FormAttachment(98);
		chatRoomGroupFormData.bottom = new FormAttachment(50);
		chatRoomGroup.setLayoutData(chatRoomGroupFormData);
		
		Label peopleInRoomLabel = new Label(chatRoomGroup,SWT.NONE);
		peopleInRoomLabel.setText("Participants: ");
		FormData peopleInRoomLabelFormData = new FormData();
		peopleInRoomLabelFormData.top = new FormAttachment(2);
		peopleInRoomLabelFormData.left = new FormAttachment(2);		
		peopleInRoomLabel.setLayoutData(peopleInRoomLabelFormData);
		
		peopleInRoomList = new List(chatRoomGroup, SWT.SINGLE|SWT.BORDER|SWT.V_SCROLL);
		FormData peopleInRoomListFormData =  new FormData();
		peopleInRoomListFormData.top = new FormAttachment(10);
		peopleInRoomListFormData.left = new FormAttachment(2);
		peopleInRoomListFormData.right = new FormAttachment(60);
		peopleInRoomListFormData.bottom = new FormAttachment(30);
		peopleInRoomList.setLayoutData(peopleInRoomListFormData);
		
		setAsLeaderButton = new Button(chatRoomGroup, SWT.PUSH);
		setAsLeaderButton.setText("Set as leader");
		FormData setAsLeaderButtonFormData = new FormData();
		setAsLeaderButtonFormData.top = new FormAttachment(12);
		setAsLeaderButtonFormData.left = new FormAttachment(65);
		setAsLeaderButtonFormData.right = new FormAttachment(95);
		setAsLeaderButton.setLayoutData(setAsLeaderButtonFormData);
		setAsLeaderButton.setVisible(false);
		setAsLeaderButton.addSelectionListener(this);
		
		isFollowTheLeaderCheck = new Button(chatRoomGroup, SWT.CHECK);
		isFollowTheLeaderCheck.setText("Synchronize location");
		FormData isFollowTheLeaderCheckFormData = new FormData();
		isFollowTheLeaderCheckFormData.top = new FormAttachment(22);
		isFollowTheLeaderCheckFormData.left = new FormAttachment(65);
		isFollowTheLeaderCheckFormData.right = new FormAttachment(95);
		isFollowTheLeaderCheckFormData.bottom = new FormAttachment(29);
		isFollowTheLeaderCheck.setLayoutData(isFollowTheLeaderCheckFormData);
		isFollowTheLeaderCheck.setSelection(true);
		
		messageTextArea = new Text(chatRoomGroup, SWT.MULTI|SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
		FormData messageTextAreaFormData = new FormData();
		messageTextAreaFormData.top = new FormAttachment(35);
		messageTextAreaFormData.left = new FormAttachment(2);
		messageTextAreaFormData.right = new FormAttachment(98);
		messageTextAreaFormData.bottom = new FormAttachment(70);
		messageTextArea.setLayoutData(messageTextAreaFormData);
		messageTextArea.setEditable(false);
		
		Composite separator = new Composite(chatRoomGroup, SWT.NONE);
		FormData separatorFormData = new FormData();
		separatorFormData.top = new FormAttachment(71);
		separatorFormData.left = new FormAttachment(2);
		separatorFormData.right = new FormAttachment(98);
		separatorFormData.bottom = new FormAttachment(72);
		separator.setLayoutData(separatorFormData);
		separator.setBackground(new Color(display,0,0,255));
		
		userInputMessageText = new Text(chatRoomGroup, SWT.BORDER|SWT.MULTI);
		FormData userInputMessageTextFormData = new FormData();
		userInputMessageTextFormData.top = new FormAttachment(separator);
		userInputMessageTextFormData.left = new FormAttachment(2);
		userInputMessageTextFormData.right = new FormAttachment(98);
		userInputMessageTextFormData.bottom = new FormAttachment(90);
		userInputMessageText.setLayoutData(userInputMessageTextFormData);
		
		sendMessageButton = new Button(chatRoomGroup, SWT.PUSH);
		sendMessageButton.setText("Send message");
		FormData sendMessageButtonFormData = new FormData();
		sendMessageButtonFormData.top = new FormAttachment(91);
		sendMessageButtonFormData.left = new FormAttachment(33);
		sendMessageButtonFormData.right = new FormAttachment(67);
		sendMessageButtonFormData.bottom = new FormAttachment(98);
		sendMessageButton.setLayoutData(sendMessageButtonFormData);
		sendMessageButton.addSelectionListener(this);
		
		
		//规划方案组
		Group urbanPlanningGroup = new Group(collaborationPanel,SWT.NONE);
		urbanPlanningGroup.setText("Urban planning");
		urbanPlanningGroup.setLayout(new FormLayout());
		FormData urbanPlanningGroupFormData = new FormData();
		urbanPlanningGroupFormData.top = new FormAttachment(51);
		urbanPlanningGroupFormData.left = new FormAttachment(2);
		urbanPlanningGroupFormData.right = new FormAttachment(98);
		urbanPlanningGroupFormData.bottom = new FormAttachment(99);
		urbanPlanningGroup.setLayoutData(urbanPlanningGroupFormData);
		
		Group planManagementGroup = new Group(urbanPlanningGroup,SWT.NONE);
		planManagementGroup.setText("Plan manager");
		planManagementGroup.setLayout(new FormLayout());
		FormData planManagementGroupFormData = new FormData();
		planManagementGroupFormData.top = new FormAttachment(2);
		planManagementGroupFormData.left = new FormAttachment(2);
		planManagementGroupFormData.right = new FormAttachment(98);
		planManagementGroupFormData.bottom = new FormAttachment(40);
		planManagementGroup.setLayoutData(planManagementGroupFormData);
		
		Label targetPlanLabel = new Label(planManagementGroup,SWT.NONE);
		targetPlanLabel.setText("Target plan: ");
		FormData targetPlanLabelFormData = new FormData();
		targetPlanLabelFormData.top = new FormAttachment(8);
		targetPlanLabelFormData.left = new FormAttachment(2);		
		targetPlanLabel.setLayoutData(targetPlanLabelFormData);
		
		targetPlanCombo = new Combo(planManagementGroup, SWT.READ_ONLY);		
		targetPlanCombo.add("Plan1");
		targetPlanCombo.select(0);
		FormData targetPlanComboFormData = new FormData();
		targetPlanComboFormData.top = new FormAttachment(8);
		targetPlanComboFormData.left = new FormAttachment(targetPlanLabel);
		targetPlanComboFormData.right = new FormAttachment(98);
		targetPlanCombo.setLayoutData(targetPlanComboFormData);
		targetPlanCombo.addSelectionListener(this);
	
		
		newPlanButton = new Button(planManagementGroup, SWT.NONE);
		newPlanButton.setText("New plan");
		FormData newPlanButtonFormData = new FormData();
		newPlanButtonFormData.top = new FormAttachment(40);
		newPlanButtonFormData.left = new FormAttachment(2);
		newPlanButtonFormData.right = new FormAttachment(45);
		newPlanButton.setLayoutData(newPlanButtonFormData);
		newPlanButton.addSelectionListener(this);
		
		savePlanButton = new Button(planManagementGroup, SWT.NONE);
		savePlanButton.setText("Save plan");
		FormData savePlanButtonFormData = new FormData();
		savePlanButtonFormData.top = new FormAttachment(40);
		savePlanButtonFormData.left = new FormAttachment(52);
		savePlanButtonFormData.right = new FormAttachment(95);
		savePlanButton.setLayoutData(savePlanButtonFormData);
		savePlanButton.addSelectionListener(this);
		
		uploadPlanButton = new Button(planManagementGroup, SWT.NONE);
		uploadPlanButton.setText("Upload Plan");
		FormData uploadPlanButtonFormData = new FormData();
		uploadPlanButtonFormData.top = new FormAttachment(70);
		uploadPlanButtonFormData.left = new FormAttachment(2);
		uploadPlanButtonFormData.right = new FormAttachment(45);
		uploadPlanButton.setLayoutData(uploadPlanButtonFormData);
		uploadPlanButton.addSelectionListener(this);
		
		deletePlanButton = new Button(planManagementGroup, SWT.NONE);
		deletePlanButton.setText("Delete Plan");
		FormData deletePlanButtonFormData = new FormData();
		deletePlanButtonFormData.top = new FormAttachment(70);
		deletePlanButtonFormData.left = new FormAttachment(52);
		deletePlanButtonFormData.right = new FormAttachment(95);
		deletePlanButton.setLayoutData(deletePlanButtonFormData);
		deletePlanButton.addSelectionListener(this);
		
	
		
		
		Group planDesignGroup = new Group(urbanPlanningGroup,SWT.NONE);
		planDesignGroup.setText("Plan design");
		planDesignGroup.setLayout(new FormLayout());
		FormData planDesignGroupFormData = new FormData();
		planDesignGroupFormData.top = new FormAttachment(42);
		planDesignGroupFormData.left = new FormAttachment(2);
		planDesignGroupFormData.right = new FormAttachment(98);
		planDesignGroupFormData.bottom = new FormAttachment(99);
		planDesignGroup.setLayoutData(planDesignGroupFormData);
					
		planTabFolder = new TabFolder(planDesignGroup,SWT.NONE);		
		FormData planTabFolderFormData = new FormData();
		planTabFolderFormData.top = new FormAttachment(0);
		planTabFolderFormData.left = new FormAttachment(0);
		planTabFolderFormData.right = new FormAttachment(100);
		planTabFolderFormData.bottom = new FormAttachment(100);		
		planTabFolder.setLayoutData(planTabFolderFormData);
		
		//模型设计面板
		TabItem planTabItem1 =  new TabItem(planTabFolder, SWT.NONE);
		planTabItem1.setText("Models");
		
		Composite planDesignPanel = new Composite(planTabFolder,SWT.NONE);
		planDesignPanel.setLayout(new FormLayout());		
		planTabItem1.setControl(planDesignPanel);
			
		addRoadButton = new Button(planDesignPanel, SWT.NONE);
		addRoadButton.setText("Roads");
		FormData addRoadButtonFormData = new FormData();
		addRoadButtonFormData.top = new FormAttachment(5);
		addRoadButtonFormData.left = new FormAttachment(2);
		addRoadButtonFormData.right = new FormAttachment(45);
		addRoadButton.setLayoutData(addRoadButtonFormData);
		addRoadButton.addSelectionListener(this);
		
		addTreeButton = new Button(planDesignPanel, SWT.NONE);
		addTreeButton.setText("Street trees ");
		FormData addTreeButtonFormData = new FormData();
		addTreeButtonFormData.top = new FormAttachment(5);
		addTreeButtonFormData.left = new FormAttachment(52);
		addTreeButtonFormData.right = new FormAttachment(95);
		addTreeButton.setLayoutData(addTreeButtonFormData);
		addTreeButton.addSelectionListener(this);
		
		addLightButton = new Button(planDesignPanel, SWT.NONE);
		addLightButton.setText("Street lamps");
		FormData addLightButtonFormData = new FormData();
		addLightButtonFormData.top = new FormAttachment(30);
		addLightButtonFormData.left = new FormAttachment(2);
		addLightButtonFormData.right = new FormAttachment(45);
		addLightButton.setLayoutData(addLightButtonFormData);
		addLightButton.addSelectionListener(this);
		
		addPedestrainButton = new Button(planDesignPanel, SWT.NONE);
		addPedestrainButton.setText("Pavements");
		FormData addPedestrainButtonFormData = new FormData();
		addPedestrainButtonFormData.top = new FormAttachment(30);
		addPedestrainButtonFormData.left = new FormAttachment(52);
		addPedestrainButtonFormData.right = new FormAttachment(95);
		addPedestrainButton.setLayoutData(addPedestrainButtonFormData);
		addPedestrainButton.addSelectionListener(this);
		
		addBuildingButton = new Button(planDesignPanel, SWT.NONE);
		addBuildingButton.setText("Buildings");
		FormData addBuildingButtonFormData = new FormData();
		addBuildingButtonFormData.top = new FormAttachment(55);
		addBuildingButtonFormData.left = new FormAttachment(2);
		addBuildingButtonFormData.right = new FormAttachment(45);
		addBuildingButton.setLayoutData(addBuildingButtonFormData);
		addBuildingButton.addSelectionListener(this);
		
		addFacilityButton = new Button(planDesignPanel, SWT.NONE);
		addFacilityButton.setText("Other models");
		FormData addFacilityButtonFormData = new FormData();
		addFacilityButtonFormData.top = new FormAttachment(55);
		addFacilityButtonFormData.left = new FormAttachment(52);
		addFacilityButtonFormData.right = new FormAttachment(95);
		addFacilityButton.setLayoutData(addFacilityButtonFormData);
		addFacilityButton.addSelectionListener(this);
		
		addPeopleButton = new Button(planDesignPanel, SWT.NONE);
		addPeopleButton.setText("People");
		FormData addPeopleButtonFormData = new FormData();
		addPeopleButtonFormData.top = new FormAttachment(80);
		addPeopleButtonFormData.left = new FormAttachment(2);
		addPeopleButtonFormData.right = new FormAttachment(45);
		addPeopleButton.setLayoutData(addPeopleButtonFormData);
		addPeopleButton.addSelectionListener(this);
		
		editModelButton = new Button(planDesignPanel, SWT.NONE);
		editModelButton.setText("Modify models");
		FormData editModelButtonFormData = new FormData();
		editModelButtonFormData.top = new FormAttachment(80);
		editModelButtonFormData.left = new FormAttachment(52);
		editModelButtonFormData.right = new FormAttachment(95);
		editModelButton.setLayoutData(editModelButtonFormData);
		editModelButton.addSelectionListener(this);
		
		
		TabItem planTabItem2 =  new TabItem(planTabFolder, SWT.NONE);
		planTabItem2.setText("Drawing");
		
		Composite labelDesignPanel = new Composite(planTabFolder,SWT.NONE);
		labelDesignPanel.setLayout(new FormLayout());		
		planTabItem2.setControl(labelDesignPanel);
		
		addPolylineButton = new Button(labelDesignPanel, SWT.NONE);
		addPolylineButton.setText("Polylines");
		FormData addPolylineButtonFormData = new FormData();
		addPolylineButtonFormData.top = new FormAttachment(5);
		addPolylineButtonFormData.left = new FormAttachment(2);
		addPolylineButtonFormData.right = new FormAttachment(45);
		addPolylineButton.setLayoutData(addPolylineButtonFormData);
		addPolylineButton.addSelectionListener(this);
		
		addPolygonButton = new Button(labelDesignPanel, SWT.NONE);
		addPolygonButton.setText("Polygons");
		FormData addPolygonButtonFormData = new FormData();
		addPolygonButtonFormData.top = new FormAttachment(5);
		addPolygonButtonFormData.left = new FormAttachment(52);
		addPolygonButtonFormData.right = new FormAttachment(95);
		addPolygonButton.setLayoutData(addPolygonButtonFormData);
		addPolygonButton.addSelectionListener(this);
		
		addTextButton = new Button(labelDesignPanel, SWT.NONE);
		addTextButton.setText("Text labels");
		FormData addTextButtonFormData = new FormData();
		addTextButtonFormData.top = new FormAttachment(30);
		addTextButtonFormData.left = new FormAttachment(2);
		addTextButtonFormData.right = new FormAttachment(45);
		addTextButton.setLayoutData(addTextButtonFormData);
		addTextButton.addSelectionListener(this);
		
		addArrowButton = new Button(labelDesignPanel, SWT.NONE);
		addArrowButton.setText("Arrows");
		FormData addArrowButtonFormData = new FormData();
		addArrowButtonFormData.top = new FormAttachment(30);
		addArrowButtonFormData.left = new FormAttachment(52);
		addArrowButtonFormData.right = new FormAttachment(95);
		addArrowButton.setLayoutData(addArrowButtonFormData);
		addArrowButton.addSelectionListener(this);
		
		addCircleButton = new Button(labelDesignPanel, SWT.NONE);
		addCircleButton.setText("Circles");
		FormData addCircleButtonFormData = new FormData();
		addCircleButtonFormData.top = new FormAttachment(55);
		addCircleButtonFormData.left = new FormAttachment(2);
		addCircleButtonFormData.right = new FormAttachment(45);
		addCircleButton.setLayoutData(addCircleButtonFormData);
		addCircleButton.addSelectionListener(this);
		
		editLabelButton = new Button(labelDesignPanel, SWT.NONE);
		editLabelButton.setText("Edit drawing");
		FormData editLabelButtonFormData = new FormData();
		editLabelButtonFormData.top = new FormAttachment(55);
		editLabelButtonFormData.left = new FormAttachment(52);
		editLabelButtonFormData.right = new FormAttachment(95);
		editLabelButton.setLayoutData(editLabelButtonFormData);
		editLabelButton.addSelectionListener(this);
		
		
		
		
		//协作面板结束================================
	
		
		browser = new Browser(shell,SWT.BORDER);
		FormData browerFormData = new FormData();
		browerFormData.top = new FormAttachment(0);
		browerFormData.left = new FormAttachment(tabFolder);
		browerFormData.bottom = new FormAttachment(status);
		browerFormData.right = new FormAttachment(100);
		browser.setLayoutData(browerFormData);
		
		browser.addStatusTextListener(new AdvancedStatusTextListener(status,this));
		
		
		
		
	}
	
	
	public void widgetSelected(SelectionEvent e)
	{
		if (e.getSource() == inputFlyAddressOKButton)
		{
			openFlyProject();
		}
		else if (e.getSource() == inputFlyAddressResetButton)
		{
			inputFlyAddressText.setText("");
		}
		else if (e.getSource() == startServerButton)
		{
			startServer();
		}
		else if (e.getSource() == stopServerButton) 
		{
			stopServer();
		}
		else if (e.getSource() == startConnectionButton)
		{
			connectToServer();
		}
		else if (e.getSource() == stopConnectionButton) 
		{
			disconnectToServer();
		}
		else if (e.getSource() == sendMessageButton)
		{
			String message = userInputMessageText.getText();
			sendMessage(message,"message");
			userInputMessageText.setText("");
		}
		else if (e.getSource() == setAsLeaderButton)
		{
			setOneUserAsLeader();
		}
		else if (e.getSource() == newPlanButton) 
		{
			browser.execute("planManager.createNewPlan()");
		}
		else if (e.getSource() == targetPlanCombo)
		{
			browser.execute("planManager.setPlanVisibility('"+targetPlanCombo.getText()+"')");			
			sendMessage("focusPlan,"+targetPlanCombo.getText(), "operation");		
		}
		else if (e.getSource() == deletePlanButton)
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("No plan to delete");
				messageBox.open();				
				
				return;
			}
			
			String deletePlanName = targetPlanCombo.getText();				
			browser.execute("planManager.deletePlan('"+deletePlanName+"')");				
			targetPlanCombo.remove(deletePlanName);
			if (targetPlanCombo.getItemCount()==0)
			{
				targetPlanCombo.add("None");
			}
			targetPlanCombo.select(0);
			
			browser.execute("planManager.setPlanVisibility('"+targetPlanCombo.getText()+"')");	
			
			sendMessage("deletePlan,"+deletePlanName, "operation");
				
			
			
		}
		else if (e.getSource() == savePlanButton) 
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("No plan to save");
				messageBox.open();				
				
				return;
			}
			
			browser.execute("planManager.savePlan('"+targetPlanCombo.getText()+"')");			
			sendMessage("savePlan,"+targetPlanCombo.getText(), "operation");
			
		}
		else if (e.getSource() == uploadPlanButton)
		{
			uploadProject();
		}
		else if (e.getSource() == addRoadButton) 
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}
			browser.execute("roadBuilder.initRoadBuilderWin()");
		}
		else if (e.getSource() == addTreeButton)
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}
			browser.execute("treeBuilder.initTreeBuilderWin()");
		}
		else if (e.getSource() == addLightButton) 
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}
			browser.execute("lightBuilder.initLightBuilderWin()");
		}
		else if (e.getSource() == addPedestrainButton) 
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}
			browser.execute("tileBuilder.initTileBuilderWin()");
		}
		else if (e.getSource() == addBuildingButton) 
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}
			
			browser.execute("buildingBuilder.initBuildingBuilderWin()");
		}
		else if (e.getSource() == addFacilityButton)
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}
			
			browser.execute("otherModelBuilder.initOtherModelBuilderWin('facilities')");
		}
		else if (e.getSource() == addPeopleButton)
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}
			
			browser.execute("otherModelBuilder.initOtherModelBuilderWin('people')");
		}
		else if (e.getSource() == editModelButton) 
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}			
			browser.execute("modifyModel.startModifyModel()");
		}
		else if (e.getSource() == addPolylineButton) 
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}			
			browser.execute("labelBuilder.startPolylineLabel()");
		}
		else if (e.getSource() == addPolygonButton) 
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}			
			browser.execute("labelBuilder.startPolygonLabel()");
		}
		else if (e.getSource() == addTextButton) 
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}			
			browser.execute("labelBuilder.startTextLabel()");
		}
		else if (e.getSource() == addArrowButton) 
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}			
			browser.execute("labelBuilder.startArrowLabel()");
		}
		else if (e.getSource() == addCircleButton) 
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}			
			browser.execute("labelBuilder.startCircleLabel()");
		}
		else if (e.getSource() == editLabelButton) 
		{
			if (targetPlanCombo.getText().equals("None"))
			{
				MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
				messageBox.setText("Message");
				messageBox.setMessage("Please create a plan first");
				messageBox.open();				
				
				return;
			}			
			browser.execute("objectEditor.editLabel()");
		}
		
	}
	
	
	
	
	
	public void keyPressed(KeyEvent e)
	{
		if (e.getSource() == inputFlyAddressText)
		{
			if(e.keyCode!=SWT.CR) return;
			
			openFlyProject();
		}
		
	}
	
	
	public void widgetDefaultSelected(SelectionEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	//打开fly工程函数
	public void openFlyProject()
	{
		String url = inputFlyAddressText.getText();
		if(!url.equals(""))
		{
			if (url.indexOf("http://")==-1)
			{
				url = "http://"+url;
			}
			browser.setUrl(url);
			currentUrlAddress = url;
		}
		else
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Address cannot be blank");
			messageBox.open();
		}		
	}
	
	//服务器启动函数
	public void startServer()
	{
		if (currentUrlAddress.equals(""))
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Please open a project first");
			messageBox.open();
			
			return;			
		}
		
		
		if (inputServerPortText.getText().equals(""))
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Port cannot be blank");
			messageBox.open();
			
			return;			
		}
		
		if (inputServerUserNameText.getText().equals(""))
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Username cannot be blank");
			messageBox.open();
			
			return;	
		}
		
		try
		{
			serverPort = Integer.parseInt(inputServerPortText.getText().trim());
		} 
		catch (Exception e)
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Port number error，port number must be an integer between 1-65535");
			messageBox.open();
			
			return;	
		}
		
		currentUserName = inputServerUserNameText.getText().trim();

		
		try
		{
			serverSocket = new ServerSocket(serverPort);
			locationServerSocket = new ServerSocket(locationPort);
			
			serverStatusText.append("Session has been created successfully\n");
			//serverStatusText.append("IP地址是"+InetAddress.getLocalHost().getHostAddress()+"\n");
			serverStatusText.append("IP address is: "+InetAddress.getLocalHost().getHostAddress()+"\n");
			serverStatusText.append("Port number is: "+serverPort+"\n");
			
			startServerButton.setEnabled(false);
			inputServerPortText.setEnabled(false);
			inputServerUserNameText.setEnabled(false);
			stopServerButton.setEnabled(true);
			collaborationPanel.setEnabled(true);
			setAsLeaderButton.setVisible(true);
			
											
			userLinkList = new UserLinkList();				
			
			serverListenThread = new ServerListen(serverSocket,locationServerSocket, serverStatusText,userLinkList);			
           // locationServerListenThread = new LocationServerListen(locationServerSocket,locationUserLinkList);	
			serverListenThread.start();
			//locationServerListenThread.start();
			
						
			
			isServer = true;
			
			//给连接面板中的IP地址和用户名赋值
			inputServerIPClientText.setText(InetAddress.getLocalHost().getHostAddress());
			inputUserNameClientText.setText(inputServerUserNameText.getText());	
			inputServerPortClientText.setText(""+serverPort);
			
			connectToServer();
						
		} 
		catch (Exception e)
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Cannot creat session: the port has been used by other programs");
			messageBox.open();
			
			return;	
		}
	}
	
	
	public void stopServer()
	{
		try
		{
			if(isClient)
				disconnectToServer();
			
			//向所有人发送服务器关闭的消息
			sendStopToAll();
					
			serverListenThread.isStop = true;
			
						
			serverSocket.close();
			locationServerSocket.close();
									
			int count = userLinkList.getCount();
			
			int i =0;
			while( i < count)
			{
				UserNode node = userLinkList.findUser(i);				
				
				node.locationInput.close();
				node.locationOutput.close();
				node.locationSocket.close();
				node.input.close();
				node.output.close();
				node.socket.close();
				
				
				i ++;
			}

			startServerButton.setEnabled(true);
			inputServerPortText.setEnabled(true);
			inputServerUserNameText.setEnabled(true);
			stopServerButton.setEnabled(false);
			collaborationPanel.setEnabled(false);
			setAsLeaderButton.setVisible(false);
						
			serverStatusText.append("Session has been closed\n");
			
			isServer = false;

		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	public void sendStopToAll()
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
			
			try
			{								
				node.output.writeUTF("serverstop");
				node.output.flush();
			
			}
			catch (Exception e)
			{
				System.out.println("$$$"+e);
			}
			
			i++;
		}
	}
	
	public void connectToServer()
	{
		
		if (currentUrlAddress.equals(""))
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Please open a project first");
			messageBox.open();
			
			return;			
		}
		
		
		if (inputServerIPClientText.getText().equals(""))
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("The server IP address cannot be blank");
			messageBox.open();
			
			return;			
		}
		
		if (inputServerPortClientText.getText().equals(""))
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Server port cannot be blank");
			messageBox.open();
			
			return;			
		}
		
		if (inputUserNameClientText.getText().equals(""))
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Username cannot be blank");
			messageBox.open();
			
			return;	
		}
		
		try
		{
			clientServerPort = Integer.parseInt(inputServerPortClientText.getText().trim());
		} 
		catch (Exception e)
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Port number error，port number must be an integer between 1-65535");
			messageBox.open();
			
			return;	
		}
				
		
		try
		{
			clientSocket = new Socket(inputServerIPClientText.getText().trim(),clientServerPort);
			locationClientSocket = new Socket(inputServerIPClientText.getText().trim(), locationPort);
			
		} 
		catch (Exception e)
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Cannot connect to server, please check IP address and port number again");
			messageBox.open();
			
			return;	
		}
		
		currentUserName = inputUserNameClientText.getText().trim();
		
		try
		{
			clientOutput = new DataOutputStream(clientSocket.getOutputStream());
			clientOutput.flush();	
			clientInput  = new DataInputStream(clientSocket.getInputStream() );				
			clientOutput.writeUTF(currentUserName);
			clientOutput.flush();
			
			locationClientOutput = new DataOutputStream(locationClientSocket.getOutputStream());
			locationClientOutput.flush();
			locationClientInput = new DataInputStream(locationClientSocket.getInputStream());
			
			
			locationClientReceiveThread = new LocationClientReceive(this);
			locationClientReceiveThread.start();		
			clientReceiveThread = new ClientReceive(this,locationClientReceiveThread.broadCastLocationTimer);
			clientReceiveThread.start();

			
			inputFlyAddressText.setEnabled(false);
			inputFlyAddressOKButton.setEnabled(false);
			inputFlyAddressResetButton.setEnabled(false);
			startConnectionButton.setEnabled(false);
			inputServerIPClientText.setEnabled(false);
			inputServerPortClientText.setEnabled(false);
			inputUserNameClientText.setEnabled(false);
			stopConnectionButton.setEnabled(true);
			collaborationPanel.setEnabled(true);
			
			
			connectionStatusText.append("Successfully connected to  "+inputServerIPClientText.getText()+":"+clientServerPort+"...\n");
		
			isClient = true;
			
		}
		catch (Exception e){
			System.out.println(e);
			return;
		}
	}
	
	public void disconnectToServer()
	{
		inputFlyAddressText.setEnabled(true);
		inputFlyAddressOKButton.setEnabled(true);
		inputFlyAddressResetButton.setEnabled(true);
		startConnectionButton.setEnabled(true);
		inputServerIPClientText.setEnabled(true);
		inputServerPortClientText.setEnabled(true);
		inputUserNameClientText.setEnabled(true);
		stopConnectionButton.setEnabled(false);
		collaborationPanel.setEnabled(false);
		
		peopleInRoomList.removeAll();
		messageTextArea.setText("");
		userInputMessageText.setText("");
		targetPlanCombo.removeAll();
		targetPlanCombo.add("Plan1");
		targetPlanCombo.select(0);
				
		isClient = false;
		
		if(clientSocket.isClosed() && locationClientSocket.isClosed())		
			return ;
		
		
		try
		{		
	
			clientOutput.writeUTF("userleft");
			clientOutput.flush();
		
			clientInput.close();
			clientOutput.close();
			clientSocket.close();
			
			locationClientInput.close();
			locationClientOutput.close();
			locationClientSocket.close();
			
			
			connectionStatusText.append("Already disconnected with the server...\n");
					
		}
		catch (Exception e)
		{
			//
		}
	
	}
	
	public void sendMessage(String message, String messageType)
	{		
		if(clientSocket.isClosed())
		{
			return ;
		}
		
		try
		{
			clientOutput.writeUTF(messageType);
			clientOutput.flush();
			
			clientOutput.writeUTF(message);
			clientOutput.flush();
		}
		catch (Exception e)
		{
			//
		}
	
	}
	
	public void setOneUserAsLeader()
	{
		String selectedPeople = peopleInRoomList.getSelection()[0];
		
		if (selectedPeople.equals(""))
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Cannot set as a leader");
			messageBox.open();
			
			return;	
		}
		
		if (selectedPeople.indexOf("Leader")!= -1)
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("The member is already a leader");
			messageBox.open();
			
			return;	
		}
		
	    userLinkList.setAsLeader(selectedPeople);
	    
	    sendUserList();
				
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
	
	
	public void afterPlanningOperation(String operationString)
	{
		String[] operationDetails = operationString.split(",");
		
		if (operationDetails[0].equals("createPlan"))
		{
			if (targetPlanCombo.getText().equals("None") )
			{
				targetPlanCombo.removeAll();				
			}
			targetPlanCombo.add(operationDetails[1]);
			targetPlanCombo.select(targetPlanCombo.getItemCount()-1);
			
			browser.execute("planManager.setPlanVisibility('"+targetPlanCombo.getText()+"')");
		}
		
		sendMessage(operationString, "operation");
		
	}
	
	public void uploadProject()
	{
		String fileName = targetPlanCombo.getText()+".fly";
		if (fileName.equals("None.fly"))
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("No plan available to be uploaded");
			messageBox.open();
			
			return;	
		}
		
		String localFolderPath = System.getProperty("user.home")+"\\Application Data\\Skyline\\TerraExplorer";		
		File flyFile = new File(localFolderPath,fileName);
		if (!flyFile.exists())
		{
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Please save plan first, and then upload");
			messageBox.open();
			
			return;	
		}
		
		String serverURL = currentUrlAddress.replaceFirst("http://", "");
		serverURL = serverURL.substring(0,serverURL.indexOf("/"));
		int portIndex = serverURL.indexOf(":");
		if (portIndex != -1)
		{
			serverURL = serverURL.substring(0,portIndex);
		}
		
		FTPDialog ftpDialog = new FTPDialog(shell);
		Object result = ftpDialog.open();
		String[] resultArray =  result.toString().split(";");
		
		//System.out.println(result.toString());
		
		
		try
		{
			SimpleFTP simpleFTP = new SimpleFTP();
			
			String ftpUsername = resultArray[0];
			String ftpPassword = resultArray[1];
			simpleFTP.connect(serverURL, 21, ftpUsername, ftpPassword);
			simpleFTP.bin();
			simpleFTP.cwd("PPGISProj");
			simpleFTP.stor(flyFile);
			simpleFTP.disconnect();
			
			MessageBox messageBox = new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			messageBox.setText("Message");
			messageBox.setMessage("Plan has been successfully uploaded");
			messageBox.open();
			
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
	}
	
	
	
		
	
	public void run() 
	{
        Display display = new Display();
        shell = new Shell(display);
        shell.setText("Design subsystem");
        createContents(shell,display);
        shell.open();               
        Toolkit toolkit = Toolkit.getDefaultToolkit();       
        shell.setSize((int)toolkit.getScreenSize().getWidth(), (int)toolkit.getScreenSize().getHeight()-30);
        shell.setLocation(0, 0);
        
        shell.addDisposeListener(new DisposeListener()
        {
        	public void widgetDisposed(DisposeEvent e)
        	{
        		if (isServer)
				{
					stopServer();
				}
        		if(isClient)
        		{
        			disconnectToServer();
        		}
        		
        	}
        });
        
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        
		
        display.dispose();
	}
	
	
	
	public static void main(String[] args) 
    {
        new ClientMain().run();
    }
	

}
