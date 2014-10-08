package RMIClient;
import utility.*;

public class RemoteObjectRef {
    String IP_adr;
    int Port;
    int Obj_Key;
    String Remote_Interface_Name;

    public RemoteObjectRef(String ip, int port, int obj_key, String interfaceName) {
        IP_adr = ip;
        Port = port;
        Obj_Key = obj_key;
        Remote_Interface_Name = interfaceName;
    }

    // this method is important, since it is a stub creator.
    // 
    public Object localise() {
        String stubName = "stub." + Remote_Interface_Name + "_stub";
        Object stub = null;
        try {
            Class<?> stubClass;
            stubClass = Class.forName(stubName);
            stub = stubClass.newInstance();
            ((StubInterface) stub).attachRef(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stub;
    }

    public String getIP() {
        return this.IP_adr;
    }

    public int getPort() {
        return this.Port;
    }

    public int getKey() {
        return this.Obj_Key;
    }

    public String getInterface() {
        return this.Remote_Interface_Name;
    }
}
