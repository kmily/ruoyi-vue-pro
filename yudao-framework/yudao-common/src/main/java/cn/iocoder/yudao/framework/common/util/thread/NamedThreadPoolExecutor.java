package cn.iocoder.yudao.framework.common.util.thread;

import java.util.concurrent.*;

/**
 * 自定义名称线程池类
 * 在 java.util.concurrent.ThreadPoolExecutor的构造器上增加name属性
 *
 * @author congqing
 */
public class NamedThreadPoolExecutor extends ThreadPoolExecutor {
    private final String name;

    public NamedThreadPoolExecutor(String name, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.name = name;
    }

    public NamedThreadPoolExecutor(String name, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.name = name;
    }

    public NamedThreadPoolExecutor(String name, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler rejectedExecutionHandler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, rejectedExecutionHandler);
        this.name = name;

    }

    public NamedThreadPoolExecutor(String name, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, rejectedExecutionHandler);
        this.name = name;

    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        t.setName(name + "-" + t.getId());
        super.beforeExecute(t, r);
    }
}
