package io.stibits.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Input.
 */
@Entity
@Table(name = "input")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Input implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_sequence")
    private Long sequence;

    @Column(name = "script_signature")
    private String scriptSignature;

    @ManyToOne
    @JsonIgnoreProperties("inputs")
    private Transaction transaction;

    @OneToOne
    @JoinColumn(unique = true)
    private Output previousOutput;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSequence() {
        return sequence;
    }

    public Input sequence(Long sequence) {
        this.sequence = sequence;
        return this;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getScriptSignature() {
        return scriptSignature;
    }

    public Input scriptSignature(String scriptSignature) {
        this.scriptSignature = scriptSignature;
        return this;
    }

    public void setScriptSignature(String scriptSignature) {
        this.scriptSignature = scriptSignature;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Input transaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Output getPreviousOutput() {
        return previousOutput;
    }

    public Input previousOutput(Output output) {
        this.previousOutput = output;
        return this;
    }

    public void setPreviousOutput(Output output) {
        this.previousOutput = output;
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
        Input input = (Input) o;
        if (input.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), input.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Input{" +
            "id=" + getId() +
            ", sequence=" + getSequence() +
            ", scriptSignature='" + getScriptSignature() + "'" +
            "}";
    }
}
