/*
 * Copyright 2023 okome.
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
package net.siisise.atp.lexicon;

import net.siisise.json.JSONObject;
import net.siisise.json.JSONValue;

/**
 * JSON から Java interface かなにかを作ることにする
 */
public class Lexicon {
    
    JSONObject lexicon;
    
    public Lexicon(JSONObject lexicon) {
        this.lexicon = lexicon;
    }
    
    /**
     * Lexicon Schema
     * @return 
     */
    public String toJava() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * 
     * @param json
     * @return 
     */
    public boolean validate(JSONValue json) {
        throw new java.lang.UnsupportedOperationException();
    }
    
}
