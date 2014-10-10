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
import utility.Remote440Exception;

/**
 * 
 * @author Siyu
 * 
 *         The implementation of the RMIServer that tracks of the important
 *         mapping, manages ID to objects, starts and method invocation thread
 *
 */
public class RMIServer {

	// mapping from id to local object
	private Map<Integer, Object> remoteToLocal;
	private Map<String, String> services;

	// remote interface to local interface name mapping
	private Map<String, String> remoteLocalInterface;
	private static final int PORT = 15640; // server port
	private int nextId = 0; // the current largest id that has been assigned
	private boolean shutDown = false;// if server shall be shut down
	private RMIRegisterServer registry; // registry server

	public RMIServer() {
		remoteToLocal = new ConcurrentHashMap<Integer, Object>();
		remoteLocalInterface = new HashMap<String, String>();
		registry = new RMIRegisterServer(this);
		services = new ConcurrentHashMap<String, String>();

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
		remoteLocalInterface.put(remote, local);
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
		return remoteLocalInterface.get(name);
	}

	/**
	 * Helper method that returns the first implemented interface of a class
	 * which is the regarded service name
	 * 
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	private String getServiceName(String className)
			throws ClassNotFoundException {
		return Class.forName(className).getInterfaces()[0].getSimpleName();
	}

	/**
	 * Add an try to the services mapping that map from a service name to the
	 * implementation class the provides the service
	 * 
	 * @param service
	 *            service name
	 * @param impl
	 *            implementation class name
	 */
	public void addService(String service, String impl) {
		services.put(service, impl);
	}

	/**
	 * Add a new running object according to client request and return the
	 * assigned id, return -1 upon unsuccessful adding
	 * 
	 * @param name
	 *            class name requested by client
	 * @return the assigned id of the instantiated object
	 */
	public int addNew(String name) {
		try {
			/*
			 * Find the local class that implements the service return -1 upon
			 * exception or no such service
			 */
			String implClass = services.get(name);
			if (implClass == null) {
				return -1;
			}

			Class<?> c = Class.forName(implClass);
			Object o = c.newInstance();
			remoteToLocal.put(nextId, o);
			nextId++;
			return nextId - 1;
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			/*
			 * return -1 upon exception
			 */
			return -1;
		}
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

		/*
		 * Add the remote interface name to local interface name mapping entry
		 */
		server.addInterface("Hello", "Hello");
		server.addInterface("Substring", "Substring");
		/*
		 * Add the service name to implementation class name mapping entry
		 */
		try {
			server.addService(server.getServiceName("test.HelloImpl"),
					"test.HelloImpl");
			server.addService(server.getServiceName("test.SubstringImpl"),
					"test.SubstringImpl");
		} catch (ClassNotFoundException e) {
			// if no class found, just ignore it and do not add an entry
			e.printStackTrace();
		}

		server.start();
	}
}
