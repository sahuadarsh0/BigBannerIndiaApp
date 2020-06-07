package technited.minds.asavideoslider;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheKeyFactory;
import com.google.android.exoplayer2.upstream.cache.CacheUtil;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyService extends Service {

    Context context;
    ArrayList<String> list;
    MyCache[] myCache;
    private int counter;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        counter = 0;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startCustomForeground();
        else
            startForeground(1, new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            list = new ArrayList<>();
            list = intent.getStringArrayListExtra("url");
            assert list != null;
            if (list.size() != 0) {
                myCache = new MyCache[list.size()];
                int i = 0;
                for (String url : list) {
                    myCache[i] = new MyCache();
                    if (myCache[i] != null) {
                        myCache[i].preCacheVideos(url);
                        i++;
                        if (i == list.size()) {
                            stopForeground(true);
                            stopSelf();
                        }
                    }
                }

            }
        }
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseService();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startCustomForeground() {
        String NOTIFICATION_CHANNEL_ID = "Try";
        String channelName = "BackgroundService";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    private void releaseService() {
        if (list.size() != 0) {
            int i = 0;
            for (String url : list) {
                if (myCache[i] != null && myCache[i].downloadPercentage == 100.0) {

                    try {
                        myCache[i].cache.release();
                        myCache[i].dataSource.close();
                        myCache[i].exoDatabaseProvider.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }

        }
    }


    private int counter() {
        return counter++;
    }

    class MyCache {
        ExoDatabaseProvider exoDatabaseProvider;
        File cacheFolder;
        LeastRecentlyUsedCacheEvictor cacheEvictor;
        SimpleCache cache;
        CacheDataSourceFactory cacheDataSourceFactory;
        DataSpec dataSpec;
        CacheKeyFactory defaultCacheKeyFactory;
        CacheUtil.ProgressListener progressListener;
        DefaultDataSource dataSource;
        Uri mediaUri;
        double downloadPercentage;

        MyCache() {
            cacheEvictor = new LeastRecentlyUsedCacheEvictor(90 * 1024 * 1024);
        }

        private void preCacheVideos(String url) {
            mediaUri = Uri.parse(url);
            exoDatabaseProvider = new ExoDatabaseProvider(context);
            cacheFolder = new File(context.getFilesDir(), "video_" + counter());
            if (cache == null)
                cache = new SimpleCache(cacheFolder, cacheEvictor, exoDatabaseProvider);
            cacheDataSourceFactory = new CacheDataSourceFactory(cache, new DefaultHttpDataSourceFactory(context.getString(R.string.lib_name)), CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);


            dataSource =
                    new DefaultDataSourceFactory(
                            context,
                            Util.getUserAgent(context, context.getString(R.string.lib_name))).createDataSource();

            defaultCacheKeyFactory = CacheUtil.DEFAULT_CACHE_KEY_FACTORY;
            progressListener = new CacheUtil.ProgressListener() {
                @Override
                public void onProgress(long requestLength, long bytesCached, long newBytesCached) {
                    downloadPercentage = (bytesCached * 100.0
                            / requestLength);

                    Log.d("asa-pre-Download", "onProgress: " + downloadPercentage + " " + cacheFolder.getPath());

                }

            };

            dataSpec = new DataSpec(mediaUri);
            cacheVideo(dataSpec, defaultCacheKeyFactory, dataSource, progressListener);

        }

        private void cacheVideo(DataSpec dataSpec, CacheKeyFactory defaultCacheKeyFactory, DataSource dataSource, CacheUtil.ProgressListener progressListener) {

            try {
                CacheUtil.cache(
                        dataSpec,
                        cache,
                        defaultCacheKeyFactory,
                        dataSource,
                        progressListener,
                        null
                );
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Log.e("asa-cache-error", "cacheVideo: " + e.toString());
            }
        }


    }

}