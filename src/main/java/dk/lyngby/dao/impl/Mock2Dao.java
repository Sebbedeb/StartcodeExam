package dk.lyngby.dao.impl;


import dk.lyngby.model.Mock1;
import dk.lyngby.model.Mock2;
import jakarta.persistence.EntityManagerFactory;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Mock2Dao implements dk.lyngby.dao.IDao<Mock2, Integer> {

    private static Mock2Dao instance;
    private static EntityManagerFactory emf;

    public static Mock2Dao getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new Mock2Dao();
        }
        return instance;
    }

    public Mock1 addRoomToHotel(Integer mock1id, Mock2 mock2) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            var mock1 = em.find(Mock1.class, mock1id);
            mock1.addMock2(mock2);
            em.persist(mock2);
            Mock1 merge = em.merge(mock1);
            em.getTransaction().commit();
            return merge;
        }
    }

    @Override
    public Mock2 read(Integer id)
    {
        try (var em = emf.createEntityManager())
        {
            return em.find(Mock2.class, id);
        }
    }

    @Override
    public List<Mock2> readAll() {
        try (var em = emf.createEntityManager()) {
            var query = em.createQuery("SELECT m2 FROM Mock2 m2", Mock2.class);
            return query.getResultList();
        }
    }

    @Override
    public Mock2 create(Mock2 mock2) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(mock2);
            em.getTransaction().commit();
            return mock2;
        }
    }

    @Override
    public Mock2 update(Integer integer, Mock2 mock2) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            var m2 = em.find(Mock2.class, integer);
            m2.setMock2Integer(mock2.getMock2Integer());
            m2.setMock2Enum(mock2.getMock2Enum());
            m2.setMock2BigDecimal(mock2.getMock2BigDecimal());

            Mock2 merge = em.merge(m2);
            em.getTransaction().commit();
            return merge;
        }
    }

    @Override
    public void delete(Integer id) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            var mock2 = em.find(Mock2.class, id);
            em.remove(mock2);
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (var em = emf.createEntityManager()) {
            var room = em.find(Mock2.class, id);
            return room != null;
        }
    }

    public Function<Integer, Boolean> validateMock2Integer = (mock2Integer) -> {
        try (var em = emf.createEntityManager()) {
            var room = em.find(Mock2.class, mock2Integer);
            return room != null;
        }
    };

    public Boolean validateHotelRoomNumber(Integer mock2Integer, Integer mock1Id) {
        try (var em = emf.createEntityManager()) {
            var mock1 = em.find(Mock1.class, mock1Id);
            return mock1.getMock2s().stream().anyMatch(r -> r.getMock2Integer().equals(mock2Integer));
        }
    }

}
