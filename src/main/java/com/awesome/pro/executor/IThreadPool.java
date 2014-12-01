package com.awesome.pro.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Thread pool executor which will take care of managing recycling
 * of threads and keeping them alive. It accepts runnable implementations.
 * @author siddharth.s
 */
public interface IThreadPool {

	/**
	 * Starts the thread pool or initializes it.
	 */
	void start();
	
	/**
	 * @param runnable Job to execute.
	 * @param v Placeholder for the output.
	 * @param <V> Return type generic parameter.
	 * @return Future task corresponding to the runnable.
	 */
	<V> FutureTask<V> execute(Runnable runnable, V v);

	/**
	 * @param runnable Job to execute.
	 */
	void execute(Runnable runnable);

	/**
	 * @param callable Job to execute.
	 * @param <V> Return type generic parameter.
	 * @return Future task corresponding to the callable.
	 */
	<V> FutureTask<V> execute(Callable<V> callable);

	/**
	 * @return Number of threads actively executing a job.
	 */
	int getActiveThreadCount();

	/**
	 * Shuts down the thread pool executor.
	 */
	void shutdown();

	/**
	 * Waits until completion of execution of jobs.
	 */
	void waitForCompletion();

}