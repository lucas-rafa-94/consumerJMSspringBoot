package com.app.conectCarConsumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PreDestroy;
import java.util.concurrent.*;

@Configuration
public class ApplicationListener {
    private static final Log logger = LogFactory.getFactory().getInstance(ApplicationListener.class);
    private static ExecutorService executor;
    private static  FutureTask<String> futureTask;


    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        MyCallable callable = new MyCallable(1000);
        futureTask = new FutureTask<String>(callable);
        executor = Executors.newFixedThreadPool(1);
        executor.execute(futureTask);

  }
    @PreDestroy
    public void onExit() {
        logger.info("Encerrando Aplicacao Consumer...");
        futureTask.cancel(true);
        executor.shutdownNow();
    }
}
