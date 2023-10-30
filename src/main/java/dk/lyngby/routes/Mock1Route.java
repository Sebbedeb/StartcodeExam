package dk.lyngby.routes;

import dk.lyngby.controller.impl.Mock1Controller;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Mock1Route
{

    private final Mock1Controller mock1Controller = new Mock1Controller();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/mock1s", () -> {
                post("/", mock1Controller::create);
                get("/", mock1Controller::readAll);
                get("/{id}", mock1Controller::read);
                put("/{id}", mock1Controller::update);
                delete("/{id}", mock1Controller::delete);
            });
        };
    }
}
