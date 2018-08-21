/*
 * Created on May 7, 2005
 */
package org.mindswap.owls.io;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.vocabulary.SWRLB;
import org.mindswap.owls.io.BaseExpressionWriter;
import org.mindswap.owls.io.ExpressionWriter;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomList;
import org.mindswap.swrl.BuiltinAtom;
import org.mindswap.swrl.ClassAtom;
import org.mindswap.swrl.DataPropertyAtom;
import org.mindswap.swrl.DifferentIndividualsAtom;
import org.mindswap.swrl.IndividualPropertyAtom;
import org.mindswap.swrl.SameIndividualAtom;


/**
 * @author Evren Sirin
 *
 */
public class PresentationSyntaxExpressionWriter extends BaseExpressionWriter implements ExpressionWriter {
    static final Map SYMBOLS;
    static {
        SYMBOLS = new HashMap();
        SYMBOLS.put( SWRLB.add,                new String[] {"=", "+"} );
        SYMBOLS.put( SWRLB.subtract,           new String[] {"=", "-"} );
        SYMBOLS.put( SWRLB.divide,             new String[] {"=", "+"} );
        SYMBOLS.put( SWRLB.multiply,           new String[] {"=", "*"} );
        SYMBOLS.put( SWRLB.lessThan,           new String[] {"<"} );
        SYMBOLS.put( SWRLB.lessThanOrEqual,    new String[] {"<="} );
        SYMBOLS.put( SWRLB.greaterThan,        new String[] {">"} );
        SYMBOLS.put( SWRLB.greaterThanOrEqual, new String[] {">="} );
        SYMBOLS.put( SWRLB.equal,              new String[] {"="} );
        SYMBOLS.put( SWRLB.notEqual,           new String[] {"~", "="} );
    }
    
    public PresentationSyntaxExpressionWriter() {
       
    }
    
    public void write( AtomList atoms ) {
        if( atoms == null ) {
            out.print( "<No-Condition-Specified>" );
            return;
        }
        boolean first = true;
        for(Iterator it = atoms.iterator(); it.hasNext();  ) {
            if( !first || firstLineIndent )
                out.print( indent );
            else
                first = false;            
            
            write( (Atom) it.next() );
            
            if( it.hasNext() )
                out.println( " &" );
        }   
    }

    public void write(ClassAtom atom) {
        print( atom.getClassPredicate() );
        out.print("(");
        print( atom.getArgument1() );
        out.print(")");
    }

    public void write(IndividualPropertyAtom atom) {
        print( atom.getPropertyPredicate() );
        out.print("(");
        print( atom.getArgument1() );
        out.print(", ");
        print( atom.getArgument2() );
        out.print(")");
    }

    public void write(DataPropertyAtom atom) {
        print( atom.getPropertyPredicate() );
        out.print("(");
        print( atom.getArgument1() );
        out.print(", ");
        print( atom.getArgument2() );
        out.print(")");
    }

    public void write(SameIndividualAtom atom) {
        out.print("(");
        print( atom.getArgument1() );
        out.print(" = ");
        print( atom.getArgument2() );
        out.print(")");
    }

    public void write(DifferentIndividualsAtom atom) {
        out.print("~(");
        print( atom.getArgument1() );
        out.print(" = ");
        print( atom.getArgument2() );
        out.print(")");
    }

    public void write(BuiltinAtom atom) {
        OWLIndividual builtin = atom.getBuiltin();
        int count = atom.getArgumentCount();
        String[] symbols = (String[]) SYMBOLS.get( builtin );
        if( symbols != null ) {
            int extra = 0;
            if( symbols.length == count ){
                out.print( symbols[0] );
                extra = 1;
            }
            
            out.print( "(" );            
            for( int i = 0; i < count; i++ ) {
                out.print( atom.getArgument( i ) );
                if( i < count - 1 )
                    out.print( " " + symbols[i + extra] + " ");
            }
            out.print(")");  
        }
        else {
            print( builtin );
            out.print( "(" );
            
            for( int i = 0; i < atom.getArgumentCount(); i++ ) {
                if( i > 0 ) out.print( ", " );
                print( atom.getArgument(0) );
            }
            out.print(")");  
        }
    }


}
