package com.nickaknudson.android.utils;

/**
 * @author nick
 */
public abstract class NThread extends Thread {
	
	private Boolean mayInterrupt = false;
	private Boolean stopped = false;
	
	public void run() {
		try {
			interruptableRun();
		} catch (InterruptedException e) {
			if(!mayInterrupt) {
				improperInterrupt(e);
			}
		}
	}
	
	/**
	 * @throws InterruptedException
	 */
	public abstract void interruptableRun() throws InterruptedException;
	
	/**
	 * @param e
	 */
	public abstract void improperInterrupt(InterruptedException e);

	/**
	 * @param mayInterrupt 
	 * @return successful stop
	 */
	public Boolean stop(Boolean mayInterrupt) {
		this.setMayInterrupt(mayInterrupt);
		this.setStopped(true);
		if(mayInterrupt) {
			interrupt();
		}
		return true;
	}

	/**
	 * @return may interrupt
	 */
	public Boolean mayInterrupt() {
		return mayInterrupt;
	}

	/**
	 * @param mayInterruptIfRunning
	 */
	public void setMayInterrupt(Boolean mayInterruptIfRunning) {
		this.mayInterrupt = mayInterruptIfRunning;
	}

	/**
	 * @return is stopped
	 */
	public Boolean isStopped() {
		return stopped;
	}

	/**
	 * @param stopped
	 */
	public void setStopped(Boolean stopped) {
		this.stopped = stopped;
	}
}
