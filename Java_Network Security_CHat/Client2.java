//package chat;


//import static Client.keyPairClient;
import java.net.*;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.KeyAgreement;

public class Client2  {

        private boolean gotKey = false;
	private ObjectInputStream socIn;
	private ObjectOutputStream socOut;
	private Socket socket;
       String ts;
	 Conversation c;
          static KeyPair keyPairClient;
	private static boolean ka = false,kaok = false;
	PublicKey otherClientPublicKey;
         public byte[] sharedKeyBytes;
	private String server, username;
	private int port;

	Client2(String server, int port, String username) {
				this.server = server;
		this.port = port;
		this.username = username;
          

	}

	public boolean start() {
		
		try {
			socket = new Socket(server, port);
		} 
		
		catch(Exception ec) {
	
			return false;
		}
		
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
	
		try
		{
			socIn  = new ObjectInputStream(socket.getInputStream());
			socOut = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
	
			return false;
		}

		
		new ServerListen().start();
		try
		{
			socOut.writeObject(username);
		}
		catch (IOException eIO) {
		
			disconnect();
			return false;
		}
		
		return true;
	}
        //this function 
void sendKey(String pubKey) throws IOException{
    socOut.writeObject(pubKey);
}
	void sendMessage(String msg) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, IOException {

            socOut.writeObject(c.encryptString(msg));
	}

	private void disconnect() {
		try { 
			if(socIn != null) socIn.close();
		}
		catch(Exception e) {}
		try {
			if(socOut != null) socOut.close();
		}
		catch(Exception e) {}
        try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {}
		
			
	}
//Once the public key of other client is received, this function generate the secret key using its private key. 
void test() throws NoSuchAlgorithmException, Exception{
	
	 ECDH object = new ECDH();
            sharedKeyBytes = object.generateSharedKey(keyPairClient.getPrivate(), otherClientPublicKey);
            System.out.println("Size of key is " + sharedKeyBytes.length);
            System.err.println("Key is \n"+sharedKeyBytes);
            c.generateKeyTemp(sharedKeyBytes);
		
      
}
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, IOException, Exception {

		int p_Num = 1500;
		String s_Add = "localhost";
		String name = "Anonymous1";
                 ECDH obj = new ECDH();
                keyPairClient = obj.getKeyPair();
                if(args.length == 3){
                    s_Add = args[2];
                }
                else if(args.length ==2){
        		try {
                            p_Num = Integer.parseInt(args[1]);
        		}
			catch(Exception e) {
                            System.out.println("Invalid port number.");
                            System.out.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
                            return;
			}
                }
                else if(args.length == 1){
                    name = args[0];
                }
                else if(args.length == 0){
                    
                }
                else
                {
             		System.out.println("Usage is: > java Client [username] [portNumber] {serverAddress]");
			return;
                }
		Client2 client = new Client2(s_Add, p_Num, name);

		if(!client.start())
			return;
		
		Scanner scan = new Scanner(System.in);
                String startit = scan.nextLine();
             // sends kaecdh-secp224r1+nocert+aes128/cbc if it initiates the converstion
                if(ka == false)
                {
                    System.out.println("sending.....:kaecdh-secp224r1+nocert+aes128/cbc");
                    client.sendKey(":kaecdh-secp224r1+nocert+aes128/cbc");
                    ka = true;
                }
                
                else if(ka == true && kaok == false){
                    kaok = true;
                     System.out.println("sending.....:kaokecdh-secp224r1+nocert+aes128/cbc");
                    client.sendKey(":kaokecdh-secp224r1+nocert+aes128/cbc");

                    client.sendKey(Base64.getEncoder().encodeToString(keyPairClient.getPublic().getEncoded()));
                }
		
		
		for(;;) {
			System.out.print("> ");
		
			String msg = scan.nextLine();
		
			if(msg.equalsIgnoreCase("LOGOUT")) {
		
                                client.sendMessage(msg);
		
				break;
			}
			
			else {	
			
                                client.sendMessage(msg);
			}
		}
		
		client.disconnect();	
	}
//Listens to message coming from server.
        
        
	class ServerListen extends Thread {

		public void run() {
                    c = new Conversation();
			for(;;) {
				try {
                                    
				String msg = (String) socIn.readObject();
        // sends and receives data before the public key is exchanged.                                  
				if(gotKey == false)
				{

                                     if(msg.equals(":kaokecdh-secp224r1+nocert+aes128/cbc")){
                                         System.out.println("received.....:kaokecdh-secp224r1+nocert+aes128/cbc");
                                         kaok = true;
                                         sendKey(Base64.getEncoder().encodeToString(keyPairClient.getPublic().getEncoded()));
                                            }
                                     else if(msg.equals(":kaecdh-secp224r1+nocert+aes128/cbc")){
                                          System.out.println("received.....:kaecdh-secp224r1+nocert+aes128/cbc");
                                          ka = true;
                                            }
                                     else {
                                          X509EncodedKeySpec  pubKey= new X509EncodedKeySpec(Base64.getDecoder().decode(msg));
                                          KeyFactory factory = KeyFactory.getInstance("EC");
                                          otherClientPublicKey = factory.generatePublic(pubKey);
                                          gotKey = true;
					  test();
                                        }
					}
                                // Communication starts here as the keys are exchanged
					else
					{
					System.out.println("Message from Client1: " + c.decryptString(msg));
					System.out.print("> ");
					}
	
				}
				catch(IOException e) {
	
					break;
				}
	
				catch(ClassNotFoundException e2) {

                                } catch (NoSuchAlgorithmException ex) { 
                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (NoSuchPaddingException ex) { 
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvalidKeyException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalBlockSizeException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (BadPaddingException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvalidAlgorithmParameterException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvalidKeySpecException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        } 
			}
		}
	}
}
