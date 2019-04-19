package io.stibits.web.rest;

import io.stibits.BlockChainApp;

import io.stibits.domain.Transaction;
import io.stibits.repository.TransactionRepository;
import io.stibits.service.TransactionService;
import io.stibits.service.dto.TransactionDTO;
import io.stibits.service.mapper.TransactionMapper;
import io.stibits.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static io.stibits.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TransactionResource REST controller.
 *
 * @see TransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlockChainApp.class)
public class TransactionResourceIntTest {

    private static final Boolean DEFAULT_DOUBLE_SPEND = Boolean.TRUE;
    private static final Boolean UPDATED_DOUBLE_SPEND = Boolean.FALSE;

    private static final Long DEFAULT_BLOCK_HEIGHT = 1L;
    private static final Long UPDATED_BLOCK_HEIGHT = 2L;

    private static final Long DEFAULT_TIME = 1L;
    private static final Long UPDATED_TIME = 2L;

    private static final Long DEFAULT_LOCK_TIME = 1L;
    private static final Long UPDATED_LOCK_TIME = 2L;

    private static final String DEFAULT_RELAYED_BY = "AAAAAAAAAA";
    private static final String UPDATED_RELAYED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final Long DEFAULT_INDEX = 1L;
    private static final Long UPDATED_INDEX = 2L;

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final Long DEFAULT_SIZE = 1L;
    private static final Long UPDATED_SIZE = 2L;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTransactionMockMvc;

    private Transaction transaction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionResource transactionResource = new TransactionResource(transactionService);
        this.restTransactionMockMvc = MockMvcBuilders.standaloneSetup(transactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .doubleSpend(DEFAULT_DOUBLE_SPEND)
            .blockHeight(DEFAULT_BLOCK_HEIGHT)
            .time(DEFAULT_TIME)
            .lockTime(DEFAULT_LOCK_TIME)
            .relayedBy(DEFAULT_RELAYED_BY)
            .hash(DEFAULT_HASH)
            .index(DEFAULT_INDEX)
            .version(DEFAULT_VERSION)
            .size(DEFAULT_SIZE);
        return transaction;
    }

    @Before
    public void initTest() {
        transaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);
        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isCreated());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getDoubleSpend()).isEqualTo(DEFAULT_DOUBLE_SPEND);
        assertThat(testTransaction.getBlockHeight()).isEqualTo(DEFAULT_BLOCK_HEIGHT);
        assertThat(testTransaction.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testTransaction.getLockTime()).isEqualTo(DEFAULT_LOCK_TIME);
        assertThat(testTransaction.getRelayedBy()).isEqualTo(DEFAULT_RELAYED_BY);
        assertThat(testTransaction.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testTransaction.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testTransaction.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testTransaction.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @Test
    @Transactional
    public void createTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // Create the Transaction with an existing ID
        transaction.setId(1L);
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTransactions() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList
        restTransactionMockMvc.perform(get("/api/transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].doubleSpend").value(hasItem(DEFAULT_DOUBLE_SPEND)))
            .andExpect(jsonPath("$.[*].blockHeight").value(hasItem(DEFAULT_BLOCK_HEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.intValue())))
            .andExpect(jsonPath("$.[*].lockTime").value(hasItem(DEFAULT_LOCK_TIME.intValue())))
            .andExpect(jsonPath("$.[*].relayedBy").value(hasItem(DEFAULT_RELAYED_BY.toString())))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH.toString())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())));
    }
    
    @Test
    @Transactional
    public void getTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get the transaction
        restTransactionMockMvc.perform(get("/api/transactions/{id}", transaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transaction.getId().intValue()))
            .andExpect(jsonPath("$.doubleSpend").value(DEFAULT_DOUBLE_SPEND))
            .andExpect(jsonPath("$.blockHeight").value(DEFAULT_BLOCK_HEIGHT.intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.intValue()))
            .andExpect(jsonPath("$.lockTime").value(DEFAULT_LOCK_TIME.intValue()))
            .andExpect(jsonPath("$.relayedBy").value(DEFAULT_RELAYED_BY.toString()))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH.toString()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX.intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTransaction() throws Exception {
        // Get the transaction
        restTransactionMockMvc.perform(get("/api/transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).get();
        // Disconnect from session so that the updates on updatedTransaction are not directly saved in db
        em.detach(updatedTransaction);
        updatedTransaction
            .doubleSpend(UPDATED_DOUBLE_SPEND)
            .blockHeight(UPDATED_BLOCK_HEIGHT)
            .time(UPDATED_TIME)
            .lockTime(UPDATED_LOCK_TIME)
            .relayedBy(UPDATED_RELAYED_BY)
            .hash(UPDATED_HASH)
            .index(UPDATED_INDEX)
            .version(UPDATED_VERSION)
            .size(UPDATED_SIZE);
        TransactionDTO transactionDTO = transactionMapper.toDto(updatedTransaction);

        restTransactionMockMvc.perform(put("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getDoubleSpend()).isEqualTo(UPDATED_DOUBLE_SPEND);
        assertThat(testTransaction.getBlockHeight()).isEqualTo(UPDATED_BLOCK_HEIGHT);
        assertThat(testTransaction.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testTransaction.getLockTime()).isEqualTo(UPDATED_LOCK_TIME);
        assertThat(testTransaction.getRelayedBy()).isEqualTo(UPDATED_RELAYED_BY);
        assertThat(testTransaction.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testTransaction.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testTransaction.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testTransaction.getSize()).isEqualTo(UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void updateNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc.perform(put("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeDelete = transactionRepository.findAll().size();

        // Delete the transaction
        restTransactionMockMvc.perform(delete("/api/transactions/{id}", transaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transaction.class);
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        Transaction transaction2 = new Transaction();
        transaction2.setId(transaction1.getId());
        assertThat(transaction1).isEqualTo(transaction2);
        transaction2.setId(2L);
        assertThat(transaction1).isNotEqualTo(transaction2);
        transaction1.setId(null);
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionDTO.class);
        TransactionDTO transactionDTO1 = new TransactionDTO();
        transactionDTO1.setId(1L);
        TransactionDTO transactionDTO2 = new TransactionDTO();
        assertThat(transactionDTO1).isNotEqualTo(transactionDTO2);
        transactionDTO2.setId(transactionDTO1.getId());
        assertThat(transactionDTO1).isEqualTo(transactionDTO2);
        transactionDTO2.setId(2L);
        assertThat(transactionDTO1).isNotEqualTo(transactionDTO2);
        transactionDTO1.setId(null);
        assertThat(transactionDTO1).isNotEqualTo(transactionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionMapper.fromId(null)).isNull();
    }
}
