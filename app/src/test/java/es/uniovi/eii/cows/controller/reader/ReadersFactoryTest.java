package es.uniovi.eii.cows.controller.reader;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ReadersFactoryTest {

	// Mocks
	private static Context mContext = mock(Context.class);
	private static SharedPreferences mSharedPreferences = mock(SharedPreferences.class);
	// Instance
	private ReadersFactory factory;

	@BeforeClass
	public static void setMocks() {
		when(mContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mSharedPreferences);
	}

	@Before
	public void setUp() {
		factory = ReadersFactory.getInstance(mContext);
	}

	@Test
	public void getInstance() {
		assertNotNull(factory);
	}

	@Test
	public void getReaders_all() {
		// Set preferences
		when(mSharedPreferences.getBoolean("source_abc", true)).thenReturn(true);
		when(mSharedPreferences.getBoolean("source_eldiario", true)).thenReturn(true);
		when(mSharedPreferences.getBoolean("source_elpais", true)).thenReturn(true);
		when(mSharedPreferences.getBoolean("source_lne", true)).thenReturn(true);
		// Get list of readers
		List<NewsReader> readerList = factory.getReaders();
		assertEquals(4, readerList.size());
	}

	@Test
	public void getReaders_some() {
		// Set preferences
		when(mSharedPreferences.getBoolean("source_abc", true)).thenReturn(true);
		when(mSharedPreferences.getBoolean("source_eldiario", true)).thenReturn(false);
		when(mSharedPreferences.getBoolean("source_elpais", true)).thenReturn(true);
		when(mSharedPreferences.getBoolean("source_lne", true)).thenReturn(false);
		// Get list of readers
		List<NewsReader> readerList = factory.getReaders();
		assertEquals(2, readerList.size());
	}

}
