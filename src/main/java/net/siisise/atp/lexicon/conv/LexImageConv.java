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

import net.siisise.atp.lexicon.blobs.LexImage;
import net.siisise.json.JSONArray;
import net.siisise.json.JSONObject;

/**
 *
 */
class LexImageConv {

    static LexImage toLex(JSONObject obj) {
        LexImage image = new LexImage(obj);
        image.accept = (String[]) ((JSONArray)obj.get("accept")).toArray(new String[0]);
        image.maxSize = (Number)obj.get("accept");
        image.maxWidth = (Number)obj.get("maxWidth");
        image.maxHeight = (Number)obj.get("maxHeight");
        return image;
    }
    
}
