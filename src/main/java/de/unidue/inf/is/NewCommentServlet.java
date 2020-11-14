package de.unidue.inf.is;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Kommentar;
import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.domain.Spenden;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.ProjektStore;
import de.unidue.inf.is.utils.DBUtil;


public final class NewCommentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static List<Projekt> projektList = new ArrayList<>();
    private static ProjektStore projektStore = new ProjektStore();
    private static String errorMessage = "";
    private static String tas;
    private static Integer kenn = null;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        tas = null;

        tas = request.getParameter("kennung");
        kenn = Integer.parseInt(tas);
        projektList = projektStore.findenProjektMitKennung(kenn);
        request.setAttribute("projekte", projektList);
        request.setAttribute("tashere", tas);
        request.setAttribute("error", errorMessage);
        errorMessage = "";

        request.getRequestDispatcher("/new_comment.ftl").forward(request, response); }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String komment = request.getParameter("explanation");
        if ((komment == null) || (komment.isEmpty()))
        {
            errorMessage = "Falls Sie nix haben, zu sagen, sind Sie hier falsch!";
            doGet(request, response);
        }
        else {
            String sicht;
            String sichtWahl = request.getParameter("version");
            if (sichtWahl == null) {
                sicht = "oeffentlich";
            } else {
                sicht = "privat";
            }
            Integer neuId = projektStore.findenLetzteId();
            if (neuId == null) {
                neuId = 1;
            } else {
                neuId = neuId + 1;
            }
            String geradeJetzt = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new Timestamp(System.currentTimeMillis()));
            Kommentar neuKomment = new Kommentar(neuId,
                    komment, geradeJetzt, sicht);

            projektStore.addKommentar(neuKomment);
            projektStore.addSchreibt(neuId, kenn, DBUtil.derBenutzer);
            errorMessage = "Erfolg beim Hinterlassen des Komments!";
            //doGet(request, response);
            ViewProjectServlet viewProjectServlet = new ViewProjectServlet();
            viewProjectServlet.doGet(request, response);
        }
    }
}