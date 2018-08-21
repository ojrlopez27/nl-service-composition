package org.mindswap.owls.vocabulary;

import java.util.HashSet;
import java.util.Hashtable;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.utils.URIUtils;

/**
 * @author Zhexuan Song
 *
 * Version independent vacabulary
 */
public class FLAServiceOnt {
	
	/*******************************************
	 ** added by guang huang @2005-4-8 **
	 ********************************************/
	public static HashSet flaDataProperties = new HashSet();
	public static Hashtable flaObjectPropertiesToDataProperties = new Hashtable();	
	public static Hashtable flaObjectProperties = new Hashtable();
	/*******************************************
	 ** end by guang huang **
	 ********************************************/
	
	public final static String URI = "http://www.flacp.fujitsulabs.com/tce/ontologies/2005/01/service.owl#";

	// Profile hierarchical structure
	public final static OWLClass TranslatorProfile = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "TranslatorProfile"));	
	public final static OWLClass ProducerProfile = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "ProducerProfile"));	
	public final static OWLClass ConsumerProfile = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "ConsumerProfile"));	
	public final static OWLClass DeviceProfile = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "DeviceProfile"));	
	public final static OWLClass InstanceProvidingServiceProfile = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "InstanceProvidingServiceProfile"));	
	public final static OWLClass ViewerProfile = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "ViewerProfile"));	
	public final static OWLClass PrinterProfile = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "PrinterProfile"));	
	public final static OWLClass DigitalCameraProfile = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "DigitalCameraProfile"));	
	public final static OWLClass InternalServiceProfile = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "InternalServiceProfile"));
	
	// multimedia description
	public final static OWLDataProperty imageDescription = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "imageDescription"));	
	public final static OWLDataProperty iconDescription = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "iconDescription"));	
	public final static OWLDataProperty voiceDescription = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "voiceDescription"));	
	public final static OWLDataProperty videoDescription = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "videoDescription"));	
	static {
		flaDataProperties.add(imageDescription);
		flaDataProperties.add(iconDescription);		
		flaDataProperties.add(voiceDescription);
		flaDataProperties.add(videoDescription);		
	}
	
	// Location related
//	  <tce-service:locatedAt>
//	    <tce-service:Location>
//	      <profile:sParameter>
//	        <geoF:Point rdf:ID="ViewServicePosition">
//	          <rdfs:label>On the table in the FLA, CP Conference Room</rdfs:label>
//	          <geoF:xyzCoordinates>2,3,0.8</geoF:xyzCoordinates>
//	          <geoC:hasCoordinateSystem rdf:resource="http://www.flacp.fujitsulabs.com/tce/ontologies/2004/03/geo.owl#MyCoordinateSystem"/>
//	        </geoF:Point>
//	      </profile:sParameter>
//	   </tce-service:Location>
//	  </tce-service:locatedAt>
	  public final static OWLObjectProperty locatedAt = 
	      EntityFactory.createObjectProperty(URIUtils.createURI(URI + "locatedAt"));
	public final static OWLClass Location = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "Location"));

	static {
		flaObjectProperties.put(locatedAt, Location);
	}			

	// Service Control UI
//	  <tce-service:hasServiceControlUI>
//	    <tce-service:ServiceControlUI>
//	      <profile:sParameter rdf:resource="http://128.8.244.15/DisplayURL2/controller.aspx" />
//	   </tce-service:ServiceControlUI>
//	  </tce-service:hasServiceControlUI>
	public final static OWLObjectProperty hasServiceControlUI = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasServiceControlUI"));
	public final static OWLClass ServiceControlUI = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "ServiceControlUI"));
	static {
		flaObjectProperties.put(hasServiceControlUI, ServiceControlUI);
	}		  
	// Owner
//	  <fla:ownedBy xmlns:fla="http://www.flacp.fujitsulabs.com/tce/ontologies/2005/01/service.owl">
//	    <fla:Owner>
//	      <profile:sParameter>
//	        <fla:OwnerEntity rdf:ID="Owner">
//	          <rdfs:label>Jon Doe</rdfs:label>
//	          <fla:ownerEntityID>jdoe</fla:ownerEntityID>
//	        </fla:OwnerEntity>
//	      </profile:sParameter>
//	    </fla:Owner>
//	  </fla:ownedBy>
	public final static OWLObjectProperty ownedBy = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "ownedBy"));
	public final static OWLClass Owner = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "Owner"));
	public final static OWLClass OwnerEntity = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "OwnerEntity"));
	public final static OWLDataProperty ownerEntityID = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "ownerEntityID"));
	static {
		flaObjectProperties.put(ownedBy, Owner);		
	}	
	// Security Setting
//	  <fla:hasSecuritySetting xmlns:fla="http://www.flacp.fujitsulabs.com/tce/ontologies/2005/01/service.owl">
//	    <fla:SecurityParameter>
//	      <profile:sParameter>
//	        <fla:SecuritySetting rdf:ID="securitySetting">
//	          <rdfs:label>Fujitsu Lab of America</rdfs:label>
//	          <fla:certificateAuthorityName>FLA</fla:certificateAuthorityName>
//	        </fla:SecuritySetting>
//	      </profile:sParameter>
//	    </fla:SecurityParameter>
//	  </fla:hasSecuritySetting>
	public final static OWLObjectProperty hasSecuritySetting = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasSecuritySetting"));
	public final static OWLClass SecurityParameter = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "SecurityParameter"));
	public final static OWLClass SecuritySetting = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "SecuritySetting"));
	public final static OWLDataProperty certificateAuthorityName = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "certificateAuthorityName"));
	
	static {
		flaObjectProperties.put(hasSecuritySetting, SecurityParameter);
	}		  		
	// self destruction function
//    <tce-service:hasSelfDestructionService>
//    <tce-service:SelfDestructionService>
//      <profile:sParameter rdf:resource="#DestroyObject"/>
//    </tce-service:SelfDestructionService>
//  </tce-service:hasSelfDestructionService>
	public final static OWLClass SelfDestructionService = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "SelfDestructionService"));
	public final static OWLObjectProperty  hasSelfDestructionService = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasSelfDestructionService"));	
	static {
		flaObjectProperties.put(hasSelfDestructionService, SelfDestructionService);
	}		
	// Process parameter strict type
//	<process:Input rdf:ID="URLInput">
//	  <fla:strictType>http://www.flacp.fujitsulabs.com/tce/ontologies/2004/03/object.owl#ViewableFile</fla:strictType>
//	</process:Input>
	public final static OWLDataProperty strictType = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "strictType"));
	static {
		flaDataProperties.add(strictType);
	}		  					
	public final static OWLObjectProperty strictTypeO = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "strictType"));
	static {
		flaObjectPropertiesToDataProperties.put(strictTypeO, strictType);
	}		  					
	// UPnP grounding
//<fla:UPnPGrounding rdf:ID="SemanticStreamProviderGrounding">
//    <service:supportedBy rdf:resource="#SemanticStreamProviderService" />
//    <fla:hasUPnPAtomicProcessGrounding rdf:resource="#SemanticStreamProviderProcessGrounding" />
//</fla:UPnPGrounding>
//<fla:UPnPAtomicProcessGrounding rdf:ID="SemanticStreamProviderProcessGrounding">
//    <grounding:damlsProcess rdf:resource="#SemanticStreamProvider"/>
//    <fla:upnpCommand>getStreamURL</fla:upnpCommand>
//    <fla:UPnPOutputMapping rdf:parseType="Collection">
//        <fla:UPnPMap>
//            <grounding:damlsParameter rdf:resource="#SemanticStreamOutput" />
//            <fla:upnpParameter>_ReturnValue</fla:upnpParameter>
//        </fla:UPnPMap>
//    </fla:UPnPOutputMapping>
//    <fla:upnpServiceID>upnp:id:stream-service-1.0:128.8.244.170:2</fla:upnpServiceID>
//    <fla:upnpDeviceURL>http://streaming.flacp.fujitsulabs.com:35002/</fla:upnpDeviceURL>
//</fla:UPnPAtomicProcessGrounding>
	public final static OWLClass UPnPGrounding = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "UPnPGrounding"));
	public final static OWLClass UPnPAtomicProcessGrounding = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "UPnPAtomicProcessGrounding"));
	
	public final static OWLObjectProperty hasUPnPAtomicProcessGrounding = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasUPnPAtomicProcessGrounding"));
	public final static OWLObjectProperty upnpOutputMapping = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "upnpOutputMapping"));
	public final static OWLObjectProperty upnpInputMapping  = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "upnpInputMapping"));
	
	public final static OWLDataProperty upnpParameter = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "upnpParameter"));	
	public final static OWLDataProperty upnpServiceID = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "upnpServiceID"));
	public final static OWLDataProperty upnpDeviceURL = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "upnpDeviceURL"));
	public final static OWLDataProperty upnpCommand = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "upnpCommand"));
	public static final OWLClass UPnPMap = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "UPnPMap"));
	
	// REST invocation indication
	public final static OWLDataProperty restInvocation = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "restInvocation"));
	static {
		flaDataProperties.add(restInvocation);
	}		  				
	// whether or not transform the input to its own accept type
	public static final OWLDataProperty transformInputType = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "transformInputType"));
	// whether or not create a random input for the field during execution
	public static final OWLDataProperty useRandomInput = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "useRandomInput"));
	static {
		flaDataProperties.add(transformInputType);
		flaDataProperties.add(useRandomInput);		
	}			
	// Meta-data properties
	// Service -> Profile
	public final static OWLObjectProperty presents = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "presents"));
	// Service -> Process
	public final static OWLObjectProperty describedBy = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "describedBy"));
	// Service -> service names
	public final static OWLDataProperty serviceName = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "serviceName"));
	// service -> service description
	public final static OWLDataProperty textDescription = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "textDescription"));
	// service -> service description URL
	public final static OWLDataProperty serviceDescriptionURL = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "serviceDescriptionURL"));
	// service -> service description text
	public final static OWLDataProperty serviceDescriptionText = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "serviceDescriptionText"));
	// service -> input
	public final static OWLObjectProperty input = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "input"));
	// service -> service input count
	public final static OWLDataProperty inputCount = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "inputCount"));
	// service -> output
	public final static OWLObjectProperty output = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "output"));
	// service -> service output count
	public final static OWLDataProperty outputCount = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "outputCount"));
	// service -> location name
	public final static OWLDataProperty location = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "location"));
	// service -> coordinates
	public final static OWLDataProperty coordinates = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "coordinates"));
	// service -> control UI
	public final static OWLDataProperty controlUI = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "controlUI"));
	// service -> service owner
	public final static OWLDataProperty owner = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "owner"));
	// service -> service discovery time
	public final static OWLDataProperty discoveryTime = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "discoveryTime"));
	// service -> service type 
	// two-letter type. First letter is selected from {L, P, R} (local, pervasive, remote},
	// second letter is selected from {G, T, I} {general, translator, internal}
	public final static OWLDataProperty serviceType = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "serviceType"));
	// service -> grounding type
	public final static OWLDataProperty groundingType = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "groundingType"));
	// service -> grounding URL (device URL for UPnP Service, WSDL URL for Web Service
	public final static OWLDataProperty groundingURL = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "groundingURL"));
	// service -> deletable
	public final static OWLDataProperty deletable = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "deletable"));
	// service -> Certificate Authority name
	public final static OWLDataProperty caName = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "caName"));
	// service -> sphere id (where the service is located) root for the default sphere
	public final static OWLDataProperty sphereID = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "sphereID"));
	// service -> service description version
	public final static OWLDataProperty descriptionVersion = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "descriptionVersion"));
}
