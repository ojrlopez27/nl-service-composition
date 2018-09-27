package edu.cmu.yahoo.inmind.launchpad.java.binders.sync;

import java.util.concurrent.ThreadPoolExecutor;

public class RunnableEvent {
    private Runnable runnable;
    private Executor executor;
    private ThreadPoolExecutor threadPoolExecutor;

    public RunnableEvent(ThreadPoolExecutor threadPoolExecutor, Runnable runnable) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.runnable = runnable;
    }

    public void setPayload(Object object) {
        this.executor = (Executor) object;
    }

    public void run() {
        this.threadPoolExecutor.execute(runnable);
    }

}
