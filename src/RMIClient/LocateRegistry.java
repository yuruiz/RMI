package RMIClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LocateRegistry {
    /*
    * handshake with the server
    * make sure that server have requested registry
    * @param host the destination host ip address
    * @param port the destination port
    */
    public static RMIRegistry getRegistry(String host, int port) {
        // open socket.
        try {
            Socket soc = new Socket(host, port);

            // get TCP streams and wrap them.
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

            // ask.
            out.println("who are you?");

            // gets answer.
            if ((in.readLine()).equals("Registry")) {
                soc.close();
                return new RMIRegistry(host, port);
            } else {
                soc.close();
                System.out.println("The requested host doesn't have registry!");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

