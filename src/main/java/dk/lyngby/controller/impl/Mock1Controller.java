package dk.lyngby.controller.impl;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.controller.IController;
import dk.lyngby.dao.impl.Mock1Dao;
import dk.lyngby.dto.Mock1Dto;
import dk.lyngby.model.Mock1;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class Mock1Controller implements IController<Mock1, Integer> {

    private final Mock1Dao dao;

    public Mock1Controller() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = Mock1Dao.getInstance(emf);
    }

    @Override
    public void read(Context ctx)  {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        Mock1 mock1 = dao.read(id);
        // dto
        Mock1Dto mock1Dto = new Mock1Dto(mock1);
        // response
        ctx.res().setStatus(200);
        ctx.json(mock1Dto, Mock1Dto.class);
    }

    @Override
    public void readAll(Context ctx) {
        // entity
        List<Mock1> mock1s = dao.readAll();
        // dto
        List<Mock1Dto> mock1Dtos = Mock1Dto.toMock2DtoList(mock1s);
        // response
        ctx.res().setStatus(200);
        ctx.json(mock1Dtos, Mock1Dto.class);
    }

    @Override
    public void create(Context ctx) {
        // request
        //Mock1 jsonRequest = validateEntity(ctx);
        Mock1 jsonRequest = ctx.bodyAsClass(Mock1.class);
        // entity
        Mock1 mock1 = dao.create(jsonRequest);
        // dto
        Mock1Dto mock1Dto = new Mock1Dto(mock1);
        // response
        ctx.res().setStatus(201);
        ctx.json(mock1Dto, Mock1Dto.class);
    }

    @Override
    public void update(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        Mock1 update = dao.update(id, validateEntity(ctx));
        // dto
        Mock1Dto mock1Dto = new Mock1Dto(update);
        // response
        ctx.res().setStatus(200);
        ctx.json(mock1Dto, Mock1.class);
    }

    @Override
    public void delete(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        dao.delete(id);
        // response
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return dao.validatePrimaryKey(integer);
    }

    @Override
    public Mock1 validateEntity(Context ctx) {
        return ctx.bodyValidator(Mock1.class)
                .check( h -> h.getMock1String() != null && !h.getMock1String().isEmpty(), "Mock1 name must be set")
                .check( h -> h.getMock1enum() != null, "Mock1 type must be set")
                .get();
    }

}
