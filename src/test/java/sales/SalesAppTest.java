package sales;

import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.spy;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(JMockit.class)
public class SalesAppTest {

    @Test
    public void should_call_when_call_getSales_given_id() {
        SalesApp salesApp = spy(new SalesApp());

        salesApp.getSales("DUMMY");

        verify(salesApp, times(1)).getSales("DUMMY");
    }

    @Test
    public void should_throws_exception_when_invoke_isDateValid() {
        SalesApp salesApp = spy(new SalesApp());

        assertThrows(NullPointerException.class, () -> salesApp.generateSalesActivityReport("DUMMY", 1000, false, false));
    }

    @Test
    public void should_call_when_call_isDateValid_given_sale() {
        SalesApp salesApp = spy(new SalesApp());
        Sales sales = mock(Sales.class);

        when(sales.getEffectiveTo()).thenReturn(new Date());
        doReturn(sales).when(salesApp).getSales(any());
        salesApp.isDateValid(sales);

        verify(sales, times(1)).getEffectiveTo();
    }

    @Test
    public void should_call_when_call_getSalesReportDate() {
        SalesApp salesApp = spy(new SalesApp());

        salesApp.getSalesReportDate(any());

        verify(salesApp, times(1)).getSalesReportDate(any());
    }

    @Test
    public void should_return_sizeOf3_when_call_getReportData_given_maxRow_2_and_size_3() {
        SalesApp salesApp = spy(new SalesApp());
        SalesReportData sales = new SalesReportData();
        List<SalesReportData> reportDataList = Arrays.asList(sales, sales, sales);

        List<SalesReportData> result = salesApp.getReportData(reportDataList, 2);

        verify(salesApp, times(1)).getReportData(reportDataList, 2);
        assertEquals(result.size(), 3);
    }

    @Test
    public void should_return_sizeOf4_when_call_getHeadersWithNatTrade() {
        SalesApp salesApp = spy(new SalesApp());

        List<String> result = salesApp.getHeadersWithNatTrade(true);

        verify(salesApp, times(1)).getHeadersWithNatTrade(anyBoolean());
        assertEquals(result.size(), 4);
        assertEquals(result.get(3), "Time");
    }

    @Test
    public void should_run_correct_when_call_whole_process_given_correct_params() throws ParseException {
        SalesApp salesApp = spy(new SalesApp());
        Sales sales = mock(Sales.class);
        SalesDao salesDao = mock(SalesDao.class);
        SalesReportDao salesReportDao = mock(SalesReportDao.class);
        EcmService ecmService = mock(EcmService.class);
        SalesActivityReport salesActivityReport = mock(SalesActivityReport.class);
        List<SalesReportData> salesReportData = Arrays.asList(new SalesReportData());

        when(sales.getEffectiveTo()).thenReturn(new SimpleDateFormat("YYYY-MM-DD hh:mm:ss").parse("2020-01-01 00:00:00"));
        when(sales.getEffectiveFrom()).thenReturn(new SimpleDateFormat("YYYY-MM-DD hh:mm:ss").parse("2010-01-01 00:00:00"));
        when(salesDao.getSalesBySalesId(any())).thenReturn(sales);
        when(salesReportDao.getReportData(sales)).thenReturn(salesReportData);
        when(salesActivityReport.toXml()).thenReturn("");

        doReturn(salesDao).when(salesApp).getSalesDao();
        doReturn(salesReportDao).when(salesApp).getSalesReportDao();
        doReturn(salesActivityReport).when(salesApp).generateReport(any(), any());
        doReturn(ecmService).when(salesApp).getEcmService();

        salesApp.generateSalesActivityReport("DUMMY", 1, false, false);
        verify(sales, times(1)).getEffectiveTo();
        verify(sales, times(1)).getEffectiveFrom();
        verify(salesApp, times(1)).getSales("DUMMY");
        verify(salesApp, times(1)).uploadDocument(any());
        verify(salesApp, times(1)).getReportData(salesReportData, 1);
        verify(salesApp, times(1)).uploadDocument(salesActivityReport);
        assertEquals(true, salesApp.isDateValid(sales));
    }
}
