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
	private Object[] argType;
	private Object returnValue;
	private Socket client;
	private ObjectOutputStream out;

	public RMIExecutor(Object o, String methodName, Object[] args,
			Object[] argType, Socket client, ObjectOutputStream out) {

		this.callOn = o;
		this.methodName = methodName;
		this.args = args;
		this.client = client;
		this.out = out;

	}

	@Override
	public void run() {

		Method m;
		RMIMessage message = null;
		try {
			m = callOn.getClass().getMethod(methodName, (Class[]) argType);
			returnValue = m.invoke(callOn, args);
			message = new RMIMessage(returnValue);
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			e.printStackTrace();
			message = new RMIMessage(new Remote440Exception(
					"Insuccessful method invocation"));

		} finally {
			try {
				out.writeObject(message);
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
