package com.johnmelodyme.sindee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @開發人員 : JOHN MELODY MELISSA 鐘智強
 * @版权 : ALL RIGHT RESERVED  ©  JOHN MELODY MELISSA COPYRIGHT || 保留所有权利 © 鐘智強 版权
 * @项目名: SinDee, {Is a Virtual Assistant Name after My GF} || 我根據我的女朋友創建的 AI 安卓 ❤️
 */

public class SINDEE extends AppCompatActivity {
    ImageView 欣蒂按键;
    TextToSpeech 文字转语音;
    SpeechRecognizer 语音识别;
    Date 日期時間;
    Calendar 日曆；

    public void onStart(){
        super.onStart();
        INIT();
    }

    // onStart Declaration:
    private void INIT() {
        欣蒂按键 = findViewById(R.id.initial);
        日期時間 = Calendar.getInstance().getTime();
        日曆 = Calendar.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        文字轉語音開始();
        語音識別開始();
    }

    // 語音識別開始功能:
    private void 語音識別開始() {
        if (SpeechRecognizer.isRecognitionAvailable(SINDEE.this)){
            语音识别 = SpeechRecognizer.createSpeechRecognizer(SINDEE.this);
            语音识别.setRecognitionListener(new RecognitionListener() {
                @Override public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                