package com.awesome.pro.executor.references;

/**
 * Thread pool configuration parameters and default configurations for
 * thread pool executor.
 * @author siddharth.s
 */
public class ThreadPoolConfigReferences {

	// Thread Pool Configuration Parameters
	public static final String PARAMETER_THREAD_POOL_CORE_SIZE = "CorePoolSize";
	public static final String PARAMETER_THREAD_POOL_MAX_SIZE = "MaxPoolSize";
	public static final String PARAMETER_THREAD_POOL_KEEP_ALIVE_TIME = "KeepAliveTime";
	public static final String PARAMETER_MAX_QUEUE_SIZE = "MaxQueueSize";
	public static final String PARAMETER_QUEUE_WAIT_TIME = "QueueWaitTime";

	// Default Thread Pool Configurations
	public static final int DEFAULT_THREAD_POOL_CORE_SIZE = 100;
	public static final int DEFAULT_THREAD_POOL_MAX_SIZE = 1000;
	public static final int DEFAULT_THREAD_POOL_KEEP_ALIVE_TIME = 10000;
	public static final int DEFAULT_MAX_QUEUE_SIZE = 200;
	public static final long DEFAULT_QUEUE_WAIT_TIME = 300;

}
