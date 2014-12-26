package org.opendaylight.controller.pse.pseCFENorthbound;


import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.controller.portStateExaminer.PSEPort;
import org.opendaylight.controller.portStateExaminer.PSESwitch;
import org.opendaylight.controller.pse.pseCFE.PseCFEApi;
import org.opendaylight.controller.sal.utils.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Path("/")
public class PseCFENorthbound {

	private static final Logger logger = LoggerFactory
       		.getLogger(PseCFENorthbound.class);

	/*Method that receive client rest api call for register agent  */
    @Path("/getInfo")
    @POST
    @StatusCodes({
    	@ResponseCode(code = 200, condition = "Destination reachable"),
        @ResponseCode(code = 503, condition = "Internal error"),
        @ResponseCode(code = 503, condition = "Destination unreachable") })

    public Response getInfo() {

        PseCFEApi pseCFEApi = (PseCFEApi) ServiceHelper.getGlobalInstance(PseCFEApi.class, this);
        String output=null;
        HashMap<String, PSESwitch> switchMap = new HashMap<String, PSESwitch>();
        HashMap<String, PSEPort> portMap  = new HashMap<String, PSEPort>();
        switchMap = pseCFEApi.getSwitch();
        String st=null;
        
        try{
        
        	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    		Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
    		Document doc = docBuilder.newDocument();		
    		Element rootElement = doc.createElement("PortStateInfo");
    		doc.appendChild(rootElement);
    		if(switchMap!=null){
    			for (Object key : switchMap.keySet()) {
    	    		
    				Element s = doc.createElement("Switch");
    				rootElement.appendChild(s);
    				Attr attr = doc.createAttribute("id");
    				attr.setValue(key.toString());
    				s.setAttributeNode(attr);
    				
    				Element type = doc.createElement("Type");  
    				st= String.valueOf(switchMap.get(key).isOpenFlow());
    				type.appendChild(doc.createTextNode(st));
    				s.appendChild(type);
    				
    				Element allPort = doc.createElement("AllPort");  
    				s.appendChild(allPort);
    				portMap=pseCFEApi.getPort(key.toString());
    				
    				if(portMap!= null){
    					
    					for (Object keyy : portMap.keySet()) {
    			    			
    						Element port = doc.createElement("Port");
    	    				allPort.appendChild(port);
    	    				Attr att = doc.createAttribute("id");
    	    				att.setValue(keyy.toString());
    	    				port.setAttributeNode(att);
    	    				
    	    				Element name = doc.createElement("Name");  
    	    				st= String.valueOf(portMap.get(keyy).getPortName());
    	    				name.appendChild(doc.createTextNode(st));
    	    				allPort.appendChild(name);	
    						
    			    	}				
    				}
    	    	}		
    		} 
    		
    		StringWriter stw = new StringWriter(); 
     		transformer.transform(new DOMSource(doc), new StreamResult(stw)); 
    		output = stw.getBuffer().toString();
        	
        }catch (ParserConfigurationException pce) {
    		pce.printStackTrace();
  	  } catch (TransformerException tfe) {
  		tfe.printStackTrace();
  	  }
     
        if(output==null){
        	
        	return Response.ok(new String("Failed To Generate XML !")).build();
        }
		
		return Response.ok(output).build();
  	}


}

