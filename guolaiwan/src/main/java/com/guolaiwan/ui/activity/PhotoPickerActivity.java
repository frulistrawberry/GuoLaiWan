package com.guolaiwan.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.ThreadPoolUtils;
import com.guolaiwan.bean.ImageFloder;
import com.guolaiwan.bean.SelectPicEvent;
import com.guolaiwan.ui.adapter.PhotoPickerAdapter;
import com.guolaiwan.ui.widget.GridSpaceItemDecoration;
import com.guolaiwan.ui.widget.ListImageDirPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;

public class PhotoPickerActivity extends BaseActivity implements ListImageDirPopupWindow.OnImageDirSelectedListener, PopupWindow.OnDismissListener, View.OnClickListener {
    /*======= 控件声明区 =======*/
    private TextView chooseDirTv;
    private TextView imageCountTv;
    private RelativeLayout chooseDirBtn;
    private RecyclerView recyclerView;
    private ListImageDirPopupWindow popupWindow;
    /*========================*/

    private PhotoPickerAdapter adapter;
    private List<String> data;

    private List<ImageFloder> imageFloders;
    private ImageFloder maxImagesFloder;
    private Runnable getImageRunnable;
    private Handler getImageHandler;
    ThreadPoolUtils threadPool;
    private SelectPicEvent event;

    public static void launcher(Activity context,int requestCode,int selectedCount){
        Intent intent = new Intent();
        intent.setClass(context,PhotoPickerActivity.class);
        intent.putExtra("selectedCount",selectedCount);
        context.startActivityForResult(intent,requestCode);
    }

    public static void launcher(Activity context,SelectPicEvent event){
        Intent intent = new Intent();
        intent.setClass(context,PhotoPickerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("event",event);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("图片").setRightText("完成", v -> {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("result",adapter.getSelectedImage());
            if (!CollectionUtils.isEmpty(adapter.getSelectedImage())&& event!=null){
                String selectImage = adapter.getSelectedImage().get(0);
                event.img = selectImage;
                EventBus.getDefault().post(event);
            }
            setResult(0,intent);
            finish();
        }).showBack().show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_photo_picker);
        chooseDirTv = (TextView) findViewById(R.id.text_photo_picker_choose_dir);
        imageCountTv = (TextView) findViewById(R.id.text_photo_picker_image_count);
        chooseDirBtn = (RelativeLayout) findViewById(R.id.btn_photo_picker_choose_dir);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new GridSpaceItemDecoration(1,3));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
        popupWindow = new ListImageDirPopupWindow(this);
        popupWindow.setOnImageDirSelectedListener(this);
        popupWindow.setOnDismissListener(this);
        chooseDirBtn.setOnClickListener(this);
        threadPool.execute(getImageRunnable);
    }

    @Override
    protected void initData() {
        imageFloders = new ArrayList<>();
        getImageHandler = new GetImageHandler();
        getImageRunnable = new GetImageRunnable();
        threadPool = ThreadPoolUtils.getInstance(ThreadPoolUtils.Type.FixedThread,5);
        adapter = new PhotoPickerAdapter();
        adapter.setSelectedCount(0);
        if (getIntent().getExtras() != null) {
            event = (SelectPicEvent) getIntent().getSerializableExtra("event");
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onSelected(ImageFloder floder) {
        adapter.setNewData(floder.getImags());
        if (popupWindow.isShowing())
            popupWindow.dismiss();
        chooseDirTv.setText(floder.getDirName());
        imageCountTv.setText(floder.getImageCount()+"张");
    }

    @Override
    public void onDismiss() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_photo_picker_choose_dir:
                if (popupWindow != null) {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = .3f;
                    getWindow().setAttributes(lp);
                    popupWindow.showAsDropDown(chooseDirBtn,0,0);
                }
                break;
        }
    }

    public class GetImageRunnable implements Runnable{

        @Override
        public void run() {
            HashSet<String> dirPaths = new HashSet<>();
            String firstImage = null;
            int maxPicSize = 0;
            imageFloders = new ArrayList<>();
            maxImagesFloder = null;
            maxImagesFloder = new ImageFloder();
            maxImagesFloder.setDirName("所有图片");
            maxImagesFloder.setDir("all");
            final List<String> allImages = new ArrayList<>();
            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(imageUri,null,MediaStore.Images.Media.MIME_TYPE+
                    "=? or " + MediaStore.Images.Media.MIME_TYPE+ "=?", new String[]{"image/jpeg", "image/png"},MediaStore.Images.Media.DATE_MODIFIED);
            while (cursor.moveToNext()){
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                if (firstImage == null){
                    firstImage = path;
                }
                File parentFile = new File(path).getParentFile();
                if (parentFile == null)
                    continue;
                final String dirPath = parentFile.getAbsolutePath();
                ImageFloder floder = null;
                if (dirPaths.contains(dirPath)) {
                    continue;
                }
                else{
                    dirPaths.add(dirPath);
                    floder = new ImageFloder();
                    floder.setDir(dirPath);
                    floder.setDirName(parentFile.getName());
                    floder.setFirstImagePath(path);
                }
                final List<String> paths = new ArrayList<>();
                final String[] fileNames = parentFile.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")){
                            allImages.add(dirPath+"/"+filename);
                            paths.add(dirPath+"/"+filename);
                            return true;
                        }
                        return false;
                    }
                });
                if (fileNames != null) {
                    int picSize = fileNames.length;
                    floder.setImageCount(picSize);
                    floder.setImags(paths);
                    imageFloders.add(floder);
                    maxPicSize+=picSize;
                }

            }
            cursor.close();
            dirPaths = null;
            maxImagesFloder.setFirstImagePath(allImages.get(0));
            maxImagesFloder.setImageCount(maxPicSize);
            maxImagesFloder.setImags(allImages);
            imageFloders.add(0,maxImagesFloder);
            getImageHandler.sendEmptyMessage(0x102);

        }
    }

    public  class GetImageHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x101:

                    break;
                case 0x102:
                    adapter.setNewData(maxImagesFloder.getImags());
                    if (popupWindow.isShowing())
                        popupWindow.dismiss();
                    chooseDirTv.setText(maxImagesFloder.getDirName());
                    imageCountTv.setText(maxImagesFloder.getImageCount()+"张");
                    popupWindow.loadDir(imageFloders);
                    break;
                default:
                    break;
            }

        }
    }
}
