package com.example.visuallyimpairedproject.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.visuallyimpairedproject.presenter.CameraPresenter;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.bean.OcrHistory;
import com.example.visuallyimpairedproject.interfaces.CameraContract;
import com.example.visuallyimpairedproject.utils.PermissionsUtils;
import com.example.visuallyimpairedproject.utils.RealPathFromUriUtils;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CameraActivity extends AppCompatActivity implements CameraContract.ICameraView, View.OnClickListener, TextureView.SurfaceTextureListener, ImageReader.OnImageAvailableListener {

    private static final String TAG = "CameraActivity";
    private CameraContract.ICameraPresenter mICameraPresenter;
    private CameraManager mCameraManager; //摄像头管理者
    @BindView(R.id.camera_history_cardView)
    MaterialCardView camera_history_cardView; //右上角"识别历史"CardView
    @BindView(R.id.return_relative)
    RelativeLayout return_relative; //返回的RelativeLayout
    @BindView(R.id.import_image_relative)
    RelativeLayout import_image_relative; //导入图片的RelativeLayout
    @BindView(R.id.camera_button_relative)
    RelativeLayout camera_button_relative; //点击拍照的按钮RelativeLayout
    @BindView(R.id.voice_recognition)
    RelativeLayout voice_recognition; //"语音识别中"的RelativeLayout
    @BindView(R.id.camera_texture)
    TextureView camera_texture; //拍照的画面：TextureView
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private int mWidth;
    private int mHeight;
    private ImageReader mImageReader;
    private String mCameraId;
    private CameraDevice mCameraDevice; //根据摄像头id得到的摄像头设备，在openCamera方法调用后的回调接口里赋值
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest mPreviewRequest;
    private static final int STATE_PREVIEW = 1; //预览状态的标志
    private static final int STATE_WAITING_PRECAPTURE = 2; //准备拍照的标志
    private int mState = STATE_PREVIEW; //相机的状态
    private final int choosePhotoRequestCode = 3; //跳转选择图片Activity的请求码
    private boolean isOnResumePreview = true; //调用onResume方法时是否恢复预览：若是跳转到识别结果Activity后用户自己返回，就需要；否则不需要
    private SharedPreferences sp;
    private File mFile; //拍照后得到的File
    //文件根目录
    private String basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/";

    //为了使照片竖直显示
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            //拿到当前活动的DecorView
            View decorView = getWindow().getDecorView();//拿到当前活动的DecorView
            //第一个参数表示活动的布局会显示在状态栏上面，第二个参数表示当状态栏的背景为浅色系时，状态栏变为深色系
            //一定要用"|"来写两个值，如果分开用两行代码写，则后面设置的会覆盖前面的，不能实现同时一起的效果
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //注意要清除 FLAG_TRANSLUCENT_STATUS flag
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //setStatusBarColor()方法将状态栏设置为透明色
            getWindow().setStatusBarColor(Color.WHITE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        mICameraPresenter = new CameraPresenter(this); //得到P层对象
        //初始化SharedPreferences
        sp = getSharedPreferences("data", MODE_PRIVATE);
        initOnClick();
        //1.获取摄像头管理者
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        //2、得到TextureView对象，让它准备好。
        camera_texture.setSurfaceTextureListener(this); //实现接口，重写方法，回调onSurfaceTextureAvailable，得到TextureView的宽和高
    }

    //初始化点击事件
    private void initOnClick() {
        camera_button_relative.setOnClickListener(this); //点击拍照的按钮RelativeLayout点击事件
        return_relative.setOnClickListener(this); //返回的RelativeLayout点击事件
        import_image_relative.setOnClickListener(this); //导入图片的RelativeLayout点击事件
        camera_history_cardView.setOnClickListener(this); //右上角"识别历史"CardView的点击事件
    }

    //获取识图结果成功，跳转识别结果Activity，把结果传过去
    @Override
    public void identifySuccess(OcrHistory ocrHistory) {
        Log.d(TAG, "identifySuccess图片识别结果-->" + ocrHistory.getText());
        voice_recognition.setVisibility(View.GONE); //让"图像识别中"View消失
        Intent intent = new Intent(CameraActivity.this, IdentificationResultActivity.class);
        intent.putExtra("IdentificationResult", ocrHistory.getText()); //把识别完的内容传过去
        intent.putExtra("IdentificationId", ocrHistory.getId()); //把这条识别结果的id传过去
        startActivity(intent);
        isOnResumePreview = true; //回来的时候要恢复预览
    }

    //获取识图结果失败
    @Override
    public void identifyFailed(Throwable throwable) {
        Log.d(TAG, "identifyFailed识别图片失败-->" + throwable);
        voice_recognition.setVisibility(View.GONE); //让"图像识别中"View消失
        unlockFocus(); //恢复预览
    }

    //上传图片成功
    @Override
    public void postImageSuccess(String imagePath) {
        Log.d(TAG, "CameraActivity处上传图片成功，图片路径-->" + imagePath);
        mICameraPresenter.getIdentificationResult(imagePath, sp.getString("token", ""));
        //删除拍照得到的图片，若是选择图片，则不用删除
        if (mFile != null && mFile.exists()) {
            mFile.delete();
        }

    }

    //上传图片失败
    @Override
    public void postImageFailed(Throwable throwable) {
        Log.d(TAG, "CameraActivity处上传图片失败-->" + throwable);
        voice_recognition.setVisibility(View.GONE); //让"图像识别中"View消失
        unlockFocus(); //恢复预览
        //删除拍照得到的图片，若是选择图片，则不用删除
        if (mFile != null && mFile.exists()) {
            mFile.delete();
        }
    }

    //点击事件的处理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //8.点击拍照按钮拍照
            case R.id.camera_button_relative:
                lockFocus(); //拍照然后上传图片
                voice_recognition.setVisibility(View.VISIBLE); //让"图像识别中"View出现
                break;
            case R.id.return_relative:
                finish();
                break;
            case R.id.import_image_relative:
                Intent intent = new Intent();
                intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), choosePhotoRequestCode);
//                Intent ChoosePhotoActivityIntent = new Intent(CameraActivity.this,ChoosePhotoActivity.class);
//                startActivityForResult(ChoosePhotoActivityIntent,choosePhotoRequestCode);
                break;
            case R.id.camera_history_cardView:
                //跳转到识别历史Activity
                Intent recognitionHistoryActivityIntent = new Intent(CameraActivity.this, RecognitionHistoryActivity.class);
                startActivity(recognitionHistoryActivityIntent);
                break;
        }
    }

    //3、mTextureView准备好后回调该方法，请求相机和读写权限，把TextureView的宽高设置为全局变量，权限通过后再openCamera。
    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        mWidth = width;
        mHeight = height;
        //读写权限和相机权限
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
//        PermissionsUtils.showSystemSetting = false;//是否支持显示系统设置权限设置窗口跳转
        //这里的this不是上下文，是Activity对象！
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);
    }

    //4、打开相机操作
    private void openCamera(int width, int height) {
        //4.1检查相机服务的访问权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Lacking privileges to access camera service, please request permission first", Toast.LENGTH_SHORT).show();
            return;
        }
        //4.2得到后置摄像头id，String形式的
        getCameraId();

        //assert(mCameraId != null);

        //4.4得到ImageReader对象
        mImageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG,/*maxImages*/7);
        //4.5设置监听者，第二个参数是线程(一旦ImageReader获取到相机设备传过来的图像，就会回调该监听器的onImageAvailable方法)
        mImageReader.setOnImageAvailableListener(this, null);

        try {
//            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
//                throw new RuntimeException("Time out waiting to lock camera opening.");
//            }
            //4.7通过CameraManager的openCamera方法传入得到的后置摄像头id来打开相机，要传入一个回调接口
            mCameraManager.openCamera(mCameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            Log.d(TAG, "openCamera-->" + e);
        }
    }

    //4.3：通过CameraManager得到摄像头列表，筛选出后置摄像头，然后把该摄像头id赋给全局变量
    private void getCameraId() {
        try {
            //Return the list of currently connected camera devices by identifier, including cameras that may be in use by other camera API clients
            for (String cameraId : mCameraManager.getCameraIdList()) {
                //Query the capabilities of a camera device
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //4.6设置ImageReader回调的逻辑：通过acquireNextImage方法在ImageReader的队列中得到下一个Image.
    //应该是在第6点.开始拍照时候将mImageReader.getSurface()作为目标，然后调用了方法拍照完毕时候就会回调这个方法
    @Override
    public void onImageAvailable(ImageReader reader) {
        Log.d(TAG, "onImageAvailable-->回调了");
        Image image = reader.acquireNextImage();
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        FileOutputStream output = null;
        try {
            //拍照完毕调用OCR接口方法来获取识别结果
            if (isOnResumePreview) { //如果为false，说明是选择图片后调用的lockFocus方法，此时只需锁住屏幕，不需要拍照上传
                String date = new SimpleDateFormat("yyMMdd-HHmmss").format(new Date());
                mFile = new File(basePath + date + ".jpg");
                output = new FileOutputStream(mFile);
                output.write(bytes);
                postImage(mFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            image.close();
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //4.8实现回调接口，打开摄像头会回调到这里，这三个方法的参数cameraDevice就是打开的摄像头设备
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice cameraDevice) {
//            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
//            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
//            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }
    };

    //5.创建CameraCaptureSession会话，请求预览,这步结束后，就可以看到预览了，接下来就是点击按钮实现拍照功能了
    private void createCameraPreviewSession() {
        try {
            //得到SurfaceTexture对象
            SurfaceTexture texture = camera_texture.getSurfaceTexture();
            //assert(texture != null);

            // We configure the size of default buffer to be the size of camera preview we want.
            //设置TextureView缓冲区的大小
            texture.setDefaultBufferSize(camera_texture.getWidth(), camera_texture.getHeight());
            // This is the output Surface we need to start preview.
            //获取Surface用来显示预览的数据
            Surface surface = new Surface(texture);
            //调用CameraDevice的createCaptureRequest方法传入以下五个参数之一来创建CaptureRequest.Builder对象，这里传入预览参数。
            /**
             * TEMPLATE_PREVIEW：预览模式
             * TEMPLATE_STILL_CAPTURE：拍照模式
             * TEMPLATE_RECORD：视频录制模式
             * TEMPLATE_VIDEO_SNAPSHOT：视频截图模式
             * TEMPLATE_MANUAL：手动配置参数模式
             */
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewBuilder.addTarget(surface);//将上面得到的surface作为显示预览数据的界面
//            mState = STATE_PREVIEW;
            //创建相机捕获对话，第一个参数是捕获数据的Surface集合
            mCameraDevice.createCaptureSession(
                    Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            //判空
                            if (null == mCameraDevice) {
                                return;
                            }
                            //把对话赋值给全局变量
                            mCaptureSession = cameraCaptureSession;
                            try {
                                //设置参数，暂时不知道什么意思
                                mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                                //创建预览请求
                                mPreviewRequest = mPreviewBuilder.build();
                                //设置反复捕获数据请求，这样预览界面才会一直有数据显示
                                mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                                Log.e(TAG, "set preview builder failed." + e.getMessage());
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                            Toast.makeText(CameraActivity.this, "Camera configuration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //5.1实现回调接口，在这里判断相机的状态，通过我们设置的全局变量。
    //当按下按钮时，状态变为等待拍照STATE_WAITING_PRECAPTURE；当拍完照后，恢复预览状态
    private CameraCaptureSession.CaptureCallback mCaptureCallback =
            new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request,
                                               TotalCaptureResult result) {
//                    Log.d(TAG, "mSessionCaptureCallback-->onCaptureCompleted");
//                    Log.d(TAG, "此时的相机状态-->" + mState);
                    mCaptureSession = session;
                    checkState(result);
                }

                @Override
                public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request,
                                                CaptureResult partialResult) {
                    Log.d(TAG, "mSessionCaptureCallback-->onCaptureProgressed");
                    mCaptureSession = session;
                    checkState(partialResult);
                }

                private void checkState(CaptureResult result) {
                    switch (mState) {
                        case STATE_PREVIEW:
                            // We have nothing to do when the camera preview is working normally.
                            break;
                        case STATE_WAITING_PRECAPTURE:
                            Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                            if (afState == null) {
                                captureStillPicture();
                            } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                                // CONTROL_AE_STATE can be null on some devices
                                Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                                if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                                    //mState = STATE_PICTURE_TAKEN;
                                    captureStillPicture();
                                } else {
                                    //runPrecaptureSequence();//视频拍摄
                                }
                            }
                            break;
                    }
                }

            };

    //6.开始拍照
    private void captureStillPicture() {
        try {
            if (null == mCameraDevice) {
                return;
            }
            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());//拍照时，是将mImageReader.getSurface()作为目标

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            CameraCaptureSession.CaptureCallback CaptureCallback
                    = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    Log.d(TAG, "onCaptureCompleted-->拍照完毕"); //先拍照完毕，再回调ImageReader的接口
                    //Log.d("customCarmeraActivity", mFile.toString());
//                    unlockFocus();//恢复预览

                }
            };

            mCaptureSession.stopRepeating();
            mCaptureSession.abortCaptures();
            mCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //7.恢复预览的操作
    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            mPreviewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
     /*       mCaptureSession.capture(mPreviewBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);*/
            // After this, the camera will go back to the normal state of preview.
            mState = STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(mPreviewBuilder.build(), mCaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //8.设置按钮点击事件，调用lockFocus方法来结束预览，开始拍照，OnClick方法在上面。

    //9.结束预览，拍照
    private void lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            mPreviewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the lock.
            mState = STATE_WAITING_PRECAPTURE;
            mCaptureSession.capture(mPreviewBuilder.build(), mCaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }

    //动态申请权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //创建监听权限的接口对象
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            //权限通过再打开相机
            openCamera(mWidth, mHeight);
//            Toast.makeText(CameraActivity.this, "权限通过，可以做其他事情!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void forbitPermissons() {
//            finish();
            Toast.makeText(CameraActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
        }
    };

    //释放资源的方法
    private void closeCamera() {
        if (null != mCaptureSession) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (null != mImageReader) {
            mImageReader.close();
            mImageReader = null;
        }
    }


    //活动销毁时释放资源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy活动销毁了");
        closeCamera();
    }

    //活动恢复时调用方法进行预览
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume活动恢复了");
        if (isOnResumePreview) {
            if (camera_texture.isAvailable()) {
                openCamera(camera_texture.getWidth(), camera_texture.getHeight());
            } else {
                camera_texture.setSurfaceTextureListener(this);
            }
        }
    }

    //跳转选择图片Activity后回调回来的图片地址
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult方法执行了");
        switch (requestCode) {
            case choosePhotoRequestCode:
                if (resultCode == RESULT_OK && data != null) {
                    //得到返回来的要识别的图片的地址，调用postImage方法来上传服务器图片
                    String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
                    voice_recognition.setVisibility(View.VISIBLE); //让"图像识别中"View出现
                    isOnResumePreview = false; //调用相册选取图片回来后调用onResume方法时不需要恢复预览，等到识别成功或者错误时候才要恢复预览
                    lockFocus();
                    postImage(new File(realPathFromUri));
                }
                break;
        }
    }

    //上传图片，封装成一个方法
    private void postImage(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        mICameraPresenter.postImage(body);
    }

}