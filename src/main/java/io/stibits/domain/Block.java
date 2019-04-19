package io.stibits.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Block.
 */
@Entity
@Table(name = "block")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Block implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "version")
    private Integer version;

    @Column(name = "previous_block_hash")
    private String previousBlockHash;

    @Column(name = "merkle_root")
    private String merkleRoot;

    @Column(name = "bits")
    private Long bits;

    @Column(name = "fees")
    private Long fees;

    @Column(name = "nonce")
    private Long nonce;

    @Column(name = "size")
    private Long size;

    @Column(name = "index")
    private Long index;

    @Column(name = "received_time")
    private Long receivedTime;

    @Column(name = "relayed_by")
    private String relayedBy;

    @Column(name = "height")
    private Long height;

    @Column(name = "hash",unique = true)
    private String hash;

    @Column(name = "time")
    private Long time;

    @Column(name = "main_chain")
    private Boolean mainChain;

    @OneToMany(mappedBy = "block")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Transaction> transactions = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public Block version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public Block previousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
        return this;
    }

    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public Block merkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
        return this;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public Long getBits() {
        return bits;
    }

    public Block bits(Long bits) {
        this.bits = bits;
        return this;
    }

    public void setBits(Long bits) {
        this.bits = bits;
    }

    public Long getFees() {
        return fees;
    }

    public Block fees(Long fees) {
        this.fees = fees;
        return this;
    }

    public void setFees(Long fees) {
        this.fees = fees;
    }

    public Long getNonce() {
        return nonce;
    }

    public Block nonce(Long nonce) {
        this.nonce = nonce;
        return this;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public Long getSize() {
        return size;
    }

    public Block size(Long size) {
        this.size = size;
        return this;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getIndex() {
        return index;
    }

    public Block index(Long index) {
        this.index = index;
        return this;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getReceivedTime() {
        return receivedTime;
    }

    public Block receivedTime(Long receivedTime) {
        this.receivedTime = receivedTime;
        return this;
    }

    public void setReceivedTime(Long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getRelayedBy() {
        return relayedBy;
    }

    public Block relayedBy(String relayedBy) {
        this.relayedBy = relayedBy;
        return this;
    }

    public void setRelayedBy(String relayedBy) {
        this.relayedBy = relayedBy;
    }

    public Long getHeight() {
        return height;
    }

    public Block height(Long height) {
        this.height = height;
        return this;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public String getHash() {
        return hash;
    }

    public Block hash(String hash) {
        this.hash = hash;
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getTime() {
        return time;
    }

    public Block time(Long time) {
        this.time = time;
        return this;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Boolean isMainChain() {
        return mainChain;
    }

    public Block mainChain(Boolean mainChain) {
        this.mainChain = mainChain;
        return this;
    }

    public void setMainChain(Boolean mainChain) {
        this.mainChain = mainChain;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public Block transactions(Set<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public Block addTransactions(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setBlock(this);
        return this;
    }

    public Block removeTransactions(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setBlock(null);
        return this;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Block block = (Block) o;
        if (block.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), block.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Block{" +
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
