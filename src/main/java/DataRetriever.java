import java.math.BigDecimal;
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

    public Double computeWeightedTurnover() {
        DBConnection conn = new DBConnection();

        String sql = """
                    SELECT SUM(
                    CASE
                        WHEN i.status = 'PAID' THEN il.quantity * il.unit_price
                        WHEN i.status = 'CONFIRMED' THEN (il.quantity * il.unit_price) * 0.5
                        ELSE 0
                    END
                ) as weighted_total
                FROM invoice i
                JOIN invoice_line il ON i.id = il.invoice_id;
                                """;

        try {
            PreparedStatement ps = conn.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            Double wheightedTotal = 0.0;

            while (rs.next()) {
                wheightedTotal = rs.getDouble("weighted_total");
            }
            return wheightedTotal;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<InvoiceTaxSummary> findInvoiceTaxSummaries() {
        DBConnection conn = new DBConnection();

        String sql = """
                    SELECT
                    i.id,
                    SUM(il.quantity * il.unit_price) as ht,
                    SUM((il.quantity * il.unit_price) * (tc.rate / 100)) as tva,
                    SUM(il.quantity * il.unit_price) * (1 + tc.rate / 100) as ttc
                FROM invoice i
                JOIN invoice_line il ON i.id = il.invoice_id
                JOIN tax_config tc ON tc.label = 'TVA STANDARD'
                GROUP BY i.id,tc.rate
                ORDER BY i.id;
                                """;

        List<InvoiceTaxSummary> taxSummaries = new ArrayList<>();

        try {
            PreparedStatement ps = conn.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                InvoiceTaxSummary taxSummary = new InvoiceTaxSummary(
                        rs.getDouble("ht"),
                        rs.getDouble("tva"),
                        rs.getDouble("ttc"));
                taxSummaries.add(taxSummary);
            }
            return taxSummaries;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  BigDecimal computeWeightedTurnoverTtc() {
        DBConnection conn = new DBConnection();
        String sql = """
                    SELECT SUM(
                    CASE
                        WHEN i.status = 'PAID' THEN (il.quantity * il.unit_price) * (1 + tc.rate / 100)
                        WHEN i.status = 'CONFIRMED' THEN ((il.quantity * il.unit_price) * 0.5) * (1 + tc.rate / 100)
                        ELSE 0
                    END
                ) as weighted_total_ttc
                FROM invoice i
                JOIN invoice_line il ON i.id = il.invoice_id
                JOIN tax_config tc ON tc.label = 'TVA STANDARD';
                                """;
        BigDecimal weightedTotalTtc = BigDecimal.ZERO;
        try {
            PreparedStatement ps = conn.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                weightedTotalTtc = rs.getBigDecimal("weighted_total_ttc");
            }
            return weightedTotalTtc;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
