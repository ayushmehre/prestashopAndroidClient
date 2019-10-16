package com.develeno.prestashopandroidclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkConnection {
    public static final String KEY = "YN8VTLLCXD98QQLZWQKMF6IA8S2PTT4K";// "VN3JZJW4M376339ZMRY4K7IH5MJHQJLH";

    static void getDataInBackground(final String uri, final OnGotResponse onGotResponse) {
        new AsyncTask<String, String, String>() {
            /* access modifiers changed from: protected */
            public String doInBackground(String... strings) {
                return NetworkConnection.getData(uri);
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(String s) {
                onGotResponse.getResponse(s);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    private static String getDataWithoutAuthentication(String uri) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(((HttpURLConnection) new URL(uri).openConnection()).getInputStream()));
            String str = "";
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    return sb.toString();
                }
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: private */
    public static String getData(String uri) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
            connection.setRequestProperty(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encodeToString("YN8VTLLCXD98QQLZWQKMF6IA8S2PTT4K:".getBytes(), 0));
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String str = "";
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    return sb.toString();
                }
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void fetchImageInBackground(final String link, final OnBackgroundTaskCompleted onBackgroundTaskCompleted, boolean serialWise) {
        AsyncTask<String, String, Bitmap> task = new AsyncTask<String, String, Bitmap>() {
            /* access modifiers changed from: protected */
            public Bitmap doInBackground(String... strings) {
                if (Data.isAvailLocally(link)) {
                    return BitmapFactory.decodeFile(Data.getFileIfAvailLocally(link));
                }
                StringBuilder loginBuilder = new StringBuilder().append("Basic ").append(Base64.encodeToString("YN8VTLLCXD98QQLZWQKMF6IA8S2PTT4K:".getBytes(), 0));
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
                    connection.setUseCaches(true);
                    connection.setRequestProperty(HttpHeaders.AUTHORIZATION, loginBuilder.toString());
                    Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                    Data.saveLocally(bitmap, link);
                    return bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Bitmap bitmap) {
                onBackgroundTaskCompleted.getResult(bitmap);
            }
        };
        if (serialWise) {
            task.execute(new String[0]);
        } else {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        }
    }

    public static void postDataInBackground(final String uri, final String parameters, final OnBackgroundTaskCompleted taskCompleted) {
        new AsyncTask<Integer, Integer, Response>() {
            /* access modifiers changed from: protected */
            public Response doInBackground(Integer... integers) {
                try {
                    return NetworkConnection.postData(uri, parameters);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Response integer) {
                taskCompleted.getResult(integer);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Integer[0]);
    }

    public static Response postData(String uri, String parameters) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
        connection.setRequestProperty(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encodeToString("YN8VTLLCXD98QQLZWQKMF6IA8S2PTT4K:".getBytes(), 0));
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(parameters);
        wr.flush();
        wr.close();
        int responseCode = connection.getResponseCode();
        Log.d("RESPONSE_CODE", "Response Code:" + responseCode + "");
        InputStream errorStream = connection.getErrorStream();
        switch (responseCode) {
            case 201:
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String str = "";
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        return new Response(responseCode, sb.toString());
                    }
                    sb.append(line + "\n");
                }
            case HttpStatus.SC_BAD_REQUEST /*400*/:
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(errorStream));
                String str2 = "";
                StringBuilder sb2 = new StringBuilder();
                while (true) {
                    String line2 = reader2.readLine();
                    if (line2 != null) {
                        sb2.append(line2 + "\n");
                    } else {
                        Log.d("RESPONSE_CODE", "Response:" + sb2.toString() + "");
                        return new Response(responseCode, sb2.toString());
                    }
                }
            default:
                BufferedReader reader3 = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String str3 = "";
                StringBuilder sb3 = new StringBuilder();
                while (true) {
                    String line3 = reader3.readLine();
                    if (line3 != null) {
                        sb3.append(line3 + "\n");
                    } else {
                        Log.d("RESPONSE_CODE", "Response:" + sb3.toString() + "");
                        return new Response(responseCode, "UNKNOWN RESPONSE CODE: \n" + sb3.toString());
                    }
                }
        }
    }
}
