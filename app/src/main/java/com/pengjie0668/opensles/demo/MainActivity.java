package com.pengjie0668.opensles.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assetManager = getAssets();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "申请权限", Toast.LENGTH_SHORT).show();
            // 申请 相机 麦克风权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native void rdSound(String path);

    public native void rdStop();

    public void recourdsound(View view) {
        rdSound(getExternalCacheDir() + "/temp.pcm");
    }

    public void recourdstop(View view) {
        rdStop();
    }

    public native void playAudioByOpenSLPcm(String pamPath);


    public void play_pcm(View view) {
        String path = getExternalCacheDir() + "/temp.pcm";
        playAudioByOpenSLPcm(path);
    }


    /**
     * java层提供pcm数据，opensl底层播放
     * <p>
     * 只是演示播放方式，停止回收资源逻辑自行设计
     *
     * @param data
     * @param size
     */
    public native void sendPcmData(byte[] data, int size);

    boolean isstart = false;

    public void play_javapcm(View view) {

        if (!isstart) {
            isstart = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream in = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/111.pcm");
                        byte[] buffer = new byte[44100 * 2 * 2];
                        int n = 0;
                        while ((n = in.read(buffer)) != -1) {
                            sendPcmData(buffer, n);
                            Thread.sleep(800);
                        }
                        isstart = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


    }
}