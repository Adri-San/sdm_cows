package es.uniovi.eii.cows.controller.reader;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.controller.reader.rss.RSSReader;
import es.uniovi.eii.cows.controller.reader.rss.parser.ABCParser;
import es.uniovi.eii.cows.controller.reader.rss.parser.ElDiarioParser;
import es.uniovi.eii.cows.controller.reader.rss.parser.ElPaisParser;
import es.uniovi.eii.cows.controller.reader.rss.parser.LNEParser;

public class ReadersFactory {

    private static ReadersFactory instance;                     // Singleton

    private SharedPreferences preferences;

    private Map<String, NewsReader> readers;

    private ReadersFactory(Context context) throws XmlPullParserException {
        // Get the shared preferences
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        // Creates a copy of each possible reader
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        readers = new HashMap<>();
        readers.put("source_abc", new RSSReader(new ABCParser(factory.newPullParser())));
        readers.put("source_eldiario", new RSSReader(new ElDiarioParser(factory.newPullParser())));
        readers.put("source_elpais", new RSSReader(new ElPaisParser(factory.newPullParser())));
        readers.put("source_lne", new RSSReader(new LNEParser(factory.newPullParser())));
    }

    /**
     * @return Instance of the factory
     */
    public static ReadersFactory getInstance(Context context) {
        try {
            if (instance == null)
                instance = new ReadersFactory(context);
        } catch (XmlPullParserException e) {
            Log.e("ReadersFactory", Objects.requireNonNull(e.getMessage()));
        }
        return instance;
    }

    /**
     * @return Readers of the sources selected by the user
     */
    public List<NewsReader> getReaders() {
        List<NewsReader> list = new ArrayList<>();
        for (String key: readers.keySet()) {
            // Returns only the readers selected in the settings
            if (preferences.getBoolean(key, true)) {
                list.add(readers.get(key));
            }
        }
        return list;
    }
}
