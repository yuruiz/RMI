package test;

import RMIClient.RemoteObjectRef;
import utility.Remote440;

public interface NameServer extends Remote440 {
    public RemoteObjectRef match(String name);

    public NameServer add(String s, RemoteObjectRef r, NameServer n);

    public NameServer next();
}

