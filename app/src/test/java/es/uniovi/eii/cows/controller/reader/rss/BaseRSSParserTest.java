package es.uniovi.eii.cows.controller.reader.rss;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import es.uniovi.eii.cows.controller.reader.rss.parser.BaseRSSParser;
import es.uniovi.eii.cows.controller.reader.rss.parser.ElPaisParser;
import es.uniovi.eii.cows.controller.reader.rss.parser.LNEParser;
import es.uniovi.eii.cows.model.NewsItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BaseRSSParserTest {

    @Mock
    XmlPullParser mXpp;
    InputStream mInputStream;

    @InjectMocks
    BaseRSSParser parser = new ElPaisParser(mXpp);

    private NewsItem item;

    @Before
    public void resetItem() {
        item = null;
    }

    @Test
    public void parse() {
        try {
            assertNull(parser.getItem());
            // Set mocking
            when(mXpp.getEventType()).thenReturn(XmlPullParser.START_TAG);
            when(mXpp.getName()).thenReturn("item");
            when(mXpp.next()).thenReturn(XmlPullParser.END_DOCUMENT);
            // Calls parsing
            parser.parse(mInputStream);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        item = parser.getItem();
        assertNotNull(item);
        assertEquals("El País", item.getSource());
        assertEquals(0, parser.getNews().size());
    }
}