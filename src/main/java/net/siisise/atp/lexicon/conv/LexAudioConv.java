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

import net.siisise.atp.lexicon.blobs.LexAudio;
import net.siisise.json.JSONArray;
import net.siisise.json.JSONObject;

/**
 *
 */
class LexAudioConv {

    static LexAudio toLex(JSONObject obj) {
        LexAudio audio = new LexAudio(obj);
        audio.accept = (String[]) ((JSONArray)obj.get("accept")).toArray(new String[0]);
        audio.maxSize = (Number)obj.get("accept");
        audio.maxLength = (Number)obj.get("maxLength");
        return audio;
    }
    
}
