package test;


import utility.Remote440;
import utility.Remote440Exception;



public interface Hello extends Remote440 {
	public String sayHello(String name) throws Remote440Exception;
}
