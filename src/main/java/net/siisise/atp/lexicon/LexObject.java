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

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.siisise.json.JSONArray;
import net.siisise.json.JSONObject;

/**
 *
 */
public class LexObject extends LexUserType {

    public List<String> required;
    public Map<String, LexType> properties; // LexRef, LexArray, LexPrimitive, LexRef[]

    public LexObject(JSONObject obj) {
        this(Type.object, obj);
    }
    
    protected LexObject(Type type, JSONObject obj) {
        super(type, obj);
        required = (JSONArray)obj.get("required");
        description = (String)obj.get("description");
    }

    @Override
    public String toJava(String name, LexRoot root) {
        StringBuilder cls = new StringBuilder();

        if (description != null) {
            cls.append("\r\n/**");
            cls.append("\r\n * ").append(description);
            cls.append("\r\n */");
        }

        cls.append("\r\npublic static class ");
        cls.append(name.toUpperCase().charAt(0));
        cls.append(name.substring(1));
        cls.append(" {");
        
        System.out.println("LexObject src:" + src.toJSON());

        Set<String> pnames = properties.keySet();
        for (String pname : pnames) {
            LexType property = properties.get(pname);
            if ( property == null) {
                System.out.println("pname: " + pname);
            }

            cls.append("\r\n");
            cls.append("    public ");
            cls.append(property.typeConvert(root));
            cls.append(" ");
            cls.append(pname);
            cls.append(";");
        }

        cls.append("\r\n}");
        return cls.toString();
    }

    @Override
    public String typeConvert(LexRoot root) {
        return "Object";
    }
}
