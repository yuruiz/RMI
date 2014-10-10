package test;

import utility.Remote440;

public interface ZipCodeServer extends Remote440 {
    public void initialise(ZipCodeList newlist);

    public String find(String city);

    public ZipCodeList findAll();

    public void printAll();
}

