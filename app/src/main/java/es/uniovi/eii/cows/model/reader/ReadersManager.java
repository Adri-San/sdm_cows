package es.uniovi.eii.cows.model.reader;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import es.uniovi.eii.cows.model.NewsItem;

/**
 * This class pulls controls all the readers and their results
 */
public class ReadersManager {

    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private static final int CORE_POOL_SIZE = 8;
    private static final int MAXIMUM_POOL_SIZE = 8;

    private BlockingQueue<Runnable> readersQueue;
    private ThreadPoolExecutor readersThreadPool;

    private List<NewsReader> readers;

    private static ReadersManager instance = new ReadersManager();               // Singleton

    private ReadersManager() {
        readersQueue = new LinkedBlockingQueue<>();
        readersThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, readersQueue);
        readers = ReadersFactory.getInstance().getReaders();
    }

    /**
     * @return  Instance of the manager
     */
    public static ReadersManager getInstance() {
        return instance;
    }

    /**
     * Starts all the readers in different Threads
     */
    public void run() {
        readers.forEach(r -> readersThreadPool.execute(r));
    }

    /**
     * @return  Pulled and parsed news when finished
     */
    public List<NewsItem> getNews() {
        while (!readersThreadPool.isTerminated()) {}
        return readers.stream().map(NewsReader::getNews).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Close readers streams
     */
    public void shutdown() {
        readers.forEach(NewsReader::stop);
    }
}
