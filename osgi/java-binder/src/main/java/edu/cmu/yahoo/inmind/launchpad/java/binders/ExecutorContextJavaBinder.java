package edu.cmu.yahoo.inmind.launchpad.java.binders;

import edu.cmu.yahoo.inmind.launchpad.java.binders.sync.Executor;
import edu.cmu.yahoo.inmind.launchpad.java.binders.sync.RunnableEvent;
import edu.cmu.yahoo.inmind.launchpad.utils.ExecutorContext;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by dangiankit on 2/14/18.
 */
public class ExecutorContextJavaBinder implements ExecutorContext {

    private static final int DEFAULT_CORE_POOL_SIZE = 1;        // number of threads in pool
    private static final int TIME_KEEP_ALIVE = 5000;            // milliseconds
    private static final int TIME_MAX_WAIT = 30;                // seconds

    private static ExecutorContextJavaBinder executorContext;
    private static ThreadPoolExecutor threadPoolExecutor;
    private Executor executor;

    private ExecutorContextJavaBinder() {
        executor = new Executor();
        EventBus.getDefault().register(executor);

        // initialize the thread pool executor
        initThreadExecutor();
    }

    public static ExecutorContext getInstance() {
        if (executorContext == null) {
            executorContext = new ExecutorContextJavaBinder();
        }
        return executorContext;
    }

    @Override
    public void execute(Runnable runnable) {
        EventBus.getDefault().post(new RunnableEvent(threadPoolExecutor, runnable));
    }

    @Override
    public <T> T execute(Callable<T> callable) throws Exception {
        if (!threadPoolExecutor.isShutdown()) {
            return threadPoolExecutor.submit(callable).get();
        }
        return null;
    }

    @Override
    public void release() {

        // unregister the subscriber from the eventbus: for runnables
        EventBus.getDefault().unregister(executor);
        executorContext = null;

        // shutdown the executor service: for callables
        shutdownThreadExecutor();
    }


    public static ThreadPoolExecutor getExecutor() {
        return threadPoolExecutor;
    }

    /**
     * Reference: {@Link https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadPoolExecutor.html}
     */
    public static void initThreadExecutor(int corePoolSize){
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ManagableThreadPool (
                    corePoolSize == 0 ? DEFAULT_CORE_POOL_SIZE : corePoolSize,
                    Integer.MAX_VALUE,
                    TIME_KEEP_ALIVE,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());
            threadPoolExecutor.allowCoreThreadTimeOut(true);
        }
    }

    public static void initThreadExecutor(){
        initThreadExecutor (DEFAULT_CORE_POOL_SIZE);
    }

    static class ManagableThreadPool extends ThreadPoolExecutor {
        public ManagableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                   TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new DiscardPolicy());
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r){
            if (r instanceof NamedRunnable) {
                t.setName(((NamedRunnable) r).getName());
            }
        }
    }

    /**
     * Reference: {@Link http://www.baeldung.com/java-executor-service-tutorial}
     */
    public static void shutdownThreadExecutor() {
        threadPoolExecutor.shutdown();
        try {
            if (!threadPoolExecutor.awaitTermination(TIME_MAX_WAIT, TimeUnit.SECONDS)) {
                threadPoolExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPoolExecutor.shutdownNow();
        }
    }

    public interface NamedRunnable extends Runnable {
        String getName();
    }

    /**
     * We need to guarantee that once a flag is set to true, it is not undone after that
     * @param flag
     * @param newValue
     */
    public static void setAtom(AtomicBoolean flag, boolean newValue){
        if( !flag.get() && newValue )
            flag.getAndSet( true );
    }

    private static Set<Thread> threadSet;

    /**
     * This method prints out the new threads created in comparison with a previous set of threads (threadSet)
     * saved in memory.
     */
    public static void printNewAddedThreads(){
        if( threadSet != null ){
            Set<Thread> threadSetNow = Thread.getAllStackTraces().keySet();
            threadSetNow.removeAll(threadSet);
            for ( Thread t : threadSetNow){
                System.out.println( String.format("Thread: %s state: %s hashcode: %s queue: %s",
                        t, t.getState(), t.hashCode(), threadPoolExecutor.getQueue().size() ));
            }
        }
        threadSet = Thread.getAllStackTraces().keySet();
    }

    public static boolean sleep(long millis) {
        try {
            Thread.yield();
            Thread.sleep(millis);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }


}

