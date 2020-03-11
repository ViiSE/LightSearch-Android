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
package ru.viise.lightsearch.cmd;

/**
 *
 * @author ViiSE
 */
public enum ClientCommandEnum {
    CONNECT {
        @Override
        public String stringValue() { return "login"; }
    },
    OPEN_SOFT_CHECK {
        @Override
        public String stringValue() { return "open_soft_check"; }
    },
    CLOSE_SOFT_CHECK {
        @Override
        public String stringValue() { return "close_soft_check"; }
    },
    CANCEL_SOFT_CHECK {
        @Override
        public String stringValue() { return "cancel_soft_check"; }
    },
    CONFIRM_SOFT_CHECK_PRODUCTS {
        @Override
        public String stringValue() { return "confirm_prod_sf"; }
    },
    SEARCH {
        @Override
        public String stringValue() { return "search"; }
    },
    BIND_CHECK {
        @Override
        public String stringValue() { return "bind_check"; }
    },
    BIND {
        @Override
        public String stringValue() { return "bind"; }
    };
    
    public abstract String stringValue();
}
