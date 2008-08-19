package de.fu_berlin.inf.gmanda.util;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DeferredVariableProxyListener<T> implements VariableProxyListener<T> {

	VariableProxyListener<T> wrapped;

	ScheduledExecutorService executor;
	
	Future<?> setFilterFuture;
	
	long time;
	
	TimeUnit timeUnit;
	
	public DeferredVariableProxyListener(VariableProxyListener<T> wrapped){
		this(wrapped, 1, TimeUnit.SECONDS);
	}
	
	public DeferredVariableProxyListener(VariableProxyListener<T> wrapped, long time, TimeUnit timeUnit ){
		this.wrapped = wrapped;
		this.time = time;
		this.timeUnit = timeUnit;
	}

	public void setVariable(final T newValue) {

		// Lazyly create Thread-Pool Executor
		if (executor == null)
			executor = Executors.newSingleThreadScheduledExecutor();

		// Cancel previous set operations
		if (setFilterFuture != null && !setFilterFuture.isCancelled())
			setFilterFuture.cancel(false);

		setFilterFuture = executor.schedule(new Runnable() {
			public void run() {
				wrapped.setVariable(newValue);
			}
		}, time, timeUnit);
	}
}
