package RMIClient;

import utility.StubInterface;

import java.io.Serializable;

public class RemoteObjectRef implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2803056063725814440L;
	private String ipAdr;
	private int port;
	private int objKey;
	private String remoteInterfaceName;

	public RemoteObjectRef(String ip, int port, int obj_key,
			String interfaceName) {
		this.ipAdr = ip;
		this.port = port;
		this.objKey = obj_key;
		this.remoteInterfaceName = interfaceName;
	}

	/**
	 * Create a new remote reference that can communicate with the server by
	 * using the stub
	 * 
	 * @return
	 */
	public Object localise() {
		/*
		 * Create a stub and attach the remote object reference to the stub
		 */
		String stubName = "test." + remoteInterfaceName + "_Stub";
		Object stub = null;
		try {
			Class<?> stubClass;
			stubClass = Class.forName(stubName);
			stub = stubClass.newInstance();
			((StubInterface) stub).attachRef(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stub;
	}

	public String getIP() {
		return this.ipAdr;
	}

	public int getPort() {
		return this.port;
	}

	public int getKey() {
		return this.objKey;
	}

	public String getInterface() {
		return this.remoteInterfaceName;
	}
}
