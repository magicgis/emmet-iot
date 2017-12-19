package com.emmet.iot.manager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emmet.iot.core.model.DeviceDataModel;
import com.emmet.iot.ds.shadow.DeviceShadow;
import com.emmet.iot.manager.collection.DeviceCollection;
import com.emmet.iot.manager.exception.DeviceNotFoundException;



@RestController
@RequestMapping(path = "devices")
public class DevicesController {
	@Autowired
	private DeviceCollection deviceConnection;

	@RequestMapping(path = "/{deviceId}", method = RequestMethod.GET)
	public ResponseEntity<?> getDeviceStatus(@PathVariable String deviceId) {
		try {
			DeviceShadow deviceShadow = deviceConnection.getDevice(deviceId);
			return new ResponseEntity<DeviceDataModel>(deviceShadow.getCurrentStatus(), HttpStatus.OK);
		} catch (DeviceNotFoundException e) {
			return new ResponseEntity<String>("Device not found!", HttpStatus.NOT_FOUND);
		}
	}

}
