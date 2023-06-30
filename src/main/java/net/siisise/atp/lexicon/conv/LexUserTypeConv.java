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
import java.util.Map;
import java.util.Set;
import net.siisise.atp.lexicon.LexUserType;
import net.siisise.json.JSONObject;

/**
 *
 * @author okome
 */
public class LexUserTypeConv {

    public static LexUserType toLex(String id, JSONObject obj) {
        String type = (String) obj.get("type");
        LexUserType v = null;
        if ("query".equals(type)) {
            v = LexXrpcQueryConv.toLex(id, obj);
        } else if ("procedure".equals(type)) {
            v = LexXrpcProcedureConv.toLex(id, obj);
        } else if ("record".equals(type)) {
            v = LexRecordConv.toLex(id, obj);
        } else if ("token".equals(type)) {
            v = LexTokenConv.toLex(obj);
        } else if ("object".equals(type)) {
            v = LexObjectConv.toLex(id, obj);
        } else if ("blob".equals(type)) {
            v = LexBlobConv.toLex(obj);
        } else if ("image".equals(type)) {
            v = LexImageConv.toLex(obj);
        } else if ("video".equals(type)) {
            v = LexVideoConv.toLex(obj);
        } else if ("audio".equals(type)) {
            v = LexAudioConv.toLex(obj);
        } else if ("union".equals(type)) {
            v = LexUnionConv.toLex(id, obj);
        } else if ("ref".equals(type)) {
            v = LexRefConv.toLex(id, obj);
        } else {
            System.out.println("userType 未 : " + obj.toJSON());
        }
        if ( v != null ) {
            v.description = (String) obj.get("description");
        }
        return v;
    }

    /**
     * Record
     *
     * @param defs
     * @return
     */
    public static Map<String, LexUserType> toUserTypeRecord(String id, JSONObject defs) {
        Map<String, LexUserType> ldefs = new HashMap<>();
        Set<String> ks = defs.keySet();

        for (String k : ks) {
            JSONObject v = (JSONObject) defs.getJSON(k);
            ldefs.put(k, LexUserTypeConv.toLex(id + '.' + k, v));
        }
        return ldefs;
    }

}
