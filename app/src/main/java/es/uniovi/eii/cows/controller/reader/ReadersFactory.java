package es.uniovi.eii.cows.controller.reader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.cows.controller.reader.rss.RSSReader;
import es.uniovi.eii.cows.controller.reader.rss.parser.ElPaisParser;
import es.uniovi.eii.cows.controller.reader.rss.parser.LNEParser;
import es.uniovi.eii.cows.controller.NewsReader;

public class ReadersFactory {

    private static ReadersFactory instance;                     // Singleton

    private XmlPullParserFactory factory;
    // TODO private T sources; <-- file containing the user selected sources

    private ReadersFactory() throws XmlPullParserException {
        this.factory = XmlPullParserFactory.newInstance();
        this.factory.setNamespaceAware(false);
    }

    /**
     * @return Instance of the factory
     */
    public static ReadersFactory getInstance() {
        try {
            if (instance == null)
                instance = new ReadersFactory();
        } catch (XmlPullParserException e) {
            Log.e("ReadersFactory", e.getMessage());
        } finally {
            return instance;
        }
    }

    /**
     * @return Readers of the sources selected by the user
     */
    public List<NewsReader> getReaders() {
        List<NewsReader> readers = new ArrayList<>();
        try {
            // TODO for s: sources -> add sLoader
            readers.add(new RSSReader(
                    new ElPaisParser(factory.newPullParser())));
            readers.add(new RSSReader(
                    new LNEParser(factory.newPullParser())));
        } catch (XmlPullParserException e) {
            Log.e("ReadersFactory", e.getMessage());
        } finally {
            return readers;
        }
    }
}
