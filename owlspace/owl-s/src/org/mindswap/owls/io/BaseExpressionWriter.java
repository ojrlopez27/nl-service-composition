/*
 * Created on May 7, 2005
 */
package org.mindswap.owls.io;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.generic.expression.Expression;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomList;
import org.mindswap.swrl.BuiltinAtom;
import org.mindswap.swrl.ClassAtom;
import org.mindswap.swrl.DataPropertyAtom;
import org.mindswap.swrl.DifferentIndividualsAtom;
import org.mindswap.swrl.IndividualPropertyAtom;
import org.mindswap.swrl.SameIndividualAtom;
import org.mindswap.utils.QNameProvider;

/**
 * @author Evren Sirin
 *
 */
public abstract class BaseExpressionWriter implements ExpressionWriter {
    protected QNameProvider qnames;
    protected PrintWriter out;
    protected String indent;
    protected boolean firstLineIndent;
   
    public BaseExpressionWriter() {
        firstLineIndent = false;
    }
    
    public void setWriter(Writer out) {
        if( out instanceof PrintWriter )
            this.out = (PrintWriter) out;
        else
            this.out = new PrintWriter( out );
    }
    
    public Writer getWriter() {
        return out;
    }
    
    public void setWriter(OutputStream out) {
        setWriter( new PrintWriter( out ) );
    }
    
    public String getIndent() {
        return indent;
    }
    
    public void setIndent(String indent) {
        this.indent = indent;
    }
    
    public boolean getFirstLineIndent() {
        return firstLineIndent;
    }
    
    public void setFirstLineIndent(boolean indent) {
        firstLineIndent = indent;
    }
    
    public void setQNames(QNameProvider qnames) {
        this.qnames = qnames;
    }

    public QNameProvider getQNames() {
        return qnames;
    }

    public void write(Expression expr, Writer out) {
        if( out instanceof PrintWriter )
            this.out = (PrintWriter) out;
        else
            this.out = new PrintWriter( out );
        
        boolean noQNameProvider = ( qnames == null );
        if( noQNameProvider )
            qnames = expr.getKB().getQNames();
        
        write( expr );
        
        if( noQNameProvider )
            qnames = null;        
    }

    public void write(Expression expr, OutputStream out) {
        write( expr, new PrintWriter( out ) );
    }
    
    public void write( Expression expr ) {
        write( expr.getBody() );
    }
    
    public abstract void write( AtomList atoms ); 
    
    public void write( Atom atom ) {        
        if(atom instanceof ClassAtom) 
            write( (ClassAtom) atom );
        else if(atom instanceof IndividualPropertyAtom)
            write( (IndividualPropertyAtom) atom );                	            
        else if(atom instanceof DataPropertyAtom) 
            write( (DataPropertyAtom) atom );
        else if(atom instanceof SameIndividualAtom) 
            write( (SameIndividualAtom) atom );
        else if(atom instanceof DifferentIndividualsAtom) 
            write( (DifferentIndividualsAtom) atom ); 
        else if(atom instanceof BuiltinAtom)
            write( (BuiltinAtom) atom );
    }

    public abstract void write( ClassAtom atom ); 

    public abstract void write( IndividualPropertyAtom atom ); 

    public abstract void write( DataPropertyAtom atom ); 

    public abstract void write( SameIndividualAtom atom ); 

    public abstract void write( DifferentIndividualsAtom atom ); 

    public abstract void write( BuiltinAtom atom ); 

    public void print( OWLValue value ) {
        try {
            if( value instanceof OWLIndividual ) {
                OWLIndividual ind = (OWLIndividual) value;
                if( ind.isAnon() )
                    out.print( "<< Anonymous Individual >>" );
                else
                    print( ind.getURI() );
            }
            else
                out.print( value );
        }
        catch( RuntimeException e ) {
            out.print( "<< Invalid URI >>" );
        }
    }
    
    public void print( OWLClass cls ) {
        if( cls.isAnon() )
            out.print( "<< Anonymous Class >>" );
        else
            print( cls.getURI() );
    }
    
    public void print( OWLProperty prop ) {
        print( prop.getURI() );
    }
    
    public void print( URI uri ) {
        out.print( qnames.shortForm( uri ) );
    }
    
}
