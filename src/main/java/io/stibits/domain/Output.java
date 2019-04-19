package io.stibits.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Output.
 */
@Entity
@Table(name = "output")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Output implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "n")
    private Integer n;

    @Column(name = "jhi_value")
    private Long value;

    @Column(name = "address")
    private String address;

    @Column(name = "tx_index")
    private Long txIndex;

    @Column(name = "script")
    private String script;

    @Column(name = "spent")
    private Boolean spent;

    @Column(name = "spent_to_address")
    private Boolean spentToAddress;

    @ManyToOne
    @JsonIgnoreProperties("outputs")
    private Transaction transaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getN() {
        return n;
    }

    public Output n(Integer n) {
        this.n = n;
        return this;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Long getValue() {
        return value;
    }

    public Output value(Long value) {
        this.value = value;
        return this;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public Output address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getTxIndex() {
        return txIndex;
    }

    public Output txIndex(Long txIndex) {
        this.txIndex = txIndex;
        return this;
    }

    public void setTxIndex(Long txIndex) {
        this.txIndex = txIndex;
    }

    public String getScript() {
        return script;
    }

    public Output script(String script) {
        this.script = script;
        return this;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Boolean isSpent() {
        return spent;
    }

    public Output spent(Boolean spent) {
        this.spent = spent;
        return this;
    }

    public void setSpent(Boolean spent) {
        this.spent = spent;
    }

    public Boolean isSpentToAddress() {
        return spentToAddress;
    }

    public Output spentToAddress(Boolean spentToAddress) {
        this.spentToAddress = spentToAddress;
        return this;
    }

    public void setSpentToAddress(Boolean spentToAddress) {
        this.spentToAddress = spentToAddress;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Output transaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
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
        Output output = (Output) o;
        if (output.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), output.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Output{" +
            "id=" + getId() +
            ", n=" + getN() +
            ", value=" + getValue() +
            ", address='" + getAddress() + "'" +
            ", txIndex=" + getTxIndex() +
            ", script='" + getScript() + "'" +
            ", spent='" + isSpent() + "'" +
            ", spentToAddress='" + isSpentToAddress() + "'" +
            "}";
    }
}
