
public class State {
	public int x, y;
	public char c;
	
	public int atoi(String s, int idx) {
		int ret=0;
		while(s.charAt(idx)>='0' && s.charAt(idx)<='9') {
			ret *= 10;
			ret += s.charAt(idx)-'0';
			idx++;
		}
		return ret;
	}
	
	public State() {
		
	}
	
	public State(String s) {
		int start=0;
		x = atoi(s, 1);
		for(int i=2; i<s.length(); i++) {
			if(s.charAt(i) == ',') {
				y = atoi(s, i+1);
				start = i+2;
				break;
			}
		}
		for(int i=start; i<s.length(); i++) {
			if(s.charAt(i) == ',') {
				c = s.charAt(i+1);
				break;
			}
		}
	}
	
	public State(int x, int y, char c) {
		this.x = x;
		this.y = y;
		this.c = c;
	}
	
	public void printState() {
		System.out.println(x + " " + y + " " + c);
	}
}
