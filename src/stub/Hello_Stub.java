// Decompiled by DJ v3.2.2.67 Copyright 2002 Atanas Neshkov  Date: 1/22/03 1:40:21 AM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.rmi.*;
import java.rmi.server.Operation;
import java.rmi.server.RemoteCall;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import test.*;

public final class Hello_Stub extends RemoteStub implements HelloInterface, Remote {

    public Hello_Stub() {
    }

    public Hello_Stub(RemoteRef remoteref) {
        super(remoteref);
    }

    static Class _mthclass$(String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    public String sayHello(String s) throws RemoteException {
        try {
            if (useNewInvoke) {
                Object obj = super.ref.invoke(this, $method_sayHello_0, new Object[]{s}, 0x742a8a860d6b7ee4L);
                return (String) obj;
            }
            RemoteCall remotecall = super.ref.newCall(this, operations, 0, 0x38bc97be171621e6L);
            try {
                ObjectOutput objectoutput = remotecall.getOutputStream();
                objectoutput.writeObject(s);
            } catch (IOException ioexception) {
                throw new MarshalException("error marshalling arguments", ioexception);
            }
            super.ref.invoke(remotecall);
            String s1;
            try {
                ObjectInput objectinput = remotecall.getInputStream();
                s1 = (String) objectinput.readObject();
            } catch (IOException ioexception1) {
                throw new UnmarshalException("error unmarshalling return", ioexception1);
            } catch (ClassNotFoundException classnotfoundexception) {
                throw new UnmarshalException("error unmarshalling return", classnotfoundexception);
            } finally {
                super.ref.done(remotecall);
            }
            return s1;
        } catch (RuntimeException runtimeexception) {
            throw runtimeexception;
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (Exception exception) {
            throw new UnexpectedException("undeclared checked exception", exception);
        }
    }

    private static final Operation operations[] = {new Operation("java.lang.String sayHello(java.lang.String)")};
    private static final long interfaceHash = 0x38bc97be171621e6L;
    private static final long serialVersionUID = 2L;
    private static boolean useNewInvoke;
    private static Method $method_sayHello_0;

    static {
        try {
            (java.rmi.server.RemoteRef.class).getMethod("invoke", new Class[]{java.rmi.Remote.class, java.lang.reflect.Method.class, java.lang.Object[].class, Long.TYPE});
            useNewInvoke = true;
            $method_sayHello_0 = (HelloInterface.class).getMethod("sayHello", new Class[]{java.lang.String.class});
        } catch (NoSuchMethodException _ex) {
            useNewInvoke = false;
        }
    }
}