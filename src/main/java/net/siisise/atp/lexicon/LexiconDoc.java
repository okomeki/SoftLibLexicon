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

import java.util.Map;
import net.siisise.json.JSONObject;

/**
 * ファイル単位っぽい
 * https://atproto.com/specs/lexicon#interface
 * https://scrapbox.io/Bluesky/Lexicon_スキーマ
 */
public class LexiconDoc {
    public JSONObject lex;
    public String id;
    public int revision;
    public String description;
    public Map<String,LexType> defs; // LexUserType|LexArray|LexPrimitive|LexRef[]


    public LexiconDoc(JSONObject lex) {
        this.lex = lex;
    }
}
