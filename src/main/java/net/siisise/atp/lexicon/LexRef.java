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
 * LexString の拡張だが LexPrimitive ではないというかたち.
 * LexconDoc で使われている
 */
public class LexRef extends LexUserType {
    String ref;
    
    public LexRef(JSONObject src) {
        super(Type.union, src);
        ref = (String) src.get("ref");
    }

    
    public String typeString() {
        String refFullName = ref;
        System.out.println("REFNAME: " + refFullName);
        String[] refNames = refFullName.split("#");
        String pkg = "";
        String cn;
        if (refNames.length == 2 && !refNames[0].isEmpty()) {
            if (packagePrefix != null) {
                pkg = packagePrefix + ".";
            }
            pkg += refNames[0].substring(0, refNames[0].lastIndexOf('.') + 1);
        }
        cn = refNames[refNames.length - 1];
        String type = pkg + cn.toUpperCase().charAt(0) + cn.substring(1);
//                type = "ref#" + obj.get("ref");
        System.out.println("  " + type);
        return type;
        
    }

    @Override
    public String toJava(String defName, LexRoot root) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String typeConvert(LexRoot root) {
        return "仮";
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
