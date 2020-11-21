
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Set;
import java.util.Base64;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


public class Server {

    // All client names, so we can check for duplicates upon registration.
    private static Set<String> names = new HashSet<>();

    // The set of all the print writers for all the clients, used for broadcast.
    private static Set<PrintWriter> writers = new HashSet<>();

    private static String AesKey, DesKey;
    private static String AesIv, DesIv;
    
    //private static BufferedWriter log;
    private static PrintWriter log;
    
    
    public static void generateKeyandIV() throws NoSuchAlgorithmException, UnsupportedEncodingException {
    	int ivSize = 16;
    	int ivSize2 = 8;
        
        // create new key for AES
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
        // get base64 encoded version of the key
        AesKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        
        // create new key for DES
        SecretKey secretKey2 = KeyGenerator.getInstance("DES").generateKey();
        // get base64 encoded version of the key
        DesKey = Base64.getEncoder().encodeToString(secretKey2.getEncoded());
        
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
     
        AesIv = random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
          .limit(ivSize)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
        
        DesIv = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(ivSize2)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        
        
        //AesIv = "7cVgr5cbdCZVw5WY";
        //DesIv = "8vBht6vnfVXBe6EU";
        
        System.out.println("Random key for AES: " + AesKey);
        System.out.println("Random key for DES: " + DesKey);
        
        System.out.println("Random init vector for AES: " + AesIv);
        System.out.println("Random init vector for DES: " + DesIv);
        
        log.println("Random key for AES: " + AesKey);
        log.println("Random key for DES: " + DesKey);
        
        log.println("Random init vector for AES: " + AesIv);
        log.println("Random init vector for DES: " + DesIv);
        log.flush();
        
    }
    
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException  {
    	
        //System.out.println("The chat server is running...");
    	//File outputFile = new File("output.txt"); 
    	FileWriter file = null;
		try {
			file = new FileWriter("log.txt",true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	log = new PrintWriter(file);
        generateKeyandIV();
        var pool = Executors.newFixedThreadPool(500);
        int port = 59001; 
        //int portNumber = detectPort(port);
        try (var listener = new ServerSocket(port)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
            
        }
    }

    /**
     * The client handler task.
     */
    private static class Handler implements Runnable {
        private String name;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;

        
        public Handler(Socket socket) {
            this.socket = socket;
        }

        
        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                // Keep requesting a name until we get a unique one.
                while (true) {
                    //out.println("HOŞGELDİNİZ");
                	out.println(AesKey);	// AES KEY
                	out.println(DesKey);	// DES KEY
                	out.println(AesIv);	// AES IV
                	out.println(DesIv);	// DES IV
                    name = in.nextLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!name.isBlank() && !names.contains(name)) {
                            names.add(name);
                            break;
                        }
                    }
                }

                
                writers.add(out);

                // Accept messages from this client and broadcast them.
                while (true) {
                    String input = in.nextLine();
                    
                    System.out.println("<" + name + ">" + " " + input);
                    log.println("<" + name + ">" + " " + input);
                    log.flush();
                    
                    // System.out.println("new message on server side");
                    //if (input.toLowerCase().startsWith("/quit")) {
                    //    return;
                    //}
                    if (writers.size() == 0)
                    {
                    	return;/*
                    	try {
    						//listener.close();
    						return;
    					} catch (IOException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}*/
                    }
                    for (PrintWriter writer : writers) {
                        writer.println(name + " " + input);
                    }
                }
                
            } catch (Exception e) {
                //System.out.println(e);
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    //System.out.println(name + " is leaving");
                    names.remove(name);
                    for (PrintWriter writer : writers) {
                        // writer.println("MESSAGE " + name + " has left");
                    }
                }
                
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}

