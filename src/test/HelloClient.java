package test;

import RMIClient.LocateRegistry;
import RMIClient.RMIRegistry;
import RMIClient.RemoteObjectRef;

class HelloClient {
    // You'll want to change this to match your own host
    private static final String HelloServerURL = "rmi://gigo.sp.cs.cmu.edu/hello";

    // This takes one command line argument: A person's first name
    public static void main(String[] args) {
        try {
            RMIRegistry registry = LocateRegistry.getRegistry("127.0.0.1", 15440);

            RemoteObjectRef ref = registry.lookup("HelloServer");

            Hello task = (Hello) ref.localise();

            String theGreeting = task.sayHello("123");

            System.out.println(theGreeting);
        } catch (Exception e) {
            // Bad things can happen to good people
            e.printStackTrace();
        }
    }
}
