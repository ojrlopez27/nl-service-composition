/*
 * Created on Apr 6, 2005
 */
package org.mindswap.swrl;

import impl.swrl.SWRLFactoryImpl;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLOntology;

/**
 * @author Evren Sirin
 *
 */
public class SWRLFactoryCreator {
    public static SWRLFactory createFactory() {
        return createFactory( OWLFactory.createOntology() );
    }
    
    public static SWRLFactory createFactory( OWLOntology ont ) {
        return new SWRLFactoryImpl( ont );
    } 
}
