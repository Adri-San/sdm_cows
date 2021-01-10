package es.uniovi.eii.cows.controller.reader.rss.parser;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.model.NewsItem;

public class ABCParser extends BaseRSSParser {

    public static final String URL = "https://www.abc.es/rss/feeds/abc_SociedadSalud.xml";
    public static final String SOURCE = "ABC";
    public static final int DEFAULT_IMAGE =  R.drawable.abc;

    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";    // html formatted
    private static final String LINK = "link";
    private static final String DATE = "pubDate";

    private final Pattern pattern = Pattern.compile("http.*");;

    public ABCParser(XmlPullParser xpp) {
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
                String description = xpp.nextText();
                String regex = "<img.*align=\".*\">";
                item.setDescription(description.replaceFirst(regex, ""));
                // Image element
                String tagContent =  StringUtils.substringBetween(description, "<img", ">");
                if (tagContent != null) {
                    Optional<String> firstImage = Arrays.stream(
                            tagContent.split("\"")).filter(pattern.asPredicate()).findFirst();
                    firstImage.ifPresent(item::setImageUrl);
                }
            } else if (xpp.getName().equalsIgnoreCase(LINK) && item != null) {
                // Link element
                item.setLink(xpp.nextText());
            } else if (xpp.getName().equalsIgnoreCase(DATE) && item != null) {
                // Date element
                item.setDate(LocalDateTime.parse(xpp.nextText(), DateTimeFormatter.RFC_1123_DATE_TIME));
            }
            // ABC news doesn't have images
            // Finished parsing the wanted element
        } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase(ITEM)) {
            news.add(item);
            item = null;
        }
    }
}
