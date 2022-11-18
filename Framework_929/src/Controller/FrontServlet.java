/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Franco
 */
public class FrontServlet extends HttpServlet {

    @Override
    public void init() {
        String[] packageNames = null;
        packageNames = this.getInitParameter("Package").split("/");
        try {
            this.getServletContext().setAttribute("link", Utilitaire.MappingLinks(packageNames));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            try {

                String url = Utilitaire.retriveUrlFromRawUrl(request.getRequestURI());
                //out.println("Url="+url);
                HashMap<String, HashMap<String, String>> link = (HashMap<String, HashMap<String, String>>) this.getServletContext().getAttribute("link");
                Utilitaire.TestUrl(url, link);
                HashMap<String, String> clsMth = link.get(url);

                ModelView resultat = new ModelView();

                Class cls = Class.forName(clsMth.get("class"));
                Method mth = cls.getDeclaredMethod(clsMth.get("method"));
                Object obj = cls.newInstance();
                Field[] flds = Reflection.getFields(obj, "get");

                Enumeration listAttr = request.getParameterNames();
                Vector<String> listAttribute = new Vector<String>(1, 1);
                while (listAttr.hasMoreElements()) {
                    listAttribute.add((String) listAttr.nextElement());
                }
                if (listAttribute.size() > 0) {
                    for (String attr : listAttribute) {
                        try {

                            Field fld = cls.getDeclaredField(attr);
                            String attrVal = request.getParameter(attr);
                            Method set = Reflection.getMethod(cls, fld, "set");
                            if (fld.getType().getName().equalsIgnoreCase("java.lang.String")) {
                                set.invoke(obj, fld.getType().cast(attrVal));
                            }
                            if (fld.getType().isPrimitive()) {
                                if (fld.getType().getName().equalsIgnoreCase("int")) {
                                    try {
                                        set.invoke(obj, Integer.parseInt(attrVal));
                                    } catch (Exception e) {
                                        out.println("incorrect");
                                    }
                                } else if (fld.getType().getName().equalsIgnoreCase("float")) {
                                    try {
                                        set.invoke(obj, Float.parseFloat(attrVal));
                                    } catch (Exception e) {
                                        out.println("incorrect");
                                    }
                                } else if (fld.getType().getName().equalsIgnoreCase("double")) {
                                    try {
                                        set.invoke(obj, Double.parseDouble(attrVal));
                                    } catch (Exception e) {
                                        out.println("incorrect");
                                    }
                                }
                            }
                        } catch (NoSuchFieldException ex) {

                            out.println(ex);
                            continue;
                        }
                    }
                }

                resultat = (ModelView) mth.invoke(obj);

                HashMap<String, Object> data = resultat.getData();
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    request.setAttribute(key, value);
                }
                String page = (resultat.getUrl());
                request.getRequestDispatcher("/" + page).forward(request, response);

            } catch (Exception e) {
                response.setContentType("text/html;charset=UTF-8");
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Error</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>error: " + e.toString() + "</h1>");
                out.println("</body>");
                out.println("</html>");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
