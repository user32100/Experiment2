import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
	private String message;
	
	private String mode;
	private String algorithm;
	
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
			    
		connectButton.addActionListener(this);
		disconnectButton.addActionListener(this);


		//JPanel connectPanel = new JPanel(new FlowLayout());
			    
		ButtonGroup buttonGroup3 = new ButtonGroup();
		buttonGroup3.add(connectButton);
		buttonGroup3.add(disconnectButton);
			    
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
		//aesButton.setSelected(true);
			    
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
		//cbcButton.setSelected(true);
			    
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
		    //String message = Crypt.Encrypt(text, key, algorithm, mode);
		    cipherText.setText(text); 	// message olacak burası
		    sendButton.setEnabled(true);
		}
		
		if (e.getActionCommand().equals(SEND_STR)) {		// SEND BUTTON
			String text = cipherText.getText();
		    cipherText.setText("");
		    chatText.append(text +"\n");
		    sendButton.setEnabled(false);
		}
		
		if (e.getActionCommand().equals(AES_STR)) {		// AES BUTTON
			algorithm = AES_STR;
			System.out.println(algorithm);
		}
		
		if (e.getActionCommand().equals(DES_STR)) {		// DES BUTTON
			algorithm = DES_STR;
		}
		
		if (e.getActionCommand().equals(CBC_STR)) {		// CBC BUTTON
			mode = CBC_STR;
			System.out.println(mode);
		}
		
		if (e.getActionCommand().equals(OFB_STR)) {		// OFB BUTTON
			mode = OFB_STR;
		}
		

    }
	
	public void append(String str) {
		chatText.append(str);
	}
	
	public void updateGUI() {
		labelConnected.setText("Connected: " + username);
		connectButton.setEnabled(false);
		disconnectButton.setEnabled(true);
	}
	
	
	public static void main(String args[]) {
		Client client = new Client();
		client.setGUI();
	    client.updateGUI();
	    System.out.println(client.getUsername());
	}
	

}
