package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {

    public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {

        if (salesId == null) {
            return;
        }

        Sales sales = getSales(salesId);
        if (!isDateValid(sales)) {
            return;
        }

        List<SalesReportData> reportDataList = getSalesReportDate(sales);
        List<SalesReportData> filteredReportDataList = getReportData(reportDataList, maxRow);
        List<String> headers = getHeadersWithNatTrade(isNatTrade);

        SalesActivityReport report = this.generateReport(headers, reportDataList);
        uploadDocument(report);
    }


    public boolean isDateValid(Sales sales) {
        Date today = new Date();
        if (today.after(sales.getEffectiveTo())
                || today.before(sales.getEffectiveFrom())) {
            return false;
        }
        return true;
    }

    public SalesDao getSalesDao() {
        return new SalesDaoImpl();
    }

    public Sales getSales(String salesId) {
        return getSalesDao().getSalesBySalesId(salesId);
    }

    public SalesReportDao getSalesReportDao() {
        return new SalesReportDaoImpl();
    }

    public List<SalesReportData> getSalesReportDate(Sales sales) {
        return getSalesReportDao().getReportData(sales);
    }

    public List<SalesReportData> getReportData(List<SalesReportData> reportDataList, int maxRow) {
        List<SalesReportData> tempList = new ArrayList<>();
        for (int i = 0; i < reportDataList.size() || i < maxRow; i++) {
            tempList.add(reportDataList.get(i));
        }
        return tempList;
    }

    public List<String> getHeadersWithNatTrade(boolean isNatTrade) {
        List<String> headers = new ArrayList<>();
        if (isNatTrade) {
            headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
        } else {
            headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
        }
        return headers;
    }

    public EcmService getEcmService() {
        return new EcmService();
    }

    public void uploadDocument(SalesActivityReport report) {
        getEcmService().uploadDocument(report.toXml());
    }

    protected SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
        // TODO Auto-generated method stub
        return null;
    }

}
