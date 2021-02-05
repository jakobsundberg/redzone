package jakobsundberg.redzone.server.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class WebServer {
    public static void main(String[] args) throws Exception {
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(GetGamesServlet.class, "/getGames");
        servletHandler.addServletWithMapping(GetGameEventsServlet.class, "/getGameEvents");
        servletHandler.addServletWithMapping(CreateGameServlet.class, "/createGame");
        servletHandler.addServletWithMapping(JoinGameServlet.class, "/joinGame");
        servletHandler.addServletWithMapping(PlayServlet.class, "/play");
        servletHandler.addServletWithMapping(ActivateServlet.class, "/activate");
        servletHandler.addServletWithMapping(DeclareAttackerServlet.class, "/declareAttacker");
        servletHandler.addServletWithMapping(PassPriorityServlet.class, "/passPriority");
        Server server = new Server(80);
        server.setHandler(servletHandler);
        server.start();
        server.join();
    }
}
