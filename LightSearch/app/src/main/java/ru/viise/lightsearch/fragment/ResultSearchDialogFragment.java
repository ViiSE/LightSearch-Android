/*
 * Copyright 2019 ViiSE.
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

package ru.viise.lightsearch.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.OnBackPressedListener;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromActivityDefaultImpl;
import ru.viise.lightsearch.fragment.adapter.ResultSearchSoftCheckArrayAdapter;

public class ResultSearchDialogFragment extends DialogFragment {

    public static final String TAG = "resultSearchDialogFragment";

    private List<SoftCheckRecord> softCheckRecords;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL,
//                android.R.style.);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenFragmentDialog);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // the content
        final CoordinatorLayout root = new CoordinatorLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_result_search, container, false);
        ListView listView = view.findViewById(R.id.fragment_dialog_result_search_list_view);
        ResultSearchSoftCheckArrayAdapter adapter = new ResultSearchSoftCheckArrayAdapter(this.getActivity(),
                R.layout.cardview_row_result_search, softCheckRecords);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, v, position, id) -> {
                getSoftCheckFragment().addSoftCheckRecord(softCheckRecords.get(position));
                dismiss();
        });

        ImageButton btnCancel = view.findViewById(R.id.imageButtonCancel);
        btnCancel.setOnClickListener(v -> {
            dismiss();
        });

        return view;
    }

    public void init(List<SoftCheckRecord> searchRecords) {
        this.softCheckRecords = searchRecords;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null)
            return;

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                getActivity().setTitle(StackFragmentTitle.pop());
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            }
            return false;
        });
    }

    private ISoftCheckContainerFragment getSoftCheckFragment() {
        ImplFinder<ISoftCheckContainerFragment> finder = new ImplFinderFragmentFromActivityDefaultImpl<>(this.getActivity());
        try { return finder.findImpl(ISoftCheckContainerFragment.class); }
        catch(FindableException ignore) { return null; }
    }
}
