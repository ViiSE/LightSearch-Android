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
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.CartRecord;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.util.CostFormat;
import ru.viise.lightsearch.util.CostFormatInit;
import ru.viise.lightsearch.util.ProductAmountFormat;
import ru.viise.lightsearch.util.ProductAmountFormatInit;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DefaultViewHolder> {

    private static int defaultColor = -1;

    private final String priceUnit;
    private final List<SoftCheckRecord> data;
    private final TextView twTotalCost;
    private final String total;
    private final Context context;

    private int lastPosition = -1;


    public RecyclerViewAdapter(Context context, List<SoftCheckRecord> data, TextView twTotalCost,
                               String priceUnit) {
        this.context = context;
        this.data = data;
        this.twTotalCost = twTotalCost;
        total = twTotalCost.getText().toString() + " ";
        this.priceUnit = priceUnit;
        getTotalCost();
    }

    private void getTotalCost() {
        float totalCost = 0;
        for(SoftCheckRecord record : data) {
            totalCost += record.totalCost();
        }
        CostFormat tCFormat = CostFormatInit.costFormat();
        totalCost = tCFormat.format(totalCost);
        String res = total + totalCost + " " + priceUnit;
        twTotalCost.setText(res);
    }

    class DefaultViewHolder extends RecyclerView.ViewHolder {

        private final TextView twCardName;
        private final TextView twCardBarcode;
        private final TextView twCardMaxAmount;
        private final TextView twCardPrice;
        private final TextView twCardTotalCost;
        private final EditText etCardCurrentAmount;

        private boolean ignore = false;

        DefaultViewHolder(View itemView) {
            super(itemView);
            twCardName          = itemView.findViewById(R.id.textViewCardNameSC);
            twCardBarcode       = itemView.findViewById(R.id.textViewCardBarcodeSC);
            twCardMaxAmount     = itemView.findViewById(R.id.textViewCardMaxAmountSC);
            twCardPrice         = itemView.findViewById(R.id.textViewCardPriceSC);
            twCardTotalCost     = itemView.findViewById(R.id.textViewCardTotalCostSC);
            etCardCurrentAmount = itemView.findViewById(R.id.editTextCardCurrentAmountSC);

            if(defaultColor == -1)
                defaultColor = twCardName.getCurrentTextColor();

            etCardCurrentAmount.addTextChangedListener(new TextWatcher() {

                String prevString = "";
                boolean delAction = false;
                int caretPos = 0;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable editable) {
                    int pos = getAdapterPosition();
                    if (!ignore) {
                        String etCurAmountStr = etCardCurrentAmount.getText().toString();
                        if(etCurAmountStr.equals("") ||
                            etCurAmountStr.equals(".") ||
                                etCurAmountStr.equals("-")) {
                            etCardCurrentAmount.removeTextChangedListener(this);
                            etCardCurrentAmount.setText("0.25");
                            etCurAmountStr = "0.25";
                            etCardCurrentAmount.addTextChangedListener(this);
                        }

                        ProductAmountFormat prAmountFormat = ProductAmountFormatInit.productAmountFormat();
                        float curAmount = prAmountFormat.format(etCurAmountStr);
                        data.get(pos).setProductsCount(curAmount);
                        ignore = true;
                        twCardTotalCost.setText(data.get(pos).totalCostWithUnit());

                        if(Float.compare(curAmount, data.get(pos).maxAmount()) > 0) {
                            etCardCurrentAmount.removeTextChangedListener(this);
                            etCardCurrentAmount.setText(String.valueOf(data.get(pos).maxAmount()));
                            etCardCurrentAmount.addTextChangedListener(this);
                        }

                        if(!etCardCurrentAmount.getText().toString().matches("^\\d+(?:\\.\\d{0,2})?$")) {
                            etCardCurrentAmount.setText(String.valueOf(data.get(pos).currentAmount()));
                        }
                     } else {
                            if(etCardCurrentAmount.getText().toString().isEmpty()) {
                                etCardCurrentAmount.removeTextChangedListener(this);
                                etCardCurrentAmount.setText(String.valueOf(data.get(pos).currentAmount()));
                                etCardCurrentAmount.addTextChangedListener(this);
                            }
                            try { etCardCurrentAmount.setSelection(caretPos - 1); }
                            catch(IndexOutOfBoundsException ignore) {}
                            prevString = editable.toString();
                        }
                        getTotalCost();
                        ignore = false;
                    }
                });
        }
    }

    @NonNull
    @Override
    public DefaultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row_open_soft_check, parent, false);
        return new DefaultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DefaultViewHolder holder, int position) {
        SoftCheckRecord record = data.get(position);
        holder.twCardName.setText(record.name());
        holder.twCardBarcode.setText(record.barcode());
        holder.twCardMaxAmount.setText(record.maxAmountWithUnit());
        holder.twCardPrice.setText(record.priceWithUnit());
        holder.ignore = true;
        holder.etCardCurrentAmount.setText(String.valueOf(record.currentAmount()));
        holder.ignore = false;
        holder.twCardTotalCost.setText(data.get(position).totalCostWithUnit());

        if(record instanceof CartRecord) {
            CartRecord cartRecord = (CartRecord) record;
            if(!cartRecord.isConfirmed())
                holder.twCardName.setTextColor(ContextCompat.getColor(context, R.color.colorDelete));
            else
                holder.twCardName.setTextColor(defaultColor);
        }

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void removeItem(int position) {
        data.remove(position);
        if(data.size() == 0)
            lastPosition = -1;
        else
            lastPosition = position;
        getTotalCost();
        notifyItemRemoved(position);
    }

    public void restoreItem(SoftCheckRecord record, int position) {
        data.add(position, record);
        lastPosition = position;
        getTotalCost();
        notifyItemInserted(position);
    }

    public void refreshItem(int position, SoftCheckRecord record) {
        data.set(position, record);
        getTotalCost();
        notifyItemChanged(position);
    }

    public void addItem(SoftCheckRecord record) {
        boolean isFound = false;
        for(int pos = 0; pos < data.size(); pos++) {
            SoftCheckRecord rec = data.get(pos);
            if(rec.barcode().equals(record.barcode())) {
                rec.setMaxAmount(rec.maxAmount());
                rec.setProductsCount(rec.currentAmount() + 1);
                isFound = true;
                break;
            }
        }
        if(!isFound) {
            data.add(record);
            //notifyItemInserted(data.size() - 1);
        }

        getTotalCost();
        notifyDataSetChanged();
    }

    public SoftCheckRecord getItem(int position) {
        return data.get(position);
    }

    public List<SoftCheckRecord> getData() {
        return data;
    }
}
