package test;

import RMIClient.LocateRegistry;
import RMIClient.RMIRegistry;
import RMIClient.RemoteObjectRef;

public class ExceptionClient {

	public static void main(String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);

		try {
			RMIRegistry registry = LocateRegistry.getRegistry(host, port);

			RemoteObjectRef ref = registry.lookup("Not existed");

			Hello task = (Hello) ref.localise();

			String theGreeting = task.sayHello("123");

			System.out.println(theGreeting);
		} catch (Exception e) {
			// Bad things can happen to good people
			e.printStackTrace();
		}
	}

}
