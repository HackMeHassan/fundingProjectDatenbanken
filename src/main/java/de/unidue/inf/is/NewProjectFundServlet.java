package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.domain.Spenden;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.ProjektStore;
import de.unidue.inf.is.utils.DBUtil;


public final class NewProjectFundServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static List<Projekt> projektList = new ArrayList<>();
    private static ProjektStore projektStore = new ProjektStore();
    private static String errorMessage = "";
    private static String tas;
    private static Integer kenn = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        tas = null;

        tas = request.getParameter("kennung");
        kenn = Integer.parseInt(tas);
        projektList = projektStore.findenProjektMitKennung(kenn);
        request.setAttribute("projekte", projektList);
        request.setAttribute("tashere", tas);
        request.setAttribute("error", errorMessage);
        errorMessage = "";

        request.getRequestDispatcher("/new_project_fund.ftl").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String sicht;
        String sichtWahl = request.getParameter("version");
        if (sichtWahl == null) {
            sicht = "oeffentlich";
        }
        else
        {
            sicht = "privat";
        }
        List<Projekt> targetPro = projektStore.findenProjektMitKennung(kenn);
        if ((targetPro == null) || (targetPro.isEmpty()))
        {
            errorMessage = "Es ist seltsam! Das Projekt ist nicht gefunden!!";
            doGet(request, response);
        }
        else if (targetPro.get(0).getStatus().equalsIgnoreCase("geschlossen"))
        {
            errorMessage = "Geschlossene Projekte dürfen keinen Betrag mehr erhalten! Warum sind Sie hier durch Addressbar hereingekommen?";
            doGet(request, response);
        }
        else {
            List<Spenden> spenderList = projektStore.findenSpenderVomProjekt(kenn);
            if ((spenderList != null) && (!spenderList.isEmpty()))
            {
                for (int u = 0; u < spenderList.size(); u++)
                {
                    if (spenderList.get(u).getSpender().equalsIgnoreCase(DBUtil.derBenutzer))
                    {
                        errorMessage = "Sie haben früher bei diesem Projekt gespendet! Mehrfaches Spenden ist nicht erlaubt!";
                        doGet(request, response);
                    }
                }
            }
            //else {
                String betrag = request.getParameter("spendenbetrag");
                if (betrag.isEmpty()) {
                    errorMessage = "Geben Sie bitte eine Zahl ein!";
                    doGet(request, response);
                } else {
                    Double betragZahl = null;
                    try {
                        betragZahl = Double.parseDouble(betrag);
                    }  catch (NumberFormatException e)
                    {
                        errorMessage = "Falsches Format beim Finanzierungslimit!";
                        doGet(request, response);
                    }
                    List<Double> checkGuthaben = projektStore.findenGuthaben(DBUtil.derBenutzer);
                    if (checkGuthaben == null) {
                        errorMessage = "Sie haben wohl kein Konto oder kein Guthaben!";
                        doGet(request, response);
                    } else {
                        if (betragZahl <= 0.00) {
                            errorMessage = "Nicht lustig! Bitte geben Sie einen echten Betrag ein!";
                            doGet(request, response);
                        } else {
                            Double unterschied = checkGuthaben.get(0) - betragZahl;
                            if (unterschied < 0) {
                                errorMessage = "Sie haben ja nicht genug Guthaben!";
                                doGet(request, response);
                            } else {
                                Spenden neuSpenden = new Spenden(kenn,
                                DBUtil.derBenutzer,
                                betragZahl, sicht);
                                projektStore.addSpenden(neuSpenden);
                                projektStore.reduzierenGuthaben(DBUtil.derBenutzer, unterschied);
                                errorMessage = "Erfolg beim Spenden!";

                                Double a = projektStore.findenTotalSpendeVomProjekt(kenn);
                                Double b = projektStore.findenFinanzierungsLimitVon(kenn);
                                if (a >= b) {
                                    projektStore.wechselStatus(kenn);
                                }
                                //doGet(request, response);
                                ViewProjectServlet viewProjectServlet = new ViewProjectServlet();
                                viewProjectServlet.doGet(request, response);
                            }
                        }

                    }
                }
            //}
        }
    }
}