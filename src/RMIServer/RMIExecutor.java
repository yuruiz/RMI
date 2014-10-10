package RMIServer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

import utility.RMIMessage;

/**
 * 
 * @author Siyu
 *
 */
public class RMIExecutor implements Runnable {

	private Object callOn;
	private String methodName;
	private Object[] args;
	private Object returnValue;
	private Socket client;

	public RMIExecutor(Object o, String methodName, Object[] args, Socket client) {

		this.callOn = o;
		this.methodName = methodName;
		this.args = args;
		this.client = client;

	}

	@Override
	public void run() {

		Method m;
		RMIMessage message = null;
		try {
			m = callOn.getClass().getMethod(methodName);
			returnValue = m.invoke(callOn, args);
			message = new RMIMessage(returnValue);
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e1) {

			message = new RMIMessage(new Remote440Exception(
					"Insuccessful method invocation"));

		} finally {
			ObjectOutputStream out;
			try {
				out = new ObjectOutputStream(client.getOutputStream());
				out.writeObject(message);
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
