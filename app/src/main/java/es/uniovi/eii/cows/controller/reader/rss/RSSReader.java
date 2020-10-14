package es.uniovi.eii.cows.controller.reader.rss;

import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.controller.NewsReader;
import es.uniovi.eii.cows.controller.reader.rss.parser.RSSParser;

public class RSSReader implements NewsReader {

    private Set<NewsItem> news;
    private RSSParser parser;
    private InputStream is;

    public RSSReader(RSSParser parser) {
        this.parser = parser;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        try {
            is = new URL(parser.getURL()).openConnection().getInputStream();
            news = parser.parse(is);                            // Parses the XML
            // TODO news.stream().filter(COVIDFilter);          // Filters the news
        } catch (MalformedURLException mue) {
            Log.e("MalformedURLException", mue.getMessage());
        } catch (IOException ioe) {
            Log.e("IOException", ioe.getMessage());
        } catch (XmlPullParserException xppe) {
            Log.e("XmlPullParserException", xppe.getMessage());
        }
    }

    @Override
    public void stop() {
        try {
            is.close();
        } catch (IOException e) {
            Log.e("RSSReader", e.getMessage());
        }
    }

    @Override
    public Set<NewsItem> getNews() {
        return news;
    }
}
