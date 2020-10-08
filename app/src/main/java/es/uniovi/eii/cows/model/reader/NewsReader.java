package es.uniovi.eii.cows.model.reader;

import java.io.IOException;
import java.util.List;

import es.uniovi.eii.cows.model.NewsItem;

/**
 * News reader interface, for RSS and Twitter readers
 */
public interface NewsReader extends Runnable {

    /**
     * Invokes the reader to get and filter the news
     */
    void run();

    /**
     * Closes the Reader connection
     */
    void stop();

    /**
     * @return  Parsed news
     */
    List<NewsItem> getNews();

}
