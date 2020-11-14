package de.unidue.inf.is;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.*;
import de.unidue.inf.is.stores.ProjektStore;
import de.unidue.inf.is.stores.UserStore;


public final class ViewProjectServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static List<Projekt> projektList = new ArrayList<>();
    private static List<Projekt> projektList2 = new ArrayList<>();
    private static ProjektStore projektStore = new ProjektStore();
    private static List<Spenden> spendenList = new ArrayList<>();
    private static UserStore userStore = new UserStore();
    private static String sta;
    private static Double totalspend;
    private static List<ShowKomment> kommentarList = new ArrayList<>();
    private static List<Schreibt> schreibtList = new ArrayList<>();
    private static List<String> textList = new ArrayList<>();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            sta = null;
            totalspend = null;
            projektList.clear();
            projektList2.clear();
            schreibtList.clear();
            kommentarList.clear();
            textList.clear();
            spendenList.clear();

        sta = request.getParameter("kennung");
        Integer proKenn = Integer.parseInt(sta);
        projektList = projektStore.findenProjektMitKennung(proKenn);

        request.setAttribute("projekte", projektList);
        if (projektList != null)
        {
            List<Projekt> vorgPro = projektStore.findenProjektMitKennung(projektList.get(0).getVorgaenger());
            //request.setAttribute("vorga", vorgPro);
            if ((vorgPro == null) || (vorgPro.isEmpty()))
            {
                vorgPro.add(new Projekt(1, "Kein Vorgänger vorhanden!",
                        "",5678.00,
                        "offen", "ali",
                        null, 3));
                request.setAttribute("vorga", vorgPro);
            }
            else
            {
                request.setAttribute("vorga", vorgPro);
            }
        }
        for (int p = 0; p < projektList.size(); p++)
        {
            if (projektList.get(p).getStatus().equalsIgnoreCase("offen"))
            {
                projektList2.add(projektList.get(p));
            }
        }
        request.setAttribute("projektes", projektList2);

        totalspend = projektStore.findenTotalSpendeVomProjekt(proKenn);
        request.setAttribute("total", totalspend);

        spendenList = projektStore.findenSpenderVomProjekt(proKenn);
        spendenList.sort(Comparator.comparingDouble(Spenden::getSpendenBetrag).reversed());
        for (int k = 0; k < spendenList.size(); k++){
            if (spendenList.get(k).getSichtbarkeit().equalsIgnoreCase("privat"))
            {
                spendenList.get(k).setSpender("Anonym");
            }
        }
        request.setAttribute("spendern", spendenList);

        schreibtList = userStore.findenSchreibtVonProjekt(proKenn);
        for (int j = 0; j < schreibtList.size(); j++)
        {
            textList.add(j, userStore.findenTextVomKommentar(schreibtList.get(j).getKommentarId()));
        }
        for (int k = 0; k < schreibtList.size(); k++)
        {
            kommentarList.add(new ShowKomment(schreibtList.get(k).getBenutzer(),
                    schreibtList.get(k).getKommentarId(),
                    textList.get(k)));
        }
        for (int kk = 0; kk < kommentarList.size(); kk++)
        {
            if (projektStore.findenSichtbarkeitVomKommentar(kommentarList.get(kk).getKomid()).equalsIgnoreCase("privat"))
            {
                kommentarList.get(kk).setBenutzer("Anonym");
            }
        }
        kommentarList.sort(Comparator.comparingInt(ShowKomment::getKomid).reversed());
        request.setAttribute("kommente", kommentarList);

        request.getRequestDispatcher("/view_project.ftl").forward(request, response); }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        doGet(request, response);
    }
}