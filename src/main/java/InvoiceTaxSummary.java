public class InvoiceTaxSummary {
    private Double ht;
    private Double tva;
    private Double ttc;

    public InvoiceTaxSummary() {
    }

    public InvoiceTaxSummary(Double ht, Double tva, Double ttc) {
        this.ht = ht;
        this.tva = tva;
        this.ttc = ttc;
    }

    public Double getHT() {
        return ht;
    }

    public void setHT(Double ht) {
        this.ht = ht;
    }

    public Double getTVA() {
        return tva;
    }

    public void setTVA(Double tva) {
        this.tva = tva;
    }

    public Double getTTC() {
        return ttc;
    }

    public void setTTC(Double ttc) {
        this.ttc = ttc;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ht == null) ? 0 : ht.hashCode());
        result = prime * result + ((tva == null) ? 0 : tva.hashCode());
        result = prime * result + ((ttc == null) ? 0 : ttc.hashCode());
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
        InvoiceTaxSummary other = (InvoiceTaxSummary) obj;
        if (ht == null) {
            if (other.ht != null)
                return false;
        } else if (!ht.equals(other.ht))
            return false;
        if (tva == null) {
            if (other.tva != null)
                return false;
        } else if (!tva.equals(other.tva))
            return false;
        if (ttc == null) {
            if (other.ttc != null)
                return false;
        } else if (!ttc.equals(other.ttc))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "InvoiceTaxSummary [HT=" + ht + ", TVA=" + tva + ", TTC=" + ttc + "]";
    }
    
}
