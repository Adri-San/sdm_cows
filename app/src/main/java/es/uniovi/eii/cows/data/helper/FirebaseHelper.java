package es.uniovi.eii.cows.data.helper;


import android.util.Pair;

import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;
import es.uniovi.eii.cows.data.repositories.LikeRepository;
import es.uniovi.eii.cows.data.repositories.NewsItemRepository;
import es.uniovi.eii.cows.data.repositories.SaveRepository;
import es.uniovi.eii.cows.model.NewsItem;


public class FirebaseHelper {

    private static FirebaseHelper instance = new FirebaseHelper();

    //Repositories
    private BaseRepository newsItemRepository;
    private BaseRepository likeRepository;
    private BaseRepository saveRepository;

    private FirebaseHelper(){
        newsItemRepository = new NewsItemRepository();
        likeRepository = new LikeRepository();
        saveRepository = new SaveRepository();
    }

    /**
     * @return  Instance of the database helper
     */
    public static FirebaseHelper getInstance() {
        return instance;
    }

    //Add Methods
    public void addNewsItem(NewsItem newsItem, Function<NewsItem, Void> callback) { newsItemRepository.add(newsItem, n-> callback.apply((NewsItem) n)); }
    public void addLike(String idNewsItem, Function<String, Void> callback){ likeRepository.add(idNewsItem, n-> callback.apply((String) n)); }
    public void addSave(String idNewsItem, Function<String, Void> callback){ saveRepository.add(idNewsItem, n-> callback.apply((String) n)); }

    //Delete Methods
    public void deleteNewsItem(NewsItem newsItem, Function<NewsItem, Void> callback){ newsItemRepository.delete(newsItem, n-> callback.apply((NewsItem) n)); }
    public void deleteLike(String idNewsItem, Function<String, Void> callback) { likeRepository.delete(idNewsItem, n-> callback.apply((String) n)); }
    public void deleteSave(String idNewsItem, Function<String, Void> callback) { saveRepository.delete(idNewsItem, n-> callback.apply((String) n));}

    //Get All Methods
    public void getNewsItems(Function<NewsItem, Void> callback) { newsItemRepository.getAll(n -> callback.apply((NewsItem) n)); }
    public void getLikedNewsItems(Function<NewsItem, Void> callback) { likeRepository.getAll(l -> callback.apply((NewsItem) l)); }
    public void getSavedNewsItems(Function<NewsItem, Void> callback) { saveRepository.getAll(s -> callback.apply((NewsItem) s)); }

    //Get by id Methods
    public void getLikeById(String idLike, Function<NewsItem, Void> callback) {likeRepository.get( Pair.create("__name__", idLike), t -> callback.apply((NewsItem) t));}
    public void getSaveById(String idSave, Function<NewsItem, Void> callback) {saveRepository.get( Pair.create("__name__", idSave), t -> callback.apply((NewsItem) t));}
    public void getNewsItemById(String idNewsItem, Function<NewsItem, Void> callback) {newsItemRepository.get( Pair.create("__name__", idNewsItem), t -> callback.apply((NewsItem) t));}


}
