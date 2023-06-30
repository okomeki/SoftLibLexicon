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

import net.siisise.atp.lexicon.LexiconDoc;
import net.siisise.json.JSONObject;

/**
 *
 */
public class LexiconDocConv {

    /**
     * 1 lexicon ファイルをLexiconDoc として読み込む.
     * @param lex
     * @return 
     */
    public static LexiconDoc toLex(JSONObject lex) {
        System.out.println("Lexicon.toLex: " + lex.toJSON());
        LexiconDoc doc = new LexiconDoc(lex);
        int lexver = ((Number) lex.get("lexicon")).intValue();
        if (lexver != 1) {
            throw new UnsupportedOperationException("Unsupport lexicon:" + lexver);
        }
        doc.id = (String) lex.get("id");
        Number revision = (Number) lex.get("revision");
        if ( revision != null ) {
            doc.revision = revision.intValue();
        }
        doc.description = (String) lex.get("description");
        JSONObject defs = (JSONObject) lex.getJSON("defs");
        System.out.println("id: " + doc.id);
        doc.defs = LexTypeConv.toTypeRecord(doc.id, defs);
        return doc;
    }
    
}
