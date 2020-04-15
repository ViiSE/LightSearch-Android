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

package ru.viise.lightsearch.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.dialog.alert.AboutAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.ExitToAuthorizationAlertDialogCreatorImpl;

public class RecyclerViewAdapterMore extends RecyclerView.Adapter<RecyclerViewAdapterMore.DefaultViewHolder> {

    private final List<String> data;
    private final Context context;
    private final FragmentActivity activity;

    public RecyclerViewAdapterMore(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
        this.data = new ArrayList<>();
        data.add("О приложении");
        data.add("Выйти");
    }

    class DefaultViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvMore;
        DefaultViewHolder(View itemView) {
            super(itemView);
            tvMore = itemView.findViewById(R.id.textViewMore);
        }
    }

    @NonNull
    @Override
    public DefaultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row_more, parent, false);
        return new DefaultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DefaultViewHolder holder, int position) {
        holder.tvMore.setText(data.get(position));
        if(position == 0) {
            holder.itemView.setOnClickListener(view ->
                    new AboutAlertDialogCreatorImpl(activity).create().show());
        } else if(position == 1) {
            holder.tvMore.setTextColor(context.getColor(R.color.colorDelete));
            holder.itemView.setOnClickListener(view ->
                    new ExitToAuthorizationAlertDialogCreatorImpl(activity).create().show());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
