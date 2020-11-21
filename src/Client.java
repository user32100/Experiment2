import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class Client implements ActionListener{
	
	private static final String MESSENGER_STR = "Crypto Messenger";
	private static final String AES_STR = "AES";
	private static final String DES_STR = "DES";
	private static final String CBC_STR = "CBC";
	private static final String OFB_STR = "OFB";
	private static final String ENC_STR = "Encrypt";
	private static final String SEND_STR = "Send";
	
	

	private JFrame frame;
	private JLabel labelConnected;
	
	private JTextArea chatText;
	private JTextArea plainText;
	private JTextArea cipherText;
	
	private JButton encryptButton;
    private JButton sendButton;
    
    private JButton connectButton;
    private JButton disconnectButton;
    
    private JRadioButton aesButton;
    private JRadioButton desButton;
    
    private JRadioButton cbcButton;
    private JRadioButton ofbButton;
    
	private String username;
	private String connectStr;
	//private String message;
	
	private String mode = CBC_STR;		// initially CBC
	private String algorithm = AES_STR;	// initially AES
	
	private String AesKey, DesKey;
	private String AesIV, DesIV;

	private Scanner in;
	private PrintWriter out;
	private static Socket socket;
	
	public Client() {

	}
	
	public void setGUI() {
		//JFrame frame = new JFrame(MESSENGER_STR);
		frame = new JFrame(MESSENGER_STR);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  
		//frame.setSize(650,700);
		  
		JLabel label = new JLabel("Server");
			    
		//Border blackline = BorderFactory.createLineBorder(Color.black);
		Border grayline = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY);	// only bottom border visible
		Border grayline2 = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY);	// only top border visible
			    
			    
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		topPanel.setBorder(grayline);
		topPanel.add(label);
			    
		connectStr = "Not connected"; 
		labelConnected = new JLabel(connectStr);
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.setBorder(grayline2);
		bottomPanel.add(labelConnected);
		// add not connected
			    
			
		connectButton = new JButton("Connect");
		disconnectButton = new JButton("Disconnect");
		disconnectButton.setEnabled(false);
			    
		disconnectButton.setActionCommand("Disconnect");
		
		connectButton.addActionListener(this);
		disconnectButton.addActionListener(this);


		//JPanel connectPanel = new JPanel(new FlowLayout());
			    
		//ButtonGroup buttonGroup3 = new ButtonGroup();
		//buttonGroup3.add(connectButton);
		//buttonGroup3.add(disconnectButton);
			    
		//connectPanel.add(connectButton);
		//connectPanel.add(disconnectButton);

			    
			    
			    
		JPanel methodPanel = new JPanel();
		TitledBorder title1;
		title1 = BorderFactory.createTitledBorder("Method");
		methodPanel.setBorder(title1);
		//methodPanel.setPreferredSize(new Dimension(150, 50));
			    
			    
		//String AesString = "AES";
		aesButton = new JRadioButton(AES_STR);
		aesButton.setActionCommand(AES_STR);
		aesButton.setSelected(true);
			    
		//String DesString = "DES";
		desButton = new JRadioButton(DES_STR);
		desButton.setActionCommand(DES_STR);
		//desButton.setSelected(true);
			    
		aesButton.addActionListener(this);
		desButton.addActionListener(this);
			    
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(aesButton);
		buttonGroup1.add(desButton);
			    
		methodPanel.add(aesButton);
		methodPanel.add(desButton);
			    
			    
		JPanel modePanel = new JPanel();
		TitledBorder title2;
		title2 = BorderFactory.createTitledBorder("Mode");
		modePanel.setBorder(title2);
		//modePanel.setPreferredSize(new Dimension(150, 50));

			    
		//String cbcString = "CBC";
		cbcButton = new JRadioButton(CBC_STR);
		cbcButton.setMnemonic(KeyEvent.VK_B);
		cbcButton.setActionCommand(CBC_STR);
		cbcButton.setSelected(true);
			    
		//String ofbString = "OFB";
		ofbButton = new JRadioButton(OFB_STR);
		ofbButton.setMnemonic(KeyEvent.VK_B);
		ofbButton.setActionCommand(OFB_STR);
		//ofbButton.setSelected(true);
			    
		cbcButton.addActionListener(this);
		ofbButton.addActionListener(this);
			    
		ButtonGroup buttonGroup2 = new ButtonGroup();
		buttonGroup2.add(cbcButton);
		buttonGroup2.add(ofbButton);
			    
		modePanel.add(cbcButton);
		modePanel.add(ofbButton);
			    
			    
			    
		JPanel upperPanel = new JPanel();
		//upperPanel.setPreferredSize(new Dimension(700, 75));
			    
		upperPanel.add(connectButton);
		upperPanel.add(disconnectButton);
		upperPanel.add(methodPanel);
		upperPanel.add(modePanel);
			    
		upperPanel.setBorder(grayline);
			    
			    
		chatText = new JTextArea(15, 50);
		chatText.setBorder(grayline);
		chatText.setEditable(false);
		//JPanel chatPanel = new JPanel();
		//chatPanel.add(chatText);
			    
			    
			    
		plainText = new JTextArea(5, 25);
		cipherText = new JTextArea(5, 25);
			    
			    
		TitledBorder title3;
		title3 = BorderFactory.createTitledBorder("Text");
		plainText.setBorder(title3);
			    
		TitledBorder title4;
		title4 = BorderFactory.createTitledBorder("Crypted Text");
		cipherText.setBorder(title4);
		cipherText.setEditable(false);
			    
			    
			    
		encryptButton = new JButton(ENC_STR);
		sendButton = new JButton(SEND_STR);
			    
		encryptButton.setActionCommand(ENC_STR);
		sendButton.setActionCommand(SEND_STR);
			    
		sendButton.setEnabled(false);
		encryptButton.addActionListener(this);
		sendButton.addActionListener(this);
			    
		JPanel lowerPanel = new JPanel();
		//lowerPanel.setPreferredSize(new Dimension(600, 75));
			    
		lowerPanel.add(plainText);
		lowerPanel.add(cipherText);
		lowerPanel.add(encryptButton);
		lowerPanel.add(sendButton);
			    
		//lowerPanel.setBorder(grayline);
			    
			    
		JPanel altogether = new JPanel();
		altogether.setLayout(new BoxLayout(altogether, BoxLayout.Y_AXIS));
		altogether.setPreferredSize(new Dimension(700, 700));
			    
		//altogether.add(upperPanel, BorderLayout.NORTH);
		altogether.add(upperPanel);
		altogether.add(chatText);
		//altogether.add(chatPanel, BorderLayout.CENTER);
		altogether.add(lowerPanel, BorderLayout.SOUTH);
		//altogether.setBackground(Color.BLUE);
			    
			    
			    
		//frame.getContentPane().add(inputPane);
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);
		frame.getContentPane().add(altogether);
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
			    
		// input prompt
		username = JOptionPane.showInputDialog(frame, "Enter user name:");   
		//System.out.println(username);    
	}
	
	
	
	public void setUsername(String name) {
		username = name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void append(String str, JTextArea chatText) {
		chatText.append(str);
	}
	
	public JLabel getConnectedLabel() {
		return labelConnected;
	}
	
	public void actionPerformed(ActionEvent e)
    {
		if (e.getActionCommand().equals(ENC_STR)) {		// ENCRYPT BUTTON
			String text = plainText.getText();
		    plainText.setText("");
		    String key = algorithm.equals(AES_STR) ? AesKey : DesKey;
		    String iv = algorithm.equals(AES_STR) ? AesIV : DesIV;
		    String message = null;
		    try {
		    	/*System.out.println(algorithm + " enc" );
		    	System.out.println(mode +  " enc");
		    	System.out.println(key + " enc");
		    	System.out.println(iv + " enc"); */
		    	//System.out.println(text); 
		    	
				message = Crypt.Encrypt(text, key, algorithm, mode, iv);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
					| UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException
					| InvalidAlgorithmParameterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    
			cipherText.setText(message);
		    sendButton.setEnabled(true);
		}
		
		if (e.getActionCommand().equals(SEND_STR)) {		// SEND BUTTON
			String message = cipherText.getText();
		    out.println(message);
		    sendButton.setEnabled(false);
		    
		    // clear text and crypted text areas
		    plainText.setText("");
		    cipherText.setText("");
		}
		
		if (e.getActionCommand().equals(AES_STR)) {		// AES BUTTON
			algorithm = AES_STR;
			// System.out.println(algorithm);
		}
		
		if (e.getActionCommand().equals(DES_STR)) {		// DES BUTTON
			algorithm = DES_STR;
			// System.out.println(algorithm);
		}
		
		if (e.getActionCommand().equals(CBC_STR)) {		// CBC BUTTON
			mode = CBC_STR;
			// System.out.println(mode);
		}
		
		if (e.getActionCommand().equals(OFB_STR)) {		// OFB BUTTON
			mode = OFB_STR;
			// System.out.println(mode);
		}
		
		if (e.getActionCommand().equals("Disconnect")) {		// DISCONNECT BUTTON
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		

    }

	private void run() throws IOException {
		try {
			InetAddress serverAddress = null;
			try {
				serverAddress = InetAddress.getLocalHost();	// ip
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			socket = new Socket(serverAddress, 59001);
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);

			out.println(username);
			
			AesKey = in.nextLine();
			DesKey = in.nextLine();
			
			AesIV = in.nextLine();
			
			
			DesIV = in.nextLine();
			//System.out.println(in.nextLine());
			/*System.out.println("I got DesKey: " + DesKey);
			System.out.println("I got AesKey: " + AesKey);
			System.out.println("I got AesIv: " + AesIV);
			System.out.println("I got DesIv: " + DesIV); */
			
			while (in.hasNextLine()) {
				
				//String textString = in.nextLine();
				
				// 1 - AES KEY
				// 2 - DES KEY
				// 3 - AES MODE IV
				// 4 - DES MODE IV
				/*
				AesKey = in.nextLine();
				DesKey = in.nextLine();
				
				AesIV = in.nextLine();
				
				System.out.println("I got AesKey: " + AesKey);
				DesIV = in.nextLine();
				//System.out.println(in.nextLine());
				System.out.println("I got DesKey: " + DesKey);
				
				System.out.println("I got AesIv: " + AesIV);
				System.out.println("I got DesIv: " + DesIV);
				*/
				
				// NEXT WILL COME AS name + message
				
				String message = in.nextLine();
				
				String[] list = message.split(" ");
				
				try {
					append(list[1], list[0]);
				} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
						| UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException
						| InvalidAlgorithmParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// var key = in.nextLine();		// get key
				// var iv = in.nextLine();		// get iv
				// here append message
			}
		} finally {
			frame.setVisible(false);
			frame.dispose();
			socket.close();
		}
	}


	public void append(String cipherText, String name) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		chatText.append(cipherText + "\n");
		String key = algorithm.equals(AES_STR) ? AesKey : DesKey;
	    String iv = algorithm.equals(AES_STR) ? AesIV : DesIV;
		//
	    
	    /*System.out.println(algorithm + " dec");
	    System.out.println(mode + " dec");
	    System.out.println(key + " key dec");
	    System.out.println(iv + " key iv"); */
	    
	    String plainText =  Crypt.Decrypt(cipherText, key, algorithm, mode, iv);
		chatText.append("<" + name + ">" + " " + plainText + "\n");
	}
	
	public void updateGUI() {
		labelConnected.setText("Connected: " + username);
		connectButton.setEnabled(false);
		disconnectButton.setEnabled(true);
	}
	
	
	public static void main(String args[]) throws IOException {
		Client client = new Client();
		client.setGUI();
	    client.updateGUI();
		client.run();
	    //System.out.println(client.getUsername());
	}
	

}
