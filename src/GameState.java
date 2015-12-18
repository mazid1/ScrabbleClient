import java.io.IOException;
import java.util.*;


public class GameState {
	public char board[][];
	public int point[], letter[][], word[][], total;
	public int ROW_MAJOR=0, COLUMN_MAJOR=1, SINGLE=2;
	public InputReader ir;
	public boolean visit[][] = new boolean[15][15];
	public boolean here[][] = new boolean[15][15];
	public int fx[] = {+0, +0, +1, -1};
	public int fy[] = {-1, +1, +0, +0};
	
	public GameState() throws IOException {
		ir = new InputReader();
		
		board = new char[15][15];
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				board[i][j] = '*';
			}
		}
		
		point = new int[] { 1,  3,  3,  2,  1, 
							4,  2,  4,  1,  8,
							5,  1,  3,  1,  1,
							3, 10,  1,  1,  1,
							1,  4,  4,  8,  4, 10 };
		
		letter = new int[15][15];
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
		
		word = new int[15][15];
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
	
	public boolean BinarySearch(String s) {
		int low=0, high=ir.key-1, mid;
		while(low <= high) {
			mid = (low+high) / 2;
			if(s.equals(ir.dic[mid])) return true;
			else if(s.compareTo(ir.dic[mid]) > 0) low = mid+1;
			else high = mid-1;
		}
		return false;
	}
	
	public void printBoard() {
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println("");
		}
	}
	
	public int atoi(String s, int idx) {
		int ret=0;
		while(s.charAt(idx)>='0' && s.charAt(idx)<='9') {
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
	
	public boolean isUsed(State states[], int size) {
		for(int i=0; i<size; i++) {
			if(board[ states[i].x ][ states[i].y ] != '*') return true;
		}
		return false;
	}
	
	public int major(State states[], int size) {
		if(size == 1) return SINGLE;
		boolean rowCheck=true, columnCheck=true;
		for(int i=1; i<size; i++) {
			if(states[i].x != states[0].x) {
				rowCheck = false;
				break;
			}
		}
		for(int i=1; i<size; i++) {
			if(states[i].y != states[0].y) {
				columnCheck = false;
				break;
			}
		}
		if(rowCheck) return ROW_MAJOR;
		else if(columnCheck) return COLUMN_MAJOR;
		else return -1;
	}
	
	public boolean isContiguousRow(State states[], int size) {
		int mn=20, mnPos=0;
		boolean now[][] = new boolean[15][15];
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				visit[i][j] = false;
				now[i][j] = false;
			}
		}
		for(int i=0; i<size; i++) {
			if(states[i].y < mn) {
				mn = states[i].y;
				mnPos = i;
			}
			now[ states[i].x ][ states[i].y ] = true;
		}
		int x = states[mnPos].x, y = states[mnPos].y;
		while(true) {
			if(y<15 && (board[x][y] != '*' || now[x][y] == true)) {
				visit[x][y] = true;
				y++;
			}
			else break;
		}
		for(int i=0; i<size; i++) {
			if(!visit[ states[i].x ][ states[i].y ]) return false;
		}
		return true;
	}
	
	public boolean isContiguousColumn(State states[], int size) {
		int mn=20, mnPos=0;
		boolean now[][] = new boolean[15][15];
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				visit[i][j] = false;
				now[i][j] = false;
			}
		}
		for(int i=0; i<size; i++) {
			if(states[i].x < mn) {
				mn = states[i].x;
				mnPos = i;
			}
			now[ states[i].x ][ states[i].y ] = true;
		}
		int x = states[mnPos].x, y = states[mnPos].y;
		while(true) {
			if(x<15 && (board[x][y] != '*' || now[x][y])) {
				visit[x][y] = true;
				x++;
			}
			else break;
		}
		for(int i=0; i<size; i++) {
			if(!visit[ states[i].x ][ states[i].y ]) return false;
		}
		return true;
	}
	
	public boolean isContiguous(State states[], int size, int m) {
		if(m == ROW_MAJOR) return isContiguousRow(states, size);
		else if(m == COLUMN_MAJOR) return isContiguousColumn(states, size);
		else return isContiguousRow(states, size) | isContiguousColumn(states, size);
	}
	
	public boolean isValidFirstMove(State states[], int size) {
		for(int i=0; i<size; i++) {
			if(states[i].x == 7 && states[i].y == 7) return true;
		}
		return false;
	}
	
	public boolean isValid(int x, int y) {
		if(x>=0 && x<15 && y>=0 && y<15) return true;
		else return false;
	}
	
	public void dfs(int x, int y) {
		visit[x][y] = true;
		for(int i=0; i<4; i++) {
			int xx = x+fx[i];
			int yy = y+fy[i];
			if(isValid(xx, yy) && !visit[xx][yy] && 
			   (board[xx][yy]!='*' || here[xx][yy])) dfs(xx, yy);
		}
	}
	
	public boolean isConnectedComponent(State states[], int size) {
		for(int i=0; i<size; i++) {
			if(states[i].x == 7 && states[i].y == 7) return true;
		}
		
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				visit[i][j] = false;
				here[i][j] = false;
			}
		}
		for(int i=0; i<size; i++) {
			here[ states[i].x ][ states[i].y ] = true;
		}
		dfs(states[0].x, states[0].y);
		
		int cnt=0;
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				if(visit[i][j]) cnt++;
			}
		}
		if(cnt>size) return true;
		else return false;
	}
	
	public String generateRow(State states[], int size, int idx) {
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				visit[i][j] = false;
			}
		}
		for(int i=0; i<size; i++) {
			visit[ states[i].x ][ states[i].y ] = true;
		}
		
		int x=states[idx].x, y=states[idx].y;
		while(y>=0 && (board[x][y]!='*' || visit[x][y])) y--;
		y++;
		String ret="";
		
		char thisMove[][] = new char[15][15];
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				thisMove[i][j] = '*';
			}
		}
		for(int i=0; i<size; i++) {
			thisMove[ states[i].x ][ states[i].y ] = states[i].c; 
		}
		
		ArrayList<State> path = new ArrayList<State>();
		while(true) {
			if(y>=15 || (board[x][y]=='*' && !visit[x][y])) break;
			if(board[x][y] != '*') {
				ret += board[x][y];
				path.add(new State(x, y, board[x][y]));
			}
			else {
				ret += thisMove[x][y];
				path.add(new State(x, y, thisMove[x][y]));
			}
			y++;
		}
		if(BinarySearch(ret)) {
			int add=0, wordMultiply=1;
			for(int i=0; i<path.size(); i++) {
				wordMultiply *= word[ path.get(i).x ][ path.get(i).y ];
				if(word[ path.get(i).x ][ path.get(i).y ] > 1) {
					word[ path.get(i).x ][ path.get(i).y ] = 1;
				}
				add += point[ path.get(i).c-65 ] * letter[ path.get(i).x ][ path.get(i).y];
				if(letter[ path.get(i).x ][ path.get(i).y ] > 1) {
					letter[ path.get(i).x ][ path.get(i).y ] = 1;
				}
			}
			total += add * wordMultiply;
		}
		return ret;
	}
	
	public String generateColumn(State states[], int size, int idx) {
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				visit[i][j] = false;
			}
		}
		for(int i=0; i<size; i++) {
			visit[ states[i].x ][ states[i].y ] = true;
		}
		
		int x=states[idx].x, y=states[idx].y;
		while(x>=0 && (board[x][y]!='*' || visit[x][y])) x--;
		x++;
		String ret="";
		
		char thisMove[][] = new char[15][15];
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				thisMove[i][j] = '*';
			}
		}
		for(int i=0; i<size; i++) {
			thisMove[ states[i].x ][ states[i].y ] = states[i].c; 
		}
		
		ArrayList<State> path = new ArrayList<State>();
		while(true) {
			if(x>=15 || (board[x][y]=='*' && !visit[x][y])) break;
			if(board[x][y] != '*') {
				ret += board[x][y];
				path.add(new State(x, y, board[x][y]));
			}
			else {
				ret += thisMove[x][y];
				path.add(new State(x, y, thisMove[x][y]));
			}
			x++;
		}
		if(BinarySearch(ret)) {
			int add=0, wordMultiply=1;
			for(int i=0; i<path.size(); i++) {
				wordMultiply *= word[ path.get(i).x ][ path.get(i).y ];
				if(word[ path.get(i).x ][ path.get(i).y ] > 1) {
					word[ path.get(i).x ][ path.get(i).y ] = 1;
				}
				add += point[ path.get(i).c-65 ] * letter[ path.get(i).x ][ path.get(i).y ];
				if(letter[ path.get(i).x ][ path.get(i).y ] > 1) {
					letter[ path.get(i).x ][ path.get(i).y ] = 1;
				}
			}
			total += add * wordMultiply;
		}
		return ret;
	}
	
	public ArrayList<String> generate(State states[], int size, int m) {
		ArrayList<String> list = new ArrayList<String>();
		String now;
		if(m == ROW_MAJOR) {
			now = generateRow(states, size, 0);
			if(now.length()>=2) list.add(now);
			for(int i=0; i<size; i++) {
				now = generateColumn(states, size, i);
				if(now.length()>=2) list.add(now);
			}
		}
		else if(m == COLUMN_MAJOR) {
			now = generateColumn(states, size, 0);
			if(now.length()>=2) list.add(now);
			for(int i=0; i<size; i++) {
				now = generateRow(states, size, i);
				if(now.length()>=2) list.add(now);
			}
		}
		else {
			now = generateRow(states, size, 0);
			if(now.length()>=2) list.add(now);
			now = generateColumn(states, size, 0);
			if(now.length()>=2) list.add(now);
		}
		return list;
	}
	
	public void updateBoard(State states[], int size) {
		for(int i=0; i<size; i++) {
			board[ states[i].x ][ states[i].y ] = states[i].c;
		}
	}
	
	public String pointCalculation(State states[], int size, int m) {
		total = 0;
		ArrayList <String> generatedWords = generate(states, size, m);
//		for(int i=0; i<generatedWords.size(); i++) {
//			System.out.println(generatedWords.get(i));
//		}
		ArrayList <String> invalid = new ArrayList<String>();
		String ret="";
		for(int i=0; i<generatedWords.size(); i++) {
			if(!BinarySearch(generatedWords.get(i))) {
				invalid.add(generatedWords.get(i));
			}
		}
		if(invalid.size()>=1) {
			ret += "These words are invalid:";
			for(int i=0; i<invalid.size(); i++) {
				if(i>0) ret += ",";
				ret += " " + invalid.get(i);
			}
			ret += ".";
			return ret;
		}
		else {
			updateBoard(states, size);
//			printBoard();
			if(size == 7) total += 50;
			return Integer.toString(total);
		}
	}
	
	public String move(String s) {
		int size = atoi(s, 0), j=0;
		String drop[] = new String[size];
		State states[] = new State[size];
		
		for(int i=1; i<s.length(); i++) {
			if(s.charAt(i) == '{') {
				drop[j] = parse(s, i);
				j++;
			}
		}
		for(int i=0; i<size; i++) {
			states[i] = new State(drop[i]);
		}
		
		// checking move validity
		if(board[7][7] == '*' && !isValidFirstMove(states, size)) {
			return "Your must cover the center tile in the first move.";
		}
		if(board[7][7] == '*' && size<2) {
			return "Your must use at least two letters in the first move";
		}
		if(isUsed(states, size)) {
			return "Grid already used.";
		}
		int m = major(states, size);
		if(m == -1) {
			return "Letters are not in same row/ column.";
		}
		if(!isContiguous(states, size, m)) { // problem here
			return "There is a blank space in your word.";
		}
		if(!isConnectedComponent(states, size)) {
			return "None of your new tiles form a connected component with the existing tiles.";
		}
		
		return pointCalculation(states, size, m);
		
//		ArrayList<String> generatedWords = generate(states, size, m);
//		for(int i=0; i<generatedWords.size(); i++) {
//			System.out.println(generatedWords.get(i));
//		}
//		return "ok";
	}
}
