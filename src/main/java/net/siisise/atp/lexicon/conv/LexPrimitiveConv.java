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

import net.siisise.atp.lexicon.primitives.LexPrimitive;
import net.siisise.json.JSONObject;

/**
 *
 */
class LexPrimitiveConv {

    /**
     * LexArray も含めて primitive
     */
    public static LexPrimitive toLex(JSONObject obj) {
        String type = (String) obj.get("type");
        if (null == type) {
            System.out.println("primitive null : " + obj.toJSON());
        } else {
            switch (type) {
                case "boolean":
                    return LexBooleanConv.toLex(obj);
                case "number":
                    return LexNumberConv.toLex(obj);
                case "integer":
                    return LexIntegerConv.toLex(obj);
                case "string":
                    return LexStringConv.toLex(obj);
                default:
                    System.out.println("primitive : " + obj.toJSON());
                    break;
            }
        }
        throw new UnsupportedOperationException(obj.toJSON());
    }

}
