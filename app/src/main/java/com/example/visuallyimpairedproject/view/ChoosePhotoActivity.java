package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.adapters.ChoosePhotoAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoosePhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ChoosePhotoActivity";
    @BindView(R.id.choose_photo_recycler) RecyclerView choose_photo_recycler;
    @BindView(R.id.confirm_text) TextView confirm_text; //右上角的"确认"TextView
    private ChoosePhotoAdapter mChoosePhotoAdapter;
    private List<String> mImagePaths;
    private int mPosition = -1; //RecyclerView中被选中的item的位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);
        ButterKnife.bind(this); //绑定黄油刀
        initOnClick();
//        //if语句 没有读写SD卡的权限，就申请权限
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        } else {
//            //获取所有图片存入list集合 getGalleryPhotos方法
//            mImagePaths = new ArrayList<>();
//            mImagePaths = getGalleryPhotos(getContentResolver());
//            Log.d("MainActivity", "所有图片地址" + mImagePaths.toString());
//            Log.d(TAG, "图片的数量-->" + mImagePaths.size());
//        }
//        mChoosePhotoAdapter = new ChoosePhotoAdapter(this, mImagePaths, mPhotoButtonOnClickListener); //初始化适配器
//        choose_photo_recycler.setAdapter(mChoosePhotoAdapter); //给RecyclerView设置适配器
//        choose_photo_recycler.setLayoutManager(new GridLayoutManager(this,4)); //给RecyclerView设置布局管理者，是Grid网格布局管理者
    }
    //初始化点击事件
    private void initOnClick() {
        confirm_text.setOnClickListener(this);
    }
    //点击事件的具体逻辑
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm_text:
                //如果位置不为-1，说明用户已经选择了图片
                if (mPosition!=-1){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("imagePath",mImagePaths.get(mPosition));
                    setResult(RESULT_OK,resultIntent);//返回用户选择的图片的Uri地址给CameraActivity
                    finish();
                }else {
                    Toast.makeText(ChoosePhotoActivity.this,"您还未选择任何图片！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //RecyclerView中单个图片右上角按钮点击事件的回调接口
    public interface PhotoButtonOnClickListener {
        void buttonOnClick(int position);
    }

    //上面回调接口的实现类，拿到RecyclerView中对应View的位置
    PhotoButtonOnClickListener mPhotoButtonOnClickListener = new PhotoButtonOnClickListener() {
        @Override
        public void buttonOnClick(int position) {
            mPosition = position;
        }
    };

    //获取所有图片存入list集合返回,MediaStore.Images.Media.DATA中的Images
    public ArrayList<String> getGalleryPhotos(ContentResolver resolver) {
        ArrayList<String> galleryList = new ArrayList<String>();

        try {
            //获取所在相册和相册id
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            //按照id排序
            final String orderBy = MediaStore.Images.Media._ID;

            //相当于sql语句默认升序排序orderBy，如果降序则最后一位参数是是orderBy+" desc "
            Cursor imagecursor =
                    resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                            null, orderBy);

            //从数据库中取出图存入list集合中
            if (imagecursor != null && imagecursor.getCount() > 0) {
                while (imagecursor.moveToNext()) {
                    int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
//                    Log.d("tgw7", "getGalleryPhotos: " + dataColumnIndex);
//                    String path = "file://" + imagecursor.getString(dataColumnIndex);
                    String path = imagecursor.getString(dataColumnIndex);
                    Log.d("tgw5", "getGalleryPhotos: " + path);
                    galleryList.add(path);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 进行反转集合
        Collections.reverse(galleryList);
        return galleryList;
    }
}