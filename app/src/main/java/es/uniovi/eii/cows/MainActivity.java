package es.uniovi.eii.cows;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titles = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    new NewsLoader().run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public class NewsLoader {

        public final static String URL = "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada";

        public NewsLoader() {}

        public void run() {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(getInputStream(), "UTF-8");
                Log.i("URL", URL);

                boolean insideItem = false;
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title") && insideItem) {
                            String text = xpp.nextText();
                            Log.i("TITLE", text);
                            titles.add(text);
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }
                    eventType = xpp.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public InputStream getInputStream() throws IOException {
            return new URL(URL).openConnection().getInputStream();
        }
    }
}