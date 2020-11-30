/**
 * This program is a rudimentary demonstration of Swing GUI programming.
 * Note, the default layout manager for JFrames is the border layout. This
 * enables us to position containers using the coordinates South and Center.
 *
 * Usage:
 *	java ChatScreen
 *
 * When the user enters text in the textfield, it is displayed backwards 
 * in the display area.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ChatScreen extends JFrame implements ActionListener, KeyListener
{
	private JButton sendButton;
	private JButton exitButton;
	private JTextField sendText;
	private JTextArea displayArea;
    public static final int PORT = 10000;

	
	public ChatScreen() {
		/**
		 * a panel used for placing components
		 */
		JPanel p = new JPanel();

		Border etched = BorderFactory.createEtchedBorder();
		Border titled = BorderFactory.createTitledBorder(etched, "Enter Message Here ...");
		p.setBorder(titled);

		/**
		 * set up all the components
		 */
		sendText = new JTextField(30);
		sendButton = new JButton("Send");
		exitButton = new JButton("Exit");

		/**
		 * register the listeners for the different button clicks
		 */
        sendText.addKeyListener(this);
		sendButton.addActionListener(this);
		exitButton.addActionListener(this);

		/**
		 * add the components to the panel
		 */
		p.add(sendText);
		p.add(sendButton);
		p.add(exitButton);

		/**
		 * add the panel to the "south" end of the container
		 */
		getContentPane().add(p,"South");

		/**
		 * add the text area for displaying output. Associate
		 * a scrollbar with this text area. Note we add the scrollpane
		 * to the container, not the text area
		 */
		displayArea = new JTextArea(15,40);
		displayArea.setEditable(false);
		displayArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

		JScrollPane scrollPane = new JScrollPane(displayArea);
		getContentPane().add(scrollPane,"Center");

		/**
		 * set the title and size of the frame
		 */
		setTitle("GUI Demo");
		pack();
 
		setVisible(true);
		sendText.requestFocus();

		/** anonymous inner class to handle window closing events */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		} );

	}
        
        /**
         * This gets the text the user entered and outputs it
         * in the display area.
         * @param message2
         */
        public void sendMessage(Boolean messageType) {
            String message = sendText.getText().trim();
			StringBuffer buffer = new StringBuffer(message.length());
			// BufferedOutputStream os = new BufferedOutputStream(client.getOutputStream());

			if(messageType){
				os.write(("PUB " + args[1] + " " + message + "\r\n").getBytes()); //here write protocol for socket
				os.flush(); 
			}
			else if(!messageType){

			}
			// now reverse it
			for (int i = 0; i < message.length();i++)
            // for (int i = message.length()-1; i >= 0; i--)
                buffer.append(message.charAt(i));

            displayArea.append(buffer.toString() + "\n");

            sendText.setText("");
            sendText.requestFocus();
		}
		/**
		* Displays a message
		*/
		public void displayMessage(String message) {
			displayArea.append(message + "\n");
		}


	/**
	 * This method responds to action events .... i.e. button clicks
         * and fulfills the contract of the ActionListener interface.
	 */

	 //onclick send message
	 //PUB  <FROMuser>  <TEXT>
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		
		if (source == sendButton) 
			sendMessage(true); //instead - package into protocl and write to server
			//os.write(("PUB " + args[1] + " " + message + "\r\n").getBytes()); //here write protocol for socket
			//os.flush(); 
		else if (source == exitButton)
			//send exit msg
			//sendMessage(false);
			//os.write(("LOGOFF " + args[1] + "\r\n").getBytes()); //here write protocol for socket
			//os.flush(); 
			System.exit(0);
	}
        
        /**
         * These methods responds to keystroke events and fulfills
         * the contract of the KeyListener interface.
         */
        
        /**
         * This is invoked when the user presses
         * the ENTER key.
         */
        public void keyPressed(KeyEvent e) { 
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
			sendMessage(true); //instead - package into protocl and write to socket
			
        }
        
        /** Not implemented */
        public void keyReleased(KeyEvent e) { }
         
        /** Not implemented */
        public void keyTyped(KeyEvent e) {  }
        

	public static void main(String[] args) throws java.io.IOException {
		JFrame win = new ChatScreen();
		
		Socket server = null;
        BufferedReader fromServer = null;
    	BufferedOutputStream os = null;

		DataOutputStream toServer = null;
		String username = args[1];
		Scanner scanner;
		boolean exitFlag = false;
		String message = null;
		int count = 0;
        
        try {
			server = new Socket(args[0],PORT);
			os=new BufferedOutputStream(server.getOutputStream());
			
			fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));

			//JOIN <username>
			os.write(("JOIN " + args[1] + "\r\n").getBytes()); //here write protocol for socket
			os.flush();          
			ChatScreen window = new ChatScreen();
			Thread ReaderThread = new Thread(new ReaderThread(server, window));

			ReaderThread.start();
		

			

			
			// String line;
			// line = fromServer.readLine();
			// System.out.println("server: " + line);

			
		
		} catch (java.io.IOException ioe) {
			String line = "unknown host";
			System.out.println(line);
			System.err.println(ioe);
		}
		finally {
			if (server != null)
				server.close();
		}
	}
}
