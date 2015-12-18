
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

	Socket socket;

	public void setConnection(String ip, int port) throws Exception {
		try {
			socket = new Socket(ip, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Connected to " + socket.getInetAddress());
	}

	public void sendMsg(String input) throws IOException {
		DataOutputStream out = new DataOutputStream(
				socket.getOutputStream());
		out.writeUTF(input);

	}

	public String receiveMsg() throws IOException {
		DataInputStream in = new DataInputStream(socket.getInputStream());
		String msg = in.readUTF();
		System.out.println("\nFrom server: " + msg);
		return msg;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
}
