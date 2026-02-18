import java.math.BigDecimal;

public class InvoiceStatusTotals {
    private Double totalPaid;
    private Double totalConfirmed;
    private Double totalDraft;
    
    public InvoiceStatusTotals() {
    }

    public InvoiceStatusTotals(Double totalPaid, Double totalConfirmed, Double totalDraft) {
        this.totalPaid = totalPaid;
        this.totalConfirmed = totalConfirmed;
        this.totalDraft = totalDraft;
    }

    public Double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(Double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public Double getTotalConfirmed() {
        return totalConfirmed;
    }

    public void setTotalConfirmed(Double totalConfirmed) {
        this.totalConfirmed = totalConfirmed;
    }

    public Double getTotalDraft() {
        return totalDraft;
    }

    public void setTotalDraft(Double totalDraft) {
        this.totalDraft = totalDraft;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((totalPaid == null) ? 0 : totalPaid.hashCode());
        result = prime * result + ((totalConfirmed == null) ? 0 : totalConfirmed.hashCode());
        result = prime * result + ((totalDraft == null) ? 0 : totalDraft.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InvoiceStatusTotals other = (InvoiceStatusTotals) obj;
        if (totalPaid == null) {
            if (other.totalPaid != null)
                return false;
        } else if (!totalPaid.equals(other.totalPaid))
            return false;
        if (totalConfirmed == null) {
            if (other.totalConfirmed != null)
                return false;
        } else if (!totalConfirmed.equals(other.totalConfirmed))
            return false;
        if (totalDraft == null) {
            if (other.totalDraft != null)
                return false;
        } else if (!totalDraft.equals(other.totalDraft))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "InvoiceStatusTotals [totalPaid=" + totalPaid + ", totalConfirmed=" + totalConfirmed + ", totalDraft="
                + totalDraft + "]";
    }

    
}