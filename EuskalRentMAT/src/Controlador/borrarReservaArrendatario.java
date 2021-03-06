/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entidades.Alquiler;
import Modelo.Entidades.Reserva;
import Modelo.Entidades.Usuario;
import Modelo.Listas.ListaReservas;
import Modelo.conexionBD.ConexionBD;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author joseba
 */
public class borrarReservaArrendatario extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            ConexionBD con = ConexionBD.getConexionBD();
            int id = Integer.parseInt(request.getParameter("idReserva"));
            Usuario usuario = (Usuario)request.getSession().getAttribute("usuario"); 
            Alquiler alquiler = con.getAlquilerDeReserva(id);
            ListaReservas ls = con.getListaReservas(usuario.getIdUsuario());
            Reserva reserva = ls.buscarReserva(id);
            Date entrada = reserva.getFechaEntrada();
            Date hoy = new Date();
            int numDias = (int)((entrada.getTime() - hoy.getTime()) / (1000*60*60*24l));
            float precio = alquiler.getPrecioTotal(reserva.getFechaEntrada(), reserva.getFechaSalida());
            String cancelacion = alquiler.getAlojamiento().getTipoCancelacion();
            
            
            if(cancelacion.equals("Gratuita")){
                usuario.setSaldo(precio + usuario.getSaldo());
                con.actualizarSaldo(usuario.getIdUsuario(), usuario.getSaldo());
                //  Quitamos el dinero al dueño
                con.actualizarSaldoporAlquiler(alquiler.getIdAlquiler(),-(precio));
            } else if ( cancelacion.equals("Flexible")){
                 if (numDias <= 2){
                    usuario.setSaldo((precio/2) + usuario.getSaldo());
                    con.actualizarSaldo(usuario.getIdUsuario(), usuario.getSaldo());
                     //  Quitamos el dinero al dueño
                    con.actualizarSaldoporAlquiler(alquiler.getIdAlquiler(),-(precio/2));
                 } else{
                    usuario.setSaldo(precio + usuario.getSaldo());
                    con.actualizarSaldo(usuario.getIdUsuario(), usuario.getSaldo());
                    //  Quitamos el dinero al dueño
                    con.actualizarSaldoporAlquiler(alquiler.getIdAlquiler(),-(precio));
                 }
            } 
            
            
            
            
            int lsize = ls.getNumeroReservas();
            ls.borrarReserva(id);
            con.borrarReserva(id);
            
            request.getRequestDispatcher("perfil.jsp").forward(request, response);
        } finally {
            out.close();
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
