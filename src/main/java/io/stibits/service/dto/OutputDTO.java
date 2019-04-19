package io.stibits.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Output entity.
 */
public class OutputDTO implements Serializable {

    private Long id;

    private Integer n;

    private Long value;

    private String address;

    private Long txIndex;

    private String script;

    private Boolean spent;

    private Boolean spentToAddress;


    private Long transactionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getTxIndex() {
        return txIndex;
    }

    public void setTxIndex(Long txIndex) {
        this.txIndex = txIndex;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Boolean isSpent() {
        return spent;
    }

    public void setSpent(Boolean spent) {
        this.spent = spent;
    }

    public Boolean isSpentToAddress() {
        return spentToAddress;
    }

    public void setSpentToAddress(Boolean spentToAddress) {
        this.spentToAddress = spentToAddress;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OutputDTO outputDTO = (OutputDTO) o;
        if (outputDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), outputDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OutputDTO{" +
            "id=" + getId() +
            ", n=" + getN() +
            ", value=" + getValue() +
            ", address='" + getAddress() + "'" +
            ", txIndex=" + getTxIndex() +
            ", script='" + getScript() + "'" +
            ", spent='" + isSpent() + "'" +
            ", spentToAddress='" + isSpentToAddress() + "'" +
            ", transaction=" + getTransactionId() +
            "}";
    }
}
