package RMIServer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import utility.Constant;

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

				/*
				 * Reading handshake/lookup information from client
				 */
				String line = reader.nextLine();
				if (line == null) {
					continue;
				}
				/*
				 * Handshake, telling a client this is the registry server
				 */
				if (line.equals(Constant.HAND_SHAKE_QUERY)) {
					response.add(Constant.HAND_SHAKE_RESPONSE);
				} else {
					/*
					 * Client tries to lookup a service
					 */
					if (line.startsWith("lookup")) {
						if (reader.hasNextLine()) {

							String name = reader.nextLine();
							int id = master.addNew(name);
							if (id != -1) {
								/*
								 * Look up successful, return the IP address,
								 * port, assigned id, remote interface name to
								 * client
								 */
								response.add("found");
								response.add(InetAddress.getLocalHost()
										.getHostAddress());
								response.add(String.valueOf(EXECUTER_PORT));
								response.add(String.valueOf(id));
								response.add(master.getInterfaceName(name));
							}
							/*
							 * Upon unsuccessful looking up, return a failing
							 * message
							 */
							else {
								response.add(Constant.LOOK_UP_FAIL_RESPONSE);
							}
						} else {
							response.add(Constant.LOOK_UP_FAIL_RESPONSE);
						}

					} else {
						response.add(Constant.UNRECOGINIZED);
					}

				}
				/*
				 * Sending back response to client
				 */
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
