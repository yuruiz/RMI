import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * The class that takes care of client registering
 * 
 * @author Siyu
 *
 */
public class RMIRegisterServer implements Runnable {

	private static final int PORT = 15440;
	protected boolean shutDown = false;

	@Override
	public void run() {
		try {

			ServerSocket server = new ServerSocket(PORT);
			while (!shutDown) {
				Socket client = server.accept();
				Scanner reader = new Scanner(client.getInputStream());
				String response;

				String line = reader.nextLine();
				if (line == null) {
					continue;
				}
				if (line.startsWith("who")) {
					response = "regsitry";
				} else {
					if (line.startsWith("lookup")) {
						response = "look";
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
