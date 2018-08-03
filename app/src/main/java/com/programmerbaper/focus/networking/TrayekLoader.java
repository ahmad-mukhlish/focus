//package com.programmerbaper.focus.networking;
//
//
//import android.content.AsyncTaskLoader;
//import android.content.Context;
//
//import com.programmerbaper.jabarjakartatravel.activities.MainActivity;
//import com.programmerbaper.jabarjakartatravel.entities.Trayek;
//
//import java.util.List;
//
//
//public class TrayekLoader extends AsyncTaskLoader<List<Trayek>> {
//
//    private final String LOG_TAG = TrayekLoader.class.getName();
//
//    private String mUrl;
//
//    public TrayekLoader(Context context, String url) {
//        super(context);
//        this.mUrl = url;
//    }
//
//    @Override
//    protected void onStartLoading() {
//        if (MainActivity.mTrayek == null)
//            forceLoad();
//    }
//
//    @Override
//    public List<Trayek> loadInBackground() {
//        return QueryUtils.fetchTrayek(mUrl);
//    }
//
//}