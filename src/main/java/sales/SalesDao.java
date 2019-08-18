package sales;

import java.util.List;

public interface SalesDao {
    Sales getSalesBySalesId(String salesId);
    List<SalesReportData> getReportData(Sales sales);
}
