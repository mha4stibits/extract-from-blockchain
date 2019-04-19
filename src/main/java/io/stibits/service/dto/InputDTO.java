package io.stibits.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Input entity.
 */
public class InputDTO implements Serializable {

    private Long id;

    private Long sequence;

    private String scriptSignature;


    private Long transactionId;

    private Long previousOutputId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getScriptSignature() {
        return scriptSignature;
    }

    public void setScriptSignature(String scriptSignature) {
        this.scriptSignature = scriptSignature;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getPreviousOutputId() {
        return previousOutputId;
    }

    public void setPreviousOutputId(Long outputId) {
        this.previousOutputId = outputId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InputDTO inputDTO = (InputDTO) o;
        if (inputDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), inputDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InputDTO{" +
            "id=" + getId() +
            ", sequence=" + getSequence() +
            ", scriptSignature='" + getScriptSignature() + "'" +
            ", transaction=" + getTransactionId() +
            ", previousOutput=" + getPreviousOutputId() +
            "}";
    }
}
