/* LAN0 - 2024-03-12
  Clase utilities que importo en la mayoria de mis proyectos con utilidades varias
*/

/*
  2024-03-12 -  getProperties
                getCfgValue
                
*/

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MyUtils {

    public static Properties getProperties(String contextPath) {

        String cfgFileName = contextPath + "/WEB-INF/main.properties";
        String globalCfgFileName = contextPath + "/../Guc.properties";

        File fGlobal = new File(globalCfgFileName);
        if (fGlobal.exists()) {
            log.info("Encontrado el fichero de configuracion : " + fGlobal.getAbsolutePath());
            cfgFileName = globalCfgFileName;
        }

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(cfgFileName));
            return properties;
        } catch (IOException e) {
            return null;
        }
    }
  
    public static String getCfgValue(String keyList, Properties cfgFileName, String defaultValue) {

        String resultado = null;

        try {

            String[] keys = keyList.split("\t");

            for (int i = 0; i < keys.length; i++) {

                // log.info("Obteniendo el valor de la variable de entorno : " + keys[i]);
                String myKey = keys[i];

                resultado = System.getenv(keys[i]);

                if (resultado == null) {
                    // String myVariable = keys[i].replaceAll(".", "_");
                    resultado = System.getenv(keys[i].replaceAll("\\.", "_"));
                    // resultado = System.getenv(myVariable);
                }

                if (resultado == null) {
                    log.debug("La variable de entorno : " + keys[i] + " no ha podido obtenerse. Obtenemos del fichero de configuración");
                    resultado = cfgFileName.getProperty(keys[i], defaultValue);
                }

                if (resultado != null) {
                    break;
                }
            }

        } catch (Exception ex) {

            log.error(ex.getMessage());

        }


        return resultado;
    }

    public static String getCfgValue(String keyList, Properties cfgFileName) {
        return getCfgValue(keyList, cfgFileName, null);
    }

    public static String changeExtension(String fileName, String extension) {

        String newFileName = "";

        if (fileName.contains(".")) {
            newFileName = fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            newFileName = fileName;
        }

        newFileName += "." + extension;

        return newFileName;
    }
    
    public static String ASCIIEncrypt(String Data) {

        StringBuilder resultado = new StringBuilder();

        String Cipher = "GucServices69";
        int Z;
        char C;
        short Code;
        int buffer;

        if (Data.length() > 0) {

            Z = Cipher.length();


            for (int i = 0; i < Data.length(); i++) {
                //Code = (short) Cipher.charAt((i-1) % Z + 1);

                Code = (short) Cipher.charAt((i) % Z);

                if ((int) Data.charAt(i) >= 128) {
                    buffer = (int) Data.charAt(i) ^ (Code & 0x7F);
                    C = (char) buffer;
                } else if ((int) Data.charAt(i) >= 64) {
                    buffer = (int) Data.charAt(i) ^ (Code & 0x3F);
                    C = (char) buffer;
                } else if ((int) Data.charAt(i) >= 32) {
                    buffer = (int) Data.charAt(i) ^ (Code & 0x1F);
                    C = (char) buffer;
                } else {
                    C = Data.charAt(i);
                }
                resultado.append(C);
            }

        }
        return resultado.toString();
    }

    public static Calendar DateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static long secondOfDay(Calendar c) {

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int secons = c.get(Calendar.SECOND);

        return (hour * 3600) + (minute * 60) + secons;
    }

    public static long secondOfDay(Date date) {

        Calendar c = DateToCalendar(date);
        return secondOfDay(c);
    }

    public static long compareTimes(Date d1, Date d2) {

        Calendar c1 = DateToCalendar(d1);
        Calendar c2 = DateToCalendar(d2);

        long secondOfDay1 = secondOfDay(c1);
        long secondOfDay2 = secondOfDay(c2);

        return secondOfDay1 - secondOfDay2;
    }

    public static String timesDifference(Date d1, Date d2) {

        long durationInMillis = d2.getTime() - d1.getTime();

        int seconds = (int) (durationInMillis / 1000) % 60;
        int minutes = (int) ((durationInMillis / (1000 * 60)) % 60);
        int hours = (int) ((durationInMillis / (1000 * 60 * 60)) % 24);

        return String.format("%02d:%02d:%02d%n", hours, minutes, seconds);
    }

    public static long secondsBetween(Date d1, Date d2) {

        long durationInMillis = d2.getTime() - d1.getTime();
        return durationInMillis / 1000;

    }

    public static String formatDateTime(String format, Date date) {

        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static Date addTimeToDate(Date myDate, String tipo, int total) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);

        if (tipo.contains("MINUTE")) {
            calendar.add(Calendar.MINUTE, total);
        } else if (tipo.contains("SECOND")) {
            calendar.add(Calendar.SECOND, total);
        } else if (tipo.contains("HOUR")) {
            calendar.add(Calendar.HOUR, total);
        }

        return calendar.getTime();
    }

    public static String timeToc(int idioma) {

        String resultado = "";

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);

        if (hour < 14) {

            switch (idioma) {
                case 0:
                    resultado = "Buenos días";
                    break;
                case 1:
                    resultado = "Bon dia";
                    break;

            }

        } else if (hour > 21) {

            switch (idioma) {
                case 0:
                    resultado = "Buenas noches";
                    break;
                case 1:
                    resultado = "Bona nit";
                    break;

            }
        } else {

            switch (idioma) {
                case 0:
                    resultado = "Buenas tardes";
                    break;
                case 1:
                    resultado = "Bona tarda";
                    break;


            }
        }

        return resultado;

    }

    public static HashMap<String, String> sortByValue(Map<String, String> unsortMap, final boolean order) {

        List<Entry<String, String>> list = new LinkedList<Entry<String, String>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, String>>() {
            public int compare(Entry<String, String> o1,
                    Entry<String, String> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, String> sortedMap = new LinkedHashMap<String, String>();
        for (Entry<String, String> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }

    public static ByteArrayOutputStream fileToByteArray(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return bos;
    }

    public static String byteArrayToFile(ByteArrayOutputStream file, String fileName) {

        try {
            FileOutputStream fos = new FileOutputStream(new File(fileName));

            file.writeTo(fos);
        } catch (IOException ex) {
            // Handle exception here
            log.error(ex.getMessage());
        }
        return fileName;
    }

    public static String decode(String s) {
        return StringUtils.newStringUtf8(Base64.decodeBase64(s));
    }

    public static String encode(String s) {
        return Base64.encodeBase64String(StringUtils.getBytesUtf8(s));
    }

    // Creacion Objeto JSon 
    public static JSONObject getJsonObject(String action, String actionId, String connectionId, InfoJsonBean info, String sessionId) {

        JSONObject obj = new JSONObject();

        obj.put("actionId", actionId);
        obj.put("connectionId", connectionId);
        obj.put("action", action);
        obj.put("sessionId", sessionId);

        try {

            if (info != null) {

                obj.put("resultCode", new Integer(info.resultCode));
                obj.put("resultDescription", info.resultDescription);

                if (info.datos != null) {
                    for (Map.Entry<String, Object> entry : info.datos.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        obj.put(key, value);
                    }
                }

            } else {
                obj.put("resultCode", "0");
                obj.put("resultDescription", "");
            }

        } catch (Exception ex) {

            log.error(ex.toString());

        }

        return obj;
    }

    public static String[] parseLine(String text, String separator) {
        return text.split(separator);
    }

    public static String quotedStr(String text, String separator) {
        String resultado = "";

        String[] slText = parseLine(text, separator);

        for (int nConta = 0; nConta <= slText.length - 1; nConta++) {
            resultado += "'" + slText[nConta] + "',";
        }

        resultado = resultado.substring(0, resultado.length() - 1);

        return resultado;
    }

    public static String getCfgValue(ServletConfig config, String key) {

        // Read properties file.
        String cfgFileName = config.getServletContext().getRealPath(MyUtils.CFG_FILE_NAME);


        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(cfgFileName));
        } catch (IOException e) {
            log.error("getCfgValue : " + e.getMessage());
        }

        String resultado = properties.getProperty(key, "");
        return resultado;
    }

    public static boolean telefonoOk(String telefono) {

        boolean resultado = false;

        if (telefono != null) {

            if (telefono.matches("[0-9]+") && telefono.length() == 9 && !telefono.startsWith("99")) {

                if (!telefono.equals("999999999")) {


                    resultado = telefono.startsWith("6")
                            || telefono.startsWith("7")
                            || telefono.startsWith("8")
                            || telefono.startsWith("9");
                }
            }
        }

        return resultado;
    }

    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            // System.out.println("encrypted string: " + Base64.encodeBase64String(encrypted));

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("MyUtils.encrypt : " + ex.getMessage());
        }

        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("MyUtils.decrypt : " + ex.getMessage());
        }

        return null;
    }

    public static String getFileNameFromPath(String pathFileName) {

        File downloadFile1 = new File(pathFileName);
        String fileName = downloadFile1.getName();

        return fileName;
    }    
    
    
  
}
