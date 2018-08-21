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
 * Created on Dec 15, 2004
 */
package org.mindswap.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomList;
import org.mindswap.swrl.SWRLObject;
import org.mindswap.swrl.Variable;

/**
 * @author Evren Sirin
 */
public class ABoxQuery {
    private AtomList body;
    private List resultVars;

    public ABoxQuery( AtomList body ) {
        this( body, body.getVars() );
    }
    
    public ABoxQuery( AtomList body, Collection resultVars ) {
        this.body = body;
        this.resultVars = new ArrayList( resultVars );
    }
    
    public void addResultVar( Variable var ) {
        resultVars.add( var );
    }
    
    public void addResultVars( Collection vars ) {
        resultVars.addAll( vars );
    }

    public AtomList getBody() {
        return body;
    }
    
    public List getResultVars() {
        return resultVars;
    }

    public List getVars() {
        List vars = new ArrayList();
        
        for( AtomList list = body; !list.isEmpty(); list = (AtomList) list.getRest() ) {
            Atom atom = (Atom) list.getFirst();
            for( int j = 0; j < atom.getArgumentCount(); j++ ) {
                SWRLObject term = atom.getArgument( j );
                if( term instanceof Variable )
                    vars.add( term );                
            }
        }
        return vars;
    }

    public List run(OWLKnowledgeBase kb) {
        return kb.query( this );
    }


    public ABoxQuery apply(ValueMap binding) {
        AtomList atoms = getBody().apply( binding );
        
        List newResultVars = new ArrayList( resultVars );
        newResultVars.removeAll( binding.getVariables() );

        return new ABoxQuery( atoms, newResultVars );
    }
   
    public String toString() {
        return "query(" + getResultVars() + ") :- " + getBody().toString();
    }
}
