package com.mby.web;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HelloWorld {

    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        final String jobID = "my_job_1";
        final AtomicInteger count = new AtomicInteger(0);
        final Map<String, Future> futures = new HashMap<>();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Future future = scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println(count.getAndIncrement());

                if (count.get() > 10) {
                    Future future = futures.get(jobID);
                    if (future != null) future.cancel(true);
                    countDownLatch.countDown();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

        futures.put(jobID, future);
        countDownLatch.await();

        scheduler.shutdown();
    }

}
