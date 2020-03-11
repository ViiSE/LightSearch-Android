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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import test.rule.CreateAuthorizationDTORule;

import static com.google.common.truth.Truth.assertThat;
import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class AuthorizationDTOJUnitTest {

    private AuthorizationDTO authDTO;

    @Rule
    public final CreateAuthorizationDTORule authDTORule = new CreateAuthorizationDTORule();

    @Before
    public void setUp() {
        authDTO = authDTORule.getAuthorizationDTO();
    }

    @Test
    public void username() {
        testBegin("AuthorizationDTO", "username()");

        String username = authDTO.username();
        assertThat(username).isNotEmpty();
        assertThat(username).isNotNull();
        System.out.println("Username: " + username);

        testEnd("AuthorizationDTO", "username()");
    }

    @Test
    public void password() {
        testBegin("AuthorizationDTO", "password()");

        String password = authDTO.password();
        assertThat(password).isNotEmpty();
        assertThat(password).isNotNull();
        System.out.println("Password: " + password);

        testEnd("AuthorizationDTO", "password()");
    }

    @Test
    public void userIdent() {
        testBegin("AuthorizationDTO", "userIdent()");

        String userIdent = authDTO.userIdent();
        assertThat(userIdent).isNotEmpty();
        assertThat(userIdent).isNotNull();
        System.out.println("User ident: " + userIdent);

        testEnd("AuthorizationDTO", "userIdent()");
    }
}
