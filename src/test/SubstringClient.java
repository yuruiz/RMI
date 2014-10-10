package test;

import RMIClient.LocateRegistry;
import RMIClient.RMIRegistry;
import RMIClient.RemoteObjectRef;

public class SubstringClient {
	public static void main(String[] args) {

		String host = args[0];
		int port = Integer.parseInt(args[1]);

		try {
			RMIRegistry registry = LocateRegistry.getRegistry(host, port);
			RemoteObjectRef ref = registry.lookup("Substring");

			Substring task = (Substring) ref.localise();

			String sub = task.SubString("I love you", 3, 5);

			System.out.println(sub);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
