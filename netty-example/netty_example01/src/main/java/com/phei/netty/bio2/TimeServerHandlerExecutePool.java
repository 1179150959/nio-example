package com.phei.netty.bio2;

import com.phei.netty.bio.TimeServerHandler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimeServerHandlerExecutePool {

    private ExecutorService executor;

    public TimeServerHandlerExecutePool( int maxPoolSize, int queueSize ) {
        this.executor = new ThreadPoolExecutor( Runtime.getRuntime().availableProcessors(), maxPoolSize, 120L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(TimeServerHandler timeServerHandler) {
        executor.execute( timeServerHandler );
    }

}
