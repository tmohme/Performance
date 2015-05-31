# Performance

Some samples to demonstrate how poorly written code manifests itself in measuring tools.

The key here is, to measure, make small refactorings and measure again to see the difference.

## Samples

### Primes
Make the program run faster.  
You can run the program with `./gradlew run -i`.  
The `-i` is required to show the log output on the console. 

### JPA
Make the program scale (almost) linear.  
You can run the program with `./gradlew test -i`.
Start with a smalll number of vouchers.  
You have to fix three problems until you get (almost) linear scalability.  
You shouldn't see a significant degression from linearity before a million vouchers.

### ByteSum
This is a different beast.
You'll have to digg deep and understand CPU performance counters here.  
Expect a speed increase by a factor of 4~6.


## Tools
Only the most important ones to detect / avoid performance problems.

### Java
[JVisualVM] - Get a quick overview of the *current* behavior (CPU, MEM usage, GC) of a JVM. Bundled with JDK.  
There are some interesting plugins for JVisualVM. Especially the btrace plugin is woth a look (see btrace below).

[JMC & Flight Recorder][jmcfr] - Starting with JDK 7 update 40, **J**ava **M**ission **C**ontrol and the Flight Recorder are bundled with the HotSpot VM. Especially the Flight Recorder provides great monitoring abilities with very low overhead. The incurred overhead is at least one order of magnitude lower than with JVisualVM. In contrast to JVisualVM, these tools are free to use only in DEV & TEST environments.

[JVisualVM]: http://docs.oracle.com/javase/8/docs/technotes/guides/visualvm/index.html

[jmcfr]: http://www.oracle.com/technetwork/java/javaseproducts/mission-control/java-mission-control-1998576.html


### Logging
The most basic way is, to simply use your favorite logging library to produce timing informations when your app requests data from another system.  
The next step is using somthing like [metrics] to collect measured data. Metrics can report the data by itself (via logging) or forward them to reporting backends like Ganglia and Graphite.  
There's a great [presentation about the why and what of metrics][m-everywhere] by Coda Hale.

Garbage Collection logging is indispensable when it comes to understand the long-term GC behavior of a JVM and tune it.  
The most important options to use in PROD are 

* -XX:-PrintGCDetails
* -XX:-PrintGC__Date__Stamps
* -XX:+PrintTenuringDistribution
* -Xloggc:<filename>
* -XX:-UseGCLogFileRotation
* -XX:NumberOfGClogFiles=<num>
* -XX:GCLogFileSize=<size>

Without these options, you simply don't have enought information to tune your garbage collection algorithm.

Last but nor least, Hibernate (with activated statistics) and Spring offer logging options that let you investigate which queries are slow or whether it takes along time to get a DB connection.

[metrics]: https://dropwizard.github.io/metrics
[m-everywhere]: http://pivotallabs.com/139-metrics-metrics-everywhere/


### The backing machine
On Linux, AIX, etc. `nmon` is a proven tool for both, getting a quick overview of the system and monitoring the rousource usage over time.  
The [nmonvisualizer] canbe used to visualize the data captured by nmon.

On Windows, the comparable tool of choice is `perfmon`. 

[nmonvisualizer]: http://nmonvisualizer.github.io/nmonvisualizer/ 


### Deep insights
[btrace] is a great tool to selectively investigate problems in a running JVM. The Documentation is included in the full distribution. A good starting points is the [user guide][btrace-ug].
Applying the btrace provided sample traceing-script [JdbcQueries.java] with the JVisualVM-btrace-plugin to the JPA sample project is incredibly easy and an eye-opener.  
It can be this easy to pin down slow queries . . . :D

If we have to digg deeper, there are the linux perf tools ([tutorial][perf-tutorial], [examples][perf-examples]) which will give you a whealth of information, about what is going on.  
These are especially usefull for very focused analysis, down to CPU performance counters.

For Windows, Intel provides its [Performance Counter Monitor][ipcm]. 

[btrace]: https://github.com/jbachorik/btrace
[btrace-ug]: https://kenai.com/projects/btrace/pages/UserGuide
[JdbcQueries.java]: https://github.com/jbachorik/btrace/blob/release-1.3/samples/JdbcQueries.java

[perf-tutorial]: https://perf.wiki.kernel.org/index.php/Tutorial
[perf-examples]: http://www.brendangregg.com/perf.html 

[ipcm]: https://software.intel.com/en-us/articles/intel-performance-counter-monitor


### Circruit-Breaker
[hystrix] from Netflix is an easy to use implementation of the circruit-breaker pattern that might be useful to prevent thrashing.

[hystrix]: https://github.com/Netflix/Hystrix


## References
Martin Thompson's talks [Responding in a Timely Manner][RiaTM] and the deep digging [Mythbusting modern Hardware][MmH] are worth to see.  

A nice online calculator for Queuing problems can be found [here][supositorio]. Unfortunately it is browser-sensitive.

[RiaTM]: https://www.youtube.com/watch?v=q_DCipkMsy0
[MmH]: https://www.youtube.com/watch?v=MC1EKLQ2Wmg
[supositorio]: http://www.supositorio.com/rcalc/rcalclite.htm
