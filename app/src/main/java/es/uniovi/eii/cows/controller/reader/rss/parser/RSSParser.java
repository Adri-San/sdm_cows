package es.uniovi.eii.cows.controller.reader.rss.parser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import es.uniovi.eii.cows.model.NewsItem;

public interface RSSParser {

    /**
     * Parses the XML file and returns the formatted news
     * @return      Parsed news
     * @throws XmlPullParserException
     */
    Set<NewsItem> parse(InputStream is) throws XmlPullParserException, IOException;

    /**
     * @return  Feed URL
     */
    String getURL();

}
