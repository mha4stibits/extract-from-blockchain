package io.stibits.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "double_spend")
    private Boolean doubleSpend;

    @Column(name = "block_height")
    private Long blockHeight;

    @Column(name = "time")
    private Long time;

    @Column(name = "lock_time")
    private Long lockTime;

    @Column(name = "relayed_by")
    private String relayedBy;

    @Column(name = "hash")
    private String hash;

    @Column(name = "index")
    private Long index;

    @Column(name = "version")
    private Integer version;

    @Column(name = "size")
    private Long size;

    @ManyToOne
    @JsonIgnoreProperties("transactions")
    private Block block;

    @OneToMany(mappedBy = "transaction")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Output> outputs = new HashSet<>();
    @OneToMany(mappedBy = "transaction")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Input> inputs = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDoubleSpend() {
        return doubleSpend;
    }

    public Transaction doubleSpend(Boolean doubleSpend) {
        this.doubleSpend = doubleSpend;
        return this;
    }

    public void setDoubleSpend(Boolean doubleSpend) {
        this.doubleSpend = doubleSpend;
    }

    public Long getBlockHeight() {
        return blockHeight;
    }

    public Transaction blockHeight(Long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    public void setBlockHeight(Long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public Long getTime() {
        return time;
    }

    public Transaction time(Long time) {
        this.time = time;
        return this;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getLockTime() {
        return lockTime;
    }

    public Transaction lockTime(Long lockTime) {
        this.lockTime = lockTime;
        return this;
    }

    public void setLockTime(Long lockTime) {
        this.lockTime = lockTime;
    }

    public String getRelayedBy() {
        return relayedBy;
    }

    public Transaction relayedBy(String relayedBy) {
        this.relayedBy = relayedBy;
        return this;
    }

    public void setRelayedBy(String relayedBy) {
        this.relayedBy = relayedBy;
    }

    public String getHash() {
        return hash;
    }

    public Transaction hash(String hash) {
        this.hash = hash;
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getIndex() {
        return index;
    }

    public Transaction index(Long index) {
        this.index = index;
        return this;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Integer getVersion() {
        return version;
    }

    public Transaction version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getSize() {
        return size;
    }

    public Transaction size(Long size) {
        this.size = size;
        return this;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Block getBlock() {
        return block;
    }

    public Transaction block(Block block) {
        this.block = block;
        return this;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Set<Output> getOutputs() {
        return outputs;
    }

    public Transaction outputs(Set<Output> outputs) {
        this.outputs = outputs;
        return this;
    }

    public Transaction addOutputs(Output output) {
        this.outputs.add(output);
        output.setTransaction(this);
        return this;
    }

    public Transaction removeOutputs(Output output) {
        this.outputs.remove(output);
        output.setTransaction(null);
        return this;
    }

    public void setOutputs(Set<Output> outputs) {
        this.outputs = outputs;
    }

    public Set<Input> getInputs() {
        return inputs;
    }

    public Transaction inputs(Set<Input> inputs) {
        this.inputs = inputs;
        return this;
    }

    public Transaction addInputs(Input input) {
        this.inputs.add(input);
        input.setTransaction(this);
        return this;
    }

    public Transaction removeInputs(Input input) {
        this.inputs.remove(input);
        input.setTransaction(null);
        return this;
    }

    public void setInputs(Set<Input> inputs) {
        this.inputs = inputs;
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
        Transaction transaction = (Transaction) o;
        if (transaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Transaction{" +
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
            "}";
    }
}
