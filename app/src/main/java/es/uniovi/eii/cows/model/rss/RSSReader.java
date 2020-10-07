package es.uniovi.eii.cows.model.rss;

import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.model.rss.parser.RSSParser;

public class RSSReader implements NewsReader {

    private List<NewsItem> news;
    private RSSParser parser;
    private boolean processing;                     // Did it finish to load the news?

    public RSSReader(RSSParser parser) {
        this.parser = parser;
        this.processing = false;
    }

    @Override
    public void run() throws IOException {
        try {
            processing = true;                              // Sets the reader working
            news = parser.parse();                          // Parses the XML
            //TODO news.stream().filter(COVIDFilter);       // Filters the news
            processing = false;                             // Set the reader finished
        } catch (MalformedURLException mue) {
            Log.e("MalformedURLException", mue.getMessage());
        } catch (IOException ioe) {
            // TODO throw RSSReaderExceptions with the error message
            Log.e("IOException", ioe.getMessage());
        } catch (XmlPullParserException xppe) {
            // TODO throw RSSReaderExceptions with the error message
            Log.e("XmlPullParserException", xppe.getMessage());
        }
    }

    @Override
    public List<NewsItem> getNews() {
        // Only returns the news when they are processed <- substitute for while processing?
        return (processing) ? null : news;
    }
}
