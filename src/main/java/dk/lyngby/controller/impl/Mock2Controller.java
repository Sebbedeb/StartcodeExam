package dk.lyngby.controller.impl;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.controller.IController;
import dk.lyngby.dao.impl.Mock2Dao;
import dk.lyngby.dto.Mock1Dto;
import dk.lyngby.dto.Mock2Dto;
import dk.lyngby.exception.Message;
import dk.lyngby.model.Mock1;
import dk.lyngby.model.Mock2;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.function.BiFunction;

public class Mock2Controller implements IController<Mock2, Integer> {

    private Mock2Dao dao;

    public Mock2Controller() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = Mock2Dao.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        Mock2 mock2 = dao.read(id);
        // dto
        Mock2Dto mock2Dto = new Mock2Dto(mock2);
        // response
        ctx.res().setStatus(200);
        ctx.json(mock2Dto, Mock2Dto.class);

    }

    @Override
    public void readAll(Context ctx) {
        // entity
        List<Mock2> mock2s = dao.readAll();
        // dto
        List<Mock2Dto> mock2Dtos = Mock2Dto.toMock2DtoList(mock2s);
        // response
        ctx.res().setStatus(200);
        ctx.json(mock2Dtos, Mock2Dto.class);

    }

    @Override
    public void create(Context ctx) {
        // request
        Mock2 jsonRequest = validateEntity(ctx);

        int hotelId = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        Boolean hasRoom = validateHotelRoomNumber.apply(jsonRequest.getMock2Integer(), hotelId);

        if (hasRoom) {
            ctx.res().setStatus(400);
            ctx.json(new Message(400, "Mock2 number already in use by mock1"));
            return;
        }

        // entity
        Mock1 mock1 = dao.addRoomToHotel(hotelId, jsonRequest);
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
        Mock2 update = dao.update(id, validateEntity(ctx));
        // dto
        Mock2Dto mock2Dto = new Mock2Dto(update);
        // response
        ctx.res().setStatus(200);
        ctx.json(mock2Dto, Mock2Dto.class);
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
    public boolean validatePrimaryKey(Integer integer) {return dao.validatePrimaryKey(integer);}

    // Checks if the room number is already in use by the hotel
    BiFunction<Integer, Integer, Boolean> validateHotelRoomNumber = (roomNumber, hotelId) -> dao.validateHotelRoomNumber(roomNumber, hotelId);

    @Override
    public Mock2 validateEntity(Context ctx) {
        return ctx.bodyValidator(Mock2.class)
                .check(r -> r.getMock2Integer() != null && r.getMock2Integer() > 0, "Not a valid room number")
                .check(r -> r.getMock2Enum() != null, "Not a valid room type")
                .check(r -> r.getMock2BigDecimal() != null , "Not a valid price")
                .get();
    }
}
