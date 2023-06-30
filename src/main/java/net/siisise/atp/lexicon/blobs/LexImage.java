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
package net.siisise.atp.lexicon.blobs;

import net.siisise.atp.lexicon.LexRoot;
import net.siisise.atp.lexicon.LexUserType;
import net.siisise.json.JSONObject;

/**
 *
 */
public class LexImage extends LexUserType {

    public String[] accept;
    public Number maxSize;
    public Number maxWidth;
    public Number maxHeight;

    public LexImage(JSONObject obj) {
        super(Type.image, obj);
    }

    @Override
    public String toJava(String defName, LexRoot root) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String typeConvert(LexRoot root) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
