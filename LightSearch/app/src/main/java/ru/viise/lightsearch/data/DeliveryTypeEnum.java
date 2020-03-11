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

public enum DeliveryTypeEnum {

    NO {
        @Override
        public String stringUIValue() { return "<Нет>"; }

        @Override
        public String stringCommandValue() { return null; }
    },
    DOSTAVKA_SO_SKLADOV {
        @Override
        public String stringUIValue() { return "Доставка со складов"; }

        @Override
        public String stringCommandValue() { return "0"; }
    },
    SAMOVYVOZ_SO_SKLADOV {
        @Override
        public String stringUIValue() { return "Самовывоз со складов"; }

        @Override
        public String stringCommandValue() { return "1"; }
    },
    SAMOVYVOZ_S_TK {
        @Override
        public String stringUIValue() { return "Самовывоз с ТК"; }

        @Override
        public String stringCommandValue() { return "2"; }
    };

    public abstract String stringUIValue();
    public abstract String stringCommandValue();
}
