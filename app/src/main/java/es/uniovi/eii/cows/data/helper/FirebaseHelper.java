package es.uniovi.eii.cows.data.helper;


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
    public void addNewsItem(NewsItem newsItem) { newsItemRepository.add(newsItem); }
    public void addLike(String id){ likeRepository.add(id); }
    public void addSave(String id){ saveRepository.add(id); }

    //Get Methods
    public void getLikedNewsItems(Function<NewsItem, Void> callback) { likeRepository.getAll(l -> callback.apply((NewsItem) l)); }
    public void getSavedNewsItems(Function<NewsItem, Void> callback) { saveRepository.getAll(s -> callback.apply((NewsItem) s)); }
}
