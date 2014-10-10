package RMIServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utility.RMIMessage;

public class RMIServer {

	private Map<Integer, Object> remoteToLocal;
	private Map<String, String> interfaceMap;
	private static final int PORT = 15640;
	private int nextId = 0;
	private boolean shutDown = false;
	private RMIRegisterServer registry;

	public RMIServer() {
		remoteToLocal = new ConcurrentHashMap<Integer, Object>();
		interfaceMap = new HashMap<String, String>();
		registry = new RMIRegisterServer(this);

	}

	public String getRemoteInterfaceName(String name) {
		return interfaceMap.get(name);
	}

	public static void main(String[] args) {
		RMIServer server = new RMIServer();
		server.start();
	}

	public int addNew(String name) {
		String localName = interfaceMap.get(name);
		try {
			Class<?> c = Class.forName(localName);
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

	public void start() {

		try {
			ServerSocket server = new ServerSocket(PORT);
			new Thread(registry).start();
			while (!shutDown) {
				Socket client = server.accept();
				ObjectInputStream in = new ObjectInputStream(
						client.getInputStream());
				try {
					RMIMessage message = (RMIMessage) in.readObject();
					Object onCall = remoteToLocal.get(message.getKey());
					RMIExecutor executor = new RMIExecutor(onCall,
							message.getMethodName(), message.getArgv(), client);
					new Thread(executor).start();

				} catch (ClassNotFoundException e) {
					System.err.println("Unrecognized message type");
					e.printStackTrace();
				}

			}
			server.close();
		} catch (IOException e) {
			System.err.println("Server starts fail");
			e.printStackTrace();
		}

	}
}
