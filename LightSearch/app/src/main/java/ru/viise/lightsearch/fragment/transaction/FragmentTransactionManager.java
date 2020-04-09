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

package ru.viise.lightsearch.fragment.transaction;

import java.util.List;

import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.pojo.BindCheckPojo;
import ru.viise.lightsearch.data.pojo.BindCheckPojoResult;
import ru.viise.lightsearch.data.pojo.UnbindCheckPojo;
import ru.viise.lightsearch.data.pojo.UnbindCheckPojoResult;

public interface FragmentTransactionManager {
    void doAuthorizationFragmentTransaction(boolean isNeedAnimation);
    void doContainerFragmentTransaction(String[] skladArray, String[] TKArray, boolean isNeedAnimation);
    void doResultSearchFragmentTransaction(String title, List<SearchRecord> searchRecords);
    void doResultBindFragmentTransaction(String title, CommandResult<BindCheckPojo, BindCheckPojoResult> result);
    void doBindingContainerFragmentTransactionFromResultBind();
    void doResultUnbindFragmentTransaction(String title, CommandResult<UnbindCheckPojo, UnbindCheckPojoResult> result);
}
