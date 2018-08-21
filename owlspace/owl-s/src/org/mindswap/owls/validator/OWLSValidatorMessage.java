package org.mindswap.owls.validator;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Mindswap (htp://www.mindswap.org)</p>
 * @author Michael Grove
 * @version 1.0
 */

public class OWLSValidatorMessage
{
    private int mCode;
    private String mMessage;

    public OWLSValidatorMessage(int theCode, String theMsg)
    {
        mCode = theCode;
        mMessage = theMsg;
    }

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public String toString() {
        return getMessage();
    }

//    public Service getService() {
//        return mService;
//    }
}
