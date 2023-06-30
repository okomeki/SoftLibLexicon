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

import net.siisise.atp.lexicon.primitives.LexNumber;
import net.siisise.json.JSONArray;
import net.siisise.json.JSONObject;

/**
 *
 */
class LexNumberConv {

    public static LexNumber toLex(JSONObject obj) {
        LexNumber n = new LexNumber(obj);
        n.Default = (Number) obj.get("default");
        n.minimum = (Number) obj.get("minimum");
        n.maximum = (Number) obj.get("maximum");
        JSONArray en = (JSONArray) obj.get("enum");
        if (en != null) {
            n.Enum = (Number[]) en.toArray(new Number[0]);
        }
        n.Const = (Number) obj.get("const");
        return n;
    }

}
