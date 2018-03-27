package com.clipfish.www.clipfish;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    String ClipText;

    private boolean checkPackInfo(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    private Boolean getClipText()
    {
        try
        {
            ClipboardManager myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            ClipData data = myClipboard.getPrimaryClip();
            ClipData.Item item = data.getItemAt(0);
            ClipText = item.getText().toString();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        moveTaskToBack(true);
        Timer timer=new Timer();
        TimerTask task=new TimerTask()
        {
            @Override
            public void run(){
                finish();
                System.exit(0);
            }
        };

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        if (!getClipText())
        {
            Toast.makeText(MainActivity.this, "Clip Error.",Toast.LENGTH_LONG).show();
            timer.schedule(task,2000);
            return;
        }

        PackageManager packageManager = getPackageManager();

        String packageName = "com.taobao.idlefish";
        if (checkPackInfo(packageName)) {
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            intent.setAction("android.intent.action.VIEW");
            Toast.makeText(MainActivity.this, ClipText,Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse(ClipText);
            intent.setData(uri);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, packageName + " is not installed.",Toast.LENGTH_LONG).show();
        }

        timer.schedule(task,2000);


    }
}
