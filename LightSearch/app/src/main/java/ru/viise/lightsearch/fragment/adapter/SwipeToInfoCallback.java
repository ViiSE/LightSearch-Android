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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import ru.viise.lightsearch.R;

abstract public class SwipeToInfoCallback extends ItemTouchHelper.Callback {

    private final Paint clearPaint;
    private final ColorDrawable background;
    private final int backgroundColor;
    private final Drawable changeDrawable;

    private final int intrinsicWidth;
    private final int intrinsicHeight;


    public SwipeToInfoCallback(Context context) {
        background = new ColorDrawable();
        backgroundColor = ContextCompat.getColor(context, R.color.colorChange);
        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        changeDrawable = ContextCompat.getDrawable(context, R.drawable.ic_info);

        intrinsicWidth = changeDrawable.getIntrinsicWidth();
        intrinsicHeight = changeDrawable.getIntrinsicHeight();
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX / 5, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX / 5, dY, actionState, false);
            return;
        }

        background.setColor(backgroundColor);
        background.setBounds(itemView.getLeft() + (int)dX + 10, itemView.getTop(), itemView.getLeft(), itemView.getBottom());
        background.draw(c);

        int changeIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int changeIconMargin = (itemHeight - intrinsicHeight) / 2;
        int changeIconLeft = itemView.getLeft() + changeIconMargin;
        int changeIconRight = itemView.getLeft() + changeIconMargin + intrinsicWidth;
        int changeIconBottom = changeIconTop + intrinsicHeight;

        changeDrawable.setBounds(changeIconLeft, changeIconTop, changeIconRight, changeIconBottom);
        changeDrawable.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX/5, dY, actionState, isCurrentlyActive);
    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, clearPaint);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.3f;
    }
}
