package com.ahut;


import java.text.SimpleDateFormat;


import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) throws Exception {
		


		 
		//String Target_Ip = JOptionPane.showInputDialog(null,"������Ի���ip"); 
		//JOptionPane.showMessageDialog(null,"��ʾ������"); 
		MyService myService1=new MyService("127.0.0.3");
		//MyService myService2=new MyService("127.0.0.2",10003);
		//myService1.start();
		//myService2.start();
		
		//String name = JOptionPane.showInputDialog(null,"�����������ǳ�");
		new ClientActivity("���ο�","127.0.0.3","127.0.0.1",10003,10001);
		new ClientActivity("������","127.0.0.3","127.0.0.2",10003,10002);
		
	}
}
