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
package test;

/**
 *
 * @author ViiSE
 */
public class TestMessage {
    
    private enum IDENTIFY {
        BEGIN {
            @Override
            public String toString() { return "BEGIN"; }
        },
        END {
            @Override
            public String toString() { return "END"; }
        }
    }
    
    private static void printTestMessage(String className, String methodName, IDENTIFY identify) {
        System.out.println("===============================================");
        System.out.println(className);
        System.out.println("Method:" + methodName + ". Test " + identify.toString());
        System.out.println("===============================================");
    }
    
    public static void testBegin(String className, String methodName) {
        printTestMessage(className, methodName, IDENTIFY.BEGIN);
    }
    
    public static void testEnd(String className, String methodName) {
        printTestMessage(className, methodName, IDENTIFY.END);
    }

    public static void catchMessage(Exception ex) {
        System.out.println("CATCH! Exception: " + ex.getMessage());
    }
}
