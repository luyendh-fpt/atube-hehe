package tube.api;

import com.google.gson.Gson;
import tube.entity.ATube;
import tube.util.JsonResponse;
import tube.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ATubeApi extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ATubeApi.class.getName());
    private static Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // return list || detail
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            String strId = req.getParameter("id");
            if (strId == null || strId.length() == 0) {
                // load list.
                List<ATube> list = ofy().load().type(ATube.class).filter("status !=", 0).list();
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println(new JsonResponse()
                        .setStatus(HttpServletResponse.SC_OK)
                        .setMessage("Save atube success!")
                        .setData(list).toJsonString());
            } else {
                // load detail
                ATube aTube = ofy().load().type(ATube.class).id(Long.parseLong(strId)).now();
                if (aTube == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println(new JsonResponse()
                            .setStatus(HttpServletResponse.SC_NOT_FOUND)
                            .setMessage("Atube is not found or has been deleted!")
                            .toJsonString());
                    return;
                }
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println(new JsonResponse()
                        .setStatus(HttpServletResponse.SC_OK)
                        .setMessage("Atube detail")
                        .setData(aTube)
                        .toJsonString());
            }

        } catch (Exception ex) {
            String messageError = String.format("Can't create atube, error: %s", ex.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .setMessage(messageError)
                    .toJsonString());
            LOGGER.log(Level.SEVERE, messageError);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // create new tube.
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            String content = StringUtil.convertInputStreamToString(req.getInputStream());
            ATube aTube = gson.fromJson(content, ATube.class);
            ofy().save().entity(aTube).now();

            resp.setStatus(HttpServletResponse.SC_CREATED);
            JsonResponse jsonResponse = new JsonResponse(
                    HttpServletResponse.SC_CREATED,
                    "Save atube success!",
                    aTube);
            resp.getWriter().println(gson.toJson(jsonResponse));
        } catch (Exception ex) {
            String messageError = String.format("Can't create atube, error: %s", ex.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonResponse jsonResponse = new JsonResponse(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    messageError,
                    null);
            resp.getWriter().println(gson.toJson(jsonResponse));
            LOGGER.log(Level.SEVERE, messageError);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // update
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        // kiểm tra tồn tại tham số id trong parameter (lưu ý, đây là cách tạm thời)
        // trong trường hợp không tồn tại thì trả về bad request.
        String strId = req.getParameter("id");
        if (strId == null || strId.length() == 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonResponse jsonResponse = new JsonResponse(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Bad request",
                    null);
            resp.getWriter().println(gson.toJson(jsonResponse));
            LOGGER.log(Level.SEVERE, "Have no id.");
            return;
        }
        // Kiểm tra sự tồn tại của Atube trong database với id truyền lên.
        // trong trường hợp không tồn tại thì trả về not found.
        ATube existTube = ofy().load().type(ATube.class).id(Long.parseLong(strId)).now();
        if (existTube == null || existTube.getStatus() == 0) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            JsonResponse jsonResponse = new JsonResponse(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Atube is not found or has been deleted!",
                    null);
            resp.getWriter().println(gson.toJson(jsonResponse));
            LOGGER.log(Level.SEVERE, "Atube is not found or has been deleted!");
            return;
        }

        try {
            String content = StringUtil.convertInputStreamToString(req.getInputStream());
            ATube updateTube = gson.fromJson(content, ATube.class);
            existTube.setName(updateTube.getName());
            existTube.setDescription(updateTube.getDescription());
            existTube.setUpdatedAt(Calendar.getInstance().getTimeInMillis());
            ofy().save().entity(existTube).now();

            resp.setStatus(HttpServletResponse.SC_OK);
            JsonResponse jsonResponse = new JsonResponse(
                    HttpServletResponse.SC_OK,
                    "Save atube success!",
                    existTube);
            resp.getWriter().println(gson.toJson(jsonResponse));
        } catch (Exception ex) {
            String messageError = String.format("Can't update atube, error: %s", ex.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonResponse jsonResponse = new JsonResponse(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    messageError,
                    null);
            resp.getWriter().println(gson.toJson(jsonResponse));
            LOGGER.log(Level.SEVERE, messageError);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // remove.
        // update
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        // kiểm tra tồn tại tham số id trong parameter (lưu ý, đây là cách tạm thời)
        // trong trường hợp không tồn tại thì trả về bad request.
        String strId = req.getParameter("id");
        if (strId == null || strId.length() == 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonResponse jsonResponse = new JsonResponse(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Bad request",
                    null);
            resp.getWriter().println(gson.toJson(jsonResponse));
            LOGGER.log(Level.SEVERE, "Have no id.");
            return;
        }
        // Kiểm tra sự tồn tại của Atube trong database với id truyền lên.
        // trong trường hợp không tồn tại thì trả về not found.
        ATube existTube = ofy().load().type(ATube.class).id(Long.parseLong(strId)).now();
        if (existTube == null || existTube.getStatus() == 0) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            JsonResponse jsonResponse = new JsonResponse(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Atube is not found or has been deleted!",
                    null);
            resp.getWriter().println(gson.toJson(jsonResponse));
            LOGGER.log(Level.SEVERE, "Atube is not found or has been deleted!");
            return;
        }

        try {
            existTube.setStatus(0);
            existTube.setDeletedAt(Calendar.getInstance().getTimeInMillis());
            ofy().save().entity(existTube).now();

            resp.setStatus(HttpServletResponse.SC_OK);
            JsonResponse jsonResponse = new JsonResponse(
                    HttpServletResponse.SC_OK,
                    "Remove atube success!",
                    null);
            resp.getWriter().println(gson.toJson(jsonResponse));
        } catch (Exception ex) {
            String messageError = String.format("Can't remove atube, error: %s", ex.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonResponse jsonResponse = new JsonResponse(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    messageError,
                    null);
            resp.getWriter().println(gson.toJson(jsonResponse));
            LOGGER.log(Level.SEVERE, messageError);
        }
    }
}
