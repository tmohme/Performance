package com.sun.btrace.samples;

import com.sun.btrace.BTraceUtils;
import com.sun.btrace.Profiler;
import com.sun.btrace.annotations.*;

import java.lang.String;

@BTrace
class DetectSlowCalls {
  @Property
  Profiler profiler = BTraceUtils.Profiling.newProfiler();

  @TLS
  int argument;

  @OnMethod(clazz = "/Primes/", method = "/isPrime/")
  void entry(@ProbeMethodName(fqn = true) String probeMethod, int current) {
    BTraceUtils.Profiling.recordEntry(profiler, probeMethod);
    argument = current;
  }

  @OnMethod(clazz = "/Primes/", method = "/isPrime/", location = @Location(value = Kind.RETURN))
  void exit(@ProbeMethodName(fqn = true) String probeMethod, @Duration long duration) {
    BTraceUtils.Profiling.recordExit(profiler, probeMethod, duration);
    if (duration > 90000000) {
      BTraceUtils.println();
      BTraceUtils.println(BTraceUtils.Strings.concat("================== Thread: ", BTraceUtils.Threads.name(BTraceUtils.Threads.currentThread())));
      BTraceUtils.println(BTraceUtils.Strings.concat("================== Duration: ", BTraceUtils.str(duration)));
      BTraceUtils.println(BTraceUtils.Strings.concat("================== Argument: ", BTraceUtils.str(argument)));
      BTraceUtils.jstack();
    }
  }

  //  @OnTimer(5000)
  void timer() {
    BTraceUtils.println();
    BTraceUtils.Profiling.printSnapshot("performance profile", profiler);
  }
}
