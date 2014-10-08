import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RMIExecutor implements Runnable {

	protected Object callOn;
	protected String methodName;
	protected Object[] args;
	protected Object returnValue;

	@Override
	public void run() {
		try {
			Method m = callOn.getClass().getMethod(methodName);
			try {
				returnValue = m.invoke(callOn, args);
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
