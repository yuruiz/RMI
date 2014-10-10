package stub;

import RMIClient.RemoteObjectRef;
import test.Hello;
import utility.RMIMessage;
import utility.StubInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public final class Hello_Stub implements Hello, StubInterface {

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

        RMIMessage mesg = new RMIMessage("Hello", null);

        mesg.setKey(roRef.getKey());

        connectServer();

        sendMesg(mesg);

        if (reply.getExcep() != null) {
            throw new RemoteException("Method Invocation Error" + reply.getExcep());
        }


        return (String) reply.getRet();
    }

    private void connectServer() {
        if (roRef == null) {
            throw new RuntimeException("Remote ref not attached");
        }

        try {
            socket = new Socket(roRef.getIP(), roRef.getPort());
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
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