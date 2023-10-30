package dk.lyngby.config;


import dk.lyngby.model.Mock1;
import dk.lyngby.model.Mock2;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Set;

public class Populate {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        Set<Mock2> mock2s_1 = getMock2s_1();
        Set<Mock2> mock2s_2 = getMock2s_2();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Mock1 mock1_1 = new Mock1("Mock1_1_String", Mock1.Mock1Enum.ENUM1);
            Mock1 mock1_2 = new Mock1("Mock1_2_String", Mock1.Mock1Enum.ENUM2);
            mock1_1.setMock2s(mock2s_1);
            mock1_2.setMock2s(mock2s_2);
            em.persist(mock1_2);
            em.persist(mock1_1);
            em.getTransaction().commit();
        }
    }

    @NotNull
    private static Set<Mock2> getMock2s_1() {
        Mock2 m2_100 = new Mock2(100, new BigDecimal(2520), Mock2.Mock2Enum.ENUM1);
        Mock2 m2_101 = new Mock2(101, new BigDecimal(2520), Mock2.Mock2Enum.ENUM1);
        Mock2 m2_102 = new Mock2(102, new BigDecimal(2520), Mock2.Mock2Enum.ENUM1);
        Mock2 m2_103 = new Mock2(103, new BigDecimal(2520), Mock2.Mock2Enum.ENUM3);
        Mock2 m2_104 = new Mock2(104, new BigDecimal(3200), Mock2.Mock2Enum.ENUM2);
        Mock2 m2_105 = new Mock2(105, new BigDecimal(4500), Mock2.Mock2Enum.ENUM2);

        Mock2[] mock2Array = {m2_100, m2_101, m2_102, m2_103, m2_104, m2_105};
        return Set.of(mock2Array);
    }

    @NotNull
    private static Set<Mock2> getMock2s_2() {
        Mock2 m2_111 = new Mock2(111, new BigDecimal(2520), Mock2.Mock2Enum.ENUM1);
        Mock2 m2_112 = new Mock2(112, new BigDecimal(2520), Mock2.Mock2Enum.ENUM1);
        Mock2 m2_113 = new Mock2(113, new BigDecimal(2520), Mock2.Mock2Enum.ENUM1);
        Mock2 m2_114 = new Mock2(114, new BigDecimal(2520), Mock2.Mock2Enum.ENUM1);
        Mock2 m2_115 = new Mock2(115, new BigDecimal(3200), Mock2.Mock2Enum.ENUM3);
        Mock2 m2_116 = new Mock2(116, new BigDecimal(4500), Mock2.Mock2Enum.ENUM2);

        Mock2[] mock2Array = {m2_111, m2_112, m2_113, m2_114, m2_115, m2_116};
        return Set.of(mock2Array);
    }
}
