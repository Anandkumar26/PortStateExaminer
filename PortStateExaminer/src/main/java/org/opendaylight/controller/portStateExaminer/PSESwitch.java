package org.opendaylight.controller.portStateExaminer;

import java.util.ArrayList;

public class PSESwitch {

	private String switchId;
    private ArrayList<String> portNo;
    private boolean openFlow;
	
    public PSESwitch() {
	    portNo = new ArrayList<String>();
	}
    
    public String getSwitchId() {
		return switchId;
	}
	public void setSwitchId(String switch_id) {
		this.switchId = switch_id;
	}
	public ArrayList<String> getPortNo() {
		return portNo;
	}
	public void addPortNo(String portNo) {
		this.portNo.add(portNo);
	}
	public boolean isOpenFlow() {
		return openFlow;
	}
	public void setOpenFlow(boolean openFlow) {
		this.openFlow = openFlow;
	}
}
