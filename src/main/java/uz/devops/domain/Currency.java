package uz.devops.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Currency.
 */
@Entity
@Table(name = "currency")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "ccy")
    private String ccy;

    @Column(name = "ccy_nm_ru")
    private String ccyNmRU;

    @Column(name = "ccy_nm_uz")
    private String ccyNmUZ;

    @Column(name = "ccy_nm_uzc")
    private String ccyNmUZC;

    @Column(name = "ccy_nm_en")
    private String ccyNmEN;

    @Column(name = "nominal")
    private String nominal;

    @Column(name = "rate")
    private String rate;

    @Column(name = "diff")
    private String diff;

    @Column(name = "date")
    private String date;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Currency id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Currency code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCcy() {
        return this.ccy;
    }

    public Currency ccy(String ccy) {
        this.setCcy(ccy);
        return this;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getCcyNmRU() {
        return this.ccyNmRU;
    }

    public Currency ccyNmRU(String ccyNmRU) {
        this.setCcyNmRU(ccyNmRU);
        return this;
    }

    public void setCcyNmRU(String ccyNmRU) {
        this.ccyNmRU = ccyNmRU;
    }

    public String getCcyNmUZ() {
        return this.ccyNmUZ;
    }

    public Currency ccyNmUZ(String ccyNmUZ) {
        this.setCcyNmUZ(ccyNmUZ);
        return this;
    }

    public void setCcyNmUZ(String ccyNmUZ) {
        this.ccyNmUZ = ccyNmUZ;
    }

    public String getCcyNmUZC() {
        return this.ccyNmUZC;
    }

    public Currency ccyNmUZC(String ccyNmUZC) {
        this.setCcyNmUZC(ccyNmUZC);
        return this;
    }

    public void setCcyNmUZC(String ccyNmUZC) {
        this.ccyNmUZC = ccyNmUZC;
    }

    public String getCcyNmEN() {
        return this.ccyNmEN;
    }

    public Currency ccyNmEN(String ccyNmEN) {
        this.setCcyNmEN(ccyNmEN);
        return this;
    }

    public void setCcyNmEN(String ccyNmEN) {
        this.ccyNmEN = ccyNmEN;
    }

    public String getNominal() {
        return this.nominal;
    }

    public Currency nominal(String nominal) {
        this.setNominal(nominal);
        return this;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getRate() {
        return this.rate;
    }

    public Currency rate(String rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDiff() {
        return this.diff;
    }

    public Currency diff(String diff) {
        this.setDiff(diff);
        return this;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getDate() {
        return this.date;
    }

    public Currency date(String date) {
        this.setDate(date);
        return this;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Currency)) {
            return false;
        }
        return id != null && id.equals(((Currency) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Currency{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", ccy='" + getCcy() + "'" +
            ", ccyNmRU='" + getCcyNmRU() + "'" +
            ", ccyNmUZ='" + getCcyNmUZ() + "'" +
            ", ccyNmUZC='" + getCcyNmUZC() + "'" +
            ", ccyNmEN='" + getCcyNmEN() + "'" +
            ", nominal='" + getNominal() + "'" +
            ", rate='" + getRate() + "'" +
            ", diff='" + getDiff() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
