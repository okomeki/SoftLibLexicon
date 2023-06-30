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

import net.siisise.atp.lexicon.LexObject;
import net.siisise.atp.lexicon.LexRef;
import net.siisise.atp.lexicon.LexType;
import net.siisise.atp.lexicon.xrpc.LexXrpcBody;
import net.siisise.json.JSONArray;
import net.siisise.json.JSONObject;

/**
 *
 */
class LexXrpcBodyConv {

    /**
     * 
     * @param obj null 可?
     * @return 
     */
    public static LexXrpcBody toLex(String id, JSONObject obj) {
        if ( obj == null ) {
            return null;
        }
        System.out.println("LexXrpcBodyConv:" + id + " " + obj.toJSON());
        LexXrpcBody body = new LexXrpcBody();
        body.description = (String) obj.get("description");
        Object enc = obj.get("encoding");
        if (enc instanceof String) {
            body.encoding = new String[]{(String) obj.get("encoding")};
        } else {
            body.encoding = (String[]) ((JSONArray) enc).toArray(new String[0]);
        }
        JSONObject schema = (JSONObject) obj.get("schema");
        String type = (String) schema.get("type");
        if (  "ref".equals(type)) {
            
        }

        // ToDo: type ref の場合は?
        body.schema = (LexType) LexUserTypeConv.toLex(id, schema);
        return body;
    }

}
