package es.uniovi.eii.cows.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;

import java.util.Objects;

import es.uniovi.eii.cows.R;

/**
 * Java Beans class of the news
 */
public class NewsItem implements Comparable<NewsItem>, Parcelable {

	@DocumentId
	private String id; 									//Identifier on database

	private String title;
	private String description;
	private String link;                                // News link
	private Long dateTime;                           	// Publication date (stored as Long)
	private String source;                              // Source of the news
	private String imageUrl;                            // URL of the image of the news
	private int fallbackImage;                       	// Image to use when no image

	private boolean covidRelated;						// Flag to set items as CovidRelated

	public NewsItem() {
		title = "";
		description = "";
		link = "";
		source = "";
		imageUrl = "";
		covidRelated = false;
		dateTime = 0L;
	}

	protected NewsItem(Parcel in) {
		title = in.readString();
		description = in.readString();
		link = in.readString();
		dateTime = (Long) in.readValue(Long.class.getClassLoader());
		source = in.readString();
		imageUrl = in.readString();
		fallbackImage = in.readInt();
		covidRelated = in.readInt() == 0;
		id = in.readString();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getImageUrl() {
		if (imageUrl == null || imageUrl.isEmpty())
			return "android.resource://" +
					Objects.requireNonNull(R.class.getPackage()).getName() + "/"+ fallbackImage;
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getFallbackImage() {
		return fallbackImage;
	}

	public void setFallbackImage(int fallbackImage) {
		this.fallbackImage = fallbackImage;
	}

	@Exclude //not used by database
	public LocalDateTime getDate() { return getLocalDateTime(dateTime); }

	@Exclude //not used by database
	public void setDate(LocalDateTime date) {
		setLocalDateTime(date);
	}


	public boolean isCovidRelated() {
		return covidRelated;
	}

	public void setCovidRelated(boolean covidRelated) {
		this.covidRelated = covidRelated;
	}

	public String getId() { return id; }

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NewsItem newsItem = (NewsItem) o;
		return link.equals(newsItem.link);
	}

	@Override
	public int hashCode() {
		return Objects.hash(link);
	}

	@Override
	@NonNull
	public String toString() {
		return "NewsItem{ " + title + " - " + source +
				", [" + link + "] }";
	}

	@Override
	public int compareTo(NewsItem newsItem) {
		return this.dateTime.compareTo(newsItem.dateTime)*(-1);
	}

	/**
	 * Private utility method that converts the received epoch seconds
	 * into a LocalDateTime object
	 * @param time epoch seconds (as stored in the database)
	 * @return localDateTime object (used by the application)
	 */
	private LocalDateTime getLocalDateTime(Long time){ return LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault()); }

	/**
	 * Private utility method that converts the received localDateTime object into
	 * a Long (epoch second) and stores it, database is going to use this Long.
	 * @param time localDateTime object (used by the application)
	 */
	private void setLocalDateTime(LocalDateTime time){ dateTime = time.toInstant(ZoneOffset.UTC).getEpochSecond(); }

	//Getters and setters to be used by database mapper
	public Long getDateTime() { return dateTime; }
	public void setDateTime(Long dateTime) { this.dateTime = dateTime; }

	//Parcelable methods

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(description);
		dest.writeString(link);
		dest.writeValue(dateTime == null ? 0 : dateTime);
		dest.writeString(source);
		dest.writeString(imageUrl);
		dest.writeInt(fallbackImage);
		dest.writeInt(covidRelated ? 0 : 1);
		dest.writeString(id);
	}

	public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
		@Override
		public NewsItem createFromParcel(Parcel in) {
			return new NewsItem(in);
		}

		@Override
		public NewsItem[] newArray(int size) {
			return new NewsItem[size];
		}
	};
}
