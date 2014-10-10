package RMIClient;

import RMIClient.RemoteObjectRef;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import utility.Constant;
import utility.Remote440Exception;

public class RMIRegistry {
	/*
	 * registry holds its port and host, and connects to it each time.
	 */
	private String host;
	private int port;

	/**
	 * 
	 * @param ipAdr
	 * @param portNum
	 */
	public RMIRegistry(String ipAdr, int portNum) {
		host = ipAdr;
		port = portNum;
	}

	/**
	 * Returns the remote object reference if found and null if not
	 * 
	 * @param serviceName
	 *            the name of the object client wants service on
	 * @return the remote object reference
	 * @throws IOException
	 * @throws Remote440Exception
	 */
	public RemoteObjectRef lookup(String serviceName) throws IOException,
			Remote440Exception {
		/*
		 * open socket. it assumes registry is already located by locate
		 * registry. you should usually do try-catch here (and later).
		 */
		Socket soc = new Socket(host, port);

		/*
		 * get TCP streams and wrap them.
		 */
		BufferedReader in = new BufferedReader(new InputStreamReader(
				soc.getInputStream()));
		PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

		/*
		 * it is locate request, with a service name.
		 */
		out.println(Constant.LOOK_UP_QUERY);
		out.println(serviceName);

		/*
		 * Branching according to server's response
		 */
		String res = in.readLine();
		RemoteObjectRef ror;

		if (res.equals(Constant.LOOK_UP_SUCCESS_RESPONSE)) {

			/*
			 * Lookup succeed, parse server info from response
			 */
			String roIPAdr = in.readLine();

			int roPortNum = Integer.parseInt(in.readLine());
			int roObjKey = Integer.parseInt(in.readLine());

			String roInterfaceName = in.readLine();

			/*
			 * create a new remote object reference
			 */
			ror = new RemoteObjectRef(roIPAdr, roPortNum, roObjKey,
					roInterfaceName);
		} else {
			soc.close();
			throw new Remote440Exception("Looking up unsuccessful");
		}

		soc.close();
		return ror;
	}

}
