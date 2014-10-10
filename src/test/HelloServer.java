package test;

import java.rmi.*;
import java.rmi.server.*;

public class HelloServer extends UnicastRemoteObject implements Hello {
	private static final String serverName = "hello";

	public HelloServer() throws RemoteException {
	}

	// This is the one real method
	public String sayHello() throws RemoteException {
		return "Hello World! Hello FuckS";
	}

	@Override
	public String sayHello(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}
