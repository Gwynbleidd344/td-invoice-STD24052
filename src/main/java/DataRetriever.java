import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    public List<InvoiceTotal> findInvoiceTotals() {
        DBConnection conn = new DBConnection();
        String sql = """
                    SELECT i.id, i.customer_name, i.status, SUM(il.quantity * il.unit_price) as total
                FROM invoice i
                JOIN invoice_line il ON i.id = il.invoice_id
                GROUP BY i.id, i.customer_name, i.status
                ORDER BY i.id;
                    """;
        List<InvoiceTotal> invoiceTotals = new ArrayList<>();
        try {
            PreparedStatement ps = conn.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                InvoiceTotal invoiceTotal = new InvoiceTotal(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        InvoiceStatus.valueOf(rs.getString("status")),
                        rs.getDouble("total"));
                invoiceTotals.add(invoiceTotal);
            }
            return invoiceTotals;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<InvoiceTotal> findConfirmedAndPaidInvoiceTotals() {
        DBConnection conn = new DBConnection();
        String sql = """
                    SELECT i.id, i.customer_name, i.status, SUM(il.quantity * il.unit_price) as total
                FROM invoice i
                JOIN invoice_line il ON i.id = il.invoice_id
                WHERE i.status IN ('CONFIRMED', 'PAID')
                GROUP BY i.id, i.customer_name, i.status
                ORDER BY i.id;
                    """;
        List<InvoiceTotal> invoiceTotals = new ArrayList<>();
        try {
            PreparedStatement ps = conn.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                InvoiceTotal invoiceTotal = new InvoiceTotal(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        InvoiceStatus.valueOf(rs.getString("status")),
                        rs.getDouble("total"));
                invoiceTotals.add(invoiceTotal);
            }
            return invoiceTotals;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InvoiceStatusTotals computeStatusTotals() {
        DBConnection conn = new DBConnection();

        String sql = """
                SELECT
                    SUM(CASE WHEN i.status = 'PAID' THEN il.quantity * il.unit_price ELSE 0 END) as total_paid,
                    SUM(CASE WHEN i.status = 'CONFIRMED' THEN il.quantity * il.unit_price ELSE 0 END) as total_confirmed,
                    SUM(CASE WHEN i.status = 'DRAFT' THEN il.quantity * il.unit_price ELSE 0 END) as total_draft
                FROM invoice i
                JOIN invoice_line il ON i.id = il.invoice_id;
                            """;

        InvoiceStatusTotals statusTotals = new InvoiceStatusTotals();

        try {
            PreparedStatement ps = conn.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                statusTotals.setTotalPaid(rs.getDouble("total_paid"));
                statusTotals.setTotalConfirmed(rs.getDouble("total_confirmed"));
                statusTotals.setTotalDraft(rs.getDouble("total_draft"));
            }
            return statusTotals;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
