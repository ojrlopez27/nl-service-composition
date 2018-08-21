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
 * Created on Dec 27, 2003
 *
 */
package org.mindswap.owls.process;

import org.mindswap.owl.OWLModel;
import org.mindswap.owls.generic.expression.Expression;
import org.mindswap.query.ValueMap;

/**
 * A condition is simply an expression. Thhe truth value of a condition needs to be 
 * evaluated with respect to a KB. By default, this KB is the one that the condition
 * is coming from but condition can be evaluated with respect to another KB (execution
 * environment may be different) 
 * 
 * @author Evren Sirin
 *
 */
public interface Condition extends Expression {
    /**
     * Returns true if this condition is true in its own KB. This is equivalent to the
     * call <code>condition.isTrue(condition.getKB(), new ValueMap())</code>.
     * 
     * @return
     */
    public boolean isTrue();
    
    /**
     * Returns true if this condition is true in its own KB after applying the given 
     * <code>binding</code>. Condition expression may have some variables in it, so
     * the <code>binding</code> is applied to the expression and then the resulting
     * condition is evaluated.
     * 
     * @return
     */
    public boolean isTrue(ValueMap binding);
    
    /**
     * Returns true if the condition is true in the given model.
     * @param model
     * @return
     */
    public boolean isTrue(OWLModel model, ValueMap binding);
}
