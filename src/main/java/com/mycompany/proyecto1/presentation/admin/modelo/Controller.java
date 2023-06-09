/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.proyecto1.presentation.admin.modelo;

import com.mycompany.proyecto1.logic.Marca;
import com.mycompany.proyecto1.logic.Modelo;
import com.mycompany.proyecto1.logic.Service;
import com.mycompany.proyecto1.logic.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kevin
 */
@MultipartConfig
@WebServlet(name = "ModeloController", urlPatterns = {"/presentation/admin/modelo/show","/presentation/admin/modelo/create",
                                                      "/presentation/admin/modelo/getImage"})
public class Controller extends HttpServlet {
    protected void processRequest(HttpServletRequest request, 
                                HttpServletResponse response)
            throws ServletException, IOException {
      
        request.setAttribute("model", new Model()); 
        
        String viewUrl="";
        switch(request.getServletPath()){
            case "/presentation/admin/modelo/show":
                viewUrl=this.show(request);
                break;  
            case "/presentation/admin/modelo/create":
                viewUrl=this.create(request);
                break;
            case "/presentation/admin/modelo/getImage":
                this.getImage(request,response);
                break;
     
                
        }
        request.getRequestDispatcher(viewUrl).forward( request, response); 
  }
    
    public static final String LOCATION="C:/AAA/seguros/";

    private void postImage(HttpServletRequest request, Integer id){
        try{
            final Part imagen = request.getPart("imagen");
            InputStream is = imagen.getInputStream();
            FileOutputStream os = new FileOutputStream(LOCATION + id);
            is.transferTo(os);
            os.close();
        }
        catch(Exception ex){}
    }
    private void getImage(HttpServletRequest request,HttpServletResponse response){
    String id=request.getParameter("id");
    try{
        ServletOutputStream os=response.getOutputStream();
        FileInputStream is=new FileInputStream(LOCATION+id);
        is.transferTo(os);
        os.close();
    }catch(IOException ex){

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

    private String show(HttpServletRequest request) {
       return this.showAction(request);
    }


    private String showAction(HttpServletRequest request) {
        Model model = (Model) request.getAttribute("model");
        Service service = Service.instance();
        HttpSession session = request.getSession(true);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Marca> marcas;
        try {
            marcas = service.marcasFind();
        } catch (Exception ex) {
            marcas=null;
        }
        try {        
            model.setMarcas(marcas);
            return "/presentation/admin/modelo/View.jsp";
        } catch (Exception ex) {
            return "";
        }
    }

    private String create(HttpServletRequest request) {
        try{
       Model model = (Model) request.getAttribute("model");
       Map<String,String> errores =  this.validar(request);
       if(errores.isEmpty()){
                this.updateModel(request);          
                return this.updateAction(request);
            }
            else{
                request.setAttribute("errores", errores);
                return "/presentation/Error.jsp";
            }            
        }
        catch(Exception e){
            return "/presentation/Error.jsp";             
        } 
    }

    private Map<String, String> validar(HttpServletRequest request) {
        Map<String,String> errores = new HashMap<>();
        if (request.getParameter("descripcionFld").isEmpty()){
            errores.put("descripcionFld","Modelo requerido");
        }
        return errores;
    }

    private void updateModel(HttpServletRequest request) {
        Model model= (Model) request.getAttribute("model");
        Service  service = Service.instance();
        
        model.getCurrent().getMarca().setId(Integer.valueOf(request.getParameter("marcaFld")));
        model.getCurrent().setDescripcion(request.getParameter("descripcionFld"));
        
        // Guardar imagen en el servidor
        //Integer id = model.getCurrent().getId();
        //postImage(request, id);
    }

    private String updateAction(HttpServletRequest request) {
        Model model= (Model) request.getAttribute("model");
        Service  service = Service.instance();
        Modelo modelo = model.getCurrent();
        try {
            model.setMarcas(service.marcasFind());
            service.modeloCreate(modelo);
            Modelo generatedkey = service.modeloFindByNombre(modelo.getDescripcion());
            postImage(request, generatedkey.getId());
            
            return "/presentation/admin/modelos/show";
        } catch (Exception ex) {
            Map<String,String> errores = new HashMap<>();
            request.setAttribute("errores", errores);
            errores.put("marcaFld","nombre incorrecto");
            return "/presentation/admin/modelos/View.jsp"; 
        }
    }

}
