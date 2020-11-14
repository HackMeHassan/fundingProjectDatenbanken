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



/**
 * Einfaches Beispiel, das die Vewendung der Template-Engine zeigt
 */
public final class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static List<User> userList = new ArrayList<>();

    // Just prepare static data to display on screen
    static {
        userList.add(new User("Bill", "Gates",
                "bill@gates.com","The richest"));
        userList.add(new User("Steve", "Jobs",
                "steve@jobs.com","Now dead"));
        userList.add(new User("Larry", "Page",
                "larry@page.com",""));
        userList.add(new User("Sergey", "Brin",
                "sergey@brin.com","Idk!"));
        userList.add(new User("Larry", "Ellison",
                "larry@ellison.com",""));
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Put the user list in request and let freemarker paint it.
        request.setAttribute("users", userList);

        request.getRequestDispatcher("/index.ftl").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {

        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String explanation = request.getParameter("explanation");



        if (null != firstname && null != lastname
                && null != email && !firstname.isEmpty()
                && !lastname.isEmpty() && !email.isEmpty()) {

            synchronized (userList) {
                userList.add(new User(firstname, lastname,
                        email, explanation));
            }

        }

        doGet(request, response);
    }
}
