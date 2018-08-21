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
 * Created on Dec 28, 2004
 */
package impl.owls.generic.expression;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.vocabulary.RDF;
import org.mindswap.owls.generic.expression.Expression;
import org.mindswap.owls.process.Condition;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomList;
import org.mindswap.utils.RDFUtils;


/**
 * @author Evren Sirin
 *
 */
public class SWRLExpressionImpl extends ExpressionImpl implements Expression, Condition {
    public SWRLExpressionImpl(OWLIndividual ind) {
        super(ind, OWLS.Expression.SWRL);
    }
    
	public AtomList getBody() {
	    OWLIndividual ind = null;

	    String body = getPropertyAsString( OWLS.Expression.expressionBody );	    
	    if(body != null) {
	        body = RDFUtils.addRDFTag( body );
	        ind = getOntology().parseLiteral( body );
	    }
        else {
            ind = getProperty( OWLS.Expression.expressionObject );
        }
	    
        return (ind == null) ? null : (AtomList) ind.castTo(AtomList.class);
	}

	public void setBody(AtomList body) {
	    String str = body.toRDF( false, true );
	    OWLDataValue dv = EntityFactory.createDataValue( str, RDF.XMLLiteral );
	    setProperty(OWLS.Expression.expressionBody, dv );
	}

    public boolean isTrue() {
        return getKB().isTrue( this );
    }

    public boolean isTrue(ValueMap binding) {
        return getKB().isTrue( this, binding );
    }
    
    public boolean isTrue(OWLModel model, ValueMap binding) {
        return model.isTrue( this, binding );
    }    
    
	@Override
	public String toPrettyString() {
		String string = toString();
		if (string == null || string.length() == 0)
			return super.toPrettyString();
		else
			return string;
	}

	@Override
	public String toString() {
		AtomList atoms = getBody();						
		String string = "";
		for (int i = 0; i < atoms.size(); i++) {
			Atom atom = atoms.atomAt(i);
			String addOperator = string.length() == 0 ? "" : " AND \n"; 
			string = string + addOperator + atom.toString();
		}
		
		return string.length() > 0 ? string : super.toString();
	}
    
    
}
