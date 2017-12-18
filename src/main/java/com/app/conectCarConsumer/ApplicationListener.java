package com.app.conectCarConsumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.annotation.PreDestroy;
import java.util.concurrent.*;

@Configuration
public class ApplicationListener {
    private static final Log logger = LogFactory.getFactory().getInstance(ApplicationListener.class);
    private ExecutorService executor = null;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        System.out.println();
        MyCallable callable = new MyCallable(1000);
        FutureTask<String> futureTask = new FutureTask<String>(callable);
        executor = Executors.newFixedThreadPool(1);
        executor.execute(futureTask);
  }
    @PreDestroy
    public void onExit() {
        logger.info("Encerrando Aplicacao Consumer...");
        executor.shutdownNow();
    }
}
