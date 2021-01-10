package es.uniovi.eii.cows.controller.reader.rss.filter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.uniovi.eii.cows.model.NewsItem;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CovidFilterTest {

	@Test
	public void evaluate_related() {
		NewsItem item = new NewsItem();
		item.setTitle("Coronavirus");
		item.setDescription("Ayuso dice que olvidó ponerse la alarma esta mañana y por eso olvidó" +
				"decir su tontería diaria, hoy tenía apuntado decir que las mascarillas causan más" +
				"contagios y que PCR viene de Partido Comunista Republicano");
		assertFalse(item.isCovidRelated());
		CovidFilter.evaluate(item);
		assertTrue(item.isCovidRelated());
	}

	@Test
	public void evaluate_unrelated() {
		NewsItem item = new NewsItem();
		item.setTitle("Willyrex abre los ojos");
		item.setDescription("En medio de esta pandemia ha saltado la noticia, resulta que durante" +
				"la PCR que el famoso youtuber se estaba practicando en Andorra abrió los ojos" +
				"después del susto por lo molesta que fue la prueba, todo esto antes de que la" +
				"enfermera sacase el palito de su envoltorio siquiera.");
		assertFalse(item.isCovidRelated());
		CovidFilter.evaluate(item);
		assertFalse(item.isCovidRelated());
	}

	@Test
	public void filter() {
		NewsItem related = new NewsItem();
		related.setCovidRelated(true);
		NewsItem unrelated = new NewsItem();
		unrelated.setCovidRelated(false);
		assertTrue(CovidFilter.filter(related));
		assertFalse(CovidFilter.filter(unrelated));
	}

}
