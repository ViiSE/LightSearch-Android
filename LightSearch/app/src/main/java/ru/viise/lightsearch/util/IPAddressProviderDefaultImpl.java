/*
 * Copyright 2016 javiersantos.
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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

class IPAddressProviderDefaultImpl implements IPAddressProvider {

    @Override
    public String ipAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface netInterface : interfaces) {
                List<InetAddress> addresses = Collections.list(netInterface.getInetAddresses());
                for (InetAddress address : addresses) {
                    if (!address.isLoopbackAddress()) {
                        String strAddress = address.getHostAddress();
                        boolean isIPv4 = strAddress.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return strAddress;
                        } else {
                            if (!isIPv4) {
                                int delimeter = strAddress.indexOf('%'); // drop ip6 zone suffix
                                return delimeter < 0 ? strAddress.toUpperCase() :
                                        strAddress.substring(0, delimeter).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return "";
    }
}
