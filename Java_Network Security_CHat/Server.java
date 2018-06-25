//package chat;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {

	private static int Id_unique;
	
	private ArrayList<ClientThread> arrlist;

	//private ServerGUI sg;

	private SimpleDateFormat simpledateformat;
	
	private int pt;
	
	private boolean kGoing;
	

	
	public Server(int pt) {
          //  		this.sg = sg;
	
		this.pt = pt;
		simpledateformat = new SimpleDateFormat("HH:mm:ss");
		
		arrlist = new ArrayList<ClientThread>();
		//this(pt, null);
	}
	
	/*public Server(int pt, ServerGUI sg) {
			this.sg = sg;
	
		this.pt = pt;
		simpledateformat = new SimpleDateFormat("HH:mm:ss");
		
		arrlist = new ArrayList<ClientThread>();
	}
	*/
	public void start() {
		kGoing = true;
		
		try 
		{
			
			ServerSocket serverSocket = new ServerSocket(pt);

			
			for(;;) 
			{
				
				display("Server waiting for Clients on port " + pt + ".");
				
				Socket socket = serverSocket.accept();  	
				if(!kGoing)
					break;
				ClientThread t1 = new ClientThread(socket);  
				arrlist.add(t1);									// save it in the ArrayList
				t1.start();
			}
		
			try {
				serverSocket.close();
                                /*
                                i=0;
                                while(i<arrlist){
                                ClientThread t2 = arrlist.get(i);
					try {
					t2.sInput.close();
					t2.sOutput.close();
					t2.socket.close();
                                i++;
                                }
                                */
				for(int i = 0; i < arrlist.size(); ++i) {
					ClientThread t2 = arrlist.get(i);
					try {
					t2.sInput.close();
					t2.sOutput.close();
					t2.socket.close();
					}
					catch(IOException ioE) {
						
					}
				}
			}
			catch(Exception e) {
				display("Clients and server will be closed by exception: " + e);
			}
		}
		
		catch (IOException e) {
            String message = simpledateformat.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(message);
		}
	}		
    
	protected void stop() {
		kGoing = false;
		
		try {
			new Socket("localhost", pt);
		}
		catch(Exception e) {
		}
	}
	
	private void display(String message) {
		String time = simpledateformat.format(new Date()) + " " + message;
		//if(sg == null)
			System.out.println(time);
		/*else
			sg.appendEvent(time + "\n");*/
	}
	
	private synchronized void broadcast(String message, String name) {
		String time = simpledateformat.format(new Date());
		String messageLf = time + " " + message + "\n";
		/*if(sg == null)
			System.out.print(messageLf);
		else
			sg.appendRoom(messageLf);  */  
                    //ClientThread t1 = arrlist.get(0);
                   // ClientThread t2 = arrlist.get(1);
                    
                    if(name.equals("Anonymous1")){
                        ClientThread t1 = arrlist.get(0);
                        t1.writeMsg(message);
                    }
                    else{
                        ClientThread t1 = arrlist.get(1);
                        t1.writeMsg(message);
                    }
                    
		/*for(int i = arrlist.size(); --i >= 0;) {
                    0
                    System.out.println(i);
			ClientThread t3 = arrlist.get(i);
			if(!t3.writeMsg(messageLf)) {
				arrlist.remove(i);
				display("Disconnected Client " + t3.uname + " removed from list.");
			}
		}*/
	}

	synchronized void remove(int id) {
            /*
            while()
            */
		for(int i = 0; i < arrlist.size(); ++i) {
			ClientThread ct = arrlist.get(i);
			if(ct.id == id) {
				arrlist.remove(i);
				return;
			}
		}
	}
	
	
	public static void main(String[] args) {
		int portNumber = 1500;
		switch(args.length) {
			case 1:
				try {
					portNumber = Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					System.out.println(" port number is not valid.");
					System.out.println("Usage is: > java Server [portNumber]");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Usage is: > java Server [portNumber]");
				return;
				
		}
		Server server = new Server(portNumber);
		server.start();
	}

	class ClientThread extends Thread {
	
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
	
		int id;
		String uname,cam;
		String date;

		ClientThread(Socket socket) {
			id = ++Id_unique;
			this.socket = socket;
			System.out.println("Thread trying to create Object Input/Output Streams");
			try
			{
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
				uname = (String) sInput.readObject();
				display(uname + " just connected.");
			}
			catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			catch (ClassNotFoundException e) {
			}
            date = new Date().toString() + "\n";
		}

		public void run() {
		
			for(;;) {
				try {
					cam = (String) sInput.readObject();
				}
				catch (IOException e) {
					display(uname + " Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
				
				String message = cam;

				
					broadcast(message,uname);
				
			}
			remove(id);
			close();
		}
		
		private void close() {
			try {
				if(sOutput != null) sOutput.close();
			}
			catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}

		private boolean writeMsg(String msg) {
			if(!socket.isConnected()) {
				close();
				return false;
			}
			try {
				sOutput.writeObject(msg);
			}
			catch(IOException e) {
				display("Error sending message to " + uname);
				display(e.toString());
			}
			return true;
		}
	}
}

