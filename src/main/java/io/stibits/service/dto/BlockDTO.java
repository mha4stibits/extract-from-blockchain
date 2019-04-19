package io.stibits.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Block entity.
 */
public class BlockDTO implements Serializable {

    private Long id;

    private Integer version;

    private String previousBlockHash;

    private String merkleRoot;

    private Long bits;

    private Long fees;

    private Long nonce;

    private Long size;

    private Long index;

    private Long receivedTime;

    private String relayedBy;

    private Long height;

    private String hash;

    private Long time;

    private Boolean mainChain;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public Long getBits() {
        return bits;
    }

    public void setBits(Long bits) {
        this.bits = bits;
    }

    public Long getFees() {
        return fees;
    }

    public void setFees(Long fees) {
        this.fees = fees;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getRelayedBy() {
        return relayedBy;
    }

    public void setRelayedBy(String relayedBy) {
        this.relayedBy = relayedBy;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Boolean isMainChain() {
        return mainChain;
    }

    public void setMainChain(Boolean mainChain) {
        this.mainChain = mainChain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BlockDTO blockDTO = (BlockDTO) o;
        if (blockDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), blockDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BlockDTO{" +
            "id=" + getId() +
            ", version=" + getVersion() +
            ", previousBlockHash='" + getPreviousBlockHash() + "'" +
            ", merkleRoot='" + getMerkleRoot() + "'" +
            ", bits=" + getBits() +
            ", fees=" + getFees() +
            ", nonce=" + getNonce() +
            ", size=" + getSize() +
            ", index=" + getIndex() +
            ", receivedTime=" + getReceivedTime() +
            ", relayedBy='" + getRelayedBy() + "'" +
            ", height=" + getHeight() +
            ", hash='" + getHash() + "'" +
            ", time='" + getTime() + "'" +
            ", mainChain='" + isMainChain() + "'" +
            "}";
    }
}
