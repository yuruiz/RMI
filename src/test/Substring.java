package test;

import utility.Remote440;
import utility.Remote440Exception;

public interface Substring extends Remote440 {
	public String SubString(String s, Integer start, Integer end)
			throws Remote440Exception;
}
