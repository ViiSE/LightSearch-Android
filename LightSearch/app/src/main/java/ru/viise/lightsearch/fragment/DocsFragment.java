package ru.viise.lightsearch.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.OnBackPressedListener;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class DocsFragment extends Fragment implements OnBackPressedListener {

    public static final String TAG = "docsFramgent";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc, container, false);

        ProgressBar pBar = view.findViewById(R.id.pBarDocs);
        TextView tvFailed = view.findViewById(R.id.tvFailedDocs);
        ImageView ivFailed = view.findViewById(R.id.ivFailedDocs);
        PDFView pdfView = view.findViewById(R.id.docPdf);

        SharedPreferences sPref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);
        String urlDoc = "http://" + prefManager.load(PreferencesManagerType.HOST_UPDATER_MANAGER) + ":"
                + prefManager.load(PreferencesManagerType.PORT_UPDATER_MANAGER)
                + "/docs/ls_manual.pdf";
        new DocsDownload(
                new WeakReference<>(pBar),
                new WeakReference<>(pdfView),
                new WeakReference<>(tvFailed),
                new WeakReference<>(ivFailed))
                .execute(urlDoc);
        return view;
    }

    @Override
    public void onBackPressed() {
        this.getActivity().getSupportFragmentManager().popBackStack(ContainerFragment.TAG, 0);
        this.getActivity().setTitle(this.getActivity().getString(R.string.fragment_container));
    }

    static class DocsDownload extends AsyncTask<String, Void, byte[]> {

        private final WeakReference<ProgressBar> pBarRef;
        private final WeakReference<PDFView> pdfViewRef;
        private final WeakReference<TextView> tvFailedRef;
        private final WeakReference<ImageView> ivFailedRef;

        DocsDownload(
                WeakReference<ProgressBar> pBarRef,
                WeakReference<PDFView> pdfViewRef,
                WeakReference<TextView> tvFailedRef,
                WeakReference<ImageView> ivFailedRef) {
            this.pBarRef = pBarRef;
            this.pdfViewRef = pdfViewRef;
            this.tvFailedRef = tvFailedRef;
            this.ivFailedRef = ivFailedRef;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBarRef.get().setVisibility(View.VISIBLE);
        }

        @Override
        protected byte[] doInBackground(String... strings) {
            try {
                URLConnection uConn = new URL(strings[0]).openConnection();
                uConn.setConnectTimeout(2000);
                uConn.setReadTimeout(2000);
                try (InputStream is = uConn.getInputStream()) {
                    return ByteStreams.toByteArray(is);
                }
            } catch (IOException e) {
                return new byte[0];
            }
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            pBarRef.get().setVisibility(View.GONE);
            if(bytes.length != 0) {
                pdfViewRef.get().setVisibility(View.VISIBLE);
                pdfViewRef.get()
                        .fromBytes(bytes)
                        .onError(t -> {
                            ivFailedRef.get().setVisibility(View.VISIBLE);
                            tvFailedRef.get().setVisibility(View.VISIBLE);
                            pdfViewRef.get().setVisibility(View.GONE);
                        })
                        .enableAntialiasing(true)
                        .load();
            } else {
                ivFailedRef.get().setVisibility(View.VISIBLE);
                tvFailedRef.get().setVisibility(View.VISIBLE);
            }
        }
    }
}
