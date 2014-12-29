package org.opendaylight.controller.pse.pseCFE;


import java.util.HashMap;

import org.opendaylight.controller.portStateExaminer.IPortStateExaminerService;
import org.opendaylight.controller.portStateExaminer.PSEPort;
import org.opendaylight.controller.portStateExaminer.PSESwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CFEImpl implements PseCFEApi{

	private static final Logger logger = LoggerFactory
            .getLogger(CFEImpl.class);

	/* External services */
	static private IPortStateExaminerService pseService = null;


	/* Default Constructor */
	public CFEImpl() {
		super();
		logger.info("Configuraton FroentEnd getting instancetiated !");
	}

	/* Setter and UnSetter of External Services */

	void setPSEService(IPortStateExaminerService s) {
		logger.info("PseServices is set!");
		pseService = s;
	}

	void UnsetPSEService(IPortStateExaminerService s) {
		if (pseService == s) {
            logger.info("PseServices is removed!");
            pseService = null;
        }
	}


	void init() {
		logger.info("Configuration Front End has Initialized !");
	}

	void destroy() {
		logger.info("Configuration Front End has destroyed!");
	}

	 void start() {
	    logger.info("Configuration Front End has started !");


	 }

	 void stop() {
		logger.info("CFE Stopped!!");
	 }

	@Override
	public HashMap<String, PSESwitch> getSwitch() {
		
		HashMap<String, PSESwitch> switchMap = new HashMap<String, PSESwitch>();
		switchMap=pseService.getSwitch();
		return switchMap;
	}

	@Override
	public HashMap<String, PSEPort> getPort(String switchId) {
		
		HashMap<String, PSEPort> portMap  = new HashMap<String, PSEPort>();
		portMap=pseService.getPort(switchId);
		return portMap;
	}

	

	
	
}

