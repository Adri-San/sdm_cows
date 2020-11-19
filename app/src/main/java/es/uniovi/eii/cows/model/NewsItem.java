package es.uniovi.eii.cows.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;

import org.threeten.bp.LocalDateTime;

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
	private LocalDateTime date;                         // Publication date
	private String source;                              // Source of the news
	private String imageUrl;                            // URL of the image of the news
	private int fallbackImage;                       	// Image to use when no image

	private boolean covidRelated;						// Flag to set items as CovidRelated

	public NewsItem() {
		covidRelated = false;
	}

	protected NewsItem(Parcel in) {
		title = in.readString();
		description = in.readString();
		link = in.readString();
		date = (LocalDateTime) in.readValue(LocalDateTime.class.getClassLoader());
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

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
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
		// TODO algorithm to recognise similar news
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
		return this.date.compareTo(newsItem.date)*(-1);
	}

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
		dest.writeValue(date);
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
