package edu.cmu.inmind.services.osgi.calculator.activator;

import edu.cmu.inmind.osgi.core.main.GenericBundleActivator;
import edu.cmu.inmind.services.osgi.calculator.impl.IntegerCalculatorService;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adangi on 4/21/18.
 */
public class IntegerCalculatorActivator extends GenericBundleActivator {

    public IntegerCalculatorActivator() {
        super();
    }

    @Override
    protected List<Class> getBundleClasses() {
        List<Class> classes = new ArrayList<>();
        classes.add(IntegerCalculatorService.class);
        return classes;
    }

    @Override
    public void shutDown() {
        // release resources
    }
}
