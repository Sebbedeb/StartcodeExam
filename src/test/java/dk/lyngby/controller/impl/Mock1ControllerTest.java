package dk.lyngby.controller.impl;

import dk.lyngby.config.ApplicationConfig;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.dto.Mock1Dto;
import dk.lyngby.dto.Mock2Dto;
import dk.lyngby.model.Mock1;
import dk.lyngby.model.Mock2;
import io.javalin.Javalin;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManagerFactory;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Mock1ControllerTest
{
    private static Javalin app;
    private static final String BASE_URL = "http://localhost:7777/api/v1";
    private static Mock1Controller mock1Controller;
    private static EntityManagerFactory emfTest;

    private static Mock1 h1, h2;

    @BeforeAll
    static void beforeAll()
    {
        HibernateConfig.setTest(true);
        emfTest = HibernateConfig.getEntityManagerFactory();
        mock1Controller = new Mock1Controller();
    }

    @BeforeEach
    void setUp()
    {
        Set<Mock2> calMock2s = getCalRooms();
        Set<Mock2> hilMock2s = getBatesRooms();

        try (var em = emfTest.createEntityManager())
        {
            em.getTransaction().begin();
            // Delete all rows
            em.createQuery("DELETE FROM Mock2 r").executeUpdate();
            em.createQuery("DELETE FROM Mock1 h").executeUpdate();
            // Reset sequence
            em.createNativeQuery("ALTER SEQUENCE room_room_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE hotel_hotel_id_seq RESTART WITH 1").executeUpdate();
            // Insert test data
            h1 = new Mock1("Mock1 California", "California", Mock1.HotelType.LUXURY);
            h2 = new Mock1("Bates Motel", "Lyngby", Mock1.HotelType.STANDARD);
            h1.setRooms(calMock2s);
            h2.setRooms(hilMock2s);
            em.persist(h1);
            em.persist(h2);
            em.getTransaction().commit();

            app = Javalin.create();
            ApplicationConfig.startServer(app, 7777);
        }
    }

    @AfterEach
    void tearDown()
    {
        HibernateConfig.setTest(false);
        ApplicationConfig.stopServer(app);
    }

    @Test
    void read()
    {
        given()
                .contentType("application/json")
                .when()
                .get(BASE_URL + "/hotels/" + h1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200)
                .body("id", equalTo(h1.getId()));
    }

    @Test
    void readAll()
    {
        // Given -> When -> Then
        List<Mock1Dto> mock1DtoList =
                given()
                        .contentType("application/json")
                        .when()
                        .get(BASE_URL + "/hotels")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK_200)  // could also just be 200
                        .extract().body().jsonPath().getList("", Mock1Dto.class);

        Mock1Dto h1DTO = new Mock1Dto(h1);
        Mock1Dto h2DTO = new Mock1Dto(h2);

        assertEquals(mock1DtoList.size(), 2);
        assertThat(mock1DtoList, containsInAnyOrder(h1DTO, h2DTO));
    }

    @Test
    void create()
    {
        Mock1 h3 = new Mock1("Cab-inn", "Østergade 2", Mock1.HotelType.BUDGET);
        Mock2 r1 = new Mock2(117, new BigDecimal(4500), Mock2.RoomType.SINGLE);
        Mock2 r2 = new Mock2(118, new BigDecimal(2300), Mock2.RoomType.DOUBLE);
        h3.addRoom(r1);
        h3.addRoom(r2);
        Mock1Dto newHotel = new Mock1Dto(h3);

        List<Mock2Dto> mock2Dtos =
        given()
                .contentType(ContentType.JSON)
                .body(newHotel)
                .when()
                .post(BASE_URL + "/hotels")
                .then()
                .statusCode(201)
                .body("id", equalTo(3))
                .body("hotelName", equalTo("Cab-inn"))
                .body("hotelAddress", equalTo("Østergade 2"))
                .body("hotelType", equalTo("BUDGET"))
                .body("rooms", hasSize(2))
                .extract().body().jsonPath().getList("rooms", Mock2Dto.class);

        assertThat(mock2Dtos, containsInAnyOrder(new Mock2Dto(r1), new Mock2Dto(r2)));
    }

    @Test
    void update()
    {
        // Update the Bates Motel to luxury

        Mock1Dto updateHotel = new Mock1Dto("Bates Motel", "Lyngby" , Mock1.HotelType.LUXURY);
        given()
                .contentType(ContentType.JSON)
                .body(updateHotel)
                .log().all()
                .when()
                .put(BASE_URL + "/hotels/" + h2.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(h2.getId()))
                .body("hotelName", equalTo("Bates Motel"))
                .body("hotelAddress", equalTo("Lyngby"))
                .body("hotelType", equalTo("LUXURY"))
                .body("rooms", hasSize(6));
    }

    @Test
    void delete()
    {
        // Remove hotel California
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(BASE_URL + "/hotels/" + h1.getId())
                .then()
                .statusCode(204);

        // Check that it is gone
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_URL + "/hotels/" + h1.getId())
                .then()
                .statusCode(400);
    }

    @NotNull
    private static Set<Mock2> getCalRooms()
    {
        Mock2 r100 = new Mock2(100, new BigDecimal(2520), Mock2.RoomType.SINGLE);
        Mock2 r101 = new Mock2(101, new BigDecimal(2520), Mock2.RoomType.SINGLE);
        Mock2 r102 = new Mock2(102, new BigDecimal(2520), Mock2.RoomType.SINGLE);
        Mock2 r103 = new Mock2(103, new BigDecimal(2520), Mock2.RoomType.SINGLE);
        Mock2 r104 = new Mock2(104, new BigDecimal(3200), Mock2.RoomType.DOUBLE);
        Mock2 r105 = new Mock2(105, new BigDecimal(4500), Mock2.RoomType.SUITE);

        Mock2[] mock2Array = {r100, r101, r102, r103, r104, r105};
        return Set.of(mock2Array);
    }

    @NotNull
    private static Set<Mock2> getBatesRooms()
    {
        Mock2 r111 = new Mock2(111, new BigDecimal(2520), Mock2.RoomType.SINGLE);
        Mock2 r112 = new Mock2(112, new BigDecimal(2520), Mock2.RoomType.SINGLE);
        Mock2 r113 = new Mock2(113, new BigDecimal(2520), Mock2.RoomType.SINGLE);
        Mock2 r114 = new Mock2(114, new BigDecimal(2520), Mock2.RoomType.DOUBLE);
        Mock2 r115 = new Mock2(115, new BigDecimal(3200), Mock2.RoomType.DOUBLE);
        Mock2 r116 = new Mock2(116, new BigDecimal(4500), Mock2.RoomType.SUITE);

        Mock2[] mock2Array = {r111, r112, r113, r114, r115, r116};
        return Set.of(mock2Array);
    }
}