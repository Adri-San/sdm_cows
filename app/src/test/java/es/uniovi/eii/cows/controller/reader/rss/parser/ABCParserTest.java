package es.uniovi.eii.cows.controller.reader.rss.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

import es.uniovi.eii.cows.model.NewsItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ABCParserTest {

    @Mock
    XmlPullParser mXpp;

    @InjectMocks
    ABCParser parser = new ABCParser(mXpp);

    private NewsItem item;

    @Before
    public void resetItem() {
        item = null;
    }

    @Test
    public void parseItem_createItem() {
        // Set mocking
        parser.setEventType(XmlPullParser.START_TAG);
        when(mXpp.getName()).thenReturn("item");
        // Calls parsing
        try {
            parser.parseItem();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        item = parser.getItem();
        assertNotNull(item);
        assertEquals("ABC", item.getSource());
    }

    @Test
    public void parseItem_parseTitle() {
        // Start item
        parseItem_createItem();
        try {
            // Set mocking
            when(mXpp.getName()).thenReturn("title");
            when(mXpp.nextText()).thenReturn("test");
            // Calls parsing
            parser.parseItem();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals("test", item.getTitle());
    }

    @Test
    public void parseItem_parseDescription() {
        // Start item
        parseItem_createItem();
        try {
            // Set mocking
            when(mXpp.getName()).thenReturn("description");
            when(mXpp.nextText()).thenReturn("test");
            // Calls parsing
            parser.parseItem();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals("test", item.getDescription());
    }

    @Test
    public void parseItem_parseImage() {
        // Start item
        parseItem_createItem();
        try {
            // Set mocking
            when(mXpp.getName()).thenReturn("description");
            when(mXpp.nextText()).thenReturn("<img align=\"left\" src=\"https://static4.abc.es/media/salud/2021/01/09/1-kPyG--620x349@abc.jpg\">test");
            // Calls parsing
            parser.parseItem();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals("test", item.getDescription());
        assertEquals("https://static4.abc.es/media/salud/2021/01/09/1-kPyG--620x349@abc.jpg", item.getImageUrl());
    }

    @Test
    public void parseItem_parseLink() {
        // Start item
        parseItem_createItem();
        try {
            // Set mocking
            when(mXpp.getName()).thenReturn("link");
            when(mXpp.nextText()).thenReturn("test");
            // Calls parsing
            parser.parseItem();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals("test", item.getLink());
    }

    @Test
    public void parseItem() {
        parseItem_createItem();
        parseItem_parseTitle();
        parseItem_parseImage();
        parseItem_parseLink();
        try {
            // Set mocking
            parser.setEventType(XmlPullParser.END_TAG);
            when(mXpp.getName()).thenReturn("item");
            // Calls parsing
            parser.parseItem();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        // Assert
        assertNull(parser.getItem());
        List<NewsItem> newsItemList = parser.getNews();
        assertEquals(1, newsItemList.size());
        assertEquals(item, newsItemList.get(0));
    }

    @Test
    public void getUrl() {
        assertEquals("https://www.abc.es/rss/feeds/abc_SociedadSalud.xml", parser.getURL());
    }
}