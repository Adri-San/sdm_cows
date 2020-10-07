package es.uniovi.eii.cows.model.rss;

import java.io.IOException;
import java.util.List;

import es.uniovi.eii.cows.model.NewsItem;

/**
 * News reader interface, for RSS and Twitter readers
 */
public interface NewsReader {

    /**
     * Invokes the reader to get and filter the news
     */
    void run() throws IOException;

    /**
     * @return  Parsed news
     */
    List<NewsItem> getNews();

}
