package RMIServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

import utility.RMIMessage;

/**
 * The implementation of a thread that executes a method invocation which will
 * be started by the @{link}RMIServer
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

	/**
	 * 
	 * @param o
	 *            The object the method being invoked on
	 * @param methodName
	 *            The name of the method being invoked
	 * @param args
	 *            The arguments for invoking the method
	 * @param argType
	 *            The argument types for the method
	 * @param client
	 *            The client socket
	 * @param out
	 *            The outputstream for sending back a message
	 */
	public RMIExecutor(Object o, String methodName, Object[] args,
			Object[] argType, Socket client, ObjectOutputStream out) {

		this.callOn = o;
		this.methodName = methodName;
		this.args = args;
		this.client = client;
		this.out = out;
		this.argType = argType;

	}

	@Override
	public void run() {

		Method m;
		RMIMessage message = null;
		try {
			/*
			 * Invoking the method with the provided arguments
			 */
			m = callOn.getClass().getMethod(methodName, (Class[]) argType);
			returnValue = m.invoke(callOn, args);
			message = new RMIMessage(returnValue);
		} catch (Exception e) {
			/*
			 * Upon any exception, send back a Remote440Exception
			 */
			e.printStackTrace();
			message = new RMIMessage(new Remote440Exception(
					"Insuccessful method invocation"));

		} finally {
			try {
				out.writeObject(message);
				client.close();
			} catch (IOException e) {
				/*
				 * If the connection is lost, we cannot send the message.
				 */
				e.printStackTrace();
			}

		}

	}
}
