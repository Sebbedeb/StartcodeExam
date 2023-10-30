package dk.lyngby.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter

@NoArgsConstructor
@Entity
@Table(name = "mock1")
public class Mock1
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mock1_id", nullable = false, unique = true)
    private Integer id;

    @Setter
    @Column(name = "mock1_name", nullable = false, unique = true)
    private String mock1String;


    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "mock1_enum", nullable = false)
    private Mock1Enum mock1enum;

    @OneToMany(mappedBy = "mock1", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Mock2> mock2s = new HashSet<>();

    public Mock1(String mock1String, Mock1Enum mock1Enum) {
        this.mock1String = mock1String;
        this.mock1enum = mock1Enum;
    }

    public void setMock2s(Set<Mock2> mock2s) {
        if(mock2s != null) {
            this.mock2s = mock2s;
            for (Mock2 mock2 : mock2s) {
                mock2.setMock1(this);
            }
        }
    }

    public void addMock2(Mock2 mock2) {
        if ( mock2 != null) {
            this.mock2s.add(mock2);
            mock2.setMock1(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mock1 mock1 = (Mock1) o;
        return Objects.equals(mock1String, mock1.mock1String);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mock1String);
    }

    public enum Mock1Enum {
        ENUM1, ENUM2, ENUM3
    }
}
