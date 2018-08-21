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
 * Created on Jul 7, 2004
 */
package impl.owls.generic.expression;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.generic.expression.Expression;
import org.mindswap.owls.generic.expression.LogicLanguage;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 */
public abstract class ExpressionImpl extends WrappedIndividual implements Expression {	
	public ExpressionImpl(OWLIndividual ind, OWLIndividual lang) {
		super(ind);
		
//		setProperty(OWLS.Expression.expressionLanguage, lang);
	}

	public LogicLanguage getLanguage() {
		return (LogicLanguage) getPropertyAs(OWLS.Expression.expressionLanguage, LogicLanguage.class);
	}

	public void setLanguage(LogicLanguage lang) {
	    setProperty(OWLS.Expression.expressionLanguage, lang);
	}
}
