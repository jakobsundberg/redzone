package jakobsundberg.redzone.server.webserver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakobsundberg.redzone.server.Game;
import jakobsundberg.redzone.server.Server;

import java.io.IOException;
import java.util.Collection;

public class GetGamesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Server server = Server.INSTANCE;
        Collection<Game> games = server.getGames();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{\"games\":[");
        boolean first = true;

        for(Game game : games) {
            if(first == true){
                first = false;
            }
            else{
                response.getWriter().print(",");
            }
            response.getWriter().print("{");
            response.getWriter().print("\"id\":" + game.id);
            response.getWriter().print(", \"players\":" + game.players.size());
            response.getWriter().print("}");
        }

        response.getWriter().println("]}");
    }
}
