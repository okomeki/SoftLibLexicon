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
package net.siisise.atp.lexicon;

import net.siisise.json.JSONObject;

/**
 *
 */
public abstract class LexUserType extends LexType {

    public enum Type {
        query,
        procedure,
        record,
        token,
        object,
        union,
        unknown,
        blob,
        image,
        video,
        audio
    }
    public String id;
    Type type;
    public String description;
    
    protected LexUserType(Type type, JSONObject obj) {
        super(obj);
        this.type = type;
        description = (String) obj.get("description");
    }

    protected LexUserType(Type type, String id, JSONObject obj) {
        super(obj);
        this.type = type;
        this.id = id;
        description = (String) obj.get("description");
    }
    
    @Override
    public String getType() {
        return type.name().toLowerCase();
    }
    
    @Override
    public String getDescription() {
        return description;
    }
}
