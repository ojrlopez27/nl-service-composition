/*
 * Created on May 7, 2005
 */
package org.mindswap.owls.io;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;

import org.mindswap.owls.process.Process;
import org.mindswap.utils.QNameProvider;

/**
 * @author Evren Sirin
 *
 */
public interface ProcessWriter {
    public void setWriter(Writer out);
    
    public Writer getWriter();
    
    public void setWriter(OutputStream out);
    
    public ExpressionWriter getExpressionWriter();
    
    public void write(Process process);
    
    public void write(Process process, Writer out);
    
    public void write(Process process, OutputStream out);
    
    public void write(Collection processes);
    
    public void write(Collection processes, Writer out);
    
    public void write(Collection processes, OutputStream out);    

    public QNameProvider getQNames();
    
    public void setQNames(QNameProvider qnames);
}
