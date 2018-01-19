package com.emmet.iot.core.model;

public class RequestDeviceStatusCommand extends DeviceCommand{

	private NAME command = DeviceCommand.NAME.REPORT_STATUS;

	@Override
	public DeviceCommand.NAME getCommand() {
		return command;
	}

}
