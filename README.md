PoolExecutor
============

Wrapper for thread pool executor.


Sample initialization code:

````java
IThreadPool executor = new ThreadPool(config.properties);
executor.start();

//...
// Execute something.
// executor.execute(runnable);
//...

// Send shutdown signal.
// Will shutdown only after all tasks/jobs have been executed.
executor.shutdown();
````

Sample configuration parameters:
````conf
# Base pool size.
CorePoolSize 5
# Maximum number of threads to be created.
MaxPoolSize 10
# Time for which the thread is to be kept alive.
KeepAliveTime 5000
````
