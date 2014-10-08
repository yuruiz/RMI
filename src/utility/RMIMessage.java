package utility;

import java.io.Serializable;

/**
 * Created by yuruiz on 10/7/14.
 */
public class RMIMessage implements Serializable {
    private static final long serialVersionUID = -2757242338506557482L;

//    private RemoteObjectRef ror;
    private String methodName;
    private boolean isexcepThrown = false;
    private Exception excep;
    private Object retValue;
    private Object[] argv;
    private Object[] argType;
    private int Obj_Key = 0;

    public RMIMessage(String method, Object[] args) {
        this.methodName = method;
        this.argv = args;
    }

    public void setKey(int Key) {
        this.Obj_Key = Key;
    }

    public int getKey() {
        return this.Obj_Key;
    }
    public void setArgv(int index, Object obj) {
        this.argv[index] = obj;
    }

    public Object[] getArgv() {
        return argv;
    }

    public void setArgType(Object[] Types) {
        this.argType = Types;
    }

    public Object[] getArgType() {
        return argType;
    }

//    public void attachRef(RemoteObjectRef ref) {
//        this.ror = ref;
//    }
//
//    public RemoteObjectRef getRor() {
//        return ror;
//    }
//
//    public void setRor(RemoteObjectRef ror) {
//        this.ror = ror;
//    }

    public Exception getExcep() {
        return this.excep;
    }

    public void setExcep(Exception excep) {
        this.excep = excep;
    }

    public void setExcepThronw(boolean excepThronw) {
        this.isexcepThrown = excepThronw;
    }

    public boolean isExcepthrown() {
        return isexcepThrown;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setRet(Object retValue) {
        this.retValue = retValue;
    }

    public Object getRet() {
        return this.retValue;
    }
}