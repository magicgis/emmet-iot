package com.emmet.iot.core.observer;

import java.util.concurrent.ArrayBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.emmet.iot.core.model.DeviceStatusNotification;

public abstract class BaseDeviceStatusObserver implements DeviceStatusObserver {
	private static final int CAPACITY = 10000;
	
	private static final Log log = LogFactory.getLog(BaseDeviceStatusObserver.class);
	private boolean isRunning;

	private ArrayBlockingQueue<DeviceStatusNotification> statusQueue = new ArrayBlockingQueue<>(CAPACITY, true);

	private Thread queueComsumerThread;

	@PostConstruct
	public void startQueueConsumerThread() {
		queueComsumerThread = new Thread(new StatusQueueComsumer());
		isRunning = true;
		queueComsumerThread.start();
		log.info("-------------------queueComsumerThread.start-----------------------");
	}

	@Override
	public void onDeviceStatusChange(DeviceStatusNotification status) {
		try {

			statusQueue.put(status);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		isRunning = false;
		try {
			statusQueue.put(new DeviceStatusNotification());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	DeviceStatusNotification laststatus;

	abstract public void handle(DeviceStatusNotification status);

	class StatusQueueComsumer implements Runnable {
		@Override
		public void run() {
			log.info("------------------------------------------");
			while (isRunning) {
				DeviceStatusNotification status;
				try {
					status = statusQueue.take();
					if (status.getDeviceId() != null) {
						handle(status);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			log.info("Device status observer stoped.");
		}
	}
}
