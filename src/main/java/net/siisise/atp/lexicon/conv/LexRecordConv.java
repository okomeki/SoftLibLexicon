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

import net.siisise.atp.lexicon.database.LexRecord;
import net.siisise.json.JSONObject;

/**
 *
 * @author okome
 */
public class LexRecordConv {

    public static LexRecord toLex(String id, JSONObject obj) {
        System.out.println("LexRecordConv:" + id + ":" + obj.toJSON());
        LexRecord rec = new LexRecord(obj);
        rec.properties = LexObjectConv.toLex(id, (JSONObject) obj.getJSON("record")).properties;
        return rec;
    }
}
