package test;
import utility.Remote440;

import java.rmi.RemoteException;

public interface Hello extends Remote440{
  public String sayHello(String name) throws RemoteException;
}
