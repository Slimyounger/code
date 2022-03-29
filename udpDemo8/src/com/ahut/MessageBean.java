package com.ahut;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.SocketAddress;

public class MessageBean {
		private byte[] name;
		
		private byte[] content;//��Ϣ����
		
		private int type;//0������һ���ַ�����1��������
		private int totalLen; //���ݵĳ���

		private int id;  //Ψһ��ID


		//���ز�����Ϊ�������������

		private SocketAddress recvRespAdd;  //�����߽���Ӧ��ĵ�ַ

		private SocketAddress destAdd;  //�����ߵ�ַ

		private int sendCount=0; //���ʹ���

		private long lastSendTime; //���һ�η��͵�ʱ��
	public byte[] getName() {
		return name;
	}
	public void setName(byte[] name) {
		this.name = name;
	}
	public byte[] getContent() {
		 

		return content;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setContent(byte[] content) {
		this.content = content;
		totalLen=4+4+6+content.length+4;
	}

	/**

	*

	* @param udpData  //���ܵ���udp���ݽ���ΪNetJavaMsg����

	*/

	public MessageBean(byte[] udpData){
	try{
	ByteArrayInputStream bins=new ByteArrayInputStream(udpData);

	DataInputStream dins=new DataInputStream(bins);

	this.totalLen=dins.readInt();

	this.id=dins.readInt();
	
	this.type=dins.readInt();
	this.name=new byte[6];
	
	this.content=new byte[totalLen-4-4-4-6];
	dins.readFully(name, 0, 6);
	
	dins.readFully(content);
	
	
	System.out.println(new String(getName()));
	
	
	}catch(Exception e){
	e.printStackTrace();

	}

	}

	public MessageBean() {
		// TODO Auto-generated constructor stub
	}

	public byte[] toByte(){
	try{
	ByteArrayOutputStream bous=new ByteArrayOutputStream();

	DataOutputStream dous=new DataOutputStream(bous);

	dous.writeInt(totalLen);

	dous.writeInt(id);
	
	dous.writeInt(type);
	
	dous.write(name);
	
	dous.write(content);
	
	

	dous.flush();

	return bous.toByteArray();

	}catch(Exception e){
	e.printStackTrace();

	}

	return null;

	}

	@Override

	public String toString() {
	// TODO Auto-generated method stub

	return "id:"+id+"  content"+new String(content)+"  totalLen"+totalLen+"sengerAdd:"+recvRespAdd+"  destAdd:"+destAdd;

	}

	public int getTotalLen() {
	return totalLen;

	}

	public void setTotalLen(int totalLen) {
	this.totalLen = totalLen;

	}

	public int getId() {
	return id;

	}

	public void setId(int id) {
	this.id = id;

	}



	
	public SocketAddress getRecvRespAdd() {
	return recvRespAdd;

	}

	public void setRecvRespAdd(SocketAddress recvRespAdd) {
	this.recvRespAdd = recvRespAdd;

	}

	public SocketAddress getDestAdd() {
	return destAdd;

	}

	public void setDestAdd(SocketAddress destAdd) {
	this.destAdd = destAdd;

	}

	public int getSendCount() {
	return sendCount;

	}

	public void setSendCount(int sendCount) {
	this.sendCount = sendCount;

	}

	public long getLastSendTime() {
	return lastSendTime;

	}

	public void setLastSendTime(long lastSendTime) {
	this.lastSendTime = lastSendTime;

	}

	
	
}
