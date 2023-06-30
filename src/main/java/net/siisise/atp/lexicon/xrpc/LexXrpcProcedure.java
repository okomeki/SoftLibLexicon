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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.siisise.atp.lexicon.LexObject;
import net.siisise.atp.lexicon.LexRef;
import net.siisise.atp.lexicon.LexRoot;
import net.siisise.atp.lexicon.LexType;
import net.siisise.atp.lexicon.LexUserType;
import net.siisise.atp.lexicon.database.LexRecord;
import net.siisise.json.JSONObject;

/**
 * POST
 */
public class LexXrpcProcedure extends LexUserType {

    public LexXrpcProcedure(String id, JSONObject src) {
        super(Type.procedure, id, src);
    }

    /**
     * Record 型のサブを作ってみた
     */
    public LexRecord parameters;
//    public Map<String, LexType> parameters;
    public LexXrpcBody input;
    public LexXrpcBody output;
    public LexXrpcError[] errors;

    @Override
    public String toJava(String defName, LexRoot root) {
        return method(src, root);
    }

    @Override
    public String typeConvert(LexRoot root) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    String method(JSONObject main, LexRoot root) {
        StringBuilder code = new StringBuilder();

        String[] names = id.split("\\.");
        String mname = names[names.length - 1];

        StringBuilder comment = new StringBuilder();

        if (description != null) {
            comment.append("\r\n * ");
            comment.append(description).append("\r\n");
        }

        // type:query
//        JSONObject parameters = (JSONObject) main.getJSON("parameters");
        // type:procedure
//        JSONObject input = (JSONObject) main.getJSON("input");

//        JSONObject output = (JSONObject) main.getJSON("output");

        if (parameters != null) {
            LexObject p = parameters;
            
            call(getType(), mname, comment, p, code, root);
        } else if (input != null) {
            String encoding = (String) input.encoding[0];
            // schema は application/jsonのときだけ
            if ("application/json".equals(encoding)) {
                LexType schema = input.schema;
                LexObject schemao;
                if ( schema instanceof LexRef ) {
                    schemao = ref((LexRef)schema, root);
                } else {
                    schemao = (LexObject)schema;
                }
                
                call(getType(), mname, comment, schemao, code, root);
            } else {
                code.append(") {\r\n");
                code.append("java.lang.UnsupportedOperationException();");
            }

        } else {
            call(getType(), mname, comment, null, code, root);
        }

        code.append("\r\n}");
        return code.toString();
    }
    
    LexObject ref(LexRef ref, LexRoot root) {
        System.out.println("##ref: " + ref.id);
//        System.out.println("ref:root: " + root);
        String docid;
        String sub;
        if ( ref.id.contains("#")) {
            String[] ids = ref.id.split("#");
            docid = ids[0];
            sub = ids[1];
        } else {
            docid = "";
            sub = ref.id;
        }

        return (LexObject) root.idmap.get(docid).defs.get(sub);
    }

    /**
     *
     * @param type
     * @param parameters なければ null ?
     * @param code
     */
    void call(String type, String name, StringBuilder comment, LexObject parameters, StringBuilder code, LexRoot root) {

        StringBuilder params = new StringBuilder(200);
        StringBuilder method = new StringBuilder(200);
        
        

        if (parameters == null) {
            parameters = new LexObject(new JSONObject());
            parameters.properties = new HashMap();
        }

        List<String> required = parameters.required;
        Map<String,LexType> properties = parameters.properties;
        Set<String> ps = properties.keySet();

        method.append("    ");
        if (output != null) {
            method.append("JSONObject json = ");
        }
        method.append("service");

        for (String p : ps) {
            //JSONObject prop = (JSONObject) properties.getJSON(p);
            LexType prop = ((LexObject)input.schema).properties.get(p);
            String description = (String) prop.getDescription();
            if (description != null) {
                comment.append("\r\n * @param ").append(p).append(' ').append(description);
            }

            params.append(", ").append(prop.typeConvert(root))
                    .append(" ").append(p);

            method.append(".");
            if (required != null && required.contains(p)) {
                method.append("req(\"");
            } else {
                method.append("opt(\"");
            }
            method.append(p);
            method.append("\", ");
            method.append(p);
            method.append(")\r\n        ");
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

}
