/*
 * Created on Mar 30, 2005
 */
package impl.jena;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mindswap.exceptions.NotImplementedException;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.ABoxQueryParser;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomList;
import org.mindswap.swrl.BuiltinAtom;
import org.mindswap.swrl.SWRLDataObject;
import org.mindswap.swrl.SWRLDataValue;
import org.mindswap.swrl.SWRLDataVariable;
import org.mindswap.swrl.SWRLFactory;
import org.mindswap.swrl.SWRLFactoryCreator;
import org.mindswap.swrl.SWRLIndividual;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLIndividualVariable;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.query.Expression;
import com.hp.hpl.jena.rdf.model.impl.LiteralImpl;
import com.hp.hpl.jena.rdql.Query;
import com.hp.hpl.jena.rdql.parser.ParsedLiteral;
import com.hp.hpl.jena.rdql.parser.Q_Equal;
import com.hp.hpl.jena.rdql.parser.Q_GreaterThan;
import com.hp.hpl.jena.rdql.parser.Q_GreaterThanOrEqual;
import com.hp.hpl.jena.rdql.parser.Q_LessThan;
import com.hp.hpl.jena.rdql.parser.Q_LessThanOrEqual;
import com.hp.hpl.jena.rdql.parser.Q_NotEqual;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * @author Evren Sirin
 *
 */
public class RDQLParser implements ABoxQueryParser {
    private static final String varNS = "var:";
    
    private OWLModel model;
    
    public RDQLParser(OWLModel model) {        
        this.model = model;
    }
    
    public ABoxQuery parse( String rdqlString ) {        
        try {            
            OWLOntology ont = OWLFactory.createOntology();   
            SWRLFactory swrl = SWRLFactoryCreator.createFactory( ont );
            List resultVars = new ArrayList();
            Map vars = new HashMap();
            
            Query rdql = new Query( rdqlString );
            AtomList atomList = swrl.createList();            
            for( Iterator i = rdql.getTriplePatterns().iterator(); i.hasNext(); ) {
            	Triple t = (Triple) i.next();

            	if( t.getPredicate().isVariable() )
            		throw new IllegalArgumentException("Variables cannot be used in predicate position in AboxQuery");
            	
            	Atom atom = null;
            	URI pred = new URI( t.getPredicate().getURI() );	
            	if( pred.toString().equals( RDF.type.getURI() ) ) {
            	    if( t.getObject().isVariable() )
            	        throw new IllegalArgumentException("Variables cannot be used as objects of rdf:type triples in ABoxQuery");
            	    
            	    OWLClass c = ont.createClass( new URI( t.getObject().getURI() ) );
            	    SWRLIndividualObject arg = makeIndividalObject( t.getSubject(), ont );
            	    
            	    atom = swrl.createClassAtom( c, arg );
            	    
            	    vars.put(t.getSubject().toString(), arg);
            	} 
            	else {
            		OWLProperty p = model.getProperty( pred );

            		if( p == null ) 
            			throw new IllegalArgumentException( pred + " is not a known [Object|Data]Property." );
            		else if( p instanceof OWLDataProperty ) { 
            		    OWLDataProperty dp = ont.createDataProperty( p.getURI() ) ;
                	    SWRLIndividualObject arg1 = makeIndividalObject( t.getSubject(), ont );
                	    SWRLDataObject arg2 = makeDataObject( t.getObject(), ont );
            		    
            		    vars.put(t.getSubject().toString(), arg1);
            		    vars.put(t.getObject().toString(), arg2);
            		    
            		    atom = swrl.createDataPropertyAtom( dp, arg1, arg2 );
            		}
            		else {
                	    OWLObjectProperty op = ont.createObjectProperty( p.getURI() ) ;
                	    SWRLIndividualObject arg1 = makeIndividalObject( t.getSubject(), ont );
                	    SWRLIndividualObject arg2 = makeIndividalObject( t.getObject(), ont );
            		    
            		    vars.put(t.getSubject().toString(), arg1);
            		    vars.put(t.getObject().toString(), arg2);
            		    
            		    atom = swrl.createIndividualPropertyAtom( op, arg1, arg2 );			    
            		}
            	}
            	
            	atomList = atomList.add( atom );
            }
            
            for( Iterator i = rdql.getConstraints().iterator(); i.hasNext(); ) {
    			Expression expr = (Expression) i.next();
    			
    			SWRLDataObject arg1 = makeDataObject( expr.getArg(0), ont) ;
    			SWRLDataObject arg2 = makeDataObject( expr.getArg(1), ont );
    			
    			BuiltinAtom atom = null;
    			if ( expr instanceof Q_Equal ) 
    			    atom = swrl.createEqual( arg1, arg2 );
    			else if ( expr instanceof Q_NotEqual ) 
    			    atom = swrl.createNotEqual( arg1, arg2 );
    			else if ( expr instanceof Q_GreaterThan ) 
    			    atom = swrl.createGreaterThan( arg1, arg2 );
    			else if ( expr instanceof Q_GreaterThanOrEqual ) 
    			    atom = swrl.createGreaterThanOrEqual( arg1, arg2 );
    			else if ( expr instanceof Q_LessThan )
    			    atom = swrl.createLessThan( arg1, arg2 );
    			else if ( expr instanceof Q_LessThanOrEqual ) 
    			    atom = swrl.createLessThanOrEqual( arg1, arg2 );
    			else
    			    throw new NotImplementedException();
    			
    			atomList.add( atom );
    		}

            
            for( Iterator i = rdql.getResultVars().iterator(); i.hasNext(); ) {
            	String var = (String) i.next();

            	resultVars.add( vars.get( "?" + var ) );
            }            

            return new ABoxQuery( atomList, resultVars );
            
        } catch(URISyntaxException e) {
            throw new IllegalArgumentException( e.getInput() + " is not a valid URI!");
        }
    }

	private SWRLDataObject makeDataObject( Expression expr, OWLOntology ont ) throws URISyntaxException {
	    if( expr.isVariable() ) {
		    OWLIndividual ind = ont.createInstance( SWRL.Variable, new URI( varNS + expr.getName() ) );
			return (SWRLDataObject) ind.castTo(SWRLDataVariable.class);
	    }
	    else if( expr.isConstant() ) {
	        OWLDataValue value = null;
	        if( expr instanceof ParsedLiteral ) {
	            ParsedLiteral lit = (ParsedLiteral) expr;
	            if( lit.isNode() ) {
	                throw new NotImplementedException();
	            }
	            else if( lit.isInt() ) {
	                value = ont.createDataValue( new Long( lit.getInt()) );
	            }
	            else if( lit.isDouble() ) {
	                value = ont.createDataValue( new Double( lit.getInt()) );
	            }
	            else if( lit.isBoolean() ) {
	                value = ont.createDataValue( lit.getBoolean() ? Boolean.TRUE : Boolean.FALSE );
	            }
	            else if( lit.isString() ) {
	                value = ont.createDataValue( lit.getString() );
	            }
	            else if( lit.isURI() ) {
	                value = ont.createDataValue( URI.create( lit.getURI() ) );
	            }
	            else
	                throw new IllegalArgumentException();
	        }
	        else 
			    value = ont.createDataValue( expr.getValue() );
	        
			return (SWRLDataValue) value.castTo(SWRLDataValue.class);
	    }
		else {
		    throw new NotImplementedException();
		}
	}

	private SWRLDataObject makeDataObject( Node node, OWLOntology ont ) throws URISyntaxException {
	    if( node.isVariable() ) {
		    OWLIndividual ind = ont.createInstance( SWRL.Variable, new URI( varNS + node.getName() ) );
			return (SWRLDataObject) ind.castTo(SWRLDataVariable.class);
	    }
		else {
		    OWLDataValue value = new OWLDataValueImpl( new LiteralImpl( node, (EnhGraph) ont.getImplementation() ) );
			return (SWRLDataValue) value.castTo(SWRLDataValue.class);
		}
	}
	
	private SWRLIndividualObject makeIndividalObject( Node node, OWLOntology ont ) throws URISyntaxException {
	    if( node.isVariable() )  {
		    OWLIndividual ind = ont.createInstance( SWRL.Variable, new URI( varNS + node.getName() ) );
			return (SWRLIndividualObject) ind.castTo( SWRLIndividualVariable.class );
	    }
	    else {
            OWLIndividual ind = ont.createInstance( org.mindswap.owl.vocabulary.OWL.Thing, new URI(
                node.getURI() ) );
            return (SWRLIndividualObject) ind.castTo( SWRLIndividual.class );
        } 
	}
}
