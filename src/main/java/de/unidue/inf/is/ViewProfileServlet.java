package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.domain.ShowGespendet;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.ProjektStore;
import de.unidue.inf.is.stores.UserStore;
import de.unidue.inf.is.utils.DBUtil;


public final class ViewProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static List<Projekt> projektList1 = new ArrayList<>();
    private static List<Integer> projektList2 = new ArrayList<>();
    private static List<Projekt> projektList3 = new ArrayList<>();
    private static List<ShowGespendet> gespendetList = new ArrayList<>();
    private static ProjektStore projektStore = new ProjektStore();
    private static UserStore userStore = new UserStore();
    private static String sta;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        sta = null;
        projektList1.clear();
        projektList2.clear();
        projektList3.clear();
        gespendetList.clear();

        sta = request.getParameter("email");
        String benutzerName = userStore.findenBenutzername(sta);
        if (benutzerName != null) {
            projektList1 = projektStore.findenErstellteProjekteVon(sta);
            for (int b = 0; b < projektList1.size(); b++) {
                projektList1.get(b).setFinanzierungslimit(projektStore.findenTotalSpendeVomProjekt(projektList1.get(b).getKennung()));
            }
            request.setAttribute("aufprojekte", projektList1);

            projektList2 = projektStore.findenUnterstuezteProjekteVon(sta);
            projektList3 = projektStore.findenProjektMitListVonKennung(projektList2);
            for (int u = 0; u < projektList3.size(); u++) {
                gespendetList.add(new ShowGespendet(projektList3.get(u).getTitel(),
                        projektList3.get(u).getKennung(),
                        projektList3.get(u).getFinanzierungslimit(),
                        projektList3.get(u).getStatus(),
                        projektStore.findenGespendet(sta,
                                projektList3.get(u).getKennung())));
            }
            request.setAttribute("zuprojekte", gespendetList);

            Integer erstAnzahl = projektStore.findenAnzahlErstellteProjekteVon(sta);
            Integer unteAnzahl = projektStore.findenAnzahlUnterstuezteProjekteVon(sta);

            request.setAttribute("kontoinhab", sta);

            request.setAttribute("kontoinhabname", benutzerName);
            request.setAttribute("anzahlerst", erstAnzahl);
            request.setAttribute("anzahlunte", unteAnzahl);
        }
        else
        {
            projektList1.clear();
            projektList2.clear();
            projektList3.clear();
            gespendetList.clear();
            request.setAttribute("aufprojekte", projektList1);
            request.setAttribute("zuprojekte", gespendetList);
            request.setAttribute("kontoinhab", "");
            request.setAttribute("kontoinhabname", "");
            request.setAttribute("anzahlerst", "");
            request.setAttribute("anzahlunte", "");
        }

        request.getRequestDispatcher("/view_profile.ftl").forward(request, response);
    }
}
