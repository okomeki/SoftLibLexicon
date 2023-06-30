package net.siisise.atp.lexicon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.siisise.atp.lexicon.conv.LexiconDocConv;
import net.siisise.atp.lexicon.database.LexRecord;
import net.siisise.io.FileIO;
import net.siisise.json.JSON;
import net.siisise.json.JSONArray;
import net.siisise.json.JSONObject;

/**
 *
 */
public class ConvertFilter {

    String packagePrefix;

    // どちらかにする
    public Map<String, LexiconDoc> docs = new HashMap<>();
    public LexRoot root = new LexRoot();

    ConvertFilter(String pre) {
        packagePrefix = pre;
    }

    /**
     * ref 用に2パスに変える.
     * 
     * @param lexicon
     * @param jsrc
     * @return 
     * @throws java.io.IOException
     */
    public String toJava(File lexicon, File jsrc) throws IOException {
        StringBuilder cls = new StringBuilder(100);
        if (lexicon.isDirectory()) {
//            if ( !jsrc.exists() ) {
//                jsrc.mkdirs();
//            }

            boolean v = false;
            cls.append("\r\n\r\npublic class ").append(lexicon.getName()).append(" {\r\n");
            File[] subs = lexicon.listFiles();
            for (File sub : subs) {
                File j = new File(jsrc, sub.getName());
                ConvertFilter cf = new ConvertFilter(packagePrefix);
                String m = cf.toJava(sub, j);

//                id = cf.id;
                int i = cf.id.lastIndexOf('.');
                if (i >= 0) {
                    id = cf.id.substring(0, i);
                } else {
                    id = "";
                }

                if (m != null) {
                    v = true;
                    cls.append(m);
                }
            }
            if (v) {

                int l = id.lastIndexOf('.');
                StringBuilder h = new StringBuilder();
                h.append("package ");
                if (packagePrefix != null) {
                    h.append(packagePrefix);
                    h.append('.');
                }
                h.append(id.substring(0, l));
                h.append(";\r\n");

                h.append("\r\nimport java.io.IOException;");
                h.append("\r\nimport net.siisise.atp.XRPC;");
                h.append("\r\nimport net.siisise.atp.lexicon.format;");
                h.append("\r\nimport net.siisise.json.JSONObject;");
                h.append("\r\nimport net.siisise.rest.RestException;\r\n");

                h.append("\r\n/**\r\n");
                h.append(" * AT Protocol Lexicon\r\n");
                h.append(" *\r\n");
                h.append(" * https://atproto.com/lexicons/");
                h.append(id.replace('.', '-'));
                h.append("\r\n");
                h.append(" */");

                cls.insert(0, h);
                cls.append("\r\n}\r\n");
                File p = jsrc.getParentFile();
                p.mkdirs();
                File j = new File(p, jsrc.getName() + ".java");
                FileOutputStream out = new FileOutputStream(j);
                out.write(cls.toString().getBytes(StandardCharsets.UTF_8));
                out.flush();
                out.close();
                System.out.println(cls.toString());
            }
        } else if (lexicon.isFile()) {
            // Lexiconファイルを LexiconDoc 化する
            File jd = jsrc.getParentFile();
            String sname = lexicon.getName();
            if (sname.endsWith(".json")) {
                LexiconDoc doc = toLexicon(lexicon);
                id = doc.id;
                docs.put(doc.id, doc);
                root.idmap.put(doc.id, doc);
                String jname = sname.substring(0, sname.length() - 5) + ".java";
                File jfile = new File(jd, jname);
                cls.append(tab(toJava(doc, jfile)));
            }
            return cls.toString();
        }
        return null;
    }

    
    public LexiconDoc toLexicon(File lexicon) throws IOException {
//        String sname = lexicon.getName();
        JSONObject lex = (JSONObject) JSON.parse(FileIO.binRead(lexicon));
        return LexiconDocConv.toLex(lex);
    }

    String id;

    /**
     * ToDo: 移動する.
     * @param doc
     * @param jfile
     * @return 
     */
    public String toJava(LexiconDoc doc, File jfile) {
        System.out.print("Lexicon ");
//        System.out.println(lexicon.toJSON(JSON.TAB_MINESC));
        id = doc.id;
        StringBuilder cls = new StringBuilder();
//        JSONObject main = (JSONObject) mainp.get(lexicon);

        JSONObject defs = (JSONObject) doc.lex.get("defs");
        Set<String> defsNames = doc.defs.keySet();
        for (String defName : defsNames) {
            System.out.println(" defName : " + defName);
            LexType def = (LexType) doc.defs.get(defName);
            def.packagePrefix = packagePrefix;
            JSONObject jsonDef = (JSONObject) defs.getJSON(defName);
            cls.append(def.toJava(defName, root));
            if ("main".equals(defName)) {
                if ( def instanceof LexObject) { // LexObject, LexRecord
                    String[] idn = id.split("\\.");
                    cls.append(def(idn[idn.length - 1], def));
                } else {
                    cls.append(method(jsonDef));
                }
            } else {
                cls.append(def(defName, jsonDef));
            }
        }

//        if ( main != null ) {
//            cls.append("class ").append(name).append(" {\r\n");
//            cls.append(method(main));
//            cls.append("\r\n}\r\n");
//            System.out.println(main.toJSON(JSON.TAB_MINESC));
//            System.out.println(cls.toString());
//        }
        return cls.toString();
    }

    String def(String name, JSONObject def) {
        StringBuilder cls = new StringBuilder();
        String type = (String) def.get("type");
        if (type.equals("object")) {
            return obj(name, def);
        } else if (type.equals("record")) {
            return record(name, def);
        }

        cls.append("static ");
        cls.append(typeConvert(def));
        cls.append(" ").append(name);
        cls.append(";");
        return cls.toString();
    }
    
    String def(String name, LexType def) {
        StringBuilder cls = new StringBuilder();
        if ( def instanceof LexRecord ) {
            return record(name, (LexRecord)def);
        } else if ( def instanceof LexObject ) {
            return obj(name, (LexObject)def);
        }
        
        cls.append("static ");
        cls.append(def.typeConvert(root));
        cls.append(" ").append(name);
        cls.append(";");
        return cls.toString();
    }
    
    String record(String name, LexRecord rec) {
        StringBuilder cls = new StringBuilder();
        cls.append("/* record */\r\n");
        
        cls.append("@key(\"").append(rec.key).append("\")\r\n");
        cls.append(obj(name, rec));
        return cls.toString();
    }

    String record(String name, JSONObject obj) {
        StringBuilder cls = new StringBuilder();
        cls.append("/* record */\r\n");
        String key = (String) obj.get("key");
        cls.append("@key(\"").append(key).append("\")\r\n");
        cls.append(obj(name, (JSONObject) obj.getJSON("record")));
        return cls.toString();

    }

    String obj(String name, JSONObject obj) {
        StringBuilder cls = new StringBuilder();
        JSONObject properties = (JSONObject) obj.get("properties");
        String description = (String) obj.get("description");

        if (description != null) {
            cls.append("\r\n/**");
            cls.append("\r\n * ").append(description);
            cls.append("\r\n */");
        }

        cls.append("\r\npublic static class ");
        cls.append(name.toUpperCase().charAt(0));
        cls.append(name.substring(1));
        cls.append(" {");

        Set<String> pnames = properties.keySet();
        for (String pname : pnames) {
            JSONObject property = (JSONObject) properties.get(pname);
            cls.append("\r\n");
            cls.append("    public ");
            cls.append(typeConvert(property));
            cls.append(" ");
            cls.append(pname);
            cls.append(";");
        }

        cls.append("\r\n}");
        return cls.toString();
    }
    
    String obj(String name, LexObject para) {
        StringBuilder cls = new StringBuilder();
        
        if (para.description != null) {
            cls.append("\r\n/**");
            cls.append("\r\n * ").append(para.description);
            cls.append("\r\n */");
        }

        cls.append("\r\npublic static class ");
        cls.append(name.toUpperCase().charAt(0));
        cls.append(name.substring(1));
        cls.append(" {");

        Set<String> pnames = para.properties.keySet();
        for (String pname : pnames) {
            LexType property = para.properties.get(pname);
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

    String method(JSONObject main) {
        StringBuilder code = new StringBuilder();
        String type = (String) main.get("type");
        String description = (String) main.get("description");

        String[] names = id.split("\\.");
        String mname = names[names.length - 1];

        StringBuilder comment = new StringBuilder();

        if (description != null) {
            comment.append("\r\n * ");
            comment.append(description).append("\r\n");
        }

        // type:query
        JSONObject parameters = (JSONObject) main.getJSON("parameters");
        // type:procedure
        JSONObject input = (JSONObject) main.getJSON("input");

        JSONObject output = (JSONObject) main.getJSON("output");

        if (parameters != null) {
            call(type, mname, comment, parameters, output, code);
        } else if (input != null) {
            String encoding = (String) input.get("encoding");
            // schema は application/jsonのときだけ
            if ("application/json".equals(encoding)) {
                JSONObject schema = (JSONObject) input.getJSON("schema");
                call(type, mname, comment, schema, output, code);
            } else {
                code.append(") {\r\n");
                code.append("java.lang.UnsupportedOperationException();");
            }

        } else {
            call(type, mname, comment, null, output, code);
        }

        code.append("\r\n}");
        return code.toString();
    }

    /**
     *
     * @param type
     * @param parameters なければ null ?
     * @param code
     */
    void call(String type, String name, StringBuilder comment, JSONObject parameters, JSONObject output, StringBuilder code) {

        StringBuilder params = new StringBuilder(200);
        StringBuilder method = new StringBuilder(200);

        if (parameters == null) {
            parameters = new JSONObject();
            parameters.addJSON("properties", new JSONObject());
        }

        JSONArray required = (JSONArray) parameters.getJSON("required");
        JSONObject properties = (JSONObject) parameters.getJSON("properties");
        Set<String> ps = properties.keySet();

        method.append("    ");
        if (output != null) {
            method.append("JSONObject json = ");
        }
        method.append("service");

        for (String p : ps) {
            JSONObject prop = (JSONObject) properties.getJSON(p);
            String description = (String) prop.get("description");
            if (description != null) {
                comment.append("\r\n * @param ").append(p).append(' ').append(description);
            }

            params.append(", ").append(typeConvert(prop))
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

    String typeConvert(JSONObject obj) {
        String type = (String) obj.get("type");
        String format = (String) obj.get("format");

        String at;

        if (format != null) {
            at = "@format(\"" + format + "\") ";
        } else {
            at = "";
        }
        if (null != type) {
            switch (type) {
                case "integer":
                    type = "int";
                    break;
                case "string":
                    type = "String";
                    break;
                case "bytes":
                case "blob":
                    type = "byte[]";
                    break;
                case "array":
                    type = typeConvert((JSONObject) obj.getJSON("items")) + "[]";
                    break;
                case "ref":
                    String refFullName = (String) obj.get("ref");
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
                    type = pkg + cn.toUpperCase().charAt(0) + cn.substring(1);
//                type = "ref#" + obj.get("ref");
                    System.out.println("  " + type);
                    break;
                case "unknown":
                case "object":
                    return "Object";
                default:
                    break;
            }
        }
        return at + type;
    }

    String tab(String src) {
        return src.replace("\r\n", "\r\n    ");
    }

    public static void main(String[] argv) throws IOException {
        ConvertFilter convert = new ConvertFilter("net.siisise.bsky");
        File lexicons = new File("c:/home/git/atproto/lexicons");
        File jsrc = new File("c:/home/git/atproto/src/java/net/siisise/bsky");
        convert.toJava(lexicons, jsrc);

    }
}
