package tube.controller;

import tube.entity.ATube;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ListTubeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ATube> list = ofy().load().type(ATube.class).list();
        req.setAttribute("list", list);
        req.setAttribute("name", "Hello World!");
        req.getRequestDispatcher("/list.jsp").forward(req, resp);
        try {

        }catch (Exception catchException){

        }
    }
}
