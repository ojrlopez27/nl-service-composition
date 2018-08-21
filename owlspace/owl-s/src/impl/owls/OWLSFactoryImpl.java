/*
 * Created on Mar 31, 2005
 */
package impl.owls;

import impl.owls.process.execution.ProcessExecutionEngineImpl;
import impl.owls.process.execution.ThreadedProcessExecutionEngineImpl;

import java.util.Map;

import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.OWLSVersionTranslator;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.execution.ThreadedProcessExecutionEngine;

/**
 * @author Evren Sirin
 *
 */
public class OWLSFactoryImpl implements OWLSFactory.Interface {

    public String getVersion() {
        return "1.1";
    }

    public Map getDefaultConverters() {
        return OWLSConverters.getConverters();
    }

    public ProcessExecutionEngine createExecutionEngine() {
        return new ProcessExecutionEngineImpl();
    }
    
	// added by Michael Daenzer
	/* (non-Javadoc)
	 * @see org.mindswap.owls.OWLSFactory.Interface#createThreadedExecutionEngine()
	 */
	public ThreadedProcessExecutionEngine createThreadedExecutionEngine() {
		return new ThreadedProcessExecutionEngineImpl();
	}
	// end added by Michael Daenzer

    public OWLSVersionTranslator createVersionTranslator() {
        return new GenericVersionTranslator();
    }

	public OWLSVersionTranslator createVersionTranslator(String version) {
//		if(version.equals("0.7")) return new DAMLSReader_0_7();
//		if(version.equals("0.9")) return new OWLSReader_0_9();
		if(version.equals("1.0")) return new OWLSTranslator_1_0();		
//		if(version.equals("1.0DL")) return new OWLSReader_1_0DL();
		if(version.equals("1.1")) return new OWLSTranslator_1_1();
		
		throw new IllegalArgumentException("There is no translator for OWLS version " + version);
	}

}
