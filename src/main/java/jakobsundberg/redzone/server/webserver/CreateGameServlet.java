package jakobsundberg.redzone.server.webserver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakobsundberg.redzone.server.DeckList;
import jakobsundberg.redzone.server.Game;
import jakobsundberg.redzone.server.Server;

import java.io.IOException;

public class CreateGameServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Server server = Server.INSTANCE;
        String username = request.getParameter("username");
        int deckListId = Integer.parseInt(request.getParameter("deckListId"));
        Game game = server.createGame(username, deckListId);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{\"result\":\"ok\",\"gameId\":"+game.id+"}");
    }
}
