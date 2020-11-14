package de.unidue.inf.is;

import com.ibm.db2.jcc.uw.DB2Exception;
import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.domain.Schreibt;
import de.unidue.inf.is.domain.ShowKomment;
import de.unidue.inf.is.domain.Spenden;
import de.unidue.inf.is.stores.ProjektStore;
import de.unidue.inf.is.stores.StoreException;
import de.unidue.inf.is.stores.UserStore;
import de.unidue.inf.is.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeleteProjektServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    private static List<Projekt> projektList = new ArrayList<>();
    private static ProjektStore projektStore = new ProjektStore();
    private static List<Spenden> spendenList = new ArrayList<>();
    private static UserStore userStore = new UserStore();
    private static String mess = "";
    private static String proName = "";
    private static String tas;
    private static Integer kenn = null;
    private static Double totalspend;
    private static List<ShowKomment> kommentarList = new ArrayList<>();
    private static List<Schreibt> schreibtList = new ArrayList<>();
    private static List<String> textList = new ArrayList<>();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        tas = null;
        totalspend = null;
        projektList.clear();
        schreibtList.clear();
        kommentarList.clear();
        textList.clear();
        spendenList.clear();

        tas = request.getParameter("kennung");
        kenn = Integer.parseInt(tas);
        projektList = projektStore.findenProjektMitKennung(kenn);
        if ((projektList != null) && (!projektList.isEmpty()))
        {
            proName = projektList.get(0).getTitel();
            request.setAttribute("error", proName+mess);
            mess = "";
        }
        else
        {
            request.setAttribute("error", "Das Projekt ist nicht mehr gefunden!");
        }
        request.setAttribute("tashere", tas);

        request.getRequestDispatcher("/delete_project.ftl").forward(request, response); }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException, StoreException {

        String antwort = request.getParameter("group");
        if (antwort == null)
        {
            mess = " wartet schon auf Ihre Wahl!";
            doGet(request, response);
        }
        else if (antwort.equalsIgnoreCase("no"))
        {
            mess = " wird dann richtig bleiben!";
            doGet(request, response);
        }
        else
            {
                if (DBUtil.derBenutzer.equalsIgnoreCase(projektList.get(0).getErsteller())) {
                    if (!projektStore.obProjektIstVorgaenger(kenn)) {
                        try {
                            List<Schreibt> schreibtList = userStore.findenSchreibtVonProjekt(kenn);
                            List<Integer> kommIdList = new ArrayList<>();
                            for (int p = 0; p < schreibtList.size(); p++) {
                                kommIdList.add(schreibtList.get(p).getKommentarId());
                            }

                            List<Spenden> spendenList = projektStore.findenSpenderVomProjekt(kenn);
                            for (int u = 0; u < spendenList.size(); u++) {
                                List<Double> guthabenAlt = projektStore.findenGuthaben(spendenList.get(u).getSpender());
                                if (guthabenAlt != null) {
                                    Double guthabenNeu = guthabenAlt.get(0) + spendenList.get(u).getSpendenBetrag();
                                    projektStore.reduzierenGuthaben(spendenList.get(u).getSpender(), guthabenNeu);
                                } else {
                                    doGet(request, response);
                                }
                            }
                            for (int u = 0; u < spendenList.size(); u++) {
                                projektStore.deleteSpenden(kenn, spendenList.get(u).getSpender());
                            }
                            userStore.deleteSchreibtMitKommId(kommIdList);
                            userStore.deleteKommentarMitKommId(kommIdList);

                            //if (!projektStore.obProjektIstVorgaenger(kenn)) {
                            projektStore.deleteProjekt(kenn);
                            //doGet(request, response);
                            MainPageServlet mainPageServlet = new MainPageServlet();
                            mainPageServlet.doGet(request, response);
                        } catch (StoreException e) {
                            mess = " ist selbst der Vorgänger eines anderen Projekts!";
                            doGet(request, response);
                        }
                    }
                    else
                    {
                        mess = " ist selbst der Vorgänger eines anderen Projekts!";
                        doGet(request, response);
                    }
                }
                else
                {
                    mess = " muss nur vom Ersteller gelöscht werden!";
                    doGet(request, response);
                }

            }
    }
}
