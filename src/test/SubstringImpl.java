package test;

public class SubstringImpl implements Substring {

	/**
	 * 
	 */
	private static final long serialVersionUID = -268435102103680333L;

	@Override
	public String SubString(String s, int start, int end) {
		return s.substring(start, end);
	}

}
