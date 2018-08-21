// The MIT License
//
// Copyright (c) 2004 Evren Sirin
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to
// deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
// sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

/*
 * Created on Dec 29, 2003
 *
 */
package impl.owls.process.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.exceptions.MultipleSatisfiedPreconditionException;
import org.mindswap.exceptions.NotImplementedException;
import org.mindswap.exceptions.UnsatisfiedPreconditionException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.RDFList;
import org.mindswap.owls.generic.expression.Expression;
import org.mindswap.owls.generic.list.OWLSObjList;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.process.AnyOrder;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Binding;
import org.mindswap.owls.process.BindingList;
import org.mindswap.owls.process.Choice;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Condition;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructBag;
import org.mindswap.owls.process.ControlConstructList;
import org.mindswap.owls.process.ForEach;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.OutputBinding;
import org.mindswap.owls.process.OutputBindingList;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.ParameterList;
import org.mindswap.owls.process.ParameterValue;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.RepeatUntil;
import org.mindswap.owls.process.RepeatWhile;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.ResultList;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.SimpleProcess;
import org.mindswap.owls.process.Split;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.process.ValueData;
import org.mindswap.owls.process.ValueOf;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.execution.ProcessExecutionListener;
import org.mindswap.owls.process.execution.ProcessMonitor;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.AtomList;
import org.mindswap.swrl.Variable;

/**
 * @author Evren Sirin
 *
 */
public class ProcessExecutionEngineImpl implements ProcessExecutionEngine {
    public static boolean DEBUG = false;
    
	protected List executionListeners ;
	protected List monitors;

	protected OWLKnowledgeBase env;
	protected OWLKnowledgeBase kb;
	protected boolean checkPreconditions;
	protected boolean allowMultipleSatisifedPreconditions;
	protected boolean stopOnException;
	
	protected Map performResults;
	
	protected Process process;
	
	public ProcessExecutionEngineImpl() {
	    executionListeners = new ArrayList();
	    monitors = new ArrayList();
	    
	    checkPreconditions = true;
	    allowMultipleSatisifedPreconditions = true;
	    stopOnException = true;
	}
	
	public void setKB(OWLKnowledgeBase kb) {
	    this.kb = kb;
	}
	
	public OWLKnowledgeBase getKB() {
	    return kb;
	}
	
	public void setPreconditionCheck(boolean checkPreconditions) {
	    this.checkPreconditions = checkPreconditions;
	}
	
	public boolean isPreconditionCheck() {
	    return checkPreconditions;
	}
	
    public boolean isAllowMultipleSatisifedPreconditions() {
        return allowMultipleSatisifedPreconditions;
    }
    
    public void setAllowMultipleSatisifedPreconditions(boolean allowMultipleSatisifedPreconditions) {
        this.allowMultipleSatisifedPreconditions = allowMultipleSatisifedPreconditions;
    }
	
    /**
     * @deprecated
     */
	protected void notifyListeners(String msg) {
		Iterator i = executionListeners.iterator();
		while(i.hasNext()) {
			ProcessExecutionListener l = (ProcessExecutionListener) i.next();
			l.printMessage(msg);
		}
	}
	
    /**
     * @deprecated
     */	
	protected void setCurrentExecuteService(Process p) {
		Iterator i = executionListeners.iterator();
		while (i.hasNext()) {
			ProcessExecutionListener l = (ProcessExecutionListener) i.next();
			l.setCurrentExecuteService(p);
		}
	}
	
    /**
     * @deprecated
     */
	protected void finishExecution(int retCode) {
		Iterator i = executionListeners.iterator();
		while (i.hasNext())
		{
			ProcessExecutionListener l = (ProcessExecutionListener) i.next();
			l.finishExecution(retCode);
		}
	}

    /**
     * @deprecated
     */
	public void addExecutionListener(ProcessExecutionListener listener) {
		executionListeners.add(listener);	
	}
	
	public void addMonitor(ProcessMonitor monitor) {
		monitors.add(monitor);	
	}

    protected void executionFailed(String msg) throws ExecutionException {
        executionFailed( new ExecutionException( msg ) );
    }

    protected void executionFailed(Exception e) throws ExecutionException {
        executionFailed( new ExecutionException( e ) );
    }
    
    protected void executionFailed(ExecutionException e) throws ExecutionException, UnsatisfiedPreconditionException, MultipleSatisfiedPreconditionException {
    	e.setProcess(process);
        for(Iterator i = monitors.iterator(); i.hasNext();) {
            ProcessMonitor monitor = (ProcessMonitor) i.next();
            monitor.executionFailed(e);
        }
        
        throw e;
    }
    
    protected boolean passFilter( ProcessMonitor monitor, Process process ) {
        int processType = Process.ANY;
        if( process instanceof AtomicProcess )
            processType = Process.ATOMIC;
        else if( process instanceof CompositeProcess )
            processType = Process.COMPOSITE;
        else
            processType = Process.SIMPLE;
        
        return (processType & monitor.getMonitorFilter()) != 0; 
    }
    
    protected void executionStarted(Process process, ValueMap inputs) {
        for(Iterator i = monitors.iterator(); i.hasNext();) {
            ProcessMonitor monitor = (ProcessMonitor) i.next();
            
            if( passFilter( monitor, process) )
                monitor.executionStarted(process, inputs);
        }
    }
    
    protected void executionFinished(Process process, ValueMap inputs, ValueMap outputs) {
        for(Iterator i = monitors.iterator(); i.hasNext();) {
            ProcessMonitor monitor = (ProcessMonitor) i.next();
            if( passFilter( monitor, process) )
                monitor.executionFinished(process, inputs, outputs);
        }
    }
	
    protected void executionStarted() {
        for(Iterator i = monitors.iterator(); i.hasNext();) {
            ProcessMonitor monitor = (ProcessMonitor) i.next();
            monitor.executionStarted();
        }
    }
    
    protected void executionFinished() {
        for(Iterator i = monitors.iterator(); i.hasNext();) {
            ProcessMonitor monitor = (ProcessMonitor) i.next();
            monitor.executionFinished();
        }
    }
    
	protected void initEnv(OWLKnowledgeBase defaultKB) {
	    if(kb != null)
	        env = kb;
	    else
	        env = defaultKB;
	    
	    performResults = new HashMap();
	}
	
	public ValueMap execute(Process p) {	    
	    ValueMap result = execute(p, new ValueMap()); 
	    
	    return result;
	}
	
	public ValueMap execute(Process p, ValueMap values) {
	    initEnv( p.getKB() );
	    
	    process = p;
	    executionStarted();
	    
	    ValueMap result = null;
		try {
            result = executeProcess( p, values );            
        } catch( ExecutionException e ) {
            // executionFailed( e );
        	System.out.println("Excecution failed cause of " + e.getMessage());
        }        
		
		notifyListeners("[DONE]");
		finishExecution(ProcessExecutionListener.EXEC_DONE);
		
		executionFinished();
		
		return result;			
	}
		
	public ValueMap execute(Perform p) {
	    initEnv( p.getKB() );
	    
	    process = p.getProcess();
	    
	    ValueMap result = null;	    
		try {
		    result = executePerform( p );            
        } catch( ExecutionException e ) {
        	// executionFailed( e );
        	System.out.println("Excecution failed");
        }
        
		notifyListeners("[DONE]");
		finishExecution(ProcessExecutionListener.EXEC_DONE);
		
	    return result;
	}
	
	protected ValueMap executeProcess(Process process, ValueMap inputs) {
        ValueMap result = null;

        executionStarted( process, inputs );
            
        setCurrentExecuteService( process );
        
        if( checkPreconditions ) {
            checkPreconditions( process, inputs );
        }     
        
        if(process instanceof AtomicProcess) {
        	result = executeAtomicProcess((AtomicProcess) process, inputs);
        }
        else if(process instanceof CompositeProcess) {
            result = executeCompositeProcess((CompositeProcess) process, inputs);
        }
        else if(process instanceof SimpleProcess) {
            executionFailed(
                new NotImplementedException("Executing simple processes is not implemented!") ); 
        }
        else
        	executionFailed( "Unknown process type " + process ); 

        
        // just for safety
        if( result == null )
            executionFailed( "Null result after executing process " + process + "!" );
        else
        	executionFinished( process, inputs, result );

        return result;
	}
	
	protected ValueMap executeAtomicProcess(AtomicProcess process, ValueMap values) {
	    if(DEBUG) System.out.println("Executing AtomicProcess " + process + "\nInputs:\n" + values.debugString());
	    
	    AtomicGrounding grounding = process.getGrounding(); 
	    
	    if( grounding == null ) throw new ExecutionException( "No grounding for " + process );
	    
	    if(DEBUG) System.out.println("Invoking " + grounding.getDescriptionURL());
	    
	    ValueMap results = grounding.invoke(values, env);
	    results.addMap( values );
	    
	    ResultList resultList = process.getResults();
	    for (int i = 0; i < resultList.size(); i++) {	    	
	    	OWLIndividualList effects = resultList.resultAt(i).getEffects();
	    	for (int j = 0; j < effects.size(); j++) {
	    		Expression effect = (Expression) effects.individualAt(j);
//	    		AtomList newList = effect.getBody().apply(results);
	    		effect.getBody().evaluate(results);
	    	}
	    }
        
        
	    
	    if(DEBUG) System.out.println("Result:\n" + results.debugString() + "\n");
 
	    if( !env.isConsistent() )
	        throw new ExecutionException("Invalid value returned from the process " + process, process);
	    
	    return results;
	}
	
	protected void applyEffects(Expression effect, ValueMap values) {
		AtomList newList = effect.getBody().apply(values);
		newList.evaluate(values);
	}
	
	protected ValueMap executeCompositeProcess(CompositeProcess process, ValueMap values) {
	    ValueMap prevParentPerform = (ValueMap) performResults.get(OWLS.Process.TheParentPerform);
//	    ValueMap prevThisPerform = (ValueMap) performResults.get(OWLS.Process.ThisPerform);
	    
	    performResults.put(OWLS.Process.TheParentPerform, values);
//	    performResults.put(OWLS.Process.ThisPerform, new ValueMap());
	    
		executeConstruct(process.getComposedOf());
			
		Result result = process.getResult();
		if( result != null ) {
			OutputBindingList bindings = result.getBindings();
			for(int i = 0; i < bindings.size(); i++) {
			    OutputBinding binding = bindings.outputBindingAt(i);
			    Output output = binding.getOutput();
			    OWLValue value = null;
			    ParameterValue paramValue = binding.getValue();
			    if(paramValue instanceof ValueOf) {
			        ValueOf valueOf = (ValueOf) paramValue;
			        Perform perform = valueOf.getPerform();
			        Parameter param = valueOf.getParameter();
			        ValueMap performResult = (ValueMap) performResults.get(perform);
				    if(performResult == null)
				        executionFailed( "Perform " + perform + " cannot be found!" );
				    
			        value = performResult.getValue(param);
			    }
			    else
			        executionFailed(
			            new NotImplementedException("Only ValueOf construct in Bindings is supported!") );
			    
			    values.setValue(output, value);
			}
		}
		
//		values.addMap( (ValueMap) performResults.get( OWLS.Process.ThisPerform ) );
				
		performResults.put(OWLS.Process.TheParentPerform, prevParentPerform);
//		performResults.put(OWLS.Process.ThisPerform, prevThisPerform);
		
		return values;
	}
	
	protected ValueMap executePerform(Perform p) {
	    Process process = p.getProcess();
	    
	    if(process == null)
	        executionFailed( "Perform " + p + " does not have a process" ); 

        ValueMap values = new ValueMap();

        ValueMap selfBinding = new ValueMap();

        ValueMap prevThisPerform = (ValueMap) performResults.get(OWLS.Process.ThisPerform);        
        performResults.put(OWLS.Process.ThisPerform, values);

	    BindingList bindings = p.getBindings();
	    for(int i = 0; i < bindings.size(); i++) {
	        Binding binding = bindings.bindingAt(i);
	        Parameter param = binding.getParameter();
	        ParameterValue paramValue = binding.getValue();
	        if(paramValue instanceof ValueData)
	            values.setValue(param, ((ValueData) paramValue).getData());
	        else if(paramValue instanceof ValueOf) {
			    ValueOf valueOf = (ValueOf) paramValue;
			    
			    Perform otherPerform = valueOf.getPerform();
			    Parameter otherParam = valueOf.getParameter();
			    
			    ValueMap performResult = (ValueMap) performResults.get(otherPerform);	
			    if(performResult == null)
			        executionFailed( "Perform " + otherPerform + " cannot be found!" );
			    OWLValue value = performResult.getValue(otherParam);
                if(value == null) {
                    if( otherPerform.equals( Perform.ThisPerform ) )
                        selfBinding.setValue( param, otherParam );
                    else
                        executionFailed( "There is no value for " + param + 
                            " (bound to Perform: " + otherPerform + " Parameter: " + otherParam + ")");
                }
                else
                    values.setValue(param, value);
	        }
	    }
        
        for( Iterator i = selfBinding.getVariables().iterator(); i.hasNext(); ) {
            Parameter param = (Parameter) i.next();
            Parameter otherParam = (Parameter) selfBinding.getValue( param );
            
            OWLValue value = values.getValue( otherParam );
            if(value == null) 
                executionFailed( "There is no value for " + param + 
                    " (bound to Perform: ThisPerform Parameter: " + otherParam + ")");
            values.setValue( param, value );
        }
	    
	    values = executeProcess(process, values);
	    
	    performResults.put(p, values);
        
        performResults.put(OWLS.Process.ThisPerform, prevThisPerform);
	    
	    return values;
	}
	
	protected void executeProduce(Produce produce) {
	    ValueMap values = (ValueMap) performResults.get( OWLS.Process.ThisPerform );
	    
	    OutputBindingList bindings = produce.getBindings();
	    for(int i = 0; i < bindings.size(); i++) {
            OutputBinding binding = bindings.outputBindingAt(i);
    	    Output output = binding.getOutput();

            ParameterValue paramValue = binding.getValue();
            if(paramValue instanceof ValueData)
                values.setValue(output, ((ValueData) paramValue).getData());
            else if(paramValue instanceof ValueOf) {
    		    ValueOf valueOf = (ValueOf) paramValue;
    		    
    		    Perform otherPerform = valueOf.getPerform();
    		    Parameter otherParam = valueOf.getParameter();
    		    
    		    ValueMap performResult = (ValueMap) performResults.get(otherPerform);	
    		    if(performResult == null)
    		        executionFailed( "Perform " + otherPerform + " cannot be found!" );
                values.setValue(output, performResult.getValue(otherParam));
            }            
        }
	}

	protected void executeConstruct(ControlConstruct cc) {
	    if(cc instanceof Perform)
			executePerform((Perform) cc);
	    else if(cc instanceof Sequence)
			executeSequence((Sequence) cc);
		else if(cc instanceof AnyOrder)
			executeAnyOrder((AnyOrder) cc);
		else if(cc instanceof Choice)
			executeChoice((Choice) cc);
		else if(cc instanceof IfThenElse)
			executeIfThenElse((IfThenElse) cc);
		else if(cc instanceof RepeatWhile)
			executeRepeatWhile((RepeatWhile) cc);
		else if(cc instanceof RepeatUntil)
			executeRepeatUntil((RepeatUntil) cc);
		else if(cc instanceof Split)
			executeParallel(((Split)cc).getComponents(), false);
		else if(cc instanceof SplitJoin)
			executeParallel(((SplitJoin)cc).getComponents(), true);
		else if(cc instanceof Produce)
			executeProduce((Produce)cc);		
		else if(cc instanceof ForEach)
			executeForEach((ForEach)cc);
		else		
		    executionFailed( "Unknown control construct " + cc.getConstructName() + "!" ); 
	}
	
	protected void executeSequence(Sequence cc) {
		ControlConstructList ccList = cc.getComponents(); 
		
		for(int i = 0; i < ccList.size(); i++) {
		    ControlConstruct component = ccList.constructAt(i);

			executeConstruct(component);
		}
	}	
	
	protected void executeAnyOrder(AnyOrder cc) {
		ControlConstructBag ccList = cc.getComponents(); 
		
		// AnyOrder says it doesn't matter in which order subelements
		// are executed so let's try the sequential order		
		// FIXME check preconditions to find a correct ordering
		OWLIndividualList list = ccList.getAll();
		for(int i = 0; i < list.size(); i++) {
		    ControlConstruct component = (ControlConstruct) list.individualAt(i);

			executeConstruct(component);
		}
	}	
	
	protected void executeChoice(Choice choice) {
	    ControlConstructBag ccList = choice.getComponents(); 
		
		// Choose a random component to execute 		
		// FIXME check preconditions to find an executable component
		int size = ccList.size();
		int index = new Random().nextInt(size);

		ControlConstruct component = ccList.constructAt(index);

		executeConstruct(component);
	}
	
	protected void executeIfThenElse(IfThenElse ifThenElse) {
	    Condition ifCondition = ifThenElse.getCondition();
	    ControlConstruct thenCC = ifThenElse.getThen();
	    ControlConstruct elseCC = ifThenElse.getElse();

	    if( isTrue( ifCondition ))
	        executeConstruct(thenCC);
	    else if(elseCC != null)
	        executeConstruct(elseCC);
	}
	
	protected void executeRepeatWhile(RepeatWhile cc) {
	    Condition whileCondition = cc.getCondition();
	    ControlConstruct loopBody = cc.getComponent();
	    
	    while( isTrue( whileCondition ) )
	        executeConstruct(loopBody);
	}
		
	protected void executeRepeatUntil(RepeatUntil cc) {
	    Condition repeatCondition = cc.getCondition();
	    ControlConstruct loopBody = cc.getComponent();
	    
	    do {
	        executeConstruct(loopBody);
	    }
	    while( !isTrue( repeatCondition ) );	        
	}
	
	protected void executeForEach(ForEach cc) {
	    ValueMap parentValues = (ValueMap) performResults.get(OWLS.Process.TheParentPerform);
	    
	    ControlConstruct loopBody = cc.getComponent();
	    Variable loopVar = cc.getLoopVar();
	    ValueOf valueOf = cc.getListValue();
	    
	    Perform otherPerform = valueOf.getPerform();
	    Parameter otherParam = valueOf.getParameter();
	    
	    ValueMap performResult = (ValueMap) performResults.get(otherPerform);	
	    if(performResult == null)
	        executionFailed( "Perform " + otherPerform + " cannot be found!" );
	    
	    OWLIndividual ind = performResult.getIndividualValue(otherParam);
	    RDFList list = (RDFList) ind.castTo(OWLSObjList.class);
	    
	    for( ; !list.isEmpty(); list = list.getRest() ) {
	        OWLIndividual value = list.getFirst();
	        parentValues.setValue(loopVar, value);
	        
	        executeConstruct(loopBody);
	    }
	}

	protected void checkPreconditions(Process process, ValueMap values) 
			throws UnsatisfiedPreconditionException, MultipleSatisfiedPreconditionException {
	    ParameterList locals = process.getLocals();
	    
	    for( Iterator i = process.getConditions().iterator(); i.hasNext(); ) {
	        // Get the precondition
	        Condition cond = (Condition) i.next();

		    if(DEBUG) System.out.println("Values="+values);

	        // Get the conjunction of atoms and apply the current value bindings
	        AtomList atoms = cond.getBody();
	        
	        if(DEBUG) System.out.println("Atoms = " + atoms);

	        // This is empty body so nothing to do here (trivially true) 
	        if( atoms == null )
	            continue;
	        
	        atoms = atoms.apply( values );

	        if(DEBUG) System.out.println("Atoms = " + atoms);

	        // Turn this into  
	        ABoxQuery query = atoms.toQuery( locals );
		    
	        if(DEBUG) System.out.println("Query = " + query);
	        
		    List results = env.query( query );
		    
		    if(DEBUG) System.out.println("Query Results = " + results);
		    
	        if( results.isEmpty() )
	            executionFailed( new UnsatisfiedPreconditionException( process, cond ) );
	        else if( results.size() > 1 && !allowMultipleSatisifedPreconditions ) 
	            executionFailed( new MultipleSatisfiedPreconditionException( process, cond ) );
	        
	        ValueMap result = (ValueMap) results.get( 0 );
	        
	        values.addMap( result );
	    }

	}
	
	protected boolean isTrue( Condition condition ) {
	    ValueMap binding = (ValueMap) performResults.get(OWLS.Process.TheParentPerform);
	    return condition.isTrue( env, binding );
	}

	protected boolean isTrue( Condition condition, ValueMap binding ) {
	    return condition.isTrue( env, binding );
	}
	
	class ProcessExecutionThread extends Thread {
	    ControlConstruct cc;
		
		ProcessExecutionThread(ControlConstruct cc) {
			this.cc  = cc;
		}
		
		public void run() {
			executeConstruct(cc);
		}
	}
	
	protected void executeParallel(ControlConstructBag ccList, boolean join) {
		ProcessExecutionThread[] threads = new ProcessExecutionThread[ccList.size()];
		
		for(int i = 0; i < ccList.size(); i++) {
		    ControlConstruct construct = ccList.constructAt(i);
		
			threads[i] = new ProcessExecutionThread(construct);
			
			if(DEBUG) System.out.println("Starting " + construct + "...");
			
			threads[i].start();
		}
		
		if(join) {
			for(int i = 0; i < threads.length; i++) {
				try {
				    if(DEBUG) System.out.println("Waiting " + threads[i].cc + " to finish...");
					threads[i].join();
				    if(DEBUG) System.out.println(threads[i].cc + " finished");
				} catch (InterruptedException e) {
					notifyListeners("[ERROR]\n");
					notifyListeners("Execution Stopped\n"); 
					finishExecution(ProcessExecutionListener.EXEC_ERROR); // done
					executionFailed( e );					
				}
			}
		}
	}
}
