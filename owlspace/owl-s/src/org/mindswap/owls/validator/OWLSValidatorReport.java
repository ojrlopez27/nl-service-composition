package org.mindswap.owls.validator;

import java.util.Set;
import java.util.Map;
import java.util.Iterator;

import java.io.PrintStream;

import org.mindswap.owls.service.Service;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Mindswap (htp://www.mindswap.org)</p>
 * @author Michael Grove
 * @version 1.0
 */


/*
  want uri of the file this report is for??
 */
public class OWLSValidatorReport
{
    private Map mMessageMap;

    public OWLSValidatorReport(Map theMsgs)
    {
        mMessageMap = theMsgs;
    }

    public void print(PrintStream theOut)
    {
        theOut.println("Validation Report");
        if (mMessageMap.isEmpty())
        {
            theOut.println("Valid:\ttrue");
            return;
        }

        Iterator kIter = mMessageMap.keySet().iterator();
        while (kIter.hasNext())
        {
            Object key = kIter.next();
            Set msgSet = (Set)mMessageMap.get(key);
            Service aService = (Service)key;
            boolean valid = msgSet.isEmpty();

            theOut.println("Service:\t"+aService);
            theOut.println("Valid:\t\t"+valid);
            if (!valid)
            {
                theOut.println("Validation messages: ");
                Iterator mIter = msgSet.iterator();
                while (mIter.hasNext())
                {
                    Object msg = mIter.next();
                    theOut.println(msg);
                }
            }
        }
    }

    public Map getMessages() {
        return mMessageMap;
    }

}
