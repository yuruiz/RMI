package RMIClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import utility.Constant;
import utility.Remote440Exception;

/**
 * 
 * @author Siyu
 *
 */
public class LocateRegistry {

	/**
	 * handshake with the server make sure that server have requested registry
	 * 
	 * @param host
	 *            the destination host ip address
	 * 
	 * @param port
	 *            the destination port
	 */

	public static RMIRegistry getRegistry(String host, int port) {
		// open socket.
		try {
			Socket soc = new Socket(host, port);

			/*
			 * get TCP streams and wrap them.
			 */
			BufferedReader in = new BufferedReader(new InputStreamReader(
					soc.getInputStream()));
			PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

			// start handshake
			out.println(Constant.HAND_SHAKE_QUERY);

			// get handshake response
			if ((in.readLine()).equals(Constant.HAND_SHAKE_RESPONSE)) {
				soc.close();
				return new RMIRegistry(host, port);
			} else {
				soc.close();
				throw new Remote440Exception(
						"The requested host doesn't have registry!");
			}
		} catch (Exception e) {
			/*
			 * In case of I/O Exception, just print I/O error
			 */
			e.printStackTrace();
			return null;
		}
	}
}
