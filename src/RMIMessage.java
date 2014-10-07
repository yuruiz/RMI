import java.io.Serializable;

/**
 * Created by yuruiz on 10/7/14.
 */
public class RMIMessage implements Serializable {
    private static final long serialVersionUID = -2757242338506557482L;

    private RemoteObjectRef ror;
    private String methodName;
    private boolean isexcepThrown = false;
    private Exception excep;

    public RMIMessage() {

    }

    public RemoteObjectRef getRor() {
        return ror;
    }

    public void setRor(RemoteObjectRef ror) {
        this.ror = ror;
    }

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
}
