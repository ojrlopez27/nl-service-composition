package edu.cmu.yahoo.inmind.launchpad.java.binders.sync;

import java.util.concurrent.Callable;

public class CallableEvent {
    private Callable callable;
    private Executor executor;

    public CallableEvent(Callable callable) {
        this.callable = callable;
    }

    public void setPayload(Object object) {
        this.executor = (Executor) object;
    }

    public void run() {
        this.executor.execute(callable);
    }
}
