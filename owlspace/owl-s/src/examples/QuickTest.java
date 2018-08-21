package examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLProperty;
import org.mindswap.pellet.jena.PelletReasoner;
import org.mindswap.utils.URIUtils;

/**
 * This class is intended for the developers of the API to perform quick tests.
 * Do use this class for whatever you intend to, but do never ask for support :-)
 *  
 * @author Michael Dänzer (University of Zurich)
 * @date 16.01.2007
 */
public class QuickTest {
	private OWLKnowledgeBase kb;
	
	public static void main(String[] args) {
		QuickTest test = new QuickTest();	
		test.OWL2Java();
	}

	public QuickTest() {
		kb = OWLFactory.createKB();
		kb.setReasoner("Pellet");	
		PelletReasoner r = (PelletReasoner) kb.getReasoner(); 
		r.setDerivationLogging(false);
		
		kb.getReader().getCache().setLocalCacheDirectory("e://workspaces//NExT//Ontologies//ont_cache");
		kb.getReader().getCache().setForced(true);
	}
	
	private void OWL2Java() {
		try {
			OWLOntology ont = kb.read("http://www.w3.org/2001/sw/WebOnt/guide-src/wine.owl");
			OWLIndividual ind = ont.getIndividual(URIUtils.createURI("http://www.w3.org/2001/sw/WebOnt/guide-src/wine#ChateauDeMeursaultMeursault"));
			Map props = ind.getProperties();			
			
			JavaCompiler jc = ToolProvider.getSystemJavaCompiler();  
			StandardJavaFileManager sjfm = jc.getStandardFileManager(null, null, null);  
			
			String userDir = System.getProperty("user.dir") + System.getProperty("file.separator");	
			System.out.println(userDir);
			File javaFile = new File(userDir + "TempOWLClass.java");  
			if (javaFile.exists())
				javaFile.delete();
			javaFile.createNewFile();
			
			FileWriter writer = new FileWriter(javaFile);
			writer.write("public class TempOWLClass {\n");
			Iterator iter = props.values().iterator();
			while (iter.hasNext()) {
				OWLProperty prop = (OWLProperty) iter.next();
				String type = getJavaType(prop);
				String value = "";				
				if (prop instanceof OWLDataProperty) 
					value = ind.getProperty((OWLDataProperty)prop).getLexicalValue();					  
				writer.write("public " + type + " " + prop.getLocalName()+ " = " + value + ";");	
			}
			
			writer.write("\tpublic void printIt(String it) {\n");
			writer.write("\t\tSystem.out.println(it);\n");
			writer.write("\t}\n");
			writer.write("}\n");
			writer.flush();
			
			Iterable fileObjects = sjfm.getJavaFileObjects(javaFile);  
			jc.getTask(null, sjfm, null, null, null, fileObjects).call();  
			sjfm.close(); 
			
			String[] options = new String[]{"-d", userDir};  
			jc.getTask(null, sjfm, null, Arrays.asList(options), null, fileObjects).call();
			
			File outputDir = new File(userDir);  
			URL[] urls = new URL[]{outputDir.toURI().toURL()};  
			URLClassLoader ucl = new URLClassLoader(urls, jc.getClass().getClassLoader());  
			Class clazz = ucl.loadClass("tempOWLClass");  
			Method m = clazz.getDeclaredMethods()[0];		
			Field f = clazz.getDeclaredFields()[0];
			Object o = clazz.newInstance();
			m.invoke(o, "test");			
			System.out.println(f.get(o));			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	
	private String getJavaType(OWLProperty prop) {
		return null;
	}
}
