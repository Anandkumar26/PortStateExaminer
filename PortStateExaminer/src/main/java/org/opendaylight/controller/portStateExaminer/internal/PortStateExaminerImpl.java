/*
 * Copyright (C) 2014 SDN Hub

 Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.
 You may not use this file except in compliance with this License.
 You may obtain a copy of the License at

    http://www.gnu.org/licenses/gpl-3.0.txt

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 implied.

 *
 */

package org.opendaylight.controller.portStateExaminer.internal;

import java.util.Map;

import org.opendaylight.controller.forwardingrulesmanager.IForwardingRulesManager;
import org.opendaylight.controller.portStateExaminer.IPortStateExaminerService;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.core.Property;
import org.opendaylight.controller.sal.core.UpdateType;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.IListenDataPacket;
import org.opendaylight.controller.sal.packet.PacketResult;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.statisticsmanager.IStatisticsManager;
import org.opendaylight.controller.switchmanager.IInventoryListener;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortStateExaminerImpl implements IPortStateExaminerService, IInventoryListener, IListenDataPacket{

	private static final Logger logger = LoggerFactory
            .getLogger(PortStateExaminerImpl.class);
	int thread_no = 0;
	
	/* External services */
	private ISwitchManager switchManager = null;
    private IFlowProgrammerService flowProgrammer = null;
    private IDataPacketService dataPacketService = null;
    private IForwardingRulesManager forwardingRulesManager = null;
	private IStatisticsManager statManager = null;

	/* Default Constructor */
	public PortStateExaminerImpl() {
		super();
		logger.info("Vnm getting instancetiated !");
	}

	/* Setter and UnSetter of External Services */
	
	void setDataPacketService(IDataPacketService s) {
    	logger.info("Datapacketservice set");
        this.dataPacketService = s;
    }

    void unsetDataPacketService(IDataPacketService s) {
    	logger.info("Datapacketservice reset");
        if (this.dataPacketService == s) {
            this.dataPacketService = null;
        }
    }

    public void setFlowProgrammerService(IFlowProgrammerService s) {
    	logger.info("FlowProgrammer is set!");
        this.flowProgrammer = s;
    }

    public void unsetFlowProgrammerService(IFlowProgrammerService s) {
    	logger.info("FlowProgrammer is removed!");
        if (this.flowProgrammer == s) {
            this.flowProgrammer = null;
        }
    }

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

    void setForwardingRulesManager(IForwardingRulesManager s) {
    	logger.info("ForwardingRulesManager is set!");
    	forwardingRulesManager = s;
    }

    void unsetForwardingRulesManager(IForwardingRulesManager s) {
    	if (this.forwardingRulesManager == s) {
            logger.info("Controller is removed!");
            this.forwardingRulesManager = null;
        }
    }

    void setStatisticsManager(IStatisticsManager s) {
    	logger.info("Statistics Manager is set!");
    	statManager = s;
    }

    void unsetStatisticsManager(IStatisticsManager s) {
    	if (this.statManager  == s) {
            logger.info("Statistics Manager is removed!");
            this.statManager = null;
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

        if(node == null) {
            logger.warn("New Node Notification : Node is null ");
            return;
        }
        
        if(type == null) {
        	logger.warn("New Node Notification : Type is null ");
            return;
        }
        
        /* Property map currently not important for Node notification handle
         * For switch deletion property is blank
        if(propMap == null) {
        	logger.warn("New Node Notification : property Map is null ");
            return;
        }
        */
	}

	@Override /* ODL NODECONNECTOR notification */
	public void notifyNodeConnector(NodeConnector nodeConnector, UpdateType type, Map<String, Property> propMap) {

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
        
        else {
        	logger.info("Ignoring Non Openflow NodeConnector notification !");
        }
	}

	/* IListenDataPacket services Interface - internal use only, not exposed */

	@Override /* ODL Packet In notification */
	public PacketResult receiveDataPacket(RawPacket pkt) {

		logger.warn("New Unexpected Data Packet Notification reached to Plugin !");
		
		/* Check if pkt is null */
		if(pkt == null){
			logger.info("Packet is null !");
			return PacketResult.CONSUME;
		}
		
		return PacketResult.CONSUME;
	}

	
	
	
	/* Plugin service Interface - exposed as a service */
	
	@Override
	public boolean dummy() {
	
		return true;
	}

}
