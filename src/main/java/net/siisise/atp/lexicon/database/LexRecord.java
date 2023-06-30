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
package net.siisise.atp.lexicon.database;

import net.siisise.atp.lexicon.LexObject;
import net.siisise.atp.lexicon.LexRoot;
import net.siisise.json.JSONObject;

/**
 * LexObject の継承でいいのかも
 */
public class LexRecord extends LexObject {
    public String key;
    
    public LexRecord(JSONObject src) {
        super(Type.record, src);
        key = (String) src.get("key");
    }

    @Override
    public String toJava(String defName, LexRoot root) {
        StringBuilder rec = new StringBuilder();
        rec.append("@record(\"");
        rec.append(key);
        rec.append("\")");
        rec.append(super.toJava(defName, root));
        return rec.toString();
    }

    @Override
    public String typeConvert(LexRoot root) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
