

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RMIServer {

	private Map<Object, Object> remoteToLocal;
	private final int port;

	public RMIServer(int port) {
		this.port = port;
		remoteToLocal = new ConcurrentHashMap<Object, Object>();

	}

	public void start() {

		try {
			ServerSocket server = new ServerSocket(port);
			while (true) {
				Socket client = server.accept();
				ObjectInputStream in = new ObjectInputStream(
						client.getInputStream());
				try {
					RMIMessage message = (RMIMessage) in.readObject();
					Object remote = message.getRor();
					Object local = remoteToLocal.get(remote);
					try {

						Method method = local.getClass().getMethod(
								message.getMethodName());
						try {
							method.invoke(local);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}

					} catch (NoSuchMethodException e) {
						System.err.println("No such method");
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
				} catch (ClassNotFoundException e) {
					System.err.println("Unrecognized message type");
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			System.err.println("Server starts fail");
			e.printStackTrace();
		}

	}
}
