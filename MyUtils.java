/* LAN0 - 2024-03-12
  Clase utilities que importo en la mayoria de mis proyectos con utilidades varias
*/

/*
  2024-03-12 - getProperties
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

}
