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

package ru.viise.lightsearch.util;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class IPAddressProviderJUnitTest {

    @Test
    public void ipAddress() {
        testBegin("IpAddressProvider", "ipAddress()");

        IPAddressProvider ipProvider = IPAddressProviderInit.ipAddressProvider();
        String ip = ipProvider.ipAddress(true);
        assertThat(ip).isNotNull();
        assertThat(ip).isNotEmpty();

        System.out.println("IP: " + ip);

        testEnd("IpAddressProvider", "ipAddress()");
    }
}
