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
package net.siisise.atp.lexicon.xrpc;

import java.util.Set;
import net.siisise.atp.lexicon.LexObject;
import net.siisise.atp.lexicon.LexRoot;
import net.siisise.atp.lexicon.LexType;
import net.siisise.atp.lexicon.LexUserType;
import net.siisise.json.JSONObject;

/**
 * interface LexXrpcQuery extends LexUserType {
 *   type = 'query'
 *   parameters?: Record&lt;string, LexPrimitive&gt;
 *   output?; LexXrpcBody
 *   errors?: LexXrpcError[]
 * }
 */
public class LexXrpcQuery extends LexUserType {

    // parameters? Record<string, LexPrimitive> を required と properties に分けている
//    public List<String> required;
//    public Map<String, LexType> properties; // LexPrimitive, LexArray
    
    public LexObject parameters;

    public LexXrpcBody output;
    public LexXrpcError[] errors;
    
    public LexXrpcQuery(String id, JSONObject src) {
        super(Type.query, id, src);
    }

    @Override
    public String toJava(String defName, LexRoot root) {
        StringBuilder comment = new StringBuilder(), code = new StringBuilder();
        call(getType(), defName, comment, root, code);
        System.out.println(comment);
        System.out.println(code);
        return code.toString();
    }

    /**
     *
     * @param type
     * @param parameters なければ null ?
     * @param code
     */
    void call(String type, String name, StringBuilder comment, LexRoot root, StringBuilder code) {

        StringBuilder params = new StringBuilder(200);
        StringBuilder method = new StringBuilder(200);

        method.append("    ");

        if (output != null) {
            method.append("JSONObject json = ");
        }
        method.append("service");

        if ( parameters != null ) {
            Set<String> ps = parameters.properties.keySet();

            for (String p : ps) {
                LexType prop = parameters.properties.get(p);
                String description = (String) prop.getDescription();
                if (description != null) {
                    comment.append("\r\n * @param ").append(p).append(' ').append(description);
                }

                params.append(", ").append(prop.typeConvert(root))
                        .append(" ").append(p);

                method.append(".");
                if (parameters.required != null && parameters.required.contains(p)) {
                    method.append("req(\"");
                } else {
                    method.append("opt(\"");
                }
                method.append(p);
                method.append("\", ");
                method.append(p);
                method.append(")\r\n        ");
            }
        }

        comment.append("\r\n * @throws IOException");
        comment.append("\r\n * @throws RestException");
        comment.append("\r\n */");

        code.append("\r\n\r\n/**");
        code.append(comment.toString());
        code.append("\r\n");
        code.append("public static ");

        if (output == null) {
            code.append("void ");
        } else {
            code.append("JSONObject ");
        }

        code.append(name).append("(XRPC service");

        code.append(params.toString());
        code.append(") throws IOException, RestException {\r\n");

        method.append(".");
        method.append(type);
        method.append("(\"");
        method.append(id);
        method.append("\");");

        code.append(method.toString());

        if (output != null) {
            code.append("\r\n    return json;");
        }

    }
    
    @Override
    public String typeConvert(LexRoot root) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
