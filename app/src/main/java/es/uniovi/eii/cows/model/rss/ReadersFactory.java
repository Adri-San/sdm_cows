package es.uniovi.eii.cows.model.rss;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.cows.model.rss.parser.ElPaisParser;

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
     * @throws XmlPullParserException
     */
    public static ReadersFactory getInstance() throws XmlPullParserException {
        if (instance == null)
            instance = new ReadersFactory();
        return instance;
    }

    /**
     * @return Readers of the sources selected by the user
     * @throws XmlPullParserException
     */
    public List<NewsReader> getReaders() {
        List<NewsReader> readers = new ArrayList<>();
        try {
            // TODO for s: sources -> add sLoader
            readers.add(new RSSReader(
                    new ElPaisParser(
                            "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada",
                            factory.newPullParser())));
        } catch (XmlPullParserException e) {
            // TODO throw RSSReaderExceptions with the error message
        } finally {
            return readers;
        }
    }
}
