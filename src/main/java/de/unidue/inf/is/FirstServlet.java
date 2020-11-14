package de.unidue.inf.is;

import de.unidue.inf.is.domain.Kommentar;
import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.stores.ProjektStore;
import de.unidue.inf.is.utils.DBUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FirstServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static List<Projekt> projektList = new ArrayList<>();
    private static ProjektStore projektStore = new ProjektStore();
    private static String errorMessage = "";
    private static String tas;
    private static Integer kenn = null;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("error", errorMessage);

        request.getRequestDispatcher("/login_page.ftl").forward(request, response); }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String username = request.getParameter("username");
        if ((username == null) || (username.isEmpty()))
        {
            errorMessage = "Falls Sie kein Konto haben, können Sie eins öffnen!";
            doGet(request, response);
        }
        else
        {
            errorMessage = username;
            DBUtil.derBenutzer = username;
            request.setAttribute("nachricht", errorMessage);

            //request.getRequestDispatcher("/inzwischen.ftl").forward(request, response);
            //request.getRequestDispatcher("/view_main.ftl").forward(request, response);
            MainPageServlet mainPageServlet = new MainPageServlet();
            mainPageServlet.doGet(request, response);
        }

    }
}
