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

package ru.viise.lightsearch.find;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import ru.viise.lightsearch.exception.FindableException;

public class ImplFinderFragmentFromActivityDefaultImpl<T extends Findable> implements ImplFinder<T> {

    private final FragmentActivity activity;

    public ImplFinderFragmentFromActivityDefaultImpl(FragmentActivity activity) {
        this.activity = activity;
    }


    @SuppressWarnings("unchecked")
    @Override
    public T findImpl(Class type) throws FindableException {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        for(Fragment fragment : fragmentManager.getFragments()) {
            if(((Class<T>) type).isInstance(fragment)) {
                try {
                    return ((Class<T>) type).cast(fragment);
                } catch(ClassCastException ex) {
                    throw new FindableException(ex.getMessage());
                }
            }
        }
        throw new FindableException("Cannot find implements for " + type);
    }
}
