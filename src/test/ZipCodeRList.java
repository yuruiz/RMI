package test;

import utility.Remote440;

public interface ZipCodeRList extends Remote440 {
    public String find(String city);

    public ZipCodeRList add(String city, String zipcode);

    public ZipCodeRList next();
}
