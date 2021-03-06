/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entidades.Reserva;
import Modelo.conexionBD.ConexionBD;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import Modelo.Listas.ListaReservas;
import Modelo.Entidades.Usuario;

/**
 *
 * @author joseba
 */
public class updateReserva extends HttpServlet {

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
            Date fechaReserva = (Date)request.getSession().getAttribute("fechaReserva");
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            int idReserva = Integer.parseInt(request.getParameter("idReserva"));
            int idUsuario = usuario.getIdUsuario();
            int idAlquiler = Integer.parseInt(request.getParameter("idAlquiler"));
            
            String fechaEntrada = request.getParameter("fechaEntrada");
            String fechaSalida = request.getParameter("fechaSalida");
           
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-mm-dd");
            try{
            java.util.Date dateIn= formato.parse(fechaEntrada);
            java.util.Date dateOut = formato.parse(fechaSalida);
            
            java.sql.Date dateEntrada = new java.sql.Date(dateIn.getTime());
            java.sql.Date dateSalida = new java.sql.Date(dateOut.getTime());
            Reserva reserva = new Reserva(idReserva, idAlquiler, fechaReserva, dateEntrada, dateSalida);
            con.actualizarReserva(reserva);
            ListaReservas ls = con.getListaReservas(idUsuario);
            usuario.setListaReservas(ls);
            
            
            request.getRequestDispatcher("perfil.jsp").forward(request, response);
            }
            catch(Exception e){
                e.printStackTrace();
            }
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
