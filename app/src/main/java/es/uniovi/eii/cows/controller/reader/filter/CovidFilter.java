package es.uniovi.eii.cows.controller.reader.filter;

import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import es.uniovi.eii.cows.model.NewsItem;

public class CovidFilter {

	public static final int THRESHOLD = 7;

	public static String[] topTerms = {
			"covid",
			"covid-19",
			"coronavirus",
			"sars-cov-2"
	};

	public static String[] relatedTerms = {
			"pandemia",
			"alarma",
			"confinamiento",
			"pcr",
			"restricciones",
			"mascarilla"
	};

	public static void evaluate(NewsItem item) {
		if (!item.isCovidRelated()) {
			long h = 0;
			h += evaluateString(item.getTitle()) * 2;
			h += evaluateString(item.getDescription());
			item.setCovidRelated(h > THRESHOLD);
			if (h > 0)	Log.d("Evaluation", "[" + h + "] " + item.getTitle());
		}
	}

	private static long evaluateString(String s) {
		String[] words = s.trim().toLowerCase().split(" ");
		AtomicLong top = new AtomicLong(0);
		Arrays.stream(topTerms)
				.map(t-> Arrays.stream(words).filter(w -> w.equals(t)).count())
				.reduce(Long::sum).ifPresent(top::set);
		AtomicLong related = new AtomicLong(0);
		Arrays.stream(relatedTerms)
				.map(t-> Arrays.stream(words).filter(w -> w.equals(t)).count())
				.reduce(Long::sum).ifPresent(related::set);
		return top.get() * 5 + related.get();
	}

	public static boolean filter(NewsItem item) {
		return item.isCovidRelated();
	}

}
