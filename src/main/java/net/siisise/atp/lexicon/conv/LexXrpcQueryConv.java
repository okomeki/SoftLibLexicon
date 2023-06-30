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
import java.util.List;
import java.util.Map;
import net.siisise.atp.lexicon.LexType;
import net.siisise.atp.lexicon.xrpc.LexXrpcQuery;
import net.siisise.json.JSONArray;
import net.siisise.json.JSONObject;

/**
 *
 * @author okome
 */
class LexXrpcQueryConv {

    public static LexXrpcQuery toLex(String id, JSONObject obj) {
        System.out.println("LexXrpcQueryConv:" + obj.toJSON());
        LexXrpcQuery query = new LexXrpcQuery(id, obj);
        // parameters 前提
        query.parameters = LexObjectConv.toLex(id, (JSONObject) obj.get("parameters"));
        
//        query.required = required(parameters);
//        query.properties = props(id, parameters);
        query.output = LexXrpcBodyConv.toLex(id, (JSONObject) obj.get("output"));
        query.errors = LexXrpcErrorConv.toLexs((JSONArray) obj.get("errors"));
        return query;
    }

    // object 内とおなじ?
    static List<String> required(JSONObject parameters) {
        JSONArray required = (JSONArray) parameters.getJSON("required");
        return required;
    }
    
    /**
     * object 内とおなじ?
     * type params の仮 LexPrimitive, LexArray のみ
     * @param parameters query の params
     * @return 
     */
    static Map<String,LexType> props(String id, JSONObject parameters) {
        Map<String,LexType> params = new HashMap<>();
        if ( parameters != null ) {
            JSONObject props = (JSONObject) parameters.getJSON("properties");
            System.out.println(props);
            for ( Object k : props.keySet() ) {
                JSONObject v = (JSONObject) props.get(k);
                params.put((String) k, LexTypeConv.toLex(id+'.'+k, v));
            }
        }
        
        return params;
    }

}
