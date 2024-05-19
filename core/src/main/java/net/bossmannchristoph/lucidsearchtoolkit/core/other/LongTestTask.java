package net.bossmannchristoph.lucidsearchtoolkit.core.other;

import net.bossmannchristoph.lucidsearchtoolkit.core.TechnicalException;

public class LongTestTask implements Runnable {

	public LongTestTask(long timeInMillis) {
		this.timeInMillis = timeInMillis;
	}
	
	long timeInMillis;
	
	@Override
	public void run() {
		try {
			Thread.sleep(timeInMillis);
		} catch (Exception e) {
			throw new TechnicalException(e);
		}
	}
}
