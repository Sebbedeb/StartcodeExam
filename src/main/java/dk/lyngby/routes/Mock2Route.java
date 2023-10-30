package dk.lyngby.routes;

import dk.lyngby.controller.impl.Mock2Controller;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Mock2Route
{

    private final Mock2Controller mock2Controller = new Mock2Controller();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/mock2s", () -> {
                post("/mock1/{id}", mock2Controller::create);
                get("/", mock2Controller::readAll);
                get("/{id}", mock2Controller::read);
                put("/{id}", mock2Controller::update);
                delete("/{id}", mock2Controller::delete);
            });
        };
    }
}
