package test;

import RMIClient.RemoteObjectRef;
import utility.RMIMessage;
import utility.Remote440Exception;
import utility.StubInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Substring_Stub implements Substring, StubInterface {

    /**
     *
     */
    private static final long serialVersionUID = -5535303883176087061L;
    private RemoteObjectRef roRef = null;
    private Socket socket;
    private RMIMessage reply;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Substring_Stub() {

    }

    @Override
    public String SubString(String s, int start, int end) throws Remote440Exception {
        if (roRef == null) {
            throw new RuntimeException("Remote ref not attached");
        }

        RMIMessage mesg = new RMIMessage("SubString", new Object[]{s, start, end});

        mesg.setArgType(new Class[]{String.class, Integer.class, Integer.class});

        mesg.setKey(roRef.getKey());

        connectServer();

        sendMesg(mesg);

        if (reply.getExcep() != null) {
            throw new Remote440Exception("Method Invocation Error" + reply.getExcep());
        }

        return (String) reply.getRet();
    }

    @Override
    public void attachRef(RemoteObjectRef ref) {
        this.roRef = ref;

    }

    @Override
    public RemoteObjectRef getRef() {
        return this.roRef;
    }

    private void connectServer() {
        if (roRef == null) {
            throw new RuntimeException("Remote ref not attached");
        }

        try {
            System.out.println("Connecting Server " + roRef.getIP() + ":" + roRef.getPort());
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
