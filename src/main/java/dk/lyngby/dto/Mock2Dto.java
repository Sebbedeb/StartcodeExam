package dk.lyngby.dto;

import dk.lyngby.model.Mock2;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class Mock2Dto
{
    private Integer mock2Integer;
    private Integer mock2BigDecimal;
    private Mock2.Mock2Enum mock2Enum;

    public Mock2Dto(Mock2 mock2) {
        this.mock2Integer = mock2.getMock2Integer();
        this.mock2BigDecimal = mock2.getMock2BigDecimal().intValue();
        this.mock2Enum = mock2.getMock2Enum();
    }

    public static List<Mock2Dto> toMock2DtoList(List<Mock2> mock2s) {
        return List.of(mock2s.stream().map(Mock2Dto::new).toArray(Mock2Dto[]::new));
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Mock2Dto mock2Dto)) return false;

        if (getMock2Integer() != null ? !getMock2Integer().equals(mock2Dto.getMock2Integer()) : mock2Dto.getMock2Integer() != null)
            return false;
        if (getMock2BigDecimal() != null ? !getMock2BigDecimal().equals(mock2Dto.getMock2BigDecimal()) : mock2Dto.getMock2BigDecimal() != null)
            return false;
        return getMock2Enum() == mock2Dto.getMock2Enum();
    }

    @Override
    public int hashCode()
    {
        int result = getMock2Integer() != null ? getMock2Integer().hashCode() : 0;
        result = 31 * result + (getMock2BigDecimal() != null ? getMock2BigDecimal().hashCode() : 0);
        result = 31 * result + (getMock2Enum() != null ? getMock2Enum().hashCode() : 0);
        return result;
    }
}