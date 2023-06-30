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

import net.siisise.atp.lexicon.primitives.LexInteger;
import net.siisise.json.JSONArray;
import net.siisise.json.JSONObject;

/**
 *
 */
class LexIntegerConv {

    public static LexInteger toLex(JSONObject obj) {
        LexInteger i = new LexInteger(obj);
        i.Default = (Number) obj.get("default");
        i.minimum = (Number) obj.get("minimum");
        i.maximum = (Number) obj.get("maximum");
        JSONArray en = (JSONArray) obj.get("enum");
        if (en != null) {
            i.Enum = (Number[]) en.toArray(new Number[0]);
        }
        i.Const = (Number) obj.get("const");
        return i;
    }
}
