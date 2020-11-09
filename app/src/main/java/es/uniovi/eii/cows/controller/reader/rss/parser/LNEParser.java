package es.uniovi.eii.cows.controller.reader.rss.parser;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.model.NewsItem;

public class LNEParser extends BaseRSSParser {

    public static final String URL = "https://www.lne.es/elementosInt/rss/42";
    public static final String SOURCE = "La Nueva España";
    public static final int DEFAULT_IMAGE = R.drawable.la_nueva_espana;

    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";            // html format
    private static final String LINK = "link";
    private static final String DATE = "pubDate";

    public LNEParser(XmlPullParser xpp) {
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
                item.setSource(SOURCE);
                item.setFallbackImage(DEFAULT_IMAGE);
            } else if (xpp.getName().equalsIgnoreCase(TITLE) && item != null) {
                // Title element
                item.setTitle(xpp.nextText());
            } else if (xpp.getName().equalsIgnoreCase(DESCRIPTION) && item != null) {
                // Description element
                item.setDescription(xpp.nextText());
            } else if (xpp.getName().equalsIgnoreCase(LINK) && item != null) {
                // Link element
                item.setLink(xpp.nextText());
            } else if (xpp.getName().equalsIgnoreCase(DATE) && item != null) {
                // Date element
                String[] date = xpp.nextText().split(" ");
                date[3] = "20" + date[3];
                item.setDate(LocalDateTime.parse(
                        Arrays.stream(date).collect(Collectors.joining(" ")),
                        DateTimeFormatter.RFC_1123_DATE_TIME));
            }
            // LNE news doesn't have images nor categories
        // Finished parsing the wanted element
        } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase(ITEM)) {
            news.add(item);
            item = null;
        }
    }
}
