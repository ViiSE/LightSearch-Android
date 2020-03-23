/*
 * Copyright 2016 javiersantos.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.viise.lightsearch.util.appupdater;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.util.appupdater.objects.Update;
import ru.viise.lightsearch.util.appupdater.objects.Version;

public class UtilsLibrary {

    static String getAppInstalledVersion(Context context) {
        String version = "0.0.0.0";

        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    static Integer getAppInstalledVersionCode(Context context) {
        int versionCode = 0;

        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    static Boolean isUpdateAvailable(Update installedVersion, Update latestVersion) {
        if (latestVersion.getLatestVersionCode() != null && latestVersion.getLatestVersionCode() > 0) {
            return latestVersion.getLatestVersionCode() > installedVersion.getLatestVersionCode();
        } else {
            if (!TextUtils.equals(installedVersion.getLatestVersion(), "0.0.0.0") && !TextUtils.equals(latestVersion.getLatestVersion(), "0.0.0.0")) {
                try
                {
                    final Version installed = new Version(installedVersion.getLatestVersion());
                    final Version latest = new Version(latestVersion.getLatestVersion());
                    return installed.compareTo(latest) < 0;
                } catch (Exception e)
                {
                    e.printStackTrace();
                    return false;
                }
            } else return false;
        }
    }

    static Boolean isStringAVersion(String version) {
        return version.matches(".*\\d+.*");
    }

    static Boolean isStringAnUrl(String s) {
        boolean res = false;
        try {
            new URL(s);
            res = true;
        } catch (MalformedURLException ignored) {}

        return res;
    }

    static Update getLatestAppVersion(String url) {
        return new ParserJSON(url).parse();
    }


    private static Intent intentToUpdate(URL url) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
    }

    public static void goToUpdate(Context context, URL url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url.toString()));
        request.setDescription(context.getString(R.string.download_description));
        request.setTitle(context.getString(R.string.download_title));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app-release.apk");
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
//        Intent intent = intentToUpdate(url);
//        context.startActivity(intent);
    }

    static Boolean isNetworkAvailable(Context context) {
        boolean res = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null) {
                res = networkInfo.isConnected();
            }
        }

        return res;
    }

}
