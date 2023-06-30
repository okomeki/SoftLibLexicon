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

import net.siisise.atp.lexicon.xrpc.LexXrpcProcedure;
import net.siisise.json.JSONArray;
import net.siisise.json.JSONObject;

/**
 *
 */
class LexXrpcProcedureConv {
    public static LexXrpcProcedure toLex(String id, JSONObject obj) {
        LexXrpcProcedure p = new LexXrpcProcedure(id, obj);
        JSONObject pa = (JSONObject) obj.getJSON("parameters");
        if ( pa != null ) {
            p.parameters = LexRecordConv.toLex(id, pa);
        }
        p.input = LexXrpcBodyConv.toLex(id, (JSONObject) obj.get("input"));
        p.output = LexXrpcBodyConv.toLex(id, (JSONObject) obj.get("output"));
        p.errors = LexXrpcErrorConv.toLexs((JSONArray) obj.get("errors"));
        return p;
    }
}
