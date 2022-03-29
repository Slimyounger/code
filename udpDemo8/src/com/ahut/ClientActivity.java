package com.ahut;

import java.awt.EventQueue;
import java.awt.List;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import com.google.gson.Gson;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import java.net.DatagramPacket;

import java.net.DatagramSocket;

import java.net.InetSocketAddress;

import java.net.SocketAddress;

import java.util.*;

import java.util.concurrent.*;

public class ClientActivity extends JFrame{

	private byte[] name;
	private JTextField textField;
	private DatagramSocket socket;//���͵�Socket����
	private InetAddress ip;
	private JTextArea textArea;
	private JButton playBtn;
	String Target_Ip;
	String My_Ip;
	int Target_port;
	int My_port;
	private int num = 0;
	private int num2 = 0;
	JComboBox comboBox = new JComboBox();
	private AudioFormat format;
	public TargetDataLine targetDataLine;
	private boolean isStartAduio=true;
	
	private SocketAddress localAddr; //����Ҫ���͵ĵ�ַ����

	//private DatagramSocket dSender; 

	private SocketAddress destAddr; //Ŀ���ַ
	Map msgQueue= new ConcurrentHashMap();

	//���ػ����ѷ��͵���ϢMap  keyΪ��ϢID  valueΪ��Ϣ������
	
	

	//startSendThread();

	
	public ClientActivity(String name,String Target_Ip,String My_Ip,int Target_port,int My_port) {
		super("����ͻ��ˣ�"+name);
		this.name=name.getBytes();
		this.Target_Ip=Target_Ip;
		this.My_Ip=My_Ip;
		this.Target_port=Target_port;
		this.My_port=My_port;
		
		setSize(500, 500);
		getContentPane().setLayout(null);
		initLayout();
		initUdp();
		show();
	}
	
	private void initLayout() {
		JButton sendBtn = new JButton("����");
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg=textField.getText();
				sendMessage(msg, 0);
				textField.setText("");
			}
		});
		sendBtn.setBounds(366, 419, 97, 23);
		getContentPane().add(sendBtn);
		
		textField = new JTextField();
		textField.setBounds(10, 419, 346, 23);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		final JButton soundRecordBtn = new JButton("���¼��");
		soundRecordBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isStartAduio) {
					captureAudio();
					soundRecordBtn.setText("�������");
				}else {
					targetDataLine.stop();
					targetDataLine.close();
					soundRecordBtn.setText("���¼��");
					sendMessage("������chat"+num+"    ", 1);
					playBtn.setVisible(true);
					comboBox.setVisible(true);
				}
				isStartAduio=!isStartAduio;
			}
		});
		soundRecordBtn.setBounds(48, 375, 97, 23);
		getContentPane().add(soundRecordBtn);
		
		playBtn = new JButton("�������");
		playBtn.setVisible(false);
		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playAduio();
				
			}
		});
		playBtn.setBounds(385, 375, 97, 23);
		getContentPane().add(playBtn);
		
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textArea.setEditable(false);
		textArea.setBounds(10, 13, 453, 305);
		getContentPane().add(textArea);
		getContentPane().add(scrollPane);
		
		
		comboBox.setBounds(216, 367, 155, 31);
		comboBox.setVisible(false);
		getContentPane().add(comboBox);
	}
	private void initUdp() {
		MyService.loginGroups(this);
		//���������߳�
		localAddr=new InetSocketAddress(My_Ip,My_port);

		try {
			socket=new DatagramSocket(localAddr);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		destAddr=new InetSocketAddress(Target_Ip,Target_port);
			
		
		try {
			socket=new DatagramSocket();
			ip=InetAddress.getByName(Target_Ip);//"172.27.85.30"
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	public void pushMessage(MessageBean bean) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ  
		System.out.println(df.format(new Date()));// new Date()Ϊ��ȡ��ǰϵͳʱ��
		String str1 = new String(bean.getContent());
		textArea.append(new String(bean.getName())+" "+df.format( new Date())+":\n"+str1+"\n");
		
		
		if(bean.getType()==0) {
			playBtn.setVisible(false);
			
		}else {
			playBtn.setVisible(true);
			comboBox.setVisible(true);
			//textArea.append(new String(bean.getName())+" "+df.format( new Date())+":\n����һ��\n");
			num2++;
			comboBox.addItem("chat"+num2+".wav");
		}
	}
	public void pushMessage(MessageBean bean,int i) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ  
		System.out.println(df.format(new Date()));// new Date()Ϊ��ȡ��ǰϵͳʱ��
		String str1 = new String(bean.getContent());
		textArea.append(new String(bean.getName())+" "+df.format( new Date())+"(����):\n"+str1+"\n");
		
		if(bean.getType()==0) {
			playBtn.setVisible(false);
			
			
		}else {
			//textArea.append(new String(bean.getName())+" "+df.format( new Date())+":\n����һ��\n");
			playBtn.setVisible(true);
			comboBox.setVisible(true);
			//comboBox.addItem("chat"+num+".wav");
		}
	}
	private void sendMessage(String msg,int type) {
		MessageBean bean=new MessageBean();
		bean.setName(name);
		bean.setContent(msg.getBytes());
		bean.setType(type);
		/*Gson gson=new Gson();
		String json=gson.toJson(bean);*/
		byte[] bytes=bean.toByte();
		DatagramPacket datagramPacket=new DatagramPacket(bytes, bytes.length,ip,MyService.PORT);
			
		
			 try {
				send(bean,name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			startRecvResponseThread();

			startReSendThread();
	
			pushMessage(bean,1);
	
	}
	private void captureAudio() {
		format=getAudioForamt();
		DataLine.Info dataLineInfo=new DataLine.Info(TargetDataLine.class, format);
		try {
			targetDataLine=(TargetDataLine) AudioSystem.getLine(dataLineInfo);
			new CaptureThread().start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	private AudioFormat getAudioForamt() {
		float sampleRate=11025.0f;
		int sampleSizeInBits=16;
		int channels=1;
		boolean signed=true;
		boolean bigEndian=false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}
	class CaptureThread extends Thread{
		@Override
		public void run() {
			AudioFileFormat.Type fileType=AudioFileFormat.Type.WAVE;
			num++;
			File audioFile=new File("chat"+num+".wav");
			
			try {
				targetDataLine.open(format);
				targetDataLine.start();
				AudioSystem.write(new AudioInputStream(targetDataLine), fileType, audioFile);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void playAduio()  {
		AudioInputStream as;
		try {
			
			as=AudioSystem.getAudioInputStream(new File(comboBox.getSelectedItem().toString()));
			AudioFormat format=as.getFormat();
			SourceDataLine sdl=null;
			DataLine.Info info=new DataLine.Info(SourceDataLine.class,format);
			sdl=(SourceDataLine) AudioSystem.getLine(info);
			sdl.open(format);
			sdl.start();
			int nBytesRead=0;
			byte[] abData=new byte[512];
			while(nBytesRead!=-1) {
				nBytesRead=as.read(abData,0,abData.length);
				if(nBytesRead>=0) {
					sdl.write(abData, 0, nBytesRead);
				}
			}
			sdl.drain();
			sdl.close();

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	
	//ģ�ⷢ����Ϣ

	public void send(MessageBean bean,byte[] name) throws Exception{
	System.out.println("���Ͷ�-���������߳�����...");
	
	int id=0;

	//while(true){
	id++;
/*
	byte[] msgData=(id+"-hello").getBytes();

	//����Ҫ���͵���Ϣ����

	MessageBean sendMsg=new MessageBean();
	sendMsg.setContent(msgData);
	sendMsg.setId(id);*/
	bean.setId(id);
	bean.setName(name);
	//Ҫ���͵����ݣ���Ҫ���͵�����תΪ�ֽ�����

	byte[] buffer=bean.toByte();

	//�������ݰ���ָ�����ݣ�ָ��Ŀ���ַ

	DatagramPacket dp=new DatagramPacket(buffer,buffer.length,destAddr);

	socket.send(dp);  //����

	bean.setSendCount(1);

	bean.setLastSendTime(System.currentTimeMillis());

	bean.setRecvRespAdd(localAddr);

	bean.setDestAdd(destAddr);

	msgQueue.put(id, bean);

	System.out.println("�ͻ���-�����ѷ���"+bean+new String(bean.getName()));

	Thread.sleep(1000);

	}

	//}

	//��������Ӧ���߳�

	public void startRecvResponseThread(){
	new Thread(new Runnable(){
	@Override

	public void run() {
	try{
	recvResponse();

	}catch(Exception e){
	e.printStackTrace();

	}

	}}).start();

	}

	//����Ӧ����Ϣ

	public void recvResponse() throws Exception{
	System.out.println("���ն�-����Ӧ���߳�����...");

	while(true){
	byte[] recvData=new byte[100];

	//�����������ݰ�����

	DatagramPacket recvPacket=new DatagramPacket(recvData,recvData.length);

	socket.receive(recvPacket);

	NetJavaRespMsg resp=new NetJavaRespMsg(recvPacket.getData());

	int respID=resp.getRepId();

	MessageBean msg=(MessageBean) msgQueue.get(respID);

	if(msg!=null){
	System.out.println("���ն����յ���"+msg);

	msgQueue.remove(respID);

	}

	}

	}

	//�����ط��߳�

	public void startReSendThread(){
	new Thread(new Runnable(){
	@Override

	public void run() {
	try{
	while(true){
	resendMsg();

	Thread.sleep(1000);

	}

	}catch(Exception e){
	e.printStackTrace();

	}

	}}).start();

	}

	//�ж�Map�е���Ϣ���������3��δ�յ�Ӧ�����ط�

	public void resendMsg(){
	Set keyset=msgQueue.keySet();

	Iterator it=keyset.iterator();

	while(it.hasNext()){
	Integer key=(Integer) it.next();

	MessageBean msg=(MessageBean) msgQueue.get(key);

	if(msg.getSendCount()>3){
	it.remove();

	System.out.println("***���Ͷ�---��⵽��ʧ����Ϣ"+msg);

	}

	long cTime=System.currentTimeMillis();

	if((cTime-msg.getLastSendTime())>3000&&msg.getSendCount()<=3){
	byte[] buffer=msg.toByte();

	try{
	DatagramPacket dp=new DatagramPacket(buffer,buffer.length,msg.getDestAdd());

	socket.send(dp);

	msg.setSendCount(msg.getSendCount()+1);

	System.out.println("�ͻ���--�ط���Ϣ:"+msg);

	}catch(Exception e){
	e.printStackTrace();

	}

	}

	}

	}
}
