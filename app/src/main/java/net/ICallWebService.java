package net;


import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kawawa on 15/06/2018.
 */
public interface ICallWebService {
    interface CallWebServiceDelegate{
        void ReceivedWebServiceAnswerWithCode(String sType, JSONObject dData, CallWebService cwsCall);
    }

    void login(String email, String password);

    void getCategories();

    void getVideos(int category_id);

    void getLocations();

    void getCurrentUser();


}
