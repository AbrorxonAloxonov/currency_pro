package uz.devops.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link uz.devops.domain.Currency} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CurrencyDTO implements Serializable {

    private Long id;

    @JsonProperty("Code")
    private String code;

    @JsonProperty("Ccy")
    private String ccy;

    @JsonProperty("CcyNm_RU")
    private String ccyNmRU;

    @JsonProperty("CcyNm_UZ")
    private String ccyNmUZ;

    @JsonProperty("CcyNm_UZC")
    private String ccyNmUZC;
    @JsonProperty("CcyNm_EN")
    private String ccyNmEN;

    @JsonProperty("Nominal")
    private String nominal;

    @JsonProperty("Rate")
    private String rate;

    @JsonProperty("Diff")
    private String diff;

    @JsonProperty("Date")
    private String date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getCcyNmRU() {
        return ccyNmRU;
    }

    public void setCcyNmRU(String ccyNmRU) {
        this.ccyNmRU = ccyNmRU;
    }

    public String getCcyNmUZ() {
        return ccyNmUZ;
    }

    public void setCcyNmUZ(String ccyNmUZ) {
        this.ccyNmUZ = ccyNmUZ;
    }

    public String getCcyNmUZC() {
        return ccyNmUZC;
    }

    public void setCcyNmUZC(String ccyNmUZC) {
        this.ccyNmUZC = ccyNmUZC;
    }

    public String getCcyNmEN() {
        return ccyNmEN;
    }

    public void setCcyNmEN(String ccyNmEN) {
        this.ccyNmEN = ccyNmEN;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyDTO)) {
            return false;
        }

        CurrencyDTO currencyDTO = (CurrencyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, currencyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyDTO{" +
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
