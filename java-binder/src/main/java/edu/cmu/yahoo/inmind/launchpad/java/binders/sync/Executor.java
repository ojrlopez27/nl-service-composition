package edu.cmu.yahoo.inmind.launchpad.java.binders.sync;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.Callable;

public class Executor {

    public Executor() {}

    public void execute(Runnable runnable) {
        runnable.run();
    }

    public void execute(Callable callable) {
        try {
            // ignoring the result, it may cause problems
            // however, it is not being used
            callable.call();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onReceive(RunnableEvent runnableEvent) {
        runnableEvent.setPayload(this);
        runnableEvent.run();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onReceive(CallableEvent callableEvent) {
        callableEvent.setPayload(this);
        callableEvent.run();
    }
}
