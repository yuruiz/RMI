import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

import utility.RMIMessage;

public class RMIExecutor implements Runnable {

	protected Object callOn;
	protected String methodName;
	protected Object[] args;
	protected Object returnValue;

	public RMIExecutor(Object o, String methodName, Object[] args, Socket client) {

		this.callOn = o;
		this.methodName = methodName;
		this.args = args;

	}

	@Override
	public void run() {
		try {
			Method m = callOn.getClass().getMethod(methodName);
			try {
				returnValue = m.invoke(callOn, args);
				RMIMessage message = new RMIMessage();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

}
