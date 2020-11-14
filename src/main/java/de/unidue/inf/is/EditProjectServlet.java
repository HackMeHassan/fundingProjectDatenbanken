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

public class EditProjectServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static List<String> vorgaengerList = new ArrayList<>();
    private static ProjektStore projektStore = new ProjektStore();
    private static String errorMessage = "";
    private static String tas;
    private static Integer kenn = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        tas = null;
        vorgaengerList.clear();

        tas = request.getParameter("kennung");
        kenn = Integer.parseInt(tas);


        vorgaengerList = projektStore.vorgaengerList(DBUtil.derBenutzer);
        for (int k = 0; k < vorgaengerList.size(); k++)
        {
            if (vorgaengerList.get(k).equalsIgnoreCase(projektStore.findenTitelVomProjekt(kenn)))
            {
                vorgaengerList.remove(k);
            }
        }
        request.setAttribute("vorprojekte", vorgaengerList);
        request.setAttribute("error", errorMessage);
        errorMessage = "";
        request.setAttribute("tashere", tas);

        request.getRequestDispatcher("/edit_project.ftl").forward(request, response);


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        List<Projekt> ch = projektStore.findenErstellteProjekteVon(DBUtil.derBenutzer);
        for (int w = 0; w < ch.size(); w++) {
            if (ch.get(w).getKennung() == kenn) {
                try {

                    String titel = request.getParameter("titel");
                    if (titel.isEmpty() || (titel.length() > 30)) {
                        errorMessage = "Titel ist entweder leer oder zu lang!";
                        doGet(request, response);
                    }
                    else {
                    String finanzLimitStr = request.getParameter("amount");
                    if (finanzLimitStr.isEmpty()) {
                        errorMessage = "Finanzierungslimit darf nicht leer bleiben!";
                        doGet(request, response);
                    }
                    else {
                    Double finanzLimit = null;
                    try {
                        finanzLimit = Double.parseDouble(finanzLimitStr);
                    } catch (NumberFormatException e) {
                        errorMessage = "Falsches Format beim Finanzierungslimit!";
                        doGet(request, response);
                    }
                    if (finanzLimit < projektStore.findenFinanzierungsLimitVon(kenn)) {
                        errorMessage = "Neues Finanzierungslimit darf nicht kleiner als" +
                                " das Alte sein!";
                        doGet(request, response);
                    }
                    else {
                        String kategorie = request.getParameter("group");
                        if (kategorie == null) {
                            errorMessage = "Wählen Sie unbedingt eine Kategorie aus!";
                            doGet(request, response);
                        }
                        else {
                            Integer katInt = null;
                            if (kategorie.contains("Health")) {
                                katInt = 1;
                            } else if (kategorie.contains("Art")) {
                                katInt = 2;
                            } else if (kategorie.contains("Education")) {
                                katInt = 3;
                            } else if (kategorie.contains("Tech")) {
                                katInt = 4;
                            }
                            String vorgenger = request.getParameter("version");
                            Integer vorgInt = null;
                            if (vorgenger == null) {
                                errorMessage = "Wählen Sie unbedingt eine der Optionen der Vorgänger aus!";
                                doGet(request, response);
                            }
                            else if (vorgenger.equalsIgnoreCase("Kein Vorg")) {
                                vorgInt = null;
                            }
                            else {
                                vorgInt = projektStore.findenKennungVon(vorgenger);
                            }
                            String explanation = request.getParameter("explanation");
                            if (explanation == null) {
                                explanation = "";
                            }
                            if (projektStore.findenProjektMitKennung(kenn).get(0).getStatus().equalsIgnoreCase("offen")) {
                                Projekt neuProjekt = new Projekt(kenn, titel, explanation,
                                        finanzLimit, "offen", DBUtil.derBenutzer, vorgInt, katInt);
                                projektStore.editProjekt(neuProjekt);
                                errorMessage = "Erfolgreich editiert!";
                                //doGet(request, response);
                                ViewProjectServlet viewProjectServlet = new ViewProjectServlet();
                                viewProjectServlet.doGet(request, response);
                            }
                            else
                            {
                                errorMessage = "Wir haben die Taste des Editierens weggeworfen! Warum sind Sie durch Addressbar hereingekommen?!";
                                doGet(request, response);
                            }
                        }
                    }
                    }
                    }
                } catch (IOException | ServletException e) {
                    errorMessage = "Etwas ist los mit Datenbanken oder Verbindungen oder dem Programm!";
                    doGet(request, response);
                }
            }
        }
        errorMessage = "Nur der Ersteller darf das Projekt editieren!";
        doGet(request, response);
    }
}
