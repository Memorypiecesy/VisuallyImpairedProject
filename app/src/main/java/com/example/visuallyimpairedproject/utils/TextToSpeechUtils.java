package com.example.visuallyimpairedproject.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class TextToSpeechUtils {
    private TextToSpeech textToSpeech;
    private static final String TAG = "TextToSpeechUtils";

    public TextToSpeechUtils(Context context) {
        this.textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.CHINA);
                    textToSpeech.setPitch(2.0f);//方法用来控制音调
                    textToSpeech.setSpeechRate(2.0f);//用来控制语速
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(context,"不支持语音朗读",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Log.d(TAG,"status = "+status);
                }
            }
        });
    }

    public void speech(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    public void stop(){
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    public void destroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;

        }
    }
}
