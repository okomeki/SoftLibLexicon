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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.siisise.atp.lexicon.LexRef;
import net.siisise.atp.lexicon.LexType;
import net.siisise.json.JSONObject;

/**
 *
 */
public class LexTypeConv {

    public static LexType toLex(String id, JSONObject obj) {
        String type = (String) obj.get("type");
        if ("array".equals(type)) { // primitive
            return LexArrayConv.toLex(obj);
        } else if ("ref".equals(type)) {
            return new LexRef(obj);
        } else if ("unknown".equals(type)) {
            return LexUnknownConv.toLex(id, obj);
        } else {
            try {
                return LexPrimitiveConv.toLex(obj); // primitive
            } catch (UnsupportedOperationException e) {

            }
            return LexUserTypeConv.toLex(id, obj);
        }
    }

    /**
     * プリミティブ型を含む.
     *
     * @param id
     * @param defs
     * @return
     */
    public static Map<String, LexType> toTypeRecord(String id, JSONObject defs) {
        System.out.println("toTypeRecord: " + id + " " + defs.toJSON());
        Map<String, LexType> ldefs = new HashMap<>();
        Set<String> ks = defs.keySet();

        for (String k : ks) {
            System.out.println("k:" + k);
            JSONObject v = (JSONObject) defs.getJSON(k);
            LexType lex = toLex(id + '.'+ k, v);
            ldefs.put(k, lex);
        }
        return ldefs;
    }
}
