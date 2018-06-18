package net;


import java.lang.reflect.Array;
import java.util.ArrayList;

import model.Category;
import model.Location;
import model.Video;

/**
 * Created by Kawawa on 16/06/2018.
 */
public interface ISyncModel {
    CallWebService GetWebService(String sCode);

    interface WebServiceErrorDelegate{
        void onErrorReceived();
    }


    void login(String email, String password);

    interface  LoginDelegate{
        void onSuccess();
        void onError(String message);
    }

    void getCategories();

    interface CategoriesDelegate{
        void onReponse(ArrayList<Category> categories);
    };

    void getVideos(int category_id);

    interface VideosDelegate{
        void onResponse(ArrayList<Video> videoList);
    }

    void getLocations();

    interface LocationsDelegate{
        void onResponse(ArrayList<Location> videoList);
    }

    void getCurrentUser();

    interface  CurrentUserDelegate{
        void onResponse(String first_name,String last_name,int location);
    }





}
