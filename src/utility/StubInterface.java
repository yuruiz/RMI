package utility;

import RMIClient.RemoteObjectRef;

/**
 * Created by yuruiz on 10/8/14.
 */
public interface StubInterface {

    public void attachRef(RemoteObjectRef ref);

    public RemoteObjectRef getRef();
}
