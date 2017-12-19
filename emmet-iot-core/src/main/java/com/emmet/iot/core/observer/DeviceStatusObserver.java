package com.emmet.iot.core.observer;

import com.emmet.iot.core.model.DeviceStatusNotification;

public interface DeviceStatusObserver {
	void onDeviceStatusChange(DeviceStatusNotification status);

	void stop();
}
