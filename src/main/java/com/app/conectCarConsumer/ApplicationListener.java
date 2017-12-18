package com.app.conectCarConsumer;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import java.util.concurrent.*;

@Configuration
public class ApplicationListener {

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        MyCallable callable = new MyCallable(1000);
        FutureTask<String> futureTask = new FutureTask<String>(callable);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(futureTask);
    }
}
