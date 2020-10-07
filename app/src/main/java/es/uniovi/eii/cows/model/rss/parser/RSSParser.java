package es.uniovi.eii.cows.model.rss.parser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

import es.uniovi.eii.cows.model.NewsItem;

public interface RSSParser {

    /**
     * Parses the XML file and returns the formatted news
     * @return      Parsed news
     * @throws XmlPullParserException
     */
    List<NewsItem> parse() throws XmlPullParserException, IOException;
}
