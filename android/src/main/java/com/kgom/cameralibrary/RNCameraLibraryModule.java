
package com.kgom.cameralibrary;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;

public class RNCameraLibraryModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNCameraLibraryModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNCameraLibrary";
  }

  @ReactMethod
  public void getPhotos(ReadableMap props, Callback callback) {


    WritableArray collection = Arguments.createArray();
    boolean nextPage = true;

    int countObjects;
    int perPage = 20;
    if(props.hasKey("perPage")){
      perPage = props.getInt("perPage");
    }

    int thumbnailWidth = 80;
    if(props.hasKey("thumbnailWidth")) {
        thumbnailWidth = props.getInt("thumbnailWidth");
    }

    int thumbnailHeight = 80;
    if(props.hasKey("thumbnailHeight")) {
        thumbnailHeight = props.getInt("thumbnailHeight");
    }

    int page=0;
    if(props.hasKey("page")) {
        page = props.getInt("page");
    }
    int lastPage = 0;
    if(props.hasKey("lastPage")) {
        page = props.getInt("lastPage");
    }

    Bitmap[] thumbnails;
    boolean[] thumbnailsselection;
    String[] arrPath;
    int[] typeMedia;
  	//Request Authorization to get photos from library
    String[] columns = { MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.TITLE,
    };
    String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
    final String orderBy = MediaStore.Files.FileColumns.DATE_ADDED;
    Uri queryUri = MediaStore.Files.getContentUri("external");

    Cursor imagecursor =this.getCurrentActivity().getContentResolver().query(queryUri, columns, selection, null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");

    int image_column_index = imagecursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
    countObjects = imagecursor.getCount();

    int from = countObjects -((page-1)*perPage)-1;
    int to = countObjects - (page * perPage);
    if(to <0) to = 0;
    if(to == 0) nextPage = false;

    for(int i= from; i>= to;  i--){
        imagecursor.moveToPosition(i);
        int id = imagecursor.getInt(image_column_index);
        int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 4;
        bmOptions.inPurgeable = true;

        WritableMap map = Arguments.createMap();
        int type = imagecursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);
        int t = imagecursor.getInt(type);
        String url = imagecursor.getString(dataColumnIndex);
        boolean fetched = false;
        if(t == 1) {

          Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(
                  getCurrentActivity().getContentResolver(), id,
                  MediaStore.Images.Thumbnails.MINI_KIND, bmOptions);
          if(thumbnail != null) {
              ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
              thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
              byte[] byteArray = byteArrayOutputStream .toByteArray();
              String base = Base64.encodeToString(byteArray, Base64.DEFAULT);

              map.putString("url", url);
              map.putString("thumbnail", base);
              map.putInt("width", thumbnail.getWidth());
              map.putInt("height", thumbnail.getHeight());
              map.putString("type", "photo");
              fetched =true;
          }


        }else if(t == 3) {
          Bitmap thumbnail = MediaStore.Video.Thumbnails.getThumbnail(
                  getCurrentActivity().getContentResolver(), id,
                  MediaStore.Video.Thumbnails.MINI_KIND, bmOptions);
          if(thumbnail != null) {
              ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
              thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
              byte[] byteArray = byteArrayOutputStream .toByteArray();
              String base = Base64.encodeToString(byteArray, Base64.DEFAULT);
              MediaPlayer mp = MediaPlayer.create(getCurrentActivity(), Uri.parse(url));
              int duration = mp.getDuration() / 1000;
              map.putString("url", url);
              map.putString("thumbnail", base);
              map.putInt("duration", duration);
              map.putInt("width", thumbnail.getWidth());
              map.putInt("height", thumbnail.getHeight());
              map.putString("type", "video");
              fetched = true;
          }

        }
        if(fetched) {
          collection.pushMap(map);
        }

    }
    WritableMap result = Arguments.createMap();
    result.putArray("objects", collection);
    result.putBoolean("next_page", nextPage);
    result.putInt("current_page", page);
    result.putInt("last_page", lastPage);
    callback.invoke(result);

  }
}