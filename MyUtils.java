/* LAN0 - 2024-03-12
  Clase utilities que importo en la mayoria de mis proyectos con utilidades varias
*/

/*
  2024-03-12 -  getProperties
                getCfgValue
                
*/

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
  
}
