package es.uniovi.eii.cows.model;

import androidx.annotation.NonNull;

import org.threeten.bp.LocalDateTime;

import java.util.Objects;

/**
 * Java Beans class of the news
 */
public class NewsItem {

    private String title;
    private String description;
    private String link;                                // News link
    private LocalDateTime date;                         // Publication date
    private String source;                              // Source of the news
    private String imageUrl;                            // URL of the image of the news
    private String fallbackImage;                       // Image to use when no image

    public NewsItem() {
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
            return fallbackImage;
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFallbackImage() {
        return fallbackImage;
    }

    public void setFallbackImage(String fallbackImage) {
        this.fallbackImage = fallbackImage;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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
}
