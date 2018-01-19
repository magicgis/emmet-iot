package com.emmet.iot.core.model;

public class DeviceCommand  {
	public enum NAME {REPORT_STATUS}
	private NAME  command;
	
	public void setCommand(NAME name) {
		this.command = name;
	}
	
	public NAME getCommand() {
		return command;	
	}
}
