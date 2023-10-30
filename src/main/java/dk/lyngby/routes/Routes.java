package dk.lyngby.routes;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final Mock1Route mock1Route = new Mock1Route();
    private final Mock2Route mock2Route = new Mock2Route();

    public EndpointGroup getRoutes(Javalin app) {
        return () -> {
            app.routes(() -> {
                path("/", mock1Route.getRoutes());
                path("/", mock2Route.getRoutes());
            });
        };
    }
}
