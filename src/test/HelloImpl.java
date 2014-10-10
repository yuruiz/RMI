package test;

import java.rmi.*;
import java.rmi.server.*;

public class HelloImpl extends UnicastRemoteObject implements Hello {
	private static final String serverName = "hello";

	public HelloImpl() throws RemoteException {
	}

	@Override
	public String sayHello(String name) throws RemoteException {
		return "Hello World! Hello Fuck" + name;
	}
}
