package com.mgjr.view.profile.accountsetting;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * Created by wim on 16/10/24.
 */

public class DownLoadBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        SharedPreferences sPreferences = context.getSharedPreferences("downloadapk", 0);

        long refernece = sPreferences.getLong("plato", 0);

        if (refernece == myDwonloadID) {

            String serviceString = Context.DOWNLOAD_SERVICE;

            DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);

            Intent install = new Intent(Intent.ACTION_VIEW);

            Uri downloadFileUri = dManager.getUriForDownloadedFile(myDwonloadID);
            File auxFile = new File(downloadFileUri.toString());

            install.setDataAndType(Uri.fromFile(auxFile), "application/vnd.android.package-archive");

            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(install);
        }
    }

//    private void openFile(File file) {
//        Log.e("OpenFile", file.getName());
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(android.content.Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file),
//                "application/vnd.android.package-archive");
//        startActivity(intent);
//    }
}
