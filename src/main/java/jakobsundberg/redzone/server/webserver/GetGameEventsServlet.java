package jakobsundberg.redzone.server.webserver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakobsundberg.redzone.server.Event;
import jakobsundberg.redzone.server.Server;

import java.io.IOException;
import java.util.List;

public class GetGameEventsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Server server = Server.INSTANCE;
        int gameId = Integer.parseInt(request.getParameter("gameId"));
        int from = Integer.parseInt(request.getParameter("from"));
        List<Event> events = server.getGameEvents(gameId, from);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{\"events\":[");
        boolean first = true;

        for (Event event : events) {
            if (first == true) {
                first = false;
            } else {
                response.getWriter().print(",");
            }

            response.getWriter().print(event);
        }

        response.getWriter().println("]}");
    }
}
