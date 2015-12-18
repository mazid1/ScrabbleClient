import java.io.*;

public class InputReader {
	public int DIC_SIZE = 200000;
	public String[] dic = new String[DIC_SIZE];
	public String fileName = "src/dictionary.txt";
	public int key=0;
	public InputReader() throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					fileName), "UTF-8"));
			String textLine;
			while((textLine = br.readLine()) != null) {
				dic[key] = textLine;
				key++;				
			}
		} catch (IOException e){
			System.err.println("File not found " + e);		
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void printDic() {
		for(int i=0; i<5; i++) {
			System.out.println(dic[i]);
		}
	}
		
}