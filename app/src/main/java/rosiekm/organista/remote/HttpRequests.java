package rosiekm.organista.remote;

import android.util.JsonReader;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rosiekm.organista.remote.FilesPackage.File;
import rosiekm.organista.remote.FilesPackage.ImageFile;
import rosiekm.organista.remote.FilesPackage.MyAudioFile;
import rosiekm.organista.remote.FilesPackage.VideoFile;

public class HttpRequests {
    public static HashMap<String, Object> GetStatus(String url) throws IOException {
        HashMap<String, Object> HashResponse = new HashMap<>();
        try {
            String urll = "http:/"+  url + ":9000/start?device=status";
            Log.d("url", urll);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urll)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            String responseString = new String(response.body().bytes(), StandardCharsets.UTF_8);
            Log.d("getStatus", responseString);
            JSONObject json = new JSONObject(responseString);
            if (json.has("audioPlaying")){
                HashResponse.put("audioPlaying", json.getBoolean("audioPlaying"));
            }
            if (json.has("videoPlaying")){
                HashResponse.put("videoPlaying", json.getBoolean("videoPlaying"));
            }
            if (json.has("imagePlaying")){
                HashResponse.put("imagePlaying", json.getBoolean("imagePlaying"));
            }
            if (json.has("nowPlaying")){
                HashResponse.put("nowPlaying", json.getString("nowPlaying"));
            }

            if (json.has("refreshingFiles")){
                HashResponse.put("refreshingFiles", json.getBoolean("refreshingFiles"));
                if (!json.getBoolean("refreshingFiles")){
                    ArrayList<String> usb = new ArrayList<>();
                    JSONArray array = json.getJSONArray("usb");
                    for (int x = 0; x < array.length(); x++){
                        usb.add(array.getString(x));
                    }
                    HashResponse.put("usb", usb);
                }
            }
            if (json.has("AudioVolume")){
                HashResponse.put("AudioVolume", json.getInt("AudioVolume"));
            }if (json.has("AudioBalance")){
                HashResponse.put("balanse", json.getInt("AudioBalance"));
            }if (json.has("stopTime")){
                HashResponse.put("stopTime", json.getBoolean("stopTime"));
            }

            return HashResponse;
        }
        catch (JSONException e){
            Log.d("error", e.toString());
            return  HashResponse;
        }
    }

    public static HashMap<String, Object> GetAudioFiles(String url) throws IOException {
        HashMap<String, Object> HashResponse = new HashMap<>();
        try {
            String urll = "http:/"+  url + ":9000/start?audio=get_files";
            Log.d("url", urll);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urll)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            String audioFilesInJson =  new String(response.body().bytes(), StandardCharsets.UTF_8);
            Log.d("audiofiles", audioFilesInJson);

            JSONArray array = new JSONArray(audioFilesInJson);

            ArrayList<AudioFileClass> audioFiles = new ArrayList<>();

            for (int x = 0; x < array.length(); x++){
                JSONObject file = array.getJSONObject(x);
                audioFiles.add(new AudioFileClassBuilder()
                        .setTitle(file.getString("title"))
                        .setAlbum(file.getString("album"))
                        .setLength(file.getInt("length"))
                        //.setLength(file.getInt("length"))
                        .setPath(file.getString("path"))
                        //.setNumber(file.getInt("number"))
                        .createAudioFileClass());
            }

            if (!audioFiles.isEmpty()) {
                HashResponse.put("audioFiles", audioFiles);
            }
            else{
                HashResponse.put("data", false);
            }

            return HashResponse;
        }
        catch (Exception e){
            HashResponse.put("error", true);
            Log.d("error", e.toString());
            return  HashResponse;
        }
    }

    public static HashMap<String, Object> GetUSBData(String url, String dir) throws IOException {
        HashMap<String, Object> HashResponse = new HashMap<>();
        ArrayList<File> data2 = new ArrayList<>();
        try {
            String urll = "http:/"+  url + ":9000/getUSBFiles?usb=get_files&path=" + dir;
            Log.d("url", urll);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urll)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            String data =  new String(response.body().bytes(), StandardCharsets.UTF_8);
            Log.d("audiofiles", data);

            JSONObject jsonObject = new JSONObject(data);



            JSONArray audiotmp = jsonObject.getJSONArray("audioFiles");
            if (audiotmp.length() > 0) {
                for (int x = 0; x < audiotmp.length(); x++) {
                    JSONObject tmp = audiotmp.getJSONObject(x);
                    //audioFileArrayList.add(new MyAudioFile(tmp.getString("title"), tmp.getString("path"),tmp.getString("album"),tmp.getInt("length")));
                    data2.add(new MyAudioFile(tmp.getString("title"), tmp.getString("path"), tmp.getString("album"), tmp.getInt("length")));
                }

                HashResponse.put("audioData", true);
            }
            else{
                HashResponse.put("audioData", false);
            }

            JSONArray videotmp = jsonObject.getJSONArray("videoFiles");
            if (videotmp.length() > 0){
                for (int x =0 ; x < videotmp.length(); x++) {
                    JSONObject tmp = videotmp.getJSONObject(x);
                    //audioFileArrayList.add(new VideoFile(tmp.getString("title"), tmp.getString("path"),tmp.getString("album"),tmp.getInt("length")));
                    data2.add(new VideoFile(tmp.getString("title"), tmp.getString("path"),tmp.getString("title"),tmp.getInt("length")));
                }
                HashResponse.put("videoData", true);
            }
            else{
                HashResponse.put("videoData", false);
            }

            JSONArray imagetmp = jsonObject.getJSONArray("imageFiles");
            if (imagetmp.length() > 0) {
                for (int x = 0; x < imagetmp.length(); x++) {
                    JSONObject tmp = imagetmp.getJSONObject(x);
                    //audioFileArrayList.add(new ImageFile(tmp.getString("title"), tmp.getString("path"),tmp.getString("album")));
                    data2.add(new ImageFile(tmp.getString("title"), tmp.getString("path"), tmp.getString("album")));
                }
                HashResponse.put("imageData", true);
            }
            else{
                HashResponse.put("imageData", false);
            }


            HashResponse.put("data", true);
            HashResponse.put("USBFiles", data2);

            return HashResponse;
        }
        catch (Exception e){
            HashResponse.put("error", true);
            Log.d("error", e.toString());
            return  HashResponse;
        }
    }

    public static int PlayAudio(String url, String path) throws IOException {
        try {
            String urll = "http:/"+  url + ":9000/start?audio=play&file="+path;
            Log.d("url", urll);
            Log.d("path", path);
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
            JSONObject object = new JSONObject();
            object.put("path", path);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), object.toString());
            Request request = new Request.Builder()
                    .url(urll)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response response = client.newCall(request).execute();
            Log.d("response status", response.body().string());

            return response.code();
        }
        catch (Exception e){
            Log.d("error", e.toString());
            return  -1;
        }
    }

    public static int PlayVideo(String url, String path) throws IOException {
        try {
            String urll = "http:/"+  url + ":9000/start?video=play&file="+path;
            Log.d("url", urll);
            Log.d("path", path);
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
            JSONObject object = new JSONObject();
            object.put("url", path);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), object.toString());
            Request request = new Request.Builder()
                    .url(urll)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response response = client.newCall(request).execute();
            Log.d("response status", response.body().string());

            return response.code();
        }
        catch (Exception e){
            Log.d("error", e.toString());
            return  -1;
        }
    }

    public static int ShowImage(String url, String path) throws IOException {
        try {
            String urll = "http:/"+  url + ":9000/showImage";
            Log.d("url", urll);
            Log.d("path", path);
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
            JSONObject object = new JSONObject();
            object.put("url", path);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), object.toString());
            Request request = new Request.Builder()
                    .url(urll)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response response = client.newCall(request).execute();
            Log.d("response status", response.body().string());

            return response.code();
        }
        catch (Exception e){
            Log.d("error", e.toString());
            return  -1;
        }
    }

    public static int SetBalanse(String url, int balanse) throws IOException {
        try {
            String urll = "http:/"+  url + ":9000/organista?audio=balance&value=" + balanse;
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), String.valueOf(balanse));
            Request request = new Request.Builder()
                    .url(urll)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response response = client.newCall(request).execute();
            Log.d("response status", response.body().string());

            return response.code();
        }
        catch (Exception e){
            Log.d("error", e.toString());
            return  -1;
        }
    }
    public static int SetVolume(String url, int volume) throws IOException {
        try {
            String urll = "http:/"+  url + ":9000/organista?audio=set_volume&value=" + volume;
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), "");
            Request request = new Request.Builder()
                    .url(urll)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response response = client.newCall(request).execute();
            Log.d("response status", response.body().string());

            return response.code();
        }
        catch (Exception e){
            Log.d("error", e.toString());
            return  -1;
        }
    }


    public static int StopAudio(String url) throws IOException {
        try {
            String urll = "http:/"+  url + ":9000/start?audio=stop";
            Log.d("url", urll);
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
            JSONObject object = new JSONObject();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), object.toString());
            Request request = new Request.Builder()
                    .url(urll)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .post(body)

                    .build();
            Response response = client.newCall(request).execute();
            Log.d("response status", response.body().string());

            return response.code();
        }
        catch (Exception e){
            Log.d("error", e.toString());
            return  -1;
        }
    }

    public static int StopTimeAudio(String url) throws IOException {
        try {
            String urll = "http:/"+  url + ":9000/start?audio=stop_time";
            Log.d("url", urll);
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
            JSONObject object = new JSONObject();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), object.toString());
            Request request = new Request.Builder()
                    .url(urll)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            Log.d("response status", response.body().string());

            return response.code();
        }
        catch (Exception e){
            Log.d("error", e.toString());
            return  -1;
        }
    }

    public static int StopTimeAudioCancel(String url) throws IOException {
        try {
            String urll = "http:/"+  url + ":9000/start?audio=stop_time_cancek";
            Log.d("url", urll);
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
            JSONObject object = new JSONObject();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), object.toString());
            Request request = new Request.Builder()
                    .url(urll)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response response = client.newCall(request).execute();
            Log.d("response status", response.body().string());

            return response.code();
        }
        catch (Exception e){
            Log.d("error", e.toString());
            return  -1;
        }
    }

    public static int StopVideo(String url) throws IOException {
        try {
            String urll = "http:/"+  url + ":9000/start?video=stop";
            Log.d("url", urll);
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
            JSONObject object = new JSONObject();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), object.toString());
            Request request = new Request.Builder()
                    .url(urll)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            Log.d("response status", response.body().string());

            return response.code();
        }
        catch (Exception e){
            Log.d("error", e.toString());
            return  -1;
        }
    }
    public static int PauseResumVideo(String url) throws IOException {
        try {
            String urll = "http:/"+  url + ":9000/pauseVideo";
            Log.d("url", urll);
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
            JSONObject object = new JSONObject();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), object.toString());
            Request request = new Request.Builder()
                    .url(urll)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response response = client.newCall(request).execute();
            Log.d("response status", response.body().string());

            return response.code();
        }
        catch (Exception e){
            Log.d("error", e.toString());
            return  -1;
        }
    }
    public static int HideImage(String url) throws IOException {
        try {
            String urll = "http:/"+  url + ":9000/hide";
            Log.d("url", urll);
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
            JSONObject object = new JSONObject();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), object.toString());
            Request request = new Request.Builder()
                    .url(urll)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response response = client.newCall(request).execute();
            Log.d("response status", response.body().string());

            return response.code();
        }
        catch (Exception e){
            Log.d("error", e.toString());
            return  -1;
        }
    }

}
