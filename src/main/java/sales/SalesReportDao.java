package sales;

import java.util.List;

public interface SalesReportDao {

    List<SalesReportData> getReportData(Sales sales);

}
