package com.company;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;

public class MySocketServer extends Thread {
    static ArrayList<Socket> list = new ArrayList<Socket>();
    static Socket socket=null;
    public MySocketServer(Socket socket){
        this.socket=socket;
        list.add(socket);
    }
    public void run(){
        try{

            System.out.println("서버:"+socket.getInetAddress()+"IP 의 client" +
                    "와연결되었습니다.");
            InputStream input=socket.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(input));

            OutputStream out = socket.getOutputStream();
            PrintWriter writer=new PrintWriter(out,true);

            writer.println("Connecting with Server. please write down your ID");

            String readValue; // save value from client
            String name=null; //save name from client
            boolean identify=false;

            while((readValue=reader.readLine())!=null){
                if(!identify){
                    name=readValue;
                    identify=true;
                    writer.println(name+"is connected.");
                    continue;
                }
                for(int i=0;i<list.size();i++){
                    out=list.get(i).getOutputStream();
                    writer=new PrintWriter(out,true);
                    writer.println(name+" : "+ readValue);

                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        try{
            final int SERVER_PORT= 5000;
            ServerSocket serverSocket=new ServerSocket(SERVER_PORT);
            System.out.println("socket :"+ SERVER_PORT+"server start ");
            while(true){
                Socket socketUser=serverSocket.accept();
                Thread thd=new MySocketServer(socketUser);
                thd.start();
            }
        }
        catch(IOException e){
            e.printStackTrace();

        }
    }
}
