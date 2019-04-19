package io.stibits.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Transaction entity.
 */
public class TransactionDTO implements Serializable {

    private Long id;

    private Boolean doubleSpend;

    private Long blockHeight;

    private Long time;

    private Long lockTime;

    private String relayedBy;

    private String hash;

    private Long index;

    private Integer version;

    private Long size;


    private Long blockId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDoubleSpend() {
        return doubleSpend;
    }

    public void setDoubleSpend(Boolean doubleSpend) {
        this.doubleSpend = doubleSpend;
    }

    public Long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getLockTime() {
        return lockTime;
    }

    public void setLockTime(Long lockTime) {
        this.lockTime = lockTime;
    }

    public String getRelayedBy() {
        return relayedBy;
    }

    public void setRelayedBy(String relayedBy) {
        this.relayedBy = relayedBy;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;
        if (transactionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
            "id=" + getId() +
            ", doubleSpend=" + getDoubleSpend() +
            ", blockHeight=" + getBlockHeight() +
            ", time=" + getTime() +
            ", lockTime=" + getLockTime() +
            ", relayedBy='" + getRelayedBy() + "'" +
            ", hash='" + getHash() + "'" +
            ", index=" + getIndex() +
            ", version=" + getVersion() +
            ", size=" + getSize() +
            ", block=" + getBlockId() +
            "}";
    }
}
