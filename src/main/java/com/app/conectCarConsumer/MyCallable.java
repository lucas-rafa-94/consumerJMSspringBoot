package com.app.conectCarConsumer;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<String> {

    private long waitTime;

    public MyCallable(int timeInMillis){
        this.waitTime=timeInMillis;
    }
    @Override
    public String call() throws Exception {
        Thread.sleep(waitTime);
        ConsumeRun consumeRun = new ConsumeRun();
        consumeRun.run();
        return "Concluido";
    }

}
