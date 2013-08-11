package com.ecnu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class UserNode
{
	String username = null;
	
	Socket socket = null;
	DataOutputStream output = null;
	DataInputStream input = null;
	
	Socket locationSocket = null;
	DataOutputStream locationOutput = null;
	DataInputStream locationInput = null;
	
	boolean isLeader = false;	
	UserNode next = null;

}
