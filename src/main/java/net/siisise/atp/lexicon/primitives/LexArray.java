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
package net.siisise.atp.lexicon.primitives;

import net.siisise.atp.lexicon.LexRoot;
import net.siisise.atp.lexicon.LexType;
import net.siisise.json.JSONObject;

/**
 *
 */
public class LexArray extends LexType {

    final String type = "array";
    public String description;
    //LexRef / LexPrimitive / LexRef[] items;
    /**
     * 型情報 LexRefは複数種類指定できる
     */
    public String[] items;
    public Number minLength;
    public Number maxLength;

    public LexArray(JSONObject src) {
        super(src);
        description = (String) src.get("description");
    }
    
    @Override
    public String getType() {
        return type;
    }

    @Override
    public String toJava(String defName, LexRoot root) {
        return "// array //";
    }

    @Override
    public String typeConvert(LexRoot root) {
        StringBuilder sb = new StringBuilder(); 
        for (String item : items) {
            if ( !sb.isEmpty() ) {
                sb.append("+");
            }
            sb.append(item);
            sb.append("[]");
        }
        return sb.toString();
    }

    @Override
    public String getDescription() {
        return description;
    }
}
