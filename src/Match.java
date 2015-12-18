
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.TransferHandler;




public class Match extends JFrame {
	
	int letter[][] = new int[15][15];
	int word[][] = new int[15][15];
	 
	JPanel rightPanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JPanel abandonPanel = new JPanel();
	
	JPanel tilePanel = new JPanel();
	JPanel[] tiles = new JPanel[7];
	JLabel[] tilesLabel = new JLabel[7];
	JLabel[] tilesLabelCopy = new JLabel[7];
	
	JPanel scorePanel = new JPanel();
	JLabel p1ScoreLabel;
	JLabel p2ScoreLabel;
	JLabel p3ScoreLabel;
	JLabel p4ScoreLabel;
	
	JPanel gridPanel = new JPanel(); /* leftPanel */
	MyLabel[][] grid = new MyLabel[15][15];
	MyLabel[][] gridCopy = new MyLabel[15][15];
	JPanel[][] squares = new JPanel[15][15];
	
	JButton shuffle, exchange, pass, sendbtn, abandon, cancel;
	int p1Score, p2Score, p3Score, p4Score;
	char player;
	int isTurn;
	int activePlayer;
	
	Client client;
	String msg;
	int totalChar;
	GameState gs;

	Match(Client client, char player) throws IOException {
		gs = new GameState();
		totalChar = 0;
		msg = new String();
		this.player = player; 
		if(this.player == '1') isTurn = 1;
		else isTurn = 0;
		this.client = client;
		p1Score = p2Score = p3Score = p4Score = 0;
		activePlayer = 1;
		setTitle("Scrabble Client " + player);
		setLayout(new BorderLayout());
		initGridPanel();
		add(gridPanel, BorderLayout.WEST);
		initRightPanel();
		add(rightPanel, BorderLayout.EAST);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		while(true) {
			try {
				msg = client.receiveMsg();
				if(msg.equals("exit")) System.exit(0);
				System.out.println(player + "received msg= " + msg);
				
				reDrawGridPanel();
				msg =  "";
				
				msg = client.receiveMsg();
				if(msg.equals("exit")) System.exit(0);
				reDrawScore();
				msg = "";
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	void copyTilesLabel() {
		for(int i=0; i<7; i++) {
			tilesLabelCopy[i].setText(tilesLabel[i].getText());
		}
	}
	
	void copyGrid() {
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				gridCopy[i][j].setText(grid[i][j].getText());
			}
		}
	}
	
	public void reDrawScore() {
		int currentPlayer = 0;
		if(msg.charAt(0) == 'p')  currentPlayer = atoi(msg, 2);
		else currentPlayer = atoi(msg, 0);
		activePlayer = currentPlayer+1;
		if(activePlayer == 5) activePlayer=1;
		
		System.out.println("currentPlayer: " + currentPlayer);
		System.out.println(" activePlayer: " + activePlayer);
		
		if(msg.charAt(0) == 'p') return;
		
		int score = atoi(msg, 2);
		if(currentPlayer == 1) {
			p1Score = score;
			p1ScoreLabel.setText("Player " + currentPlayer + ": " + Integer.toString(score));
			p2ScoreLabel.setText(p2ScoreLabel.getText());
			p2ScoreLabel.repaint();
		}
		if(currentPlayer == 2) {
			p2Score = score;
			p2ScoreLabel.setText("Player " + currentPlayer + ": " + Integer.toString(score));
			p3ScoreLabel.setText(p3ScoreLabel.getText());
			p3ScoreLabel.repaint();
		}
		if(currentPlayer == 3) {
			p3Score = score;
			p3ScoreLabel.setText("Player " + currentPlayer + ": " + Integer.toString(score));
			p4ScoreLabel.setText(p4ScoreLabel.getText());
			p4ScoreLabel.repaint();
		}
		if(currentPlayer == 4) {
			p4Score = score;
			p4ScoreLabel.setText("Player " + currentPlayer + ": " + Integer.toString(score));
			p1ScoreLabel.setText(p1ScoreLabel.getText());
			p1ScoreLabel.repaint();
		}
	}
	
	public int atoi(String s, int idx) {
		int ret=0;
		while(idx<s.length() && (s.charAt(idx)>='0' && s.charAt(idx)<='9')) {
			ret *= 10;
			ret += s.charAt(idx)-'0';
			idx++;
		}
		return ret;
	}
	
	public String parse(String s, int idx) {
		String ret = "";
		while(s.charAt(idx) != '}') {
			ret += s.charAt(idx);
			idx++;
		}
		ret += '}';
		return ret;
	}
	
	void reDrawGridPanel() {
		if(msg.charAt(0) == 'p') return;
		
		gs.move(msg);
		
		int size = atoi(msg, 0), j=0;
		String drop[] = new String[size];
		State states[] = new State[size];
		
		for(int i=1; i<msg.length(); i++) {
			if(msg.charAt(i) == '{') {
				drop[j] = parse(msg, i);
				j++;
			}
		}
		for(int i=0; i<size; i++) {
			states[i] = new State(drop[i]);
		}
		
		
		
		for(int i=0; i<size; i++) {
			final MyLabel ml = grid[states[i].x][states[i].y];
			ml.setText( ""+states[i].c );
		}
		
		
		copyGrid();
		//state.printState();
	}
	
	void initGridPanel() {
		
		initBonus();
		int s = (int)(Toolkit.getDefaultToolkit().getScreenSize().height * .7 );	
		gridPanel.setPreferredSize(new Dimension(s,s));
		gridPanel.setLayout(new GridLayout(15, 15));
		for (int row = 0; row < 15; row++) {
			for (int column = 0; column < 15; column++) {
				squares[row][column] = new JPanel();
				grid[row][column] = new MyLabel(row, column, "  ");
				gridCopy[row][column] = new MyLabel(row, column, "  ");
				
				squares[row][column].setSize(50, 50);
				grid[row][column].setMinimumSize(squares[row][column].getPreferredSize());
				
				//gridCopy[row][column].setText(grid[row][column].getText());
				
				final MyLabel y = grid[row][column];
				y.addMouseListener(new MouseListener(){

					@Override
					public void mouseClicked(MouseEvent e) {
						
						
						y.setText(clickedvalue);
						//System.out.println(y.row +" "+y.column);
						String tmps = new String("{" + y.row + "," + y.column + "," + clickedvalue + "}");
						msg = msg.concat(tmps);
						System.out.println(msg);
						totalChar++;
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						//  Auto-generated method stub
						
					}

					@Override
					public void mouseExited(MouseEvent e) {
						//  Auto-generated method stub
						
					}

					@Override
					public void mousePressed(MouseEvent e) {
						//  Auto-generated method stub
						
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						//  Auto-generated method stub
						
					}
					
				});

				squares[row][column].setBorder(BorderFactory.createEtchedBorder());
				squares[row][column].add(grid[row][column]);
				
				if(letter[row][column] == 2) squares[row][column].setBackground(new Color(0x87CEFA)); // light blue
				else if(letter[row][column] == 3) squares[row][column].setBackground(new Color(0x4169E1)); // blue
				else if(word[row][column] == 2) squares[row][column].setBackground(new Color(0xFFB6C1)); // light pink
				else if(word[row][column] == 3) squares[row][column].setBackground(new Color(0xFF1493)); // pink
				
				gridPanel.add(squares[row][column]);
			}
		}
	}
	
	void recover() {
		for(int i=0; i<7; i++) {
			tilesLabel[i].setText(tilesLabelCopy[i].getText());
			tilesLabel[i].repaint();
		}
		
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				grid[i][j].setText(gridCopy[i][j].getText());
				grid[i][j].repaint();
			}
		}
		
//		for(int i=0; i<15; i++) {
//			for(int j=0; j<15; j++) {
//				final MyLabel l = grid[i][j];
//				l.setText(gridCopy[i][j].getText());
//			}
//		}
//		
//		for(int i=0; i<7; i++) {
//			final JLabel l = tilesLabel[i];
//			l.setText(tilesLabelCopy[i].getText());
//		}
//		
//		for(int i=0; i<15; i++) {
//			for(int j=0; j<15; j++) {
//				grid[i][j] = gridCopy[i][j];
//				grid[i][j].revalidate();
//				grid[i][j].repaint();
//			}
//		}
//		
//		for(int i=0; i<7; i++) {
//			tilesLabel[i] = tilesLabelCopy[i];
//			tilesLabel[i].revalidate();
//			tilesLabel[i].repaint();
//		}
	}
	
	void updateSidePanel() {
		Random randomGenerator = new Random();
		boolean f=true;
		for(int i=0; i<7; i++) {
			if(tilesLabel[i].getText().equals("")) {
				int randomInt = randomGenerator.nextInt(30);
				char randomChar=0;
				if(randomInt < 26) randomChar = ((char) (randomInt+65));
				else if(randomInt == 26) randomChar = 'A';
				else if(randomInt == 27) randomChar = 'E';
				else if(randomInt == 28) randomChar = 'I';
				else if(randomInt == 29) randomChar = 'O';
				if(f) {
					randomChar = 'A';
					f = false;
				}
				tilesLabel[i].setText(String.valueOf(randomChar));
				tilesLabelCopy[i].setText(String.valueOf(randomChar));
			}
		}
	}
	
	String clickedvalue="";
	void initRightPanel() {
		
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		int s = (int)(Toolkit.getDefaultToolkit().getScreenSize().width * .03);
		
		/* scorePanel */
		scorePanel.setLayout(new GridLayout(4,1));
		
		p1ScoreLabel = new JLabel("Player 1: " + p1Score, JLabel.CENTER);		
//		p1ScoreLabel.setForeground(Color.red);
		p2ScoreLabel = new JLabel("Player 2: " + p2Score, JLabel.CENTER);		
		p3ScoreLabel = new JLabel("Player 3: " + p3Score, JLabel.CENTER);		
		p4ScoreLabel = new JLabel("Player 4: " + p4Score, JLabel.CENTER);
		
		scorePanel.add(p1ScoreLabel);
		scorePanel.add(p2ScoreLabel);
		scorePanel.add(p3ScoreLabel);
		scorePanel.add(p4ScoreLabel);
		
		/* tilePanel */
		tilePanel.setLayout(new FlowLayout());
		tilePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
		
		Random randomGenerator = new Random();
		for(int i=0; i<7; i++) {
			tiles[i] = new JPanel();
			int randomInt = randomGenerator.nextInt(30);
			char randomChar=0;
			if(randomInt < 26) randomChar = ((char) (randomInt+65));
			else if(randomInt == 26) randomChar = 'A';
			else if(randomInt == 27) randomChar = 'E';
			else if(randomInt == 28) randomChar = 'I';
			else if(randomInt == 29) randomChar = 'O';
			if(i==0) randomChar='A';
			tilesLabel[i] = new JLabel(String.valueOf(randomChar), JLabel.CENTER);
			tilesLabel[i].setPreferredSize(new Dimension(s, s));
			tilesLabel[i].setBorder(BorderFactory.createEtchedBorder());
			
			//tilesLabelCopy[i].setText(tilesLabel[i].getText());
			tilesLabelCopy[i] = new JLabel(String.valueOf(randomChar), JLabel.CENTER);
			
			final JLabel x = tilesLabel[i];
			x.addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent arg0) {
//					System.out.println(player-48 + " " + activePlayer);
//					if(player-48 != activePlayer) {
//						JOptionPane.showMessageDialog(new JFrame(), "Waiting for other player's move");
//					}
//					else {
						clickedvalue = ""+x.getText();
						x.setText("");
						//x.removeMouseListener(null);
//					}
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					
					
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					
				}
				
			});
			
			tiles[i].add(tilesLabel[i]);
			tilePanel.add(tiles[i]);
		}
		
		/* buttonPanel */
		buttonPanel.setLayout(new FlowLayout());
		shuffle = new JButton("Shuffle");
		//exchange = new JButton("Exchange");
		pass = new JButton("Pass");
		sendbtn = new JButton("Send");
		cancel = new JButton("Cancel");
		buttonPanel.add(shuffle);
		//buttonPanel.add(exchange);
		buttonPanel.add(pass);
		buttonPanel.add(sendbtn);
		buttonPanel.add(cancel);
		
		sendbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        String tmps = Integer.toString(totalChar);
		        msg = tmps + msg;
		        System.out.println("send = " + msg);
		        
		        try {
		        	String s = gs.move(msg);
	        		totalChar = 0;
		        	
					if(s.charAt(0)>='0' && s.charAt(0)<='9') {
						client.sendMsg(msg);
						if(player=='1') {
							p1Score += atoi(s, 0);
							p1ScoreLabel.setText("Player " + player + ": " + Integer.toString(p1Score));
							client.sendMsg(player + " " + p1Score);
						}
						else if(player=='2') {
							p2Score += atoi(s, 0);
							p2ScoreLabel.setText("Player " + player + ": " + Integer.toString(p2Score));
							client.sendMsg(player + " " + p2Score);
						}
						else if(player=='3') {
							p3Score += atoi(s, 0);
							p3ScoreLabel.setText("Player " + player + ": " + Integer.toString(p3Score));
							client.sendMsg(player + " " + p3Score);
						}
						else if(player=='4') {
							p4Score += atoi(s, 0);
							p4ScoreLabel.setText("Player " + player + ": " + Integer.toString(p4Score));
							client.sendMsg(player + " " + p4Score);
						}
						
						msg = "";
						//System.out.println(msg);
						
						updateSidePanel();
		        	}
					else {
						JOptionPane.showMessageDialog(new JFrame(), s);
						msg = "";
						recover();
						
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		pass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        try {
		        	totalChar = 0;
		        	String s = "p " + player;
		        	client.sendMsg(s);
		        	client.sendMsg(s);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				recover();
			}
		});
		
		shuffle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        Random random = new Random();
				for(int i=0; i<7; i++) {
					int a = random.nextInt(7);
					int b = random.nextInt(7);
					JLabel aa = new JLabel(tilesLabel[a].getText());
					tilesLabel[a].setText(tilesLabel[b].getText());
					tilesLabel[b].setText(aa.getText());
					tilesLabel[a].repaint();
					tilesLabel[b].repaint();
				}
			}
		});
		
		
		/* abandonPanel */
		//abandon = new JButton("Abandon");
		//abandonPanel.add(abandon);
		
		/* add all panels in rightPanel */
		rightPanel.add(scorePanel);
		rightPanel.add(tilePanel);
		rightPanel.add(buttonPanel);
		rightPanel.add(abandonPanel);
	}
	
	void initBonus() {
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				letter[i][j] = 1;
			}
		}
		letter[0][3] = 2;
		letter[0][11] = 2;
		letter[1][5] = 3;
		letter[1][9] = 3;
		letter[2][6] = 2;
		letter[2][8] = 2;
		letter[3][0] = 2;
		letter[3][7] = 2;
		letter[3][14] = 2;
		letter[5][1] = 3;
		letter[5][5] = 3;
		letter[5][9] = 3;
		letter[5][13] = 3;
		letter[6][2] = 2;
		letter[6][6] = 2;
		letter[6][8] = 2;
		letter[6][12] = 2;
		letter[7][3] = 2;
		letter[7][11] = 2;
		letter[8][2] = 2;
		letter[8][6] = 2;
		letter[8][8] = 2;
		letter[8][12] = 2;
		letter[9][1] = 3;
		letter[9][5] = 3;
		letter[9][9] = 3;
		letter[9][13] = 3;
		letter[11][0] = 2;
		letter[11][7] = 2;
		letter[11][14] = 2;
		letter[12][6] = 2;
		letter[12][8] = 2;
		letter[13][5] = 3;
		letter[13][9] = 3;
		letter[14][3] = 2;
		letter[14][11] = 2;
		
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				word[i][j] = 1;
			}
		}
		word[0][0] = 3;
		word[0][7] = 3;
		word[0][14] = 3;
		word[1][1] = 2;
		word[1][13] = 2;
		word[2][2] = 2;
		word[2][12] = 2;
		word[3][3] = 2;
		word[3][11] = 2;
		word[4][4] = 2;
		word[4][10] = 2;
		word[7][0] = 3;
		word[7][7] = 2;
		word[7][14] = 3;
		word[10][4] = 2;
		word[10][10] = 2;
		word[11][3] = 2;
		word[11][11] = 2;
		word[12][2] = 2;
		word[12][12] = 2;
		word[13][1] = 2;
		word[13][13] = 2;
		word[14][0] = 3;
		word[14][7] = 3;
		word[14][14] = 3;
	}
	
//	public static void main(String[] args) throws IOException {
//		new Match(new Client(), '1');
//	}
}

class MyLabel extends JLabel {
	int row, column;
	MyLabel(int r, int c, String s) {
		super(s);
		row = r;
		column = c;
	}
	
	public void setText(String s) {
		super.setText(s);
	}
}



