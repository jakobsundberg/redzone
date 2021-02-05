package jakobsundberg.redzone.server.webserver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakobsundberg.redzone.server.Server;

import java.io.IOException;

public class DeclareAttackerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Server server = Server.INSTANCE;
        int gameId = Integer.parseInt(request.getParameter("gameId"));
        int creatureId = Integer.parseInt(request.getParameter("creatureId"));
        int targetId = Integer.parseInt(request.getParameter("targetId"));
        server.declareAttacker(gameId, creatureId, targetId);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{\"result\":\"ok\"}");
    }
}
