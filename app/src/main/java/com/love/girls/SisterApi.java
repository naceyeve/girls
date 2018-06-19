package com.love.girls;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/16.
 *
 *  网络请求
 */

public class SisterApi {
    private static final String BASE_URL = "http://gank.io/api/data/福利/";
    private static final String TAG = "sever tag:";

    public ArrayList<Sister> fetchSister(int count, int page) {
        ArrayList<Sister> sisters = new ArrayList<>();

        String url = BASE_URL + count + "/" + page;
        try {
            URL url_sister = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url_sister.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            int code = conn.getResponseCode();
            Log.v(TAG, "Server response：" + code);
            if (code == 200) {
                InputStream in = conn.getInputStream();

                byte[] bytes = readFromStream(in);
                String s = new String(bytes, "UTF-8");
                sisters = parseByteArray(s);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sisters;
    }

    private ArrayList<Sister> parseByteArray(String content) {
        ArrayList<Sister> sisters = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(content);
            JSONArray results = object.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject j = (JSONObject) results.get(i);
                Sister sister = new Sister();
                sister.set_id(j.getString("_id"));
                sister.setCreatedAt(j.getString("createdAt"));
                sister.setDesc(j.getString("desc"));
                sister.setPublishedAt(j.getString("publishedAt"));
                sister.setSource(j.getString("source"));
                sister.setType(j.getString("type"));
                sister.setUrl(j.getString("url"));
                sister.setUsed(j.getBoolean("used"));
                sister.setWho(j.getString("who"));
                sisters.add(sister);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sisters;
    }

    public byte[] readFromStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len ;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        return outputStream.toByteArray();
    }
}
