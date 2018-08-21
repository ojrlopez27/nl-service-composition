/*
 * Created on May 7, 2005
 */
package org.mindswap.owls.io;

import java.io.OutputStream;
import java.io.Writer;

import org.mindswap.owls.generic.expression.Expression;
import org.mindswap.utils.QNameProvider;

/**
 * @author Evren Sirin
 *
 */
public interface ExpressionWriter {
    public void setWriter(Writer out);
    
    public Writer getWriter();
    
    public void setWriter(OutputStream out);
    
    public void write(Expression expr);
    
    public void write(Expression expr, Writer out);
    
    public void write(Expression expr, OutputStream out);
    
    public String getIndent();
    
    public void setIndent(String indent);

    public boolean getFirstLineIndent();
    
    public void setFirstLineIndent(boolean indent);
    
    public void setQNames(QNameProvider qnames);

    public QNameProvider getQNames();
}
