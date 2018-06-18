package net;

import android.content.Context;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceActivity;
import android.util.Log;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOError;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.auth.AuthScope;
import cz.msebera.android.httpclient.auth.Credentials;
import cz.msebera.android.httpclient.entity.StringEntity;
import util.PrefUtil;

public class CallWebService extends AsyncHttpResponseHandler implements ICallWebService {
    CallWebServiceDelegate delegate = null;
    String sCode = null;
    private Context context;

    private final String base_url = "http://api.smartduka.busaracenterlab.org";


    public CallWebService(Context context) {
        this.context = context;
    }


    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)  {

        String result = new String(responseBody);//json result

         Log.i("JSONResponse", result);
        // Toast.makeText(context,result,Toast.LENGTH_LONG).show();

        try {
            JSONArray jsonArray = new JSONArray();


            JSONObject object = new JSONObject(result);

            jsonArray.put(object);


            if (delegate != null)
                delegate.ReceivedWebServiceAnswerWithCode(sCode, object, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        //web service call failed //show error message


        error.printStackTrace();

        if (responseBody != null) {
            Log.e("WebService Error:", "" + new String(responseBody));

            try {
                JSONArray jsonArray = new JSONArray();


                JSONObject object = new JSONObject(new String(responseBody));

                jsonArray.put(object);


                if (delegate != null)
                    delegate.ReceivedWebServiceAnswerWithCode(sCode, object, this);

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }


    @Override
    public void login( String email, String password) {
        //post method
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();

            params.put("grant_type","password");
            params.put("username", email);
            params.put("password",password);

            client.setBasicAuth("XmOVq9Hj9EMSVdszH6bgewKTW9sHKmWCjmBJ01M0","nGtgx01hkxUKzI2tt84oArc3Ll5OrYsBKZKbTmZxGLNtivYkGEvuw8YLN5EM8yGOS5jHUFPmJCwoVVSDrgEERnnl9gaKCuxjWjTZMmwoO3W2gUMDP9Fcr0Z3Skz0pz6o");

            client.post(base_url+"/oauth/token/",params,this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getCategories() {
        try {
            AsyncHttpClient client = new AsyncHttpClient();

            client.addHeader("Authorization","Bearer "+new PrefUtil(context).getAccessToken());

            client.get(base_url+"/api/v1/categories/",this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void getVideos(int category_id) {
        try {
            AsyncHttpClient client = new AsyncHttpClient();

            client.addHeader("Authorization","Bearer "+new PrefUtil(context).getAccessToken());

            client.get(base_url+"/api/v1/videos/?category_id="+category_id,this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void getLocations() {
        try {
            AsyncHttpClient client = new AsyncHttpClient();

            client.addHeader("Authorization","Bearer "+new PrefUtil(context).getAccessToken());

            client.get(base_url+"/api/v1/locations/",this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void getCurrentUser() {
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();

            client.addHeader("Authorization","Bearer "+new PrefUtil(context).getAccessToken());

            client.get(base_url+"/api/v1/users/current-user",params,this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public class HttpRequest {

        // A SyncHttpClient is an AsyncHttpClient
        public AsyncHttpClient syncHttpClient = new SyncHttpClient();
        public AsyncHttpClient asyncHttpClient = new AsyncHttpClient();


        public void setCookieStore(PersistentCookieStore cookieStore) {
            getClient().setCookieStore(cookieStore);
        }

        public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            getClient().get(url, params, responseHandler);
        }



        /**
         * @return an async client when calling from the main thread, otherwise a sync client.
         */
        private AsyncHttpClient getClient() {

            asyncHttpClient.setMaxRetriesAndTimeout(3,5000);
            syncHttpClient.setMaxRetriesAndTimeout(3,5000);

            // Return the synchronous HTTP client when the thread is not prepared
            if (Looper.myLooper() == null)
                return syncHttpClient;
            return asyncHttpClient;
        }
    }

}
