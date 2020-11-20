package es.uniovi.eii.cows.data.helper;


import java.util.List;
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

    /**
     * Adds the specified newsItem to the collection
     * if it does not already exists
     * @param newsItem
     */
    public void addNewsItem(NewsItem newsItem) { newsItemRepository.add(newsItem); }

    /**
     * Adds a reference to the liked newsItem
     * @param id identifier of the liked newsItem
     */
    public void addLike(String id){ likeRepository.add(id); }

    /**
     * Adds a reference to the saved newsItem
     * @param id identifier of the saved newsItem
     */
    public void addSave(String id){ saveRepository.add(id); }



}
