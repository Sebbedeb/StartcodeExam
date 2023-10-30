package dk.lyngby.dao.impl;

import dk.lyngby.dao.IDao;
import dk.lyngby.model.Mock1;
import jakarta.persistence.EntityManagerFactory;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Mock1Dao implements IDao<Mock1, Integer>
{

    private static Mock1Dao instance;
    private static EntityManagerFactory emf;

    public static Mock1Dao getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new Mock1Dao();
        }
        return instance;
    }

    @Override
    public Mock1 read(Integer integer) {
       try (var em = emf.createEntityManager())
       {
           return em.find(Mock1.class, integer);
       }
    }

    @Override
    public List<Mock1> readAll() {
        try (var em = emf.createEntityManager())
        {
            var query = em.createQuery("SELECT m1 FROM Mock1 m1", Mock1.class);
            return query.getResultList();
        }
    }

    @Override
    public Mock1 create(Mock1 mock1) {
        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(mock1);
            em.getTransaction().commit();
            return mock1;
        }
    }

    @Override
    public Mock1 update(Integer id, Mock1 mock1) {
        try(var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            var h = em.find(Mock1.class, id);
            h.setMock1String(mock1.getMock1String());
            h.setMock1enum(mock1.getMock1enum());
            Mock1 merge = em.merge(h);
            em.getTransaction().commit();
            return merge;
        }
    }

    @Override
    public void delete(Integer id) {
        try(var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            var mock1 = em.find(Mock1.class, id);
            em.remove(mock1);
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try(var em = emf.createEntityManager()) {
            var person = em.find(Mock1.class, id);
            return person != null;
        }
    }
}
