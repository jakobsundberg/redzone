package jakobsundberg.redzone.server.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

public class WebServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(80);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase("src/main/webapp/");

        context.addServlet(GetGamesServlet.class, "/getGames");
        context.addServlet(GetGameEventsServlet.class, "/getGameEvents");
        context.addServlet(CreateGameServlet.class, "/createGame");
        context.addServlet(JoinGameServlet.class, "/joinGame");
        context.addServlet(PlayServlet.class, "/play");
        context.addServlet(ActivateServlet.class, "/activate");
        context.addServlet(DeclareAttackerServlet.class, "/declareAttacker");
        context.addServlet(PassPriorityServlet.class, "/passPriority");

        context.addServlet(DefaultServlet.class, "/");
        server.setHandler(context);
        server.start();
        server.join();
    }
}
