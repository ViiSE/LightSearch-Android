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

public enum AuthorizationPreferenceEnum {

    FIRST_TIME {
        @Override
        public String stringValue() { return "firstTime"; }
    },
    USERNAME {
        @Override
        public String stringValue() { return "username"; }
    },
    SUPERUSER {
        @Override
        public String stringValue() { return "superuser"; }
    },
    HOST {
        @Override
        public String stringValue() { return "host"; }
    },
    PORT {
        @Override
        public String stringValue() { return "port"; }
    },
    HOST_UPDATER {
        @Override
        public String stringValue() { return "hostU"; }
    },
    PORT_UPDATER {
        @Override
        public String stringValue() { return "portU"; }
    },
    TOKEN {
        @Override
        public String stringValue() { return "token"; }
    },
    USER_IDENT {
        @Override
        public String stringValue() { return "userIdent"; }
    };

    public abstract String stringValue();
}
