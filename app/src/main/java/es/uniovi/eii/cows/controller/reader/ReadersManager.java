package es.uniovi.eii.cows.controller.reader;

import android.content.Context;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import es.uniovi.eii.cows.data.helper.FirebaseHelper;
import es.uniovi.eii.cows.model.NewsItem;

/**
 * This class pulls controls all the readers and their results
 */
public class ReadersManager {

    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private static final int CORE_POOL_SIZE = 8;
    private static final int MAXIMUM_POOL_SIZE = 8;

    private ThreadPoolExecutor readersThreadPool;

    private List<NewsReader> readers;

    private static ReadersManager instance = new ReadersManager();               // Singleton

    private ReadersManager() { }

    /**
     * @return  Instance of the manager
     */
    public static ReadersManager getInstance() {
        return instance;
    }

    /**
     * Starts all the readers in different Threads
     */
    public void run(Context context) {
        initThreads();
        readers = ReadersFactory.getInstance(context).getReaders();
        readers.forEach(r -> readersThreadPool.execute(r));
    }

    private void initThreads() {
        BlockingQueue<Runnable> readersQueue = new LinkedBlockingQueue<>();
        readersThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, readersQueue);
    }

    /**
     * @return  Pulled and parsed news when finished
     */
    public List<NewsItem> getNews() {
        readersThreadPool.shutdown();
        try {
            readersThreadPool.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return readers.stream().map(NewsReader::getNews).flatMap(Collection::stream)
                .sorted().collect(Collectors.toList());
    }

    /**
     * Close readers streams
     */
    public void shutdown() {
        readers.forEach(NewsReader::stop);
    }
}
