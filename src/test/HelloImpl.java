package test;

import java.rmi.*;
import java.rmi.server.*;

import utility.Remote440Exception;

public class HelloImpl extends UnicastRemoteObject implements Hello {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5332557223225713115L;

	public HelloImpl() throws RemoteException {
	}

	@Override
	public String sayHello(String name) throws Remote440Exception {
		return "Hello World! Hello" + name;
	}
}
