package ru.viise.lightsearch.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.OnBackPressedListener;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class DocsFragment extends Fragment implements OnBackPressedListener {

    public static final String TAG = "docsFragment";

    private WebView pdfView;
    private int usableHeightPrevious;
    private View childAt;
    private FrameLayout.LayoutParams frameLayoutParams;
    private ProgressBar pBar;
    private TextView tvFailed;
    private ImageView ivFailed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.toolbar_doc);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc, container, false);
        pBar = view.findViewById(R.id.pBarDocs);
        tvFailed = view.findViewById(R.id.tvFailedDocs);
        ivFailed = view.findViewById(R.id.ivFailedDocs);
        pdfView = view.findViewById(R.id.docPdf);
        pBar.setVisibility(View.VISIBLE);

        FrameLayout frameLayout = view.findViewById(R.id.flDoc);
        childAt = frameLayout.getChildAt(0);
        childAt.getViewTreeObserver().addOnGlobalLayoutListener(() -> possiblyResizeChildOfContent());
        frameLayoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();

        SharedPreferences sPref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);
        String urlDoc = "http://" + prefManager.load(PreferencesManagerType.HOST_UPDATER_MANAGER) + ":"
                + prefManager.load(PreferencesManagerType.PORT_UPDATER_MANAGER)
                + "/man";
        pdfView.getSettings().setJavaScriptEnabled(true);
        pdfView.getSettings().setBuiltInZoomControls(true);
        pdfView.getSettings().setSupportZoom(true);
        pdfView.getSettings().setDisplayZoomControls(false);
        pdfView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pdfView.setVisibility(View.VISIBLE);
                pBar.setVisibility(View.GONE);
                tvFailed.setVisibility(View.GONE);
                ivFailed.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                pBar.setVisibility(View.GONE);
                pdfView.setVisibility(View.GONE);
                tvFailed.setVisibility(View.VISIBLE);
                ivFailed.setVisibility(View.VISIBLE);
            }
        });
        pdfView.loadUrl(urlDoc);

        return view;
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = childAt.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard/4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightNow;
            }
            childAt.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        childAt.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        pdfView.clearCache(true);
        Log.d("DocsFragment:onDestroyView", "Cache is clear");
    }

    @Override
    public void onBackPressed() {
        this.getActivity().getSupportFragmentManager().popBackStack(ContainerFragment.TAG, 0);
        this.getActivity().setTitle(this.getActivity().getString(R.string.fragment_container));
    }
}
