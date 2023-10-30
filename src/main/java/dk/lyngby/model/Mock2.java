package dk.lyngby.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "mock2")
public class Mock2
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mock2_id", nullable = false, unique = true)
    private Integer mock2Id;

    @Setter
    @Column(name = "mock2_Integer", nullable = false)
    private Integer mock2Integer;

    @Setter
    @Column(name = "mock2_bigDecimal", nullable = false)
    private BigDecimal mock2BigDecimal;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "mock2_enum", nullable = false)
    private Mock2Enum mock2Enum;

    @Setter
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Mock1 mock1;

    public Mock2(Integer mock2Integer, BigDecimal mock2BigDecimal, Mock2Enum mock2Enum) {
        this.mock2Integer = mock2Integer;
        this.mock2BigDecimal = Mock2.this.mock2BigDecimal;
        this.mock2Enum = mock2Enum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mock2 mock2 = (Mock2) o;
        return Objects.equals(mock2Integer, mock2.mock2Integer) && Objects.equals(mock1, mock2.mock1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mock2Integer, mock1);
    }

    public enum Mock2Enum {
        ENUM1, ENUM2, ENUM3
    }
}
