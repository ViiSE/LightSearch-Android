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

package ru.viise.lightsearch.security;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class HashAlgorithmSHA256JUnitTest {

    @Test
    public void sha256() {
        testBegin("HashAlgorithm", "digest()");

        String password = "password";
        String SHA256OfString_password_from_passwordsgenerator_net =
                "5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8".toLowerCase();

        HashAlgorithm hashAlgorithm = HashAlgorithmInit.hashAlgorithm();
        String hash = hashAlgorithm.digest(password);
        assertThat(hash).isNotNull();
        assertThat(hash).isNotEmpty();
        assertThat(hash).isNotEqualTo(password);

        assertThat(hash).isEqualTo(SHA256OfString_password_from_passwordsgenerator_net);

        System.out.println("SHA 256 of string 'password': " + hash);

        testEnd("HashAlgorithm", "digest()");
    }
}
