package uz.devops.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link uz.devops.domain.Currency} entity. This class is used
 * in {@link uz.devops.web.rest.CurrencyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /currencies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CurrencyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter ccy;

    private StringFilter ccyNmRU;

    private StringFilter ccyNmUZ;

    private StringFilter ccyNmUZC;

    private StringFilter ccyNmEN;

    private StringFilter nominal;

    private StringFilter rate;

    private StringFilter diff;

    private InstantFilter date;

    private Boolean distinct;

    public CurrencyCriteria() {}

    public CurrencyCriteria(CurrencyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.ccy = other.ccy == null ? null : other.ccy.copy();
        this.ccyNmRU = other.ccyNmRU == null ? null : other.ccyNmRU.copy();
        this.ccyNmUZ = other.ccyNmUZ == null ? null : other.ccyNmUZ.copy();
        this.ccyNmUZC = other.ccyNmUZC == null ? null : other.ccyNmUZC.copy();
        this.ccyNmEN = other.ccyNmEN == null ? null : other.ccyNmEN.copy();
        this.nominal = other.nominal == null ? null : other.nominal.copy();
        this.rate = other.rate == null ? null : other.rate.copy();
        this.diff = other.diff == null ? null : other.diff.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CurrencyCriteria copy() {
        return new CurrencyCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getCcy() {
        return ccy;
    }

    public StringFilter ccy() {
        if (ccy == null) {
            ccy = new StringFilter();
        }
        return ccy;
    }

    public void setCcy(StringFilter ccy) {
        this.ccy = ccy;
    }

    public StringFilter getCcyNmRU() {
        return ccyNmRU;
    }

    public StringFilter ccyNmRU() {
        if (ccyNmRU == null) {
            ccyNmRU = new StringFilter();
        }
        return ccyNmRU;
    }

    public void setCcyNmRU(StringFilter ccyNmRU) {
        this.ccyNmRU = ccyNmRU;
    }

    public StringFilter getCcyNmUZ() {
        return ccyNmUZ;
    }

    public StringFilter ccyNmUZ() {
        if (ccyNmUZ == null) {
            ccyNmUZ = new StringFilter();
        }
        return ccyNmUZ;
    }

    public void setCcyNmUZ(StringFilter ccyNmUZ) {
        this.ccyNmUZ = ccyNmUZ;
    }

    public StringFilter getCcyNmUZC() {
        return ccyNmUZC;
    }

    public StringFilter ccyNmUZC() {
        if (ccyNmUZC == null) {
            ccyNmUZC = new StringFilter();
        }
        return ccyNmUZC;
    }

    public void setCcyNmUZC(StringFilter ccyNmUZC) {
        this.ccyNmUZC = ccyNmUZC;
    }

    public StringFilter getCcyNmEN() {
        return ccyNmEN;
    }

    public StringFilter ccyNmEN() {
        if (ccyNmEN == null) {
            ccyNmEN = new StringFilter();
        }
        return ccyNmEN;
    }

    public void setCcyNmEN(StringFilter ccyNmEN) {
        this.ccyNmEN = ccyNmEN;
    }

    public StringFilter getNominal() {
        return nominal;
    }

    public StringFilter nominal() {
        if (nominal == null) {
            nominal = new StringFilter();
        }
        return nominal;
    }

    public void setNominal(StringFilter nominal) {
        this.nominal = nominal;
    }

    public StringFilter getRate() {
        return rate;
    }

    public StringFilter rate() {
        if (rate == null) {
            rate = new StringFilter();
        }
        return rate;
    }

    public void setRate(StringFilter rate) {
        this.rate = rate;
    }

    public StringFilter getDiff() {
        return diff;
    }

    public StringFilter diff() {
        if (diff == null) {
            diff = new StringFilter();
        }
        return diff;
    }

    public void setDiff(StringFilter diff) {
        this.diff = diff;
    }

    public InstantFilter getDate() {
        return date;
    }

    public InstantFilter date() {
        if (date == null) {
            date = new InstantFilter();
        }
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CurrencyCriteria that = (CurrencyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(ccy, that.ccy) &&
            Objects.equals(ccyNmRU, that.ccyNmRU) &&
            Objects.equals(ccyNmUZ, that.ccyNmUZ) &&
            Objects.equals(ccyNmUZC, that.ccyNmUZC) &&
            Objects.equals(ccyNmEN, that.ccyNmEN) &&
            Objects.equals(nominal, that.nominal) &&
            Objects.equals(rate, that.rate) &&
            Objects.equals(diff, that.diff) &&
            Objects.equals(date, that.date) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, ccy, ccyNmRU, ccyNmUZ, ccyNmUZC, ccyNmEN, nominal, rate, diff, date, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (ccy != null ? "ccy=" + ccy + ", " : "") +
            (ccyNmRU != null ? "ccyNmRU=" + ccyNmRU + ", " : "") +
            (ccyNmUZ != null ? "ccyNmUZ=" + ccyNmUZ + ", " : "") +
            (ccyNmUZC != null ? "ccyNmUZC=" + ccyNmUZC + ", " : "") +
            (ccyNmEN != null ? "ccyNmEN=" + ccyNmEN + ", " : "") +
            (nominal != null ? "nominal=" + nominal + ", " : "") +
            (rate != null ? "rate=" + rate + ", " : "") +
            (diff != null ? "diff=" + diff + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
