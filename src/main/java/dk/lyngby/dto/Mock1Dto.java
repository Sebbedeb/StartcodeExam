package dk.lyngby.dto;

import dk.lyngby.model.Mock1;
import dk.lyngby.model.Mock2;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class Mock1Dto
{

    private Integer id;
    private String mock1String;
    private Mock1.Mock1Enum mock1Enum;
    private Set<Mock2> mock2s = new HashSet<>();

    public Mock1Dto(Mock1 mock1) {
        this.id = mock1.getId();
        this.mock1String = mock1.getMock1String();
        this.mock1Enum = mock1.getMock1enum();
        if (mock1.getMock2s() != null)
        {
            mock1.getMock2s().forEach(mock2 -> mock2s.add(new Mock2(mock2.getMock2Integer(), mock2.getMock2BigDecimal(), mock2.getMock2Enum())));
        }
    }

    public Mock1Dto(String mock1String, Mock1.Mock1Enum mock1Enum)
    {
        this.mock1String = mock1String;
        this.mock1Enum = mock1Enum;
    }

    public static List<Mock1Dto> toMock2DtoList(List<Mock1> mock1s) {
        return mock1s.stream().map(Mock1Dto::new).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Mock1Dto mock1Dto)) return false;

        return getId().equals(mock1Dto.getId());
    }

    @Override
    public int hashCode()
    {
        return getId().hashCode();
    }

}
