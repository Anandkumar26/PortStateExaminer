/*
 * NEC Technologies India Ltd
 */

package org.opendaylight.controller.portStateExaminer.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.opendaylight.controller.portStateExaminer.IPortStateExaminerService;
import org.opendaylight.controller.portStateExaminer.PSEPort;
import org.opendaylight.controller.portStateExaminer.PSESwitch;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.core.Property;
import org.opendaylight.controller.sal.core.UpdateType;
import org.opendaylight.controller.switchmanager.IInventoryListener;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortStateExaminerImpl implements IPortStateExaminerService, IInventoryListener{

	private static final Logger logger = LoggerFactory
            .getLogger(PortStateExaminerImpl.class);
	int thread_no = 0;
	
	/* External services */
	private ISwitchManager switchManager = null;
	private HashMap<String, Node> nodeList = null;
	private HashMap<String, String> portList = null;
	
	/* Default Constructor */
	public PortStateExaminerImpl() {
		super();
		logger.info("Vnm getting instancetiated !");
	}

	/* Setter and UnSetter of External Services */
	
    void setSwitchManager(ISwitchManager s) {
        logger.info("SwitchManager is set!");
        this.switchManager = s;
    }

    void unsetSwitchManager(ISwitchManager s) {
        if (this.switchManager == s) {
            logger.info("SwitchManager is removed!");
            this.switchManager = null;
        }
    }

    /* Function to be called by ODL */

    /**
     * Function called by the dependency manager when all the required
     * dependencies are satisfied
     *
     */
    public void init() {

    	/* Initialize Plugin components */    	
    	logger.info("Plugin getting Initilizing by Dependency Manager!");
    	nodeList = new HashMap<String, Node>();
    	portList = new HashMap<String, String>();
    }

	/**
     * Function called by the dependency manager when at least one
     * dependency become unsatisfied or when the component is shutting
     * down because for example bundle is being stopped.
     *
     */
    void destroy() {
        logger.error("Plugin has destroyed!");
    }

    /**
     * Function called by dependency manager after "init ()" is called
     * and after the services provided by the class are registered in
     * the service registry
     *
     */
    void start() {
    	logger.info("Plugin has started!");
    }

    /**
     * Function called by the dependency manager before the services
     * exported by the component are unregistered, this will be
     * followed by a "destroy ()" calls
     *
     */
    void stop() {
        logger.info("Stopped");
    }


    /* InventoryListener service Interface - internal use only, exposed To ODL */

	@Override /* ODL NODE notification */
	public void notifyNode(Node node, UpdateType type, Map<String, Property> propMap) {

		String switchId;
		
        if(node == null) {
            logger.warn("New Node Notification : Node is null ");
            return;
        }
        
        if(type == null) {
        	logger.warn("New Node Notification : Type is null ");
            return;
        }
        
        /* Extract dpId from node */
        switchId = OdlUtil.getDpIdFromNode(node);
        if(switchId == null){
			logger.error("Switch Id could not be extracted !");
			return;
		}
        
        /* Check type of switch notification */
		switch (type) {
        	case ADDED:
        		this.nodeList.put(switchId, node);
        		break;

        	case CHANGED:
	            this.nodeList.put(switchId, node);
	            break;

        	case REMOVED:
	        	this.nodeList.remove(switchId);
	        	break;

        	default:
        		logger.error("Unknown Type of Switch Notification!");
		}
	}

	@Override /* ODL NODECONNECTOR notification */
	public void notifyNodeConnector(NodeConnector nodeConnector, UpdateType type, Map<String, Property> propMap) {

		logger.info("Ignoring node Connecrtor notification for now");
		
		/* Port name is determined later */

		String portName = null;
		String portNo = null;
		PSEPort port = null;
		
        if (nodeConnector == null) {
            logger.warn("New NodeConnector Notification : NodeConnector is null");
            return;
        }
        
        if(type == null){
        	logger.warn("New NodeConnector Notification : Type is null");
            return;
        }

        if(propMap == null){
        	logger.warn("New NodeConnector Notification : Property Map is null");
            return;
        }
        
        /* Extract port No */
		portNo = OdlUtil.getPortNo(nodeConnector);
		if(portNo == null){
			logger.error("Port No could not be extracted !");
			return;
		}

		
		/* Extract dpId from Node */
		switch (type) {
	        case ADDED:
	        	/* Extract port Name */
	    		portName = OdlUtil.getPortName(propMap);
	    		portList.put(portNo, portName);
	        	break;
	
	        case CHANGED:
	        	/* Extract port Name */
	        	portName = OdlUtil.getPortName(propMap);
	    		portList.put(portNo, portName);
	        	break;
	
	        case REMOVED:
	        	
	        	portList.remove(portNo);
	        	break;
	
	        default:
	            logger.error("Unknown NodeConnector notification received");
		}
	}


	
	/* Plugin service Interface - exposed as a service */
	
	@Override
	public HashMap<String, PSESwitch> getSwitch() {
		
		HashMap<String, PSESwitch> switchMap = new HashMap<String, PSESwitch>();
		Node switchNode = null;
		PSESwitch switchInfo = null;
		
		/* For all switch in the  */
		for(String switchId: nodeList.keySet()) {
			
			switchNode = nodeList.get(switchId);
			switchInfo = new PSESwitch();
			switchInfo.setSwitchId(switchId);
			switchInfo.setOpenFlow(OdlUtil.isOpenFlowSwitch(switchNode));
			Set<NodeConnector> allNodeConnectors = switchManager.getNodeConnectors(switchNode);
			/* check if no node is attached */
			if(allNodeConnectors == null){
				logger.info("No node connector is attached to node: {}", switchNode);
			}
			else {
				for(NodeConnector nodeConnector : allNodeConnectors) {
					String portNo = OdlUtil.getPortNo(nodeConnector);
				    switchInfo.addPortNo(portNo);
				}
			}
			switchMap.put(switchId, switchInfo);
		}
		
		return switchMap;
	}

	@Override
	public HashMap<String, PSEPort> getPort(String switchId) {
		
		Node switchNode = null;
		HashMap<String, PSEPort> portMap  = new HashMap<String, PSEPort>();
		
		if(switchId == null) {
			logger.info("User must specifiy a valid switch ID");
		}
		
		switchNode = nodeList.get(switchId);
		Set<NodeConnector> allNodeConnectors = switchManager.getNodeConnectors(switchNode);
		/* check if no node is attached */
		if(allNodeConnectors == null){
			logger.info("No node connector is attached to node: {}", switchNode);
		}
		else {
			for(NodeConnector nodeConnector : allNodeConnectors) {
				PSEPort port = new PSEPort();
				
				String portNo = OdlUtil.getPortNo(nodeConnector);
				port.setPortNo(portNo);
				/* TODO: port name is not being added */
			    String portName = portList.get(portNo);
			    port.setPortName(portName);
			    
			    portMap.put(portNo, port);
			}
		}
		return portMap;
	}
}
