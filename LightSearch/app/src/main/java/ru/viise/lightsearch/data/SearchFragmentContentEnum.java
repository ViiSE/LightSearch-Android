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

package ru.viise.lightsearch.data;

public enum SearchFragmentContentEnum {
    SKLAD {
        @Override
        public String stringValue() { return "sklad"; }

        @Override
        public String uiValue() { return "Склады"; }
    },
    TK {
        @Override
        public String stringValue() { return "TK"; }

        @Override
        public String uiValue() { return "ТК"; }
    },
    ALL {
        @Override
        public String stringValue() { return "all"; }


        @Override
        public String uiValue() { return "Все"; }
    },
    NULL {
        @Override
        public String stringValue() { return "null"; }

        @Override
        public String uiValue() { return "Пусто"; }
    },
    ALL_UI {
        @Override
        public String stringValue() { return "<Все>"; }

        @Override
        public String uiValue() { return "<Все>"; }
    };

    public abstract String stringValue();
    public abstract String uiValue();
}
