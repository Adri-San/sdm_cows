package es.uniovi.eii.cows.controller.reader.rss;

import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import es.uniovi.eii.cows.controller.reader.rss.filter.CovidFilter;
import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.controller.NewsReader;
import es.uniovi.eii.cows.controller.reader.rss.parser.RSSParser;

public class RSSReader implements NewsReader {

    private List<NewsItem> news;
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
            news.forEach(CovidFilter::evaluate);                // Search for COVID news
            news = news.stream().filter(CovidFilter::filter).collect(Collectors.toList());
        } catch (MalformedURLException mue) {
            Log.e("MalformedURLException", Objects.requireNonNull(mue.getMessage()));
        } catch (IOException ioe) {
            Log.e("IOException", Objects.requireNonNull(ioe.getMessage()));
        } catch (XmlPullParserException xppe) {
            Log.e("XmlPullParserException", Objects.requireNonNull(xppe.getMessage()));
        }
    }

    @Override
    public void stop() {
        try {
            is.close();
        } catch (IOException e) {
            Log.e("RSSReader", Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    public List<NewsItem> getNews() {
        return news.stream().sorted().collect(Collectors.toList());
    }
}
