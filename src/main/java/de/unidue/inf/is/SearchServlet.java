package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.ProjektStore;
import de.unidue.inf.is.utils.DBUtil;


public final class SearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static List<Projekt> projektList = new ArrayList<>();
    private static List<Projekt> projektList2 = new ArrayList<>();
    private static ProjektStore projektStore = new ProjektStore();
    private static String errormessage = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        projektList2.clear();
        if (projektList != null) {
            request.setAttribute("aufprojekte", projektList);
            request.setAttribute("error", errormessage);
            errormessage = "";
        }
        else
        {
            request.setAttribute("aufprojekte", projektList2);
            request.setAttribute("error", errormessage);
            errormessage = "";
        }


        request.getRequestDispatcher("/search.ftl").forward(request, response); }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String suchWort = request.getParameter("projektname");
        if ((suchWort == null) || (suchWort.isEmpty()))
        {
            errormessage = "Suchen Sie nix?!";
            projektList.clear();
            doGet(request, response);
        }
        else
        {
            String suchWort1 = suchWort.toLowerCase();
            String suchWort2 = suchWort.toUpperCase();
            projektList = projektStore.findenProjekteDurchSuchen(suchWort1, suchWort2);
            if ((projektList == null) || (projektList.isEmpty()))
            {
                errormessage = "Kein Ergebnis!";
                doGet(request, response);
            }
            else {
                for (int b = 0; b < projektList.size(); b++) {
                    projektList.get(b).setFinanzierungslimit(projektStore.findenTotalSpendeVomProjekt(projektList.get(b).getKennung()));
                }
                errormessage = "";
                doGet(request, response);
            }
        }
    }
}