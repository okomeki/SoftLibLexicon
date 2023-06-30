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
import net.siisise.json.JSONObject;

/**
 *
 */
public class LexString extends LexPrimitive {

    public String Default;
    public Number minLength;
    public Number maxLength;
    public String[] Enum;
    public String Const;
    public String[] knownValues;

    public LexString(JSONObject src) {
        super(Type.string, src);
        Default = (String) src.get("default");
        minLength = (Number) src.get("minLength");
    }

    @Override
    public String toJava(String defName, LexRoot root) {
        return "String.toJava(" + defName + ")";
    }

    @Override
    public String typeConvert(LexRoot root) {
        String format = (String) src.get("format");
        String at;
        if ( format != null ) {
            at = "@format(\"" + format + "\") " ;
        } else {
            at = "";
        }
        return at + "String";
    }
}
