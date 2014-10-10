package RMIServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utility.RMIMessage;

/**
 * 
 * @author Siyu
 * 
 *         The implementation of the RMIServer that tracks of the important
 *         mapping, manages ID to objects, starts and method invocation thread
 *
 */
public class RMIServer {

	private Map<Integer, Object> remoteToLocal; // mapping from id to local
												// object
	private Map<String, String> interfaceMap; // Object name to interface name
	private static final int PORT = 15640; // server port
	private int nextId = 0; // the current largest id that has been assigned
	private boolean shutDown = false;// if server shall be shut down
	private RMIRegisterServer registry; // registry server

	public RMIServer() {
		remoteToLocal = new ConcurrentHashMap<Integer, Object>();
		interfaceMap = new HashMap<String, String>();
		registry = new RMIRegisterServer(this);

	}

	/**
	 * Add an entry from client side interface to server interface name mapping
	 * 
	 * @param remote
	 *            remote interface name
	 * @param local
	 *            local interface name
	 */
	public void addInterface(String remote, String local) {
		interfaceMap.put(remote, local);
	}

	/**
	 * 
	 * get the interface name
	 * 
	 * @param name
	 *            client side name
	 * @return
	 */
	public String getInterfaceName(String name) {
		return interfaceMap.get(name);
	}

	/**
	 * add a new running object according to client request
	 * 
	 * @param name
	 *            class name requested by client
	 * @return the assigned id of the instantiated object
	 */
	public int addNew(String name) {
		try {
			Class<?> c = Class.forName("test." + name);
			Object o = c.newInstance();
			remoteToLocal.put(nextId, o);
			nextId++;
			return nextId - 1;
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Start the server, poll for client connection
	 */
	public void start() {

		try {
			/*
			 * Create server socket, start registry server on another port
			 */
			ServerSocket server = new ServerSocket(PORT);
			new Thread(registry).start();
			while (!shutDown) {
				/*
				 * Initialize connection with client
				 */
				Socket client = server.accept();
				ObjectOutputStream out = new ObjectOutputStream(
						client.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(
						client.getInputStream());
				/*
				 * Invoke the method specified by client
				 */
				try {
					RMIMessage message = (RMIMessage) in.readObject();
					Object onCall = remoteToLocal.get(message.getKey());
					RMIExecutor executor = new RMIExecutor(onCall,
							message.getMethodName(), message.getArgv(),
							message.getArgType(), client, out);
					new Thread(executor).start();

				} catch (ClassNotFoundException e) {
					/*
					 * Invalid message type, throw a remote exception
					 */
					RMIMessage wrong = new RMIMessage(new Remote440Exception(
							"Unrecognized Message Type"));
					out.writeObject(wrong);
					client.close();
				}

			}
			server.close();
		} catch (IOException e) {
			System.err.println("Server starts fail");
			e.printStackTrace();
		}

	}

	/**
	 * Start the server
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		RMIServer server = new RMIServer();
		server.addInterface("HelloImpl", "Hello");
		server.start();

	}
}
