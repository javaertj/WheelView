package com.ykbjson.app.wheelview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ykbjson.lib.wheelview.WheelView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageLoader(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<WheelItem> wheelItems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            WheelItem wheelItem = new WheelItem();
            wheelItem.setName("我是Wheel" + i);
            wheelItem.setAvatarUrl("drawable://" + R.mipmap.customer_service_head);
            wheelItems.add(wheelItem);
        }
        WheelView wheelView = findViewById(R.id.wheel);
        WheelViewAdapter adapter = new WheelViewAdapter(this, wheelItems);
        wheelView.setAdapter(adapter);
    }


    private void initImageLoader(Context context) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.empty_photo)// 空uri时的默认图片
                .showImageOnFail(R.mipmap.empty_photo)// 加载失败时的默认图片
                .cacheInMemory(true)// 是否缓存到内存
                .cacheOnDisk(true)// 是否缓存到磁盘
                .bitmapConfig(Bitmap.Config.RGB_565)// 图片格式比RGB888少消耗2倍内存
                .imageScaleType(ImageScaleType.EXACTLY)// 图片缩放方式
                .build();

        // 获取到缓存的目录地址
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "ImageLoader/Cache");
        // 创建配置ImageLoader,可以设定在Application，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
//		.memoryCacheExtraOptions(480, 800)//缓存文件的最大长宽，默认屏幕宽高
                // Can slow ImageLoader, use it carefully (Better don't use it)设置缓存的详细信息，最好不要设置这个
                // .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75,null)
                .threadPoolSize(3)// 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)// 线程优先级
                /*
                 *当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                 * When you display an image in a small ImageView and later you
                 * try to display this image (from identical URI) in a larger
                 * ImageView so decoded image of bigger size will be cached in
                 * memory as a previous decoded image of smaller size. So the
                 * default behavior is to allow to cache multiple sizes of one
                 * image in memory. You can deny it by calling this method: so
                 * when some image will be cached in memory then previous cached
                 * size of this image (if it exists) will be removed from memory
                 * cache before.
                 */
                // .denyCacheImageMultipleSizesInMemory()
                // You can pass your own memory cache implementation
                .memoryCache(new UsingFreqLimitedMemoryCache(10 * 1024 * 1024))
//                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(10 * 1024 * 1024)//内存缓存20MB
                .diskCacheSize(50 * 1024 * 1024)// 硬盘缓存50MB
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())// 将保存的时候的URI名称用MD5
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100)// 缓存的File数量
                .diskCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs()
                .build();

        // 全局初始化此配置
        ImageLoader.getInstance().init(config);
    }
}
