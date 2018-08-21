/*
 * Created on Jun 1, 2004
 */
package org.mindswap.owls.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLConfig;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLEntity;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.vocabulary.XSD;
import org.mindswap.owls.generic.expression.Expression;
import org.mindswap.owls.generic.list.OWLSObjList;
import org.mindswap.owls.process.AnyOrder;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Binding;
import org.mindswap.owls.process.BindingList;
import org.mindswap.owls.process.Choice;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Condition;
import org.mindswap.owls.process.ConditionList;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.InputList;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.OutputBindingList;
import org.mindswap.owls.process.OutputList;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.ParameterList;
import org.mindswap.owls.process.ParameterValue;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.ResultList;
import org.mindswap.owls.process.ResultVar;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.Split;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.process.ValueData;
import org.mindswap.owls.process.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.swrl.AtomList;
import org.mindswap.utils.QNameProvider;



/**
 * This is a experimental version of OWL-S presentation syntax writer.
 * 
 * @author Evren Sirin
 */
public class PresentationSyntaxWriter implements ProcessWriter {
    private static final String INDENT = "   ";
    private String indent = "";
    
    private QNameProvider qnames = new QNameProvider();
    
    private PrintWriter out;
    private Writer writer;
    
    private PresentationSyntaxExpressionWriter exprWriter;
 
    private String defaultNS;
    
    public PresentationSyntaxWriter() {
        init();
    }
    
    public String getDefaultNS() {
        return defaultNS;
    }

    public void setDefaultNS( String defaultNS ) {
        if( defaultNS.endsWith( "#" ) )           
            this.defaultNS = defaultNS;
        else
            this.defaultNS = defaultNS + "#";
    }

    public void init() {
        exprWriter = new PresentationSyntaxExpressionWriter();
    }
    
    private String qname( OWLEntity entity ) {
        if( entity.isAnon() ) {            
            return "<< Anonymous " + (entity instanceof OWLClass?"Class":"Individual")+" >>";
        }
        else
            return qnames.shortForm( entity.getURI() );
    }
    
    private String qname( URI uri ) {
        try {
            return qnames.shortForm( uri );
        }
        catch( Exception e ) {
            return "<< Invalid URI >>";
        }
    }
    
    private void indent( boolean left ) {
        if( left )
            indent += INDENT;
        else
            indent = indent.substring( INDENT.length() );
    }
    
    private void printIndented() {
        out.print( indent );
    }

    private void printIndented( String str ) {
        printIndented();
        out.print( str );
    }

    private void printlnIndented( String str ) {
        printIndented();
        out.println( str );
    }

    private String repeat( char c, int n ) {
        char[] chars = new char[n];
        Arrays.fill( chars, c );
        return String.copyValueOf( chars );
    }
    
    private void block( boolean start ) {
        if( start ) {
            printlnIndented( "{" );
            indent( true );
        }
        else {
            indent( false );
            printIndented( "}" );
        }        
    }

    public void setWriter(Writer writer) {
        this.writer = writer; 
    }

    public void setWriter(OutputStream stream) {
        setWriter( new OutputStreamWriter( stream ) );
    }


    public void write(Process process, OutputStream stream) {
        setWriter( stream );
        
        write( process );   
    }
    
    public void write(Process process, Writer writer) {
        setWriter( writer );
        
        write( process );   
    }
    
    public void write(Process process) {
        write( Collections.singleton( process ) );              
    }
    
    public void write(Collection processes) {
        if( defaultNS != null )
            qnames.setMapping( "", defaultNS );
        
        StringWriter buffer = new StringWriter();
        out = new PrintWriter( buffer );        
        exprWriter.setWriter( out );
        exprWriter.setIndent( "" );
        exprWriter.setQNames( qnames );
        
        indent( true );
        for(Iterator i = processes.iterator(); i.hasNext();) {
            Process process = (Process) i.next();
            writeProcess(process);                  
        }
        indent( false );
        
        out.flush();
        
        out = new PrintWriter( writer );
        writeHeader();
        out.println( buffer.toString() );
        writeFooter();
        out.flush();
    }
    
    public void writeHeader() {
        out.println("with_namespaces");

        Set prefixSet = new HashSet( qnames.getPrefixSet() );
        String defaultNS = qnames.getURI( "" );
        if( defaultNS != null ) {
            out.println("  (uri\"" + defaultNS + "\",");
            prefixSet.remove( "" );
        }

        for(Iterator i = prefixSet.iterator(); i.hasNext(); ) {
            String prefix = (String) i.next();
            String uri = qnames.getURI( prefix );
            
            out.print("   " + prefix + ":  \"" + uri + "\"");
            if( i.hasNext() )
                out.println(",");
            else
                out.println(")");
        } 
        out.println("{");
        out.println();
    }

    public void writeFooter() {
        out.println("}");
    }

    private boolean emptyProcess( Process process ) {
        return process.getInputs().isEmpty() &&
               process.getLocals().isEmpty() &&
               process.getOutputs().isEmpty() &&
               process.getConditions().isEmpty() &&
               process.getResults().isEmpty();
    }
    
    private void writeProcess(Process process) {               
        if(process instanceof AtomicProcess) 
            writeProcess((AtomicProcess) process);
        else if(process instanceof CompositeProcess) 
            writeProcess((CompositeProcess) process);
        else
            throw new RuntimeException( "Only writing Atomic and Composite process implemented yet" );
        
        out.println();
    }   
    
    private void writeProcess(CompositeProcess process) {   
        printIndented("define composite process ");
        out.print(process.getLocalName() + "(");
        
        if( !emptyProcess( process ) ) {
            out.println();
            writeIO(process);
            writePreconditions(process);  
            writeResults(process);
        }
        out.println(")");

        ControlConstruct cc = process.getComposedOf();
        if( cc != null ) 
            writeConstruct( cc, true );        
        
        out.println();
    }
    
    private void writeIO( Process process ) {   
        indent( true );
        
        InputList inputs = process.getInputs();
        if (inputs.size()>0)
        {
            printIndented("inputs: (" );
            for(int i = 0; i < inputs.size(); i++) {    
                Input input = (Input)inputs.get(i);
                OWLType type = input.getParamType();
                out.print(qname(input));
                out.print(" - " + qname(type.getURI()));
                if (i<inputs.size()-1)
                {
                    out.println();
                    printIndented("         ");
                }
                else
                    out.print(")");
            }
        }
        
        ParameterList locals = process.getLocals();
        if (locals.size()>0)
        {
            out.println(",");
            printIndented("locals: (" );
            for(int i = 0; i < locals.size(); i++) {    
                Parameter local = (Parameter) locals.get(i);
                OWLType type = local.getParamType();
                out.print(qname(local));
                out.print(" - " + qname(type.getURI()));
                if (i<locals.size()-1)
                {
                    out.println();
                    printIndented("         ");
                }
                else
                    out.print(")");
            }
        }

        OutputList outputs = process.getOutputs();
        if (outputs.size()>0)
        {
            out.println(",");
            printIndented("outputs: (" );
            for(int i = 0; i < outputs.size(); i++) {   
                Output output = (Output) outputs.get(i);
                OWLType type = output.getParamType();
                out.print(qname(output));
                out.print(" - " + qname(type.getURI()));
                if (i<outputs.size()-1)
                {
                    out.println();
                    printIndented("          ");
                }
                else
                    out.print(")");
            }
        }
        
        indent( false );
    }
    
    private void writePerform(Perform perform){
        Process process = perform.getProcess();
        String str = qname(perform) + " :: perform " + qname(process) + "(";
        printIndented( str );
        
        BindingList bindings = perform.getBindings();
        writeBindings( bindings, str.length() );
                
        out.print(")");
    }
    
    private void writeBindings( BindingList bindings, int extraIndent ) {
        String pad = repeat( ' ', extraIndent );
        for(int i = 0; i < bindings.size(); i++) {        
            Binding binding = bindings.bindingAt( i );
            Parameter param = binding.getParameter();
            
            String mappedValue = "";
            ParameterValue mappedParamValue = binding.getValue();
            if(mappedParamValue instanceof ValueOf) {
                ValueOf valueOf = (ValueOf) mappedParamValue;
                
                Perform otherPerform = valueOf.getPerform();
                Parameter otherParam = valueOf.getParameter();
                if( !otherPerform.equals( Perform.TheParentPerform ) )
                    mappedValue = qname(otherPerform) + ".";
                mappedValue += qname(otherParam);
            }
            else if(mappedParamValue instanceof ValueData) {
                OWLValue value = ((ValueData) mappedParamValue).getData();
                if( value.isDataValue() ) {
                    URI datatypeURI = ((OWLDataValue) value).getDatatypeURI();
                    boolean quote = 
                        datatypeURI == null || (
                        !datatypeURI.equals( XSD.xsdBoolean ) &&
                        !datatypeURI.equals( XSD.integer ) &&
                        !datatypeURI.equals( XSD.xsdInt ) &&
                        !datatypeURI.equals( XSD.decimal ) &&
                        !datatypeURI.equals( XSD.xsdDouble ));
                                        
                    mappedValue = ((OWLDataValue)value).toString();
                    if( quote ) {
                        mappedValue = "\"" + mappedValue + "\"";
                        if( datatypeURI != null )
                            mappedValue += "^^" + qname( datatypeURI );
                    }
                }
                else
                    mappedValue = qname(((OWLIndividual)value));
            }            
            else
                mappedValue = "<< Unknown Binding Type >>";
            
            out.print(qname(param) + " <= " + mappedValue);
            if (i < bindings.size()-1) {
                out.print(", ");
                if(bindings.size() > 1) {
                    out.println();
                    printIndented();
                    out.print( pad );
                }
            }                
        }        
    }

//    private void writeConstruct(ControlConstruct cc) {
//        writeConstruct(cc, false);
//    }
    
    private void writeConstruct(ControlConstruct cc, boolean block) {
        if(cc instanceof Perform)
        {
            if( block ) block( true );
            writePerform((Perform) cc);            
            if( block ) {
                out.println();
                block( false );
            }
        }
        else if(cc instanceof Sequence)
        {
            writeComponents( ((Sequence) cc).getComponents(), ";" );
        }
        else if(cc instanceof AnyOrder)
        {
            writeComponents( ((AnyOrder) cc).getComponents(), "||;" );
        }
        else if(cc instanceof Choice)
        {
            writeComponents( ((Choice) cc).getComponents(), ";?" );
        }
        else if(cc instanceof Split)
        {
            writeComponents( ((Split) cc).getComponents(), "||<" );        
        }
        else if(cc instanceof SplitJoin)
        {
            writeComponents( ((SplitJoin) cc).getComponents(), "||>" );
        }
        else if(cc instanceof IfThenElse) 
        {
            if( block ) block( true );
            writeIfThenElse((IfThenElse) cc);
            if( block ) {
                out.println();
                block( false );
            }
        }
        else if(cc instanceof Produce)
        {
            if( block ) block( true );
            writeProduce((Produce) cc);
            if( block ) {
                out.println();
                block( false );
            }
        }
        else
            out.println( "<< Unknown Control Construct " + cc.getConstructName() + " >>" );            
    } 

    private void writeComponents( OWLSObjList components, String delim ) {
        block( true );
        for( Iterator i = components.iterator(); i.hasNext(); ) {
            ControlConstruct cc = (ControlConstruct) i.next();
            writeConstruct( cc, false );
            
            if( i.hasNext() ) 
                out.println( delim );
            else
                out.println();
        }       
        block( false );
    }
    
    private void writeIfThenElse(IfThenElse ifThenElse) {
        Condition condition = ifThenElse.getCondition();
        ControlConstruct thenP = ifThenElse.getThen();
        ControlConstruct elseP = ifThenElse.getElse();
        
        printIndented("if( ");
        exprWriter.setIndent( indent + "    " );
        exprWriter.write( condition.getBody() );
        out.println( " )" );
        
        printlnIndented( "then" );
        writeConstruct( thenP, true );

        if( elseP != null ) {
            out.println();
            printlnIndented( "else" );
            writeConstruct( elseP, true );
        }
    }

    private void writeProduce( Produce produce ) {
        OutputBindingList bindings = produce.getBindings();     
        if( !bindings.isEmpty() ) {
            String str = "produce(";
            printIndented( str );
            writeBindings( bindings, str.length() );
            
            out.print( ")" );
        }    
    }
    
    private void writeProcess(AtomicProcess process) {
        printIndented("define atomic process " + process.getLocalName() + "(");

        if( !emptyProcess( process ) ) {
            out.println();
            writeIO(process);
            writePreconditions(process);
            writeResults(process);
        }
        out.println(")");   
    }
    
    private void writePreconditions(Process process) {
        indent( true );
        
        ConditionList conditions = process.getConditions();
        for(int i = 0; i < conditions.size(); i++) {
            out.println(",");
            printIndented("precondition: (");

            exprWriter.setIndent( indent + "               " );

            Condition condition = conditions.conditionAt(i);                           
            AtomList atoms = condition.getBody();
            exprWriter.write( atoms );
            
            out.print( ")" );
        }
        
        indent( false );
    }

    private void writeResults(Process process) {
        indent( true );
        
        ResultList results = process.getResults();
        for( int r = 0; r < results.size(); r++ ) {
            Result result = results.resultAt( r );
            
            out.println(",");
            printIndented( "result: (" );
            
            ParameterList resultVars = result.getParameters();
            if( !resultVars.isEmpty() ) {
                out.print("forall (" );
                for(int i = 0; i < resultVars.size(); i++) {    
                    ResultVar resultVar = (ResultVar) resultVars.get(i);
                    OWLType type = resultVar.getParamType();
                    out.print(qname(resultVar));
                    out.print(" - " + qname(type.getURI()));
                    if (i<resultVars.size()-1)
                    {
                        out.println();
                        printIndented("                 ");
                    }
                    else
                        out.println(")");
                }  
                printIndented("         ");
            }
            
            Condition condition = result.getCondition();
            if( condition != null && !condition.equals( OWLS.Expression.AlwaysTrue ) ) {
                AtomList atoms = condition.getBody();
    
                exprWriter.setIndent("               ");
                exprWriter.write( atoms );
                out.println();
                printlnIndented( "         |->" );
                printIndented( "         " );
            }
            
            OutputBindingList bindings = result.getBindings();
            if( !bindings.isEmpty() ) {
                out.print( "output(" );
                
                int pad = "result: (".length() + "output(".length();
                writeBindings( bindings, pad );
                out.print( ")" );
            }
            
            Expression effect = result.getEffect();
            if( effect != null ) {
                if( !bindings.isEmpty() ) {
                    out.println( " & " );
                    printIndented( "         " );
                }
                
                AtomList atoms = effect.getBody();
    
                exprWriter.setIndent( "               " );
                exprWriter.write( atoms );
            }
            
            out.print( ")" );
        }
        
        indent( false );
    }

    public Writer getWriter() {
        return writer;
    }

    public ExpressionWriter getExpressionWriter() {
        return exprWriter;
    }

    public void write(Collection processes, Writer out) {
        setWriter( out );
        
        write( processes );
    }

    public void write(Collection processes, OutputStream out) {
        setWriter( out );
        
        write( processes );
    }

    public QNameProvider getQNames() {
        return qnames;
    }

    public void setQNames(QNameProvider qnames) {
        this.qnames = qnames;
    }

    
    public static void main(String[] args) throws URISyntaxException, IOException {              
        OWLConfig.setStrictConversion( false );

        String uri = null;
//        if( args.length == 0 ) {
//            System.out.println( "No URI given!" );
//            return;
//        }
//        
//        uri = args[0];
        
      uri = "http://www.daml.org/services/owl-s/1.2/CongoProcess.owl";
//      uri = "http://www.daml.org/services/owl-s/1.2/BravoAirService.owl";
//      uri = "http://www.cs.umd.edu/~nwlin/haha/services.owl";
//      uri = "http://www.mindswap.org/2004/owl-s/1.1/BookPrice.owl";
//      uri = "http://www.mindswap.org/2004/owl-s/1.1/FindCheaperBook.owl";

        
        PresentationSyntaxWriter writer = new PresentationSyntaxWriter();
        writer.setDefaultNS( uri );
        
        OWLKnowledgeBase kb = OWLFactory.createKB();
        kb.read( uri );

        List processes = new ArrayList();
        processes.addAll( kb.getProcesses( Process.ATOMIC ) );
        processes.addAll( kb.getProcesses( Process.COMPOSITE ) );
        
        if( processes.isEmpty() )
            System.err.println( "No processes found in the URI");
        else
            writer.write( processes, System.out);
    }
}
