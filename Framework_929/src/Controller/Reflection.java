/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

/**
 *
 * @author Franco
 */
public class Reflection {

    public static Method[] getMethods(Object o, String type) {
        Class cl = o.getClass();
        Method[] mthds = cl.getDeclaredMethods();
        String[] text = new String[getFields(o, type).length];
        int same = 0;

        for (int n = 0; n < getFields(o, type).length; n++) {
            text[n] = getFields(o, type)[n].getName();
            text[n] = upperFirstChar(text[n]);
            text[n] = type + text[n];
        }
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < mthds.length; j++) {
                if (text[i].compareTo(mthds[j].getName()) == 0) {
                    same++;
                }
            }
        }
        Method[] mthds2 = new Method[same];
        int same1 = same;
        while (same > 0) {
            for (int i = 0; i < text.length; i++) {
                for (int j = 0; j < mthds.length; j++) {
                    if (text[i].compareTo(mthds[j].getName()) == 0) {
                        mthds2[same1 - same] = mthds[j];
                        same--;
                    }
                }
            }
        }
        return mthds2;
    }

    public static Method getMethod(Class cl, Field arg, String type) {
        Method result = null;
        Method[] mthds = cl.getDeclaredMethods();
        String methd = "";
        methd = arg.getName();
        methd = upperFirstChar(methd);
        methd = type + methd;

        for (int j = 0; j < mthds.length; j++) {
            if (methd.compareTo(mthds[j].getName()) == 0) {
                result = mthds[j];
                return result;
            }
        }
        return result;
    }

    public static Vector getValues(Object o) throws InvocationTargetException {
        Method[] meth = getMethods(o, "get");
        Vector vect = new Vector();
        for (int n = 0; n < meth.length; n++) {
            try {
                if (meth[n].invoke(o) != null) {
                    vect.add(meth[n].invoke(o));
                } else {
                    vect.add("");
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                continue;
            }
        }
        return vect;
    }

    public static Field[] getFields(Object o, String type) {
        Class cl = o.getClass();
        Method[] mthds = cl.getDeclaredMethods();
        String[] text = new String[cl.getDeclaredFields().length];
        Field[] text1 = cl.getDeclaredFields();
        int same = 0;
        for (int n = 0; n < cl.getDeclaredFields().length; n++) {
            text[n] = cl.getDeclaredFields()[n].getName();
            text[n] = upperFirstChar(text[n]);
            text[n] = type + text[n];
        }
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < mthds.length; j++) {
                if (text[i].compareTo(mthds[j].getName()) == 0) {
                    same++;
                }
            }
        }
        Field[] result = new Field[same];
        int same1 = same;
        while (same > 0) {
            for (int i = 0; i < text.length; i++) {
                for (int j = 0; j < mthds.length; j++) {
                    if (text[i].compareTo(mthds[j].getName()) == 0) {
                        result[same1 - same] = text1[i];
                        same--;
                    }
                }
            }
        }
        return result;
    }

    public static String upperFirstChar(String word) {
        String first = word.substring(0, 1);
        String next = word.substring(1);
        first = first.toUpperCase();
        return first + next;
    }
}
