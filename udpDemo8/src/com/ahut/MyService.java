package com.ahut;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;

import com.google.gson.Gson;

public class MyService {
	static int PORT=10003;
	private static DatagramSocket socket;
	private static ArrayList<ClientActivity> mList=new ArrayList<>();
	private SocketAddress localAddr;
	String My_Ip;
	//DatagramPacket datagramPacket;

	public MyService(String My_Ip) throws Exception {
		this.My_Ip=My_Ip;
		localAddr=new InetSocketAddress(My_Ip,PORT);

			socket=new DatagramSocket(localAddr);
	
		startRecvThread();
	}
	public static void loginGroups(ClientActivity clientActivity) {
		if(clientActivity==null)
			return;
		mList.add(clientActivity);
	}
	

	public void recvMsg() throws Exception{
		System.out.println("接收线程启动");
		byte[] recvData=new byte[1024];

		DatagramPacket datagramPacket=new DatagramPacket(recvData,recvData.length);
		while(true){
		
			
		socket.receive(datagramPacket);
		
		String msg=new String(datagramPacket.getData(),0,datagramPacket.getLength());
		System.out.println("msg"+msg);
		MessageBean b=new MessageBean(datagramPacket.getData());
		//b.setContent(msg.getBytes());
		/*Gson gson=new Gson();
		MessageBean bean=gson.fromJson(msg, MessageBean.class);*/
		System.out.println("....sdasd"+b.getId()+new String(b.getName())+new String(b.getContent()));
		for(ClientActivity clientActivity:mList) {
			//发送数据给每一个客户端
			clientActivity.pushMessage(b);
			System.out.println("...."+new String(b.getContent()));

				}
		//receiveMessage();
		MessageBean recvMsg=new MessageBean(datagramPacket.getData());

		NetJavaRespMsg resp=new NetJavaRespMsg(recvMsg.getId(),(byte)0,System.currentTimeMillis());

		byte[] data=resp.toByte();

		DatagramPacket dp=new DatagramPacket(data,data.length,datagramPacket.getSocketAddress());

		socket.send(dp);

		System.out.println("接收端-已发送应答"+resp+new String(recvMsg.getContent())+new String(recvMsg.getName()));
		
		}
	
	}
	public void startRecvThread(){
	new Thread(new Runnable(){
	public void run() {
		//while(true) {
		try{
			recvMsg();
			

			}catch(Exception e){
			e.printStackTrace();

			}

			}}).start();
			
		
	//}


}}