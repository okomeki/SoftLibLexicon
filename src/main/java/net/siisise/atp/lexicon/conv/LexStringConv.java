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

import net.siisise.atp.lexicon.primitives.LexString;
import net.siisise.json.JSONObject;
import net.siisise.json.bind.OMAP;

/**
 *
 */
class LexStringConv {

    public static LexString toLex(JSONObject obj) {
        LexString str = new LexString(obj);
        str.minLength = (Number) obj.get("minLength");
        str.maxLength = (Number) obj.get("maxLength");
        str.Enum = (String[]) OMAP.valueOf(obj.getJSON("enum"), String[].class);
        str.Const = (String) obj.get("const");
        obj.get("knownValues");
        return str;
    }
}
