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
package net.siisise.atp.lexicon.conv;

import net.siisise.atp.lexicon.LexUnion;
import net.siisise.atp.lexicon.LexUserType;
import net.siisise.json.JSONArray;
import net.siisise.json.JSONObject;

/**
 *
 */
public class LexUnionConv {

    static LexUserType toLex(String id, JSONObject obj) {
        JSONArray<String> refs = (JSONArray) obj.getJSON("refs");
        LexUnion union = new LexUnion(obj);
        for ( String ref : refs ) {
            union.refs.add(ref);
        }
        return union;
    }
    
}
