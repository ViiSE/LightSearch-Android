/*
 * Copyright 2020 ViiSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.viise.lightsearch.security;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import ru.viise.lightsearch.exception.InformationException;
import ru.viise.lightsearch.exception.KeyException;

public class EncryptedInformation implements Information<String> {

    private final Information<String> decInfo;
    private final String alg;
    private final Key<PublicKey> key;

    public EncryptedInformation(
            Information<String> decInfo,
            String alg,
            Key<PublicKey> key) {
        this.decInfo = decInfo;
        this.alg = alg;
        this.key = key;
    }

    @Override
    public String data() throws InformationException {
        try {
            Cipher cipher = Cipher.getInstance(alg);
            cipher.init(Cipher.ENCRYPT_MODE, key.as());
            byte[] encryptedBytes = cipher.doFinal(decInfo.data().getBytes(StandardCharsets.UTF_8));

            return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP);
        } catch (NoSuchPaddingException |
                NoSuchAlgorithmException |
                InvalidKeyException |
                BadPaddingException |
                IllegalBlockSizeException |
                KeyException ex) {
            throw new InformationException(ex);
        }
    }
}
