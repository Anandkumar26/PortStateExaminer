package org.opendaylight.controller.pse.pseCFE;

import java.util.HashMap;

import org.opendaylight.controller.portStateExaminer.PSEPort;
import org.opendaylight.controller.portStateExaminer.PSESwitch;

public interface PseCFEApi {

/* Interface To be exposed*/
	
	/* getSwitch() : To get all the switches details mapped against the switchId  */
	public HashMap<String, PSESwitch> getSwitch();
	
	/* getPort() : To get all the port info for a switch mapped against portNo */
	public HashMap<String, PSEPort> getPort(String switchId);


}
