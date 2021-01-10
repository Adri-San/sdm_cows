package es.uniovi.eii.cows.controller.reader.rss.parser;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.model.NewsItem;

public class ElPaisParser extends BaseRSSParser {

    public static final String URL = "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada";
    public static final String SOURCE = "El País";
    public static final int DEFAULT_IMAGE =  R.drawable.el_pais;

    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "content:encoded";    // html formatted
    private static final String LINK = "link";
    private static final String DATE = "pubDate";
    private static final String MEDIA = "media:content";
    //private static final String CATEGORY = "category";
    //private static final String THUMBNAIL = "media:thumbnail";
    //private static final String COVID19 = "Coronavirus";

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
                item.setDate(LocalDateTime.parse(xpp.nextText(), DateTimeFormatter.RFC_1123_DATE_TIME));
            } else if (xpp.getName().equalsIgnoreCase(MEDIA)) {
                // Media element
                if (!xpp.getAttributeValue(0).isEmpty()) {
                    // Image element
                    item.setImageUrl(xpp.getAttributeValue(0));
                }
            }
        } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase(ITEM)) {
            // Finished parsing the wanted element
            news.add(item);
            item = null;
        }
    }
}
