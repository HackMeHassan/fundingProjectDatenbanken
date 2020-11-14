package de.unidue.inf.is;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.ProjektStore;
import de.unidue.inf.is.stores.StoreException;
import de.unidue.inf.is.utils.DBUtil;


public final class MainPageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static List<Projekt> projektList1 = new ArrayList<>();
    private static List<Projekt> projektList2 = new ArrayList<>();
    private static ProjektStore projektStore = new ProjektStore();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (!DBUtil.derBenutzer.isEmpty()) {
            projektList1 = projektStore.findenOffeneProjekte();
            for (int b = 0; b < projektList1.size(); b++) {
                projektList1.get(b).setFinanzierungslimit(projektStore.findenTotalSpendeVomProjekt(projektList1.get(b).getKennung()));
            }
            projektList2 = projektStore.findenAbgeschlosseneProjekte();
            for (int bb = 0; bb < projektList2.size(); bb++) {
                projektList2.get(bb).setFinanzierungslimit(projektStore.findenTotalSpendeVomProjekt(projektList2.get(bb).getKennung()));
            }
            request.setAttribute("aufprojekte", projektList1);
            request.setAttribute("zuprojekte", projektList2);
            request.setAttribute("meinprof", DBUtil.derBenutzer);

            request.getRequestDispatcher("/view_main.ftl").forward(request, response);
        }
        else
        {
            projektList1.clear();
            projektList2.clear();
            request.setAttribute("aufprojekte", projektList1);
            request.setAttribute("zuprojekte", projektList2);

            request.getRequestDispatcher("/view_main.ftl").forward(request, response);
        }
    }
}