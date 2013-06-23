package com.sg.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

import com.sg.main.Constants;

public class Connector implements Runnable{
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	Thread client;
	boolean runable;
	PacketMgr pkMgr;

	public Connector(){

		try {
			socket = new Socket(Constants.serverIP, Constants.serverPort);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			
			pkMgr = new PacketMgr();
			runable = true;
			client = new Thread(this);
			client.start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public DataInputStream getDis() {
		return dis;
	}

	public DataOutputStream getDos() {
		return dos;
	}
	
	public void sendHeader(int type, int length){
		byte[] bytes = new byte[] {
				(byte)(type >>> 24),(byte)(type >>> 16),
				(byte)(type >>> 8),(byte)(type >>> 0),
				(byte)(length >>> 24),(byte)(length >>> 16),
				(byte)(length >>> 8),(byte)(length >>> 0)
		};
		try {
			dos.write(bytes);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendData(String data){
		byte out[];
		data.trim();
		out = data.getBytes();
		
		try {
			System.out.println(out);
			dos.write(out);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receiveHeader(){
		byte[] headerBuf = new byte[8];
		int n1,n2,n3,n4;
		int type;
		int length;
		try {
			dis.read(headerBuf);
			
			n1 = (headerBuf[0] & (int)0xFF)<<24;   
			n2 = (headerBuf[1] & (int)0xFF)<<16;   
			n3 = (headerBuf[2] & (int)0xFF)<<8;   
			n4 = (headerBuf[3] & (int)0xFF);
			
			type = n1+n2+n3+n4;
			
			n1 = (headerBuf[4] & (int)0xFF)<<24;   
			n2 = (headerBuf[5] & (int)0xFF)<<16;   
			n3 = (headerBuf[6] & (int)0xFF)<<8;   
			n4 = (headerBuf[7] & (int)0xFF);
			
			length = n1+n2+n3+n4;

			System.out.println("type "+type);
			System.out.println("length "+length);
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public void receiveData(int length){
	public void receiveData(){
		byte[] dataBuf = new byte[1024];
//		byte[] dataBuf = new byte[length];
		try {
			dis.read(dataBuf);
			String data = new String(dataBuf);
			System.out.println("data " +data);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receiveData(String data){

	}
	
	public void disconnect(){
		runable = false;
		try {
			dis.close();
			dos.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String pk;
		
		while(runable) {
//			try {
//				msg = dis.readUTF();
//				byte[] temp = new byte[1024];
//				dis.read(temp);
//				pk = new String(temp); 
//				pk = pk.trim();
//				System.out.println(temp[0]);
//				System.out.println(temp[1]);
//				System.out.println(temp[2]);
//				System.out.println(temp[3]);
//				if(!(pk.equals(""))){
//					System.out.println("receive from : " + pk);
//					System.out.println(pk.getBytes().length);
//				}
//
//				pkMgr.managePacket(pk);
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
	}
}
