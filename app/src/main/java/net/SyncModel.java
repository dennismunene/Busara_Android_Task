package net;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import model.Category;
import model.Location;
import model.Video;
import util.PrefUtil;

/**
 * Created by DENNOH on 10/19/2016.
 */
public class SyncModel implements ISyncModel, ICallWebService.CallWebServiceDelegate {
    private Context context;
    private LoginDelegate loginDelegate;

    public void setCurrentUserDelegate(CurrentUserDelegate currentUserDelegate) {
        this.currentUserDelegate = currentUserDelegate;
    }

    private CurrentUserDelegate currentUserDelegate;

    public void setLocationsDelegate(LocationsDelegate locationsDelegate) {
        this.locationsDelegate = locationsDelegate;
    }

    private  LocationsDelegate locationsDelegate;



    public void setVideosDelegate(VideosDelegate videosDelegate) {
        this.videosDelegate = videosDelegate;
    }

    private  VideosDelegate videosDelegate;

    public void setCategoriesDelegate(CategoriesDelegate categoriesDelegate) {
        this.categoriesDelegate = categoriesDelegate;
    }

    private  CategoriesDelegate categoriesDelegate;


    public SyncModel(Context context) {
        this.context = context;
    }

    public void setLoginDelegate(LoginDelegate loginDelegate) {
        this.loginDelegate = loginDelegate;
    }

    @Override
    public CallWebService GetWebService(String sCode) {
        CallWebService cws = new CallWebService(context);
        cws.sCode = sCode;
        cws.delegate = this;
        return cws;
    }

    @Override
    public void login(String email, String password) {
        GetWebService(SCode.LOGIN).login(email, password);
    }

    @Override
    public void getCategories() {
        GetWebService(SCode.GET_CATEGORIES).getCategories();
    }

    @Override
    public void getVideos(int category_id) {
        GetWebService(SCode.GET_VIDEOS).getVideos(category_id);
    }

    @Override
    public void getLocations() {
        GetWebService(SCode.GET_LOCATIONS).getLocations();
    }

    @Override
    public void getCurrentUser() {
        GetWebService(SCode.GET_USER).getCurrentUser();
    }


    @Override
    public void ReceivedWebServiceAnswerWithCode(String sType, final JSONObject dData, CallWebService cwsCall) {

        if (cwsCall.sCode.equals(SCode.LOGIN)) {
            loginAction(dData);
        }

        if (cwsCall.sCode.equals(SCode.GET_CATEGORIES)) {
            getCategoriesAction(dData);
        }


        if (cwsCall.sCode.equals(SCode.GET_VIDEOS)) {
            getVideosAction(dData);
        }

        if (cwsCall.sCode.equals(SCode.GET_LOCATIONS)) {
           getLocationsAction(dData);
        }

        if (cwsCall.sCode.equals(SCode.GET_USER)) {
            getUserAction(dData);
        }

        Log.v("ParsedJSON", "sCode " + cwsCall.sCode + " " + dData.toString());


    }

    private void getUserAction(JSONObject dData) {
        try{
            String first_name = dData.getString("first_name");
            String last_name = dData.getString("last_name");
            int location = dData.getInt("location");

            if(currentUserDelegate!=null)
                currentUserDelegate.onResponse(first_name,last_name,location);

        }catch (JSONException e){e.printStackTrace();}
    }

    private void getLocationsAction(JSONObject dData) {
        try{


            JSONArray results = dData.getJSONArray("results");

            ArrayList<Location> locations = new ArrayList<>();


            for(int i=0;i<results.length();i++) {
                Location location = new Location();


                location.name = results.getJSONObject(i).getString("name");
                location.id = results.getJSONObject(i).getInt("id");

                locations.add(location);
            }

            if(locationsDelegate!=null)
                locationsDelegate.onResponse(locations);


        }catch (JSONException e){
            e.printStackTrace();
            if(locationsDelegate!=null)
                locationsDelegate.onResponse(new ArrayList<Location>());

        }
    }

    private void getVideosAction(JSONObject dData) {
        try{


            JSONArray results = dData.getJSONArray("results");

            ArrayList<Video> videos = new ArrayList<>();


            for(int i=0;i<results.length();i++) {
                Video video = new Video();


                video.name = results.getJSONObject(i).getString("name");
                video.category = results.getJSONObject(i).getInt("category");
                video.description = results.getJSONObject(i).getString("description");
                video.file_path = results.getJSONObject(i).getString("file_path");

                videos.add(video);
            }

            if(videosDelegate!=null)
                videosDelegate.onResponse(videos);


        }catch (JSONException e){
            e.printStackTrace();
            if(videosDelegate!=null)
                videosDelegate.onResponse(new ArrayList<Video>());

        }
    }

    private void getCategoriesAction(JSONObject dData) {
        try{


            JSONArray results = dData.getJSONArray("results");

            ArrayList<Category> categories = new ArrayList<>();


            for(int i=0;i<results.length();i++) {
                Category category = new Category();


                category.id = results.getJSONObject(i).getInt("id");
                category.name = results.getJSONObject(i).getString("name");
                category.description = results.getJSONObject(i).getString("description");
                category.isActive = results.getJSONObject(i).getBoolean("is_active");
                category.created = results.getJSONObject(i).getString("created");

                categories.add(category);
            }

            if(categoriesDelegate!=null)
                categoriesDelegate.onReponse(categories);


        }catch (JSONException e){
            e.printStackTrace();
            if(categoriesDelegate!=null)
                categoriesDelegate.onReponse(new ArrayList<Category>());

        }
    }


    private void loginAction(JSONObject dData) {
        try {


           String access_token = dData.getString("access_token");
           String token_type = dData.getString("token_type");
           String expires_in = dData.getString("expires_in");
           String refresh_token = dData.getString("refresh_token");
           String scope = dData.getString("scope");

            new PrefUtil(context).setAccessToken(access_token);

           if(loginDelegate!=null)
               loginDelegate.onSuccess();

        } catch (JSONException e) {
            e.printStackTrace();

            if(loginDelegate!=null)
                loginDelegate.onError("Login Failed! "+e.getMessage());

        }
    }


}
