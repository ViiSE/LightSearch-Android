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

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import ru.viise.lightsearch.util.appupdater.enums.AppUpdaterError;
import ru.viise.lightsearch.util.appupdater.objects.Update;

class UtilsAsync {

    static class LatestAppVersion extends AsyncTask<Void, Void, Update> {
        private WeakReference<Context> contextRef;
        private String jsonUrl;
        private AppUpdater.LibraryListener listener;

        public LatestAppVersion(Context context, String jsonUrl, AppUpdater.LibraryListener listener) {
            this.contextRef = new WeakReference<>(context);
            this.jsonUrl = jsonUrl;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Context context = contextRef.get();
            if (context == null || listener == null) {
                cancel(true);
            } else if (UtilsLibrary.isNetworkAvailable(context)) {
                if (jsonUrl == null || !UtilsLibrary.isStringAnUrl(jsonUrl)) {
                    listener.onFailed(AppUpdaterError.JSON_URL_MALFORMED);

                    cancel(true);
                }
            } else {
                listener.onFailed(AppUpdaterError.NETWORK_NOT_AVAILABLE);
                cancel(true);
            }
        }

        @Override
        protected Update doInBackground(Void... voids) {
            try {
                Update update = UtilsLibrary.getLatestAppVersion(jsonUrl);
                if (update != null) {
                    return update;
                } else {
                    AppUpdaterError error = AppUpdaterError.JSON_ERROR;

                    if (listener != null)
                        listener.onFailed(error);

                    cancel(true);
                    return null;
                }
            } catch (Exception ex) {
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Update update) {
            super.onPostExecute(update);

            if (listener != null) {
                if (UtilsLibrary.isStringAVersion(update.getLatestVersion())) {
                    listener.onSuccess(update);
                } else {
                    listener.onFailed(AppUpdaterError.UPDATE_VARIES_BY_DEVICE);
                }
            }
        }
    }

}
