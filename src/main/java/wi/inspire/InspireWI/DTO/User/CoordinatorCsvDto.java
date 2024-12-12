package wi.inspire.InspireWI.DTO;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class CoordinatorCsvDto {
    @CsvBindByName(column = "Coordinator")
    private String name;

    @CsvBindByName(column = "Email")
    private String email;

    @CsvBindByName(column = "Serving County")
    private String servingCounty;

    @CsvBindByName(column = "Schools")
    private String schools;
}