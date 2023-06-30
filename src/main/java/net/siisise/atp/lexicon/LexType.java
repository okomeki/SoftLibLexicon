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
 * 仕様にないJava風独自型.
 * LexiconDoc での定義 LexUserType | LexArray | LexPrimitive | LexRef[]
 * LexObject での定義  LexRef      | LexArray | LexPrimitive | LexRef[]
 * LexArray での定義   LexRef                 | LexPrimitive | LexRef[]
 * 
 */
public abstract class LexType {
    
    protected JSONObject src;
    protected String packagePrefix;
    
    protected LexType(JSONObject obj) {
        src = obj;
    }
    
    public abstract String getType();

    public abstract String toJava(String defName, LexRoot root);

    public abstract String typeConvert(LexRoot root);
    
    /**
     * Ref以外で使うかも
     * @return 
     */
    public abstract String getDescription();
    
    public JSONObject getSrc() {
        return src;
    }
}
