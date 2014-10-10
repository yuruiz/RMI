package test;

import RMIClient.RemoteObjectRef;
import utility.RMIMessage;
import utility.StubInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * The stub implementation of the Hello service
 * 
 * @author Yurui Zhou
 *
 */
public final class Hello_Stub implements Hello, StubInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2862224793902768100L;
	private RemoteObjectRef roRef = null;
	private Socket socket;
	private RMIMessage reply;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	public Hello_Stub() {
	}

	public RemoteObjectRef getRef() {
		return this.roRef;
	}

	public void attachRef(RemoteObjectRef ref) {
		this.roRef = ref;
	}

	public String sayHello(String s) throws RemoteException {
		if (roRef == null) {
			throw new RuntimeException("Remote ref not attached");
		}

		RMIMessage mesg = new RMIMessage("sayHello", new Object[] { s });

		mesg.setArgType(new Class[] { String.class });

		mesg.setKey(roRef.getKey());

		connectServer();

		sendMesg(mesg);

		if (reply.getExcep() != null) {
			throw new RemoteException("Method Invocation Error"
					+ reply.getExcep());
		}

		return (String) reply.getRet();
	}

	private void connectServer() {
		if (roRef == null) {
			throw new RuntimeException("Remote ref not attached");
		}

		try {
			System.out.println("Connecting Server " + roRef.getIP()
					+ roRef.getPort());
			socket = new Socket(roRef.getIP(), roRef.getPort());
			System.out.println("connected");
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			System.out.println("connected2");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void sendMesg(RMIMessage mesg) {
		try {
			output.writeObject(mesg);

			reply = (RMIMessage) input.readObject();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}