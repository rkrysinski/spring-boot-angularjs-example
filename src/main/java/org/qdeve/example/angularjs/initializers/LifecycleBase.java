package org.qdeve.example.angularjs.initializers;

import org.springframework.context.SmartLifecycle;

/**
 * Abstract class that implements SmartLifecycle boiler plate for.
 */
abstract public class LifecycleBase implements SmartLifecycle {

	private static final int INITIALIZATION_MOMENT = 0;
	private boolean isRunning;

	@Override
	public void start() {
		isRunning = true;
	}

	@Override
	public void stop() {
		isRunning = false;
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public int getPhase() {
		return INITIALIZATION_MOMENT;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		stop();
		callback.run();
	}

}
