package com.ahut;


import java.text.SimpleDateFormat;


import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) throws Exception {
		


		 
		//String Target_Ip = JOptionPane.showInputDialog(null,"请输入对话的ip"); 
		//JOptionPane.showMessageDialog(null,"显示的内容"); 
		MyService myService1=new MyService("127.0.0.3");
		//MyService myService2=new MyService("127.0.0.2",10003);
		//myService1.start();
		//myService2.start();
		
		//String name = JOptionPane.showInputDialog(null,"请输入您的昵称");
		new ClientActivity("霍鑫凯","127.0.0.3","127.0.0.1",10003,10001);
		new ClientActivity("范鹏辉","127.0.0.3","127.0.0.2",10003,10002);
		
	}
}
