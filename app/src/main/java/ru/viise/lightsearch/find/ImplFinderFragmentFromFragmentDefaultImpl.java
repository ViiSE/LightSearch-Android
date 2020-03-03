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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import ru.viise.lightsearch.exception.FindableException;

public class ImplFinderFragmentFromFragmentDefaultImpl<T extends Findable> implements ImplFinder<T>  {

    private final Fragment fragment;

    public ImplFinderFragmentFromFragmentDefaultImpl(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public T findImpl(Class<T> type) throws FindableException {
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        for(Fragment fragment : fragmentManager.getFragments()) {
            if(type.isInstance(fragment)) {
                try {
                    return type.cast(fragment);
                } catch (ClassCastException ex) {
                    throw new FindableException(ex.getMessage());
                }
            }
        }
        throw new FindableException("Cannot find implements for " + type);
    }
}
