package com.example.visuallyimpairedproject.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visuallyimpairedproject.view.ChoosePhotoActivity;
import com.example.visuallyimpairedproject.R;

import java.util.List;

public class ChoosePhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "MyAttentionAndFansAdapt";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<String> mImagePaths;
    private ChoosePhotoActivity.PhotoButtonOnClickListener mPhotoButtonOnClickListener;
    private ImageView buttonTemp;

    public ChoosePhotoAdapter(Context context, List<String> imagePaths, ChoosePhotoActivity.PhotoButtonOnClickListener photoButtonOnClickListener) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mImagePaths = imagePaths;
        mPhotoButtonOnClickListener = photoButtonOnClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_choose_photo, parent, false);
        return new ChoosePhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String imagePath = mImagePaths.get(position);
        ChoosePhotoViewHolder choosePhotoViewHolder = (ChoosePhotoViewHolder) holder;
        choosePhotoViewHolder.choose_photo_image.setImageURI(Uri.parse(imagePath)); //根据得到的图片地址设置图片的内容
        choosePhotoViewHolder.choose_photo_button_image.setOnClickListener(new View.OnClickListener() { //给右上角的按钮Image设置点击事件
            @Override
            public void onClick(View v) {
                //如果按钮是点击状态就设置为取消点击状态，否则就设置为点击状态
                if (choosePhotoViewHolder.choose_photo_button_image.isSelected()){
                    choosePhotoViewHolder.choose_photo_button_image.setSelected(false);
                    mPhotoButtonOnClickListener.buttonOnClick(-1); //如果用户点击了处于点击状态的按钮Image，说明用户此时未选择任何Image，传-1过去
                }else {
                    //点击了另一个处于未点击状态的按钮Image后，要让该按钮Image处于点击状态，上一个处于点击状态的按钮Image变成未点击状态
                    if (buttonTemp!=null){
                        buttonTemp.setSelected(false);
                    }
                    choosePhotoViewHolder.choose_photo_button_image.setSelected(true);
                    buttonTemp = choosePhotoViewHolder.choose_photo_button_image; //把当前点击了的ButtonImage赋值给暂时的ButtonTemp对象
                    mPhotoButtonOnClickListener.buttonOnClick(holder.getAdapterPosition()); //把处于点击状态的Image的位置传过去
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImagePaths.size();
    }

    class ChoosePhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView choose_photo_image; //RecyclerView中单个显示的图片Image
        private ImageView choose_photo_button_image; //单个显示的图片右上角的按钮Image

        public ChoosePhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            choose_photo_image = itemView.findViewById(R.id.choose_photo_image); //RecyclerView中单个显示的图片Image
            choose_photo_button_image = itemView.findViewById(R.id.choose_photo_button_image); //单个显示的图片右上角的按钮Image
        }
    }
}
