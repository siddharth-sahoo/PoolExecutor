package com.awesome.pro.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.awesome.pro.executor.references.ThreadPoolConfigReferences;
import com.awesome.pro.utilities.PropertyFileUtility;

/**
 * Wrapper over Java's native thread pool executor.
 * @author siddharth.s
 */
public final class ThreadPool implements IThreadPool {

	/**
	 * Root logger instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(ThreadPool.class);

	/**
	 * Thread pool executor instance.
	 */
	private final ThreadPoolExecutor executor;

	/**
	 * Time to wait before retrying to queue.
	 */
	private final long queueWaitTime;

	/**
	 * Max size of queued jobs.
	 */
	private final int maxQueueSize;

	/**
	 * Configures a new thread pool.
	 * @param configFile Configuration file name.
	 */
	public ThreadPool(final String configFile) {
		final PropertyFileUtility config =
				new PropertyFileUtility(configFile);
		queueWaitTime = config.getLongValue(
				ThreadPoolConfigReferences.PARAMETER_QUEUE_WAIT_TIME,
				ThreadPoolConfigReferences.DEFAULT_QUEUE_WAIT_TIME);
		maxQueueSize = config.getIntegerValue(
				ThreadPoolConfigReferences.PARAMETER_MAX_QUEUE_SIZE,
				ThreadPoolConfigReferences.DEFAULT_MAX_QUEUE_SIZE);

		executor = new ThreadPoolExecutor(
				config.getIntegerValue(
						ThreadPoolConfigReferences.PARAMETER_THREAD_POOL_CORE_SIZE,
						ThreadPoolConfigReferences.DEFAULT_THREAD_POOL_CORE_SIZE
						),
						config.getIntegerValue(
								ThreadPoolConfigReferences.PARAMETER_THREAD_POOL_MAX_SIZE,
								ThreadPoolConfigReferences.DEFAULT_THREAD_POOL_MAX_SIZE
								),
								config.getIntegerValue(
										ThreadPoolConfigReferences.PARAMETER_THREAD_POOL_KEEP_ALIVE_TIME,
										ThreadPoolConfigReferences.DEFAULT_THREAD_POOL_KEEP_ALIVE_TIME
										),
										TimeUnit.MILLISECONDS,
										new LinkedBlockingQueue<Runnable>(
												config.getIntegerValue(
														ThreadPoolConfigReferences.PARAMETER_MAX_QUEUE_SIZE,
														ThreadPoolConfigReferences.DEFAULT_MAX_QUEUE_SIZE)
												));
		LOGGER.info("Thread pool initialized.");
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.executor.IThreadPool#start()
	 */
	@Override
	public void start() {
		executor.prestartAllCoreThreads();
		LOGGER.info("Threads have been prestarted.");
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.executor.IThreadPool#execute(java.lang.Runnable, V)
	 */
	@Override
	public final <V> FutureTask<V> execute(final Runnable runnable,
			final V v) {
		LOGGER.debug("Starting a new job.");
		final FutureTask<V> futureTask = new FutureTask<>(runnable, v);
		executor.execute(futureTask);
		return futureTask;
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.executor.IThreadPool#execute(java.lang.Runnable)
	 */
	@Override
	public final void execute(final Runnable runnable) {
		LOGGER.debug("Starting a new job.");
		//final FutureTask<Void> futureTask = new FutureTask<>(runnable, null);
		while (!tryToQueue(runnable)) {
			try {
				Thread.sleep(queueWaitTime);
			} catch (InterruptedException e) {
				LOGGER.error("Interrupted while waiting to queue job.");
			}
		}
	}

	private final synchronized boolean tryToQueue(final Runnable runnable) {
		if (executor.getQueue().size() < maxQueueSize) {
			executor.execute(runnable);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.executor.IThreadPool#execute(java.util.concurrent.Callable)
	 */
	@Override
	public final <V> FutureTask<V> execute(Callable<V> callable) {
		LOGGER.debug("Starting a new job.");
		final FutureTask<V> futureTask = new FutureTask<>(callable);
		executor.execute(futureTask);
		return futureTask;
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.executor.IThreadPool#getActiveThreadCount()
	 */
	@Override
	public final int getActiveThreadCount() {
		return executor.getActiveCount();
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.executor.IThreadPool#waitForCompletion()
	 */
	@Override
	public final void waitForCompletion() {
		while (getActiveThreadCount() > 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				LOGGER.error("Interrupted while waiting for completion of execution", e);
				System.exit(1);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.executor.IThreadPool#shutdown()
	 */
	@Override
	public final void shutdown() {
		LOGGER.info("Shutdown signal received.");
		executor.shutdown();
	}

}
