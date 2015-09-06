package com.tonini.diego.musicat.entity;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Diego on 06/09/2015.
 */
public class SimpleServlet extends HttpServlet {

    private static final long serialVersionUID = -621262173529602L;

    /**
     * This method is called when the user enters the URL in the
     * browser.
     */
    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out=resp.getWriter();
        out.println("<html>" +
                "<title>Simple Form</title>" +
                "<body>" +
                "<form method='post'>" +
                "<input type='text' name='user_name'/><br/>" +
                "<input type='submit' value='Go'/>" +
                "</form>" +
                "</body>" +
                "</html>");


    }

    /**
     * This method is called when the 'Go' button is pressed
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out=resp.getWriter();

        String username=req.getParameter("user_name");

        if(username==null || username.equals(""))
            username="<NO_NAME_SPECIFIED>";

        out.println("<html>" +
                "<title>Welcome</title>" +
                "<body>" +
                "Welcome " + username +
                "</body>" +
                "</html>");
    }

}
