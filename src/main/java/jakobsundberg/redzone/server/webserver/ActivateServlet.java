package jakobsundberg.redzone.server.webserver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakobsundberg.redzone.server.Server;

import java.io.IOException;

public class ActivateServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Server server = Server.INSTANCE;
        int gameId = Integer.parseInt(request.getParameter("gameId"));
        int cardId = Integer.parseInt(request.getParameter("cardId"));
        int activatedAbilityId = Integer.parseInt(request.getParameter("activatedAbilityId"));
        server.activate(gameId, cardId, activatedAbilityId);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{\"result\":\"ok\"}");
    }
}
