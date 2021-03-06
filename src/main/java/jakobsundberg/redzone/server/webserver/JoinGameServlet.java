package jakobsundberg.redzone.server.webserver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakobsundberg.redzone.server.Game;
import jakobsundberg.redzone.server.Server;

import java.io.IOException;

public class JoinGameServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Server server = Server.INSTANCE;
        int gameId = Integer.parseInt(request.getParameter("gameId"));
        String username = request.getParameter("username");
        int deckListId = Integer.parseInt(request.getParameter("deckListId"));
        Game game = server.joinGame(gameId, username, deckListId);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{\"result\":\"ok\",\"gameId\":"+game.id+"}");
    }
}
