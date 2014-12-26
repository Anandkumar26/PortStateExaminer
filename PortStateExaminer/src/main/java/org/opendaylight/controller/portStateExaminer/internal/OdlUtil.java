package org.opendaylight.controller.portStateExaminer.internal;

import java.math.BigInteger;
import java.util.Map;

import org.opendaylight.controller.sal.core.Name;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.core.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OdlUtil {
	
	private static final Logger logger = LoggerFactory
            .getLogger(OdlUtil.class);
	
	public static String getPortName(Map<String, Property> portPropMap){
		
		String ret = null;
		
		/* Get Name from property */
		Property name = portPropMap.get(Name.NamePropName);
		if(name == null){
			logger.error("Port Name information could not extracted form propMap !");
			return null;
		}
		ret = name.getStringValue();
				
		return ret;
	}
	
	public static String getPortNo(NodeConnector nodeConnector){
		
		String ret = null;
		
		/* get portNo from node connector */
		String portId = nodeConnector.getNodeConnectorIDString();
		if(portId == null){
			logger.error("Port ID information could not extracted for NodeConnector: {}", nodeConnector);
			return null;
		}
		ret = portId;
		
		return ret;
	}
	
	public static Node getNodeFromPort(NodeConnector nodeConnector){
		
		/* Get node Object from NodeConnector */
		Node node = nodeConnector.getNode();
		if(node == null){
			logger.error("Node information could not extracted for NodeConnector: {}", nodeConnector);
			return null;
		}
		
		return node;
	}
	
	public static String getDpIdFromNode(Node node){
		
		/* Get Data-path ID from Node to identify the switch */
		String dataPathId = node.getNodeIDString();
		
		if(dataPathId == null){
			logger.error("DataPath iD could not extracted for Node: {}", node);
			return null;
		}
		
		return dataPathId;
	}
	
	public static boolean isOpenFlowSwitch(Node node) {
		
		boolean ret = node.getType().equals(Node.NodeIDType.OPENFLOW);
		
		return ret;
	}
	
	public static boolean isOpenFlowSwitchPort(NodeConnector nodeConnector) {
		boolean ret = false;
		
		/* get node from node connector */
		Node node = getNodeFromPort(nodeConnector);
		if(node == null){
			ret = false;
		}
		else {
			ret = isOpenFlowSwitch(node);
		}
		
		return ret;
	}
	
}
