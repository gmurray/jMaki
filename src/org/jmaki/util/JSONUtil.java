package org.jmaki.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class JSONUtil {
   /**
    * Converts a JSON Object to an Object Literal
    *
    *
    * @param jo
    * @param buff
    *
    * @return Object literal
    *
    * @throws JSONException
    */
   public static String jsonToObjectLiteral(JSONObject jo, StringBuffer buff)
           throws JSONException
   {
       if (buff == null) {
           buff = new StringBuffer("{");
       } else {
           buff.append("{");
       }

       JSONArray names = jo.names();

       for (int l = 0; (names != null) && (l < names.length()); l++) {
           String key   = names.getString(l);
           String value = null;

           if (jo.optJSONObject(key) != null) {
               value = key + ":";
               buff.append(value);
               jsonToObjectLiteral(jo.optJSONObject(key), buff);
           } else if (jo.optJSONArray(key) != null) {
               value = key + ":";
               buff.append(value);
               jsonArrayToString(jo.optJSONArray(key), buff);
           } else if (jo.optLong(key, -1) != -1) {
               value = key + ":" + jo.get(key) + "";
               buff.append(value);
           } else if (jo.optDouble(key, -1) != -1) {
               value = key + ":" + jo.get(key) + "";
               buff.append(value);
           } else if (jo.opt(key) != null) {
               Object obj = jo.opt(key);

               if (obj instanceof Boolean) {
                   value = key + ":" + jo.getBoolean(key) + "";
               } else {
                   value = key + ":" + "'" + jo.get(key) + "'";
               }

               buff.append(value);
           }

           if (l < names.length() - 1) {
               buff.append(",");
           }
       }

       buff.append("}");

       return buff.toString();
   }

   /**
    * Converts a json array to string.
    *
    *
    * @param ja
    * @param buff
    *
    * @return string of the array
    *
    * @throws JSONException
    */
   public static String jsonArrayToString(JSONArray ja, StringBuffer buff)
           throws JSONException
   {
       if (buff == null) {
           buff = new StringBuffer("[");
       } else {
           buff.append("[");
       }

       for (int key = 0; (ja != null) && (key < ja.length()); key++) {
           String value = null;

           if (ja.optJSONObject(key) != null) {
               jsonToObjectLiteral(ja.optJSONObject(key), buff);
           } else if (ja.optJSONArray(key) != null) {
               jsonArrayToString(ja.optJSONArray(key), buff);
           } else if (ja.optLong(key, -1) != -1) {
               value = ja.get(key) + "";
               buff.append(value);
           } else if (ja.optDouble(key, -1) != -1) {
               value = ja.get(key) + "";
               buff.append(value);
           } else if (ja.optBoolean(key)) {
               value = ja.getBoolean(key) + "";
               buff.append(value);
           } else if (ja.opt(key) != null) {
               Object obj = ja.opt(key);

               if (obj instanceof Boolean) {
                   value = ja.getBoolean(key) + "";
               } else {
                   value = "'" + ja.get(key) + "'";
               }

               buff.append(value);
           }

           if (key < ja.length() - 1) {
               buff.append(",");
           }
       }

       buff.append("]");

       return buff.toString();
   }
   
   public static JSONObject loadFromInputStream(InputStream in) {
       if (in == null) {
           return null;
       }
       ByteArrayOutputStream out = null;
       try {

           byte[] buffer = new byte[1024];
           int read = 0;
           out = new ByteArrayOutputStream();
           while (true) {
               read = in.read(buffer);
               if (read <= 0)
                   break;
               out.write(buffer, 0, read);
           }
           return new JSONObject(out.toString());
       } catch (Exception e) {
           getLogger().severe("Error reading in JSON from stream : " + e);
       } finally {
           try {
               if (in != null) {
                   in.close();
               }
               if (out != null) {
                   out.flush();
                   out.close();
               }
           } catch (Exception e) {
           }
       }
       return null;
   }
   
   public static JSONObject loadFromFile(String fileName) {
       try {
           File f = new File(fileName);
           FileInputStream in = new FileInputStream(f);
           return loadFromInputStream(in);
       } catch (Exception e) {
           getLogger().severe("Error loading JSON from file " + e);
       }
       return null;
   }

   private static Logger logger;

   public static Logger getLogger() {
       if (logger == null) {
           logger = Logger.getLogger("org.jmaki");
       }
       return logger;
   }
}
