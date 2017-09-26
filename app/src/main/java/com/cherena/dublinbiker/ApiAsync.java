package com.cherena.dublinbiker;

import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cherena on 19/09/2017.
 */

public class ApiAsync extends ListActivity {





    private static class ApiShit{

        OkHttpClient okHttpClient;
        Request request;


        private String ApiShitDoer(String url){

            okHttpClient = new OkHttpClient();
            request = new Request.Builder().url(url).build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    return response.body().toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
        }
    }


    public static class HttpsGetAsync extends AsyncTask<Void, Void, List<String>>{
        private static final String API_KEY = "&apiKey=089c7b4cf8de20a2b563d9e016d6b73563996885";

        private static final String URL = "https://api.jcdecaux.com/vls/v1/stations?contract=Dublin" + API_KEY;

        List<String> responseArray = null;
        ApiListFragment ALF;

        public HttpsGetAsync(ApiListFragment view){


        }

//    OkHttpClient client = new OkHttpClient();
//    AndroidHttpClient mClient = AndroidHttpClient.newInstance("");
//    protected String run(String URL) throws IOException {
//        Request request = new Request.Builder()
//            .url(URL)
//            .build();

//    String run(String URL) throws IOException {
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        } catch (IOException e) {
//            return e.Message;
//        }
//    }
    @Override
    protected List<String> doInBackground(Void... params) {
        JSONResponseHandler responseHandler = new JSONResponseHandler();
        ApiShit as = new ApiShit();
        String response = as.ApiShitDoer(URL);
        try {
            responseArray = responseHandler.handleResponse(response);
        }catch (IOException e){
            e.printStackTrace();
        }
        return responseArray;





//        HttpGet request = new HttpGet(URL);
//        JSONResponseHandler responseHandler = new JSONResponseHandler();
//        try {
//            return mClient.execute(request, responseHandler);
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    @Override
    protected void onPostExecute(List<String> result) {

//        if (responseArray != null){
//
//            ALF.getListView().setAdapter( ALF.getListAdapter()<String>(
//                    ALF.getContext(), R.layout.fragment_apilist_list, result
//            ));
//        }
        ALF.setResults(result);
        Intent intent = new Intent();
        intent.putExtra("result", result.toArray());
//        if (null != mClient)
//            mClient.close();
//        setListAdapter(new ArrayAdapter<String>(
//                NetworkingAndroidHttpClientJSONActivity.this,
//                R.layout.list_item, result));
//        Intent intent = new Intent();
//
//        intent.putExtra("result", result.toArray());


    }
}

private static class JSONResponseHandler {

    private static final String LONGITUDE_TAG = "lng";
    private static final String LATITUDE_TAG = "lat";
    private static final String BIKE_STANDS_TAG = "bike_stands";
    private static final String OPEN_STANDS_TAG = "available_bike_stands";
    private static final String AVAILABLE_BIKES_TAG = "available_bikes";
    private static final String BIKE_STAND_NAME = "name";


    public List<String> handleResponse(String response)
            throws IOException {
        List<String> result = new ArrayList<String>();

        try {
            //get top level object, this may couse errors as the top level object seems to be an array
            JSONArray responseArray = (JSONArray) new JSONTokener(response).nextValue();
            for(int i=0;i<responseArray.length();i++){
                JSONObject stationObject = responseArray.getJSONObject(i);
                JSONObject stationLocation = stationObject.getJSONObject("position");

                result.add("Bike Stand Name: " + stationObject.get(BIKE_STAND_NAME) + ", Available Bikes: " + stationObject.get(AVAILABLE_BIKES_TAG) + ", Location lat: " + stationLocation.get("lat") + ", Location long: " + stationLocation.get("lng"));
            }
        }catch(JSONException j){
            j.printStackTrace();
        }
//            for(int i=0;i<responseArray.length();i++){
//                JSONObject stationObject = responseArray.getJSONObject(i);
//                JSONObject stationLocation = stationObject.getJSONObject("position");
//
//                result.add("Bike Stand Name: " + stationObject.get(BIKE_STAND_NAME) + ", Available Bikes: " + stationObject.get(AVAILABLE_BIKES_TAG) + ", Location lat: " + stationLocation.get("lat") + ", Location long: " + stationLocation.get("lng"));
//            }


            // Extract value of "earthquakes" key -- a List

            //int bikesAvailable = responseObject.getInt(AVAILABLE_BIKES_TAG);
            //JSONArray earthquakes = responseObject
            //		.getJSONArray(EARTHQUAKE_TAG);

            //JSONObject stand = responseObject
            // Iterate over earthquakes list
            //for (int idx = 0; idx < earthquakes.length(); idx++) {

            // Get single earthquake data - a Map
            //	JSONObject earthquake = (JSONObject) earthquakes.get(idx);

            // Summarize earthquake data as a string and add it to
            // result
            //	result.add("Earthquake " + earthquake.getString("eqid") + " was\n"
            //			+ MAGNITUDE_TAG + " = " + earthquake.get(MAGNITUDE_TAG) + "\n"
            //			+ LATITUDE_TAG + " = "	+ earthquake.getString(LATITUDE_TAG) + "\n"
            //			+ LONGITUDE_TAG + " = " + earthquake.get(LONGITUDE_TAG) + "\n"
            //			+ "Source = " + earthquake.getString("src"));
            //}
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return result;
    }
}


}
