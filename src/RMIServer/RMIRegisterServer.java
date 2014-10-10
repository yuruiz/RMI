package RMIServer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The registry server implementation that takes care of client registering
 * 
 * @author Siyu
 *
 */
public class RMIRegisterServer implements Runnable {

	private static final int PORT = 15440; // The port registry is running on
	private static final int EXECUTER_PORT = 15640; // The port the server is on
	protected boolean shutDown = false;
	private RMIServer master; // registry has a reference to master to get the
								// mapping information

	public RMIRegisterServer(RMIServer master) {
		this.master = master;
	}

	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket(PORT);
			while (!shutDown) {
				/*
				 * Polling for client connection and manage the handshake
				 */
				Socket client = server.accept();
				Scanner reader = new Scanner(client.getInputStream());
				PrintStream out = new PrintStream(client.getOutputStream());
				List<String> response = new ArrayList<String>();

				String line = reader.nextLine();
				if (line == null) {
					continue;
				}
				/*
				 * Handshake, telling a client this is the registry server
				 */
				if (line.startsWith("who")) {
					response.add("Registry");
				} else {
					if (line.startsWith("lookup")) {
						String name = reader.nextLine();
						System.out.println("Object name: " + name);

						response.add("found");
						response.add(InetAddress.getLocalHost()
								.getHostAddress());
						response.add(String.valueOf(EXECUTER_PORT));
						response.add(String.valueOf(master.addNew(name)));
						response.add(master.getInterfaceName(name));

					}

				}

				for (String s : response) {
					out.println(s);
				}
				client.close();
				reader.close();

			}
			server.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
