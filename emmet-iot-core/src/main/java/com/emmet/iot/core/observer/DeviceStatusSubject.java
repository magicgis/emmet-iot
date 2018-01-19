package com.emmet.iot.core.observer;

import com.emmet.iot.core.model.device.DeviceStatusNotification;

public interface DeviceStatusSubject {
	void addObserver(DeviceStatusObserver observer);
	void updateStatus(DeviceStatusNotification status);
}
