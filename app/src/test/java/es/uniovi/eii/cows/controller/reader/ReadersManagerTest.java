package es.uniovi.eii.cows.controller.reader;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import es.uniovi.eii.cows.controller.reader.rss.RSSReader;
import es.uniovi.eii.cows.model.NewsItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ReadersManagerTest {

	@Mock
	ThreadPoolExecutor mTPE = mock(ThreadPoolExecutor.class);
	NewsReader mNewsReader = mock(NewsReader.class);

	// Instance
	@InjectMocks
	ReadersManager manager = ReadersManager.getInstance();


	@Test
	public void getInstance() {
		assertNotNull(manager);
	}

	@Test
	public void run() {
		// Mock
		Context mContext = mock(Context.class);
		SharedPreferences mSharedPreferences = mock(SharedPreferences.class);
		when(mContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mSharedPreferences);
		when(mSharedPreferences.getBoolean("source_abc", true)).thenReturn(true);
		when(mSharedPreferences.getBoolean("source_eldiario", true)).thenReturn(true);
		// Run
		manager.run(mContext);
		// Assert
		doAnswer(invocation -> {
			Object invoker = invocation.getArguments()[0];
			assertTrue(invoker instanceof RSSReader);
			return null;
		}).when(mTPE).execute(any(NewsReader.class));
	}

	@Test
	public void getNews() {
		Context mContext = mock(Context.class);
		SharedPreferences mSharedPreferences = mock(SharedPreferences.class);
		when(mContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mSharedPreferences);
		// Run
		manager.run(mContext);
		List<NewsItem> managerList = manager.getNews();
		// Assert
		assertEquals(0, managerList.size());
	}
}
