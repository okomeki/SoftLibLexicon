package net.siisise.atp.lexicon;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import net.siisise.io.FileIO;
import net.siisise.json.JSON;
import net.siisise.json.JSONArray;
import net.siisise.json.JSONObject;
import net.siisise.json.JSONPointer;

/**
 *
 */
public class ConvertFilter {
    
    
    public String toJava(File lexicon, File jsrc) throws IOException {
        StringBuilder cls = new StringBuilder(100);
        if ( lexicon.isDirectory() ) {
//            if ( !jsrc.exists() ) {
//                jsrc.mkdirs();
//            }
            
            boolean v = false;
            cls.append("public class ").append(lexicon.getName()).append(" {\r\n");
            File[] subs = lexicon.listFiles();
            for ( File sub : subs ) {
                File j = new File(jsrc, sub.getName());
                String m = toJava(sub,j);
                if ( m != null ) {
                    v = true;
                    cls.append(m );
                }
            }
            if (v) {
                cls.append("\r\n}\r\n");
                System.out.println(cls.toString());
            }
        } else if ( lexicon.isFile() ) {
            File jd = jsrc.getParentFile();
            String sname = lexicon.getName();
            if (sname.endsWith(".json")) {
                JSONObject lex = (JSONObject) JSON.parse(FileIO.binRead(lexicon));
                String jname = sname.substring(0, sname.length() - 5) + ".java";
                File jfile = new File(jd, jname);
                cls.append(tab(toJava(lex, jfile)));
            }
            return cls.toString();
        }
        return null;
    }
    
    JSONPointer mainp = new JSONPointer("/defs/main");

    String id;

    public String toJava(JSONObject lexicon, File jfile) {
        System.out.print("Lexicon ");
        System.out.println(lexicon.toJSON(JSON.TAB_MINESC));
        id = (String) lexicon.get("id");
        StringBuilder cls = new StringBuilder();
        JSONObject main = (JSONObject) mainp.get(lexicon);
        
        JSONObject defs = (JSONObject) lexicon.get("defs");
        Set<String> defsNames = defs.keySet();
        for ( String defName : defsNames ) {
            JSONObject def = (JSONObject) defs.get(defName);
            if ( "main".equals(defName)) {
                cls.append(method(def));
            } else {
                cls.append(def(defName, def));
            }
        }
        
        if ( main != null ) {
//            cls.append("class ").append(name).append(" {\r\n");
            cls.append(method(main));
//            cls.append("\r\n}\r\n");
//            System.out.println(main.toJSON(JSON.TAB_MINESC));
//            System.out.println(cls.toString());
        }
        return cls.toString();
    }
    
    String def(String name, JSONObject def) {
        StringBuilder cls = new StringBuilder();
        String type = (String) def.get("type");
        if ( type.equals("object")) {
            return obj(name, def);
        }
        
        cls.append("static ");
        cls.append( typeConvert(def) );
        cls.append(" ").append(name);
        cls.append(";");
        return cls.toString();
    }
    
    String obj(String name, JSONObject obj) {
        StringBuilder cls = new StringBuilder();
        JSONObject properties = (JSONObject) obj.get("properties");

        cls.append("\r\npublic static class ");
        cls.append(name.toUpperCase().charAt(0));
        cls.append(name.substring(1));
        cls.append(" {");

        Set<String> pnames = properties.keySet();
        for ( String pname : pnames ) {
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
    
    String method(JSONObject main) {
        StringBuilder code = new StringBuilder();
        String type = (String) main.get("type");
        String description = (String) main.get("description");
        
        String[] names = id.split("\\.");
        String mname = names[names.length - 1];
        
        StringBuilder comment = new StringBuilder();

        if ( description != null ) {
            comment.append("\r\n * ");
            comment.append(description);
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
            if ( "application/json".equals(encoding)) {
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
        
        if ( parameters == null ) {
            parameters = new JSONObject();
            parameters.addJSON("properties", new JSONObject());
        }
        
        JSONArray required = (JSONArray) parameters.getJSON("required");
        JSONObject properties = (JSONObject) parameters.getJSON("properties");
        Set<String> ps = properties.keySet();

        method.append("    ");
        if ( output != null) {
            method.append("return ");
        }
        method.append("service");
        
        for ( String p : ps ) {
            JSONObject prop = (JSONObject)properties.getJSON(p);
            String description = (String) prop.get("description");
            if ( description != null ) {
                comment.append("\r\n * @param ").append(p).append(' ').append(description);
            }

            params.append(", ").append(typeConvert(prop))
                    .append(" ").append(p);

            method.append(".");
            if ( required != null && required.contains(p)) {
                method.append("req(\"");
            } else {
                method.append("opt(\"");
            }
            method.append(p);
            method.append("\", ");
            method.append(p);
            method.append(")\r\n        ");
        }

        code.append("\r\n\r\n/**");
        comment.append("\r\n */");
        code.append(comment.toString());
        code.append("\r\n");
        code.append("public static ");

        if ( output == null ) {
            code.append("void ");
        } else {
            code.append("JSONObject ");
        }
        
        code.append(name).append("(XRPC service");
        
        code.append(params.toString());
        code.append(") {\r\n");
        code.append(method.toString());

        code.append(".");

        code.append(type);
        code.append("(\"");
        code.append(id);
        code.append("\");");
    }
    
    String typeConvert(JSONObject obj) {
        String type = (String) obj.get("type");
        String format = (String) obj.get("format");
        
        String at;
        
        if ( format != null ) {
            at = "@format(\""+ format + "\") ";
        } else {
            at = "";
        }
        if (null != type) switch (type) {
            case "integer":
                type = "int";
                break;
            case "string":
                type = "String";
                break;
            case "bytes":
                type = "byte[]";
                break;
            case "array":
                type = typeConvert((JSONObject) obj.getJSON("items")) +"[]";
                break;
            case "ref":
                String refName = (String) obj.get("ref");
                type = refName.toUpperCase().charAt(1) + refName.substring(2);
//                type = "ref#" + obj.get("ref");
                break;
            case "object":
                return "Object";
            default:
                break;
        }
        return at + type;
    }
    
    String tab(String src) {
        return src.replace("\r\n", "\r\n    ");
    }


    public static void main(String[] argv) throws IOException {
        ConvertFilter convert = new ConvertFilter();
        File lexicons = new File("c:/home/git/atproto/lexicons");
        File jsrc = new File("c:/home/git/atproto/src/java");
        convert.toJava(lexicons, jsrc);
        
    }
}
