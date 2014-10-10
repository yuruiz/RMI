package test;

import RMIClient.LocateRegistry;
import RMIClient.RMIRegistry;
import RMIClient.RemoteObjectRef;

import java.io.*;

// we test simple registry by binding a service to ROR.

public class testRebind
{

    public static void main(String args[])    
	throws IOException
    {
	// it takes seven arguments.
	// these are it wishes to connect to.
	String host = args[0];
	int port = Integer.parseInt(args[1]);

	// these are data.
	String ServiceName = args[2];
	String IPAdr = args[3];
	int PortNum = Integer.parseInt(args[4]);
	int ObjKey = Integer.parseInt(args[5]);
	String InterfaceName = args[6];

	// make ROR.
	RemoteObjectRef ror = new RemoteObjectRef(IPAdr, PortNum, ObjKey, InterfaceName);

	// this is the ROR content.
	System.out.println("IP address is "+ror.getIP());
	System.out.println("Port num is "+ror.getPort());
	System.out.println("Object key is "+ror.getKey());
	System.out.println("Interface name is "+ror.getInterface());

	// locate.
	RMIRegistry sr = LocateRegistry.getRegistry(host, port);

	System.out.println("located."+sr+"/n");
	
	if (sr != null)
	    {
		// bind.
		sr.rebind(ServiceName, ror);

		// test the binding by looking up.
		RemoteObjectRef ror2 = sr.lookup(ServiceName);

		System.out.println("IP address is "+ror2.getIP());
		System.out.println("Port num is "+ror2.getPort());
		System.out.println("Object key is "+ror2.getKey());
		System.out.println("Interface name is "+ror2.getInterface());

	    }
	else		
	    {
		System.out.println("no registry found.");
	    }

    }
}














	

	

	
	



	
