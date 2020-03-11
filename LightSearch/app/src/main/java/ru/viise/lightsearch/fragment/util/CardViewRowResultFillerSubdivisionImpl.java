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

package ru.viise.lightsearch.fragment.util;

import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.viise.lightsearch.data.Subdivision;

public class CardViewRowResultFillerSubdivisionImpl implements ViewFiller {

    private static final int stepDP = 20;

    private final float scale;
    private final CardView cardView;

    private int cardHeightCurrentDP  = 75;
    private int tvMarginTopCurrentDP = 50;

    public CardViewRowResultFillerSubdivisionImpl(View cardView) {
        this.scale    = cardView.getContext().getResources().getDisplayMetrics().density;
        this.cardView = (CardView) cardView;
    }

    @Override
    public void addView(Object... elements) {
        Subdivision subdivision = (Subdivision) elements[0];
        String amountUnit = (String) elements[1];
        int color = (Integer) elements[2];

        int cardHeightCurrentPx = increaseHeightPx();
        int tvMarginTopCurrentPx = increaseMarginTopPx();

        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, cardHeightCurrentPx);
        cardView.setLayoutParams(cardParams);

        int tvSubdivRSWidthPx  = (int) (250 * scale + 0.5f);
        int tvSubdivRSHeightPx = (int) (20  * scale + 0.5f);
        RelativeLayout.LayoutParams tvSubdivRSLayout = new RelativeLayout.LayoutParams(tvSubdivRSWidthPx, tvSubdivRSHeightPx);
        tvSubdivRSLayout.setMargins(0, tvMarginTopCurrentPx, 0, 0);
        tvSubdivRSLayout.setMarginStart((int) (10 * scale + 0.5f));

        TextView tvSubdivRS = new TextView(cardView.getContext());
        tvSubdivRS.setId(View.generateViewId());
        tvSubdivRS.setLayoutParams(tvSubdivRSLayout);
        tvSubdivRS.setTextSize(15);
        tvSubdivRS.setTextColor(color);
        tvSubdivRS.setText(subdivision.name());

        cardView.addView(tvSubdivRS);

        int tvSubdivRSAmountWidthPx  = (int) (112 * scale + 0.5f);
        int tvSubdivRSAmountHeightPx = (int) (20  * scale + 0.5f);
        RelativeLayout.LayoutParams tvSubdivRSAmountLayout = new RelativeLayout.LayoutParams(tvSubdivRSAmountWidthPx, tvSubdivRSAmountHeightPx);
        tvSubdivRSAmountLayout.setMargins(0, tvMarginTopCurrentPx, 0, 0);
        tvSubdivRSAmountLayout.setMarginStart((int) (230 * scale + 0.5f));

        TextView tvSubdivAmountRS = new TextView(cardView.getContext());
        tvSubdivAmountRS.setId(View.generateViewId());
        tvSubdivAmountRS.setLayoutParams(tvSubdivRSAmountLayout);
        tvSubdivAmountRS.setTextSize(15);
        tvSubdivAmountRS.setGravity(Gravity.RIGHT);
        tvSubdivAmountRS.setTextColor(color);
        tvSubdivAmountRS.setText(String.format("%s %s", subdivision.productAmount(), amountUnit));

        cardView.addView(tvSubdivAmountRS);
    }

    private int increaseHeightPx() {
        cardHeightCurrentDP += stepDP;
        return (int) (cardHeightCurrentDP * scale + 0.5f);
    }

    private int increaseMarginTopPx() {
        tvMarginTopCurrentDP += stepDP;
        return (int) (tvMarginTopCurrentDP * scale + 0.5f);
    }
}
