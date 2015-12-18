
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;




public class Home extends JFrame {
	
	JPanel welcomePanel;
	JPanel waitingPanel;
	JPanel buttonPanel;
	
	JLabel welcomeLabel;
	JLabel waitingLabel;
	JButton findMatch, exit; 
	//JTextArea ta;
	
	//Socket clientSocket;
	Client client;
	Match match;
	
	String receiveMsg() throws IOException {
       
            DataInputStream in = new DataInputStream(client.getSocket().getInputStream());
            String msg = in.readUTF();
            return msg;
    }
	
	Home() {
		client  = new Client();
		//ta =  new JTextArea();
		welcomePanel = new JPanel();
		waitingPanel = new JPanel();
		buttonPanel = new JPanel();
		setTitle("Scrabble home");
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(500, 300));
		//welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
		
		welcomeLabel = new JLabel("Welcome to scrabble game", JLabel.CENTER);
		welcomePanel.add(welcomeLabel);
		//ta.setEditable(true);
		//welcomePanel.add(ta);
		
		waitingLabel = new JLabel("<html><font color='red'>Waiting for other players...</font></html>");
		waitingLabel.setVisible(false);
		welcomePanel.add(waitingLabel);
		waitingPanel.add(waitingLabel);
		
		findMatch = new JButton("Find Match");
		exit = new JButton("Exit");
		exit.setPreferredSize(findMatch.getPreferredSize() );
		buttonPanel.add(findMatch);
		buttonPanel.add(exit);
		
		this.getContentPane().add(welcomePanel);
		this.getContentPane().add(waitingPanel);
		this.getContentPane().add(buttonPanel);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		/* button actions */
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        System.exit(0);
		    }
		});
		
		findMatch.setActionCommand("find match");
		findMatch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("find match")) {
					try {
						//String ip = ta.getText();
						client.setConnection("localhost", 2300);
						findMatch.setEnabled(false);
						waitingLabel.setVisible(true);
						
						new Thread() {
				            @Override
				            public void run() {
				                try {
				                    String s = receiveMsg();
				                    if(s.charAt(0)=='o' && s.charAt(1)=='k') {
				                    	setVisible(false);
				                    	match = new Match(client, s.charAt(2));
				                    }
				                } catch (IOException ex) {
				                    System.exit(0);
				                }
				            }
				        }.start();
						
					} catch (Exception ex) {
						ex.printStackTrace();
			            System.exit(0);
			        }
				}
			}
		});
	}
	
	
	public static void main(String[] args) {
		
		new Home();
	}
}


