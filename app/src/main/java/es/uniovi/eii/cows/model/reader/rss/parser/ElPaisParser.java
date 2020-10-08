package es.uniovi.eii.cows.model.reader.rss.parser;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import es.uniovi.eii.cows.model.NewsItem;

public class ElPaisParser extends BaseRSSParser {

    private static final String URL = "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada";

    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    public ElPaisParser(XmlPullParser xpp) {
        super(URL, xpp);
    }

    @Override
    protected void parseItem() throws IOException, XmlPullParserException {
        // Is a new element
        if (eventType == XmlPullParser.START_TAG) {
            // The element is the wanted type
            if (xpp.getName().equalsIgnoreCase(ITEM)) {
                // We create a new item to fill
                item = new NewsItem();
            } else if (xpp.getName().equalsIgnoreCase(TITLE) && item != null) {
                // Title element
                item.setTitle(xpp.nextText());
            } else if (xpp.getName().equalsIgnoreCase(DESCRIPTION) && item != null) {
                // Description element
                item.setDescription(xpp.nextText());
            }
        // Finished parsing the wanted element
        } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase(ITEM)) {
            Log.i("LNE", item.toString());
            news.add(item);
            item = null;
        }
    }
}
