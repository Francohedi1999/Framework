/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.FrontServlet;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author Franco
 */
public class Utilitaire {

    public static String retriveUrlFromRawUrl(String url) {
        String[] parseUrl;
        parseUrl = url.split("/");
        String UrlTraite = parseUrl[parseUrl.length - 1];
        return UrlTraite.replace(".", "-").split("-")[0];
    }

    public static HashMap<String, HashMap<String, String>> MappingLinks(String[] listepkgs) throws PackageNotExist, Exception {
        HashMap<String, HashMap<String, String>> links = new HashMap<String, HashMap<String, String>>();
        for (String pkg : listepkgs) {
            Vector<Class> listeClass;
            try {
                listeClass = getClass(pkg);
            } catch (PackageNotExist e) {
                continue;
            }
            for (Class cl : listeClass) {
                Method[] listeMethod = cl.getDeclaredMethods();
                for (Method method : listeMethod) {
                    if (method.isAnnotationPresent(Url_annotation.class)) {
                        HashMap<String, String> classMeth = new HashMap<>();
                        classMeth.put("class", cl.getName());
                        classMeth.put("method", method.getName());
                        links.put(method.getAnnotation(Url_annotation.class).name(), classMeth);

                    }

                }
            }
        }
        return links;
    }

    public static Vector<Class> getClass(String namepkg) throws PackageNotExist, Exception {
        Vector<Class> classe = new Vector<Class>(1, 1);
        URL root = Thread.currentThread().getContextClassLoader().getResource(namepkg.replace(".", "/"));
        File fichier = null;

        try {
            fichier = new File(root.getFile());
        } catch (NullPointerException e) {
            throw new PackageNotExist();
        }
        FilenameFilter fltr = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                try {
                    return name.endsWith(".class");
                } catch (Exception e) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

                }
            }
        };
        File[] files = fichier.listFiles(fltr);
        for (File file : files) {
            String className = file.getName().replaceAll(".class$", "");
            Class<?> cls = Class.forName(namepkg + "." + className);
            boolean cond = !cls.equals(FrontServlet.class) || !cls.equals(ModelView.class);
            if (cond) {
                classe.add(cls);
            }
        }
        return classe;
    }

    public static void TestUrl(String url, HashMap<String, HashMap<String, String>> urlLink) throws UrlNotSupportedException {
        if (!urlLink.containsKey(url)) {
            throw new UrlNotSupportedException("URL not supported");
        }
    }
}
