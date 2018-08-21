package org.mindswap.owls.validator;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Mindswap (htp://www.mindswap.org)</p>
 * @author Michael Grove
 * @version 1.0
 */

public class OWLSValidationException extends Exception
{
    public static final int ERROR_PARSE = 0;
    public static final int ERROR_FILE_NOT_FOUND = 1;

    private int mId;

    public OWLSValidationException(int theId, String theMsg)
    {
        super(theMsg);
        mId = theId;
    }

    public int getId() {
        return mId;
    }

    public static OWLSValidationException createParseException(String theMsg) {
        return new OWLSValidationException(ERROR_PARSE,theMsg);
    }

    public static OWLSValidationException createFileNotFoundException(String theMsg) {
        return new OWLSValidationException(ERROR_FILE_NOT_FOUND,theMsg);
    }
}
