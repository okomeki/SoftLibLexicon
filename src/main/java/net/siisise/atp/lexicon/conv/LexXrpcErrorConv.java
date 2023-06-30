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

import java.util.ArrayList;
import java.util.List;
import net.siisise.atp.lexicon.xrpc.LexXrpcError;
import net.siisise.json.JSONArray;
import net.siisise.json.JSONObject;

/**
 *
 */
class LexXrpcErrorConv {

    public static LexXrpcError[] toLexs(JSONArray<JSONObject> objs) {
        if ( objs != null ) {
            List<LexXrpcError> rs = new ArrayList<>();
            for (JSONObject o : objs) {
                rs.add(toLex(o));
            }
            return rs.toArray(new LexXrpcError[0]);
        }
        return null;
    }

    public static LexXrpcError toLex(JSONObject obj) {
        LexXrpcError error = new LexXrpcError();
        error.name = (String) obj.get("name");
        error.description = (String) obj.get("description");
        return error;
    }

}
