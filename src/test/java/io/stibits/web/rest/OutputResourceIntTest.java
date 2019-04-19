package io.stibits.web.rest;

import io.stibits.BlockChainApp;

import io.stibits.domain.Output;
import io.stibits.repository.OutputRepository;
import io.stibits.service.OutputService;
import io.stibits.service.dto.OutputDTO;
import io.stibits.service.mapper.OutputMapper;
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
 * Test class for the OutputResource REST controller.
 *
 * @see OutputResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlockChainApp.class)
public class OutputResourceIntTest {

    private static final Integer DEFAULT_N = 1;
    private static final Integer UPDATED_N = 2;

    private static final Long DEFAULT_VALUE = 1L;
    private static final Long UPDATED_VALUE = 2L;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Long DEFAULT_TX_INDEX = 1L;
    private static final Long UPDATED_TX_INDEX = 2L;

    private static final String DEFAULT_SCRIPT = "AAAAAAAAAA";
    private static final String UPDATED_SCRIPT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SPENT = false;
    private static final Boolean UPDATED_SPENT = true;

    private static final Boolean DEFAULT_SPENT_TO_ADDRESS = false;
    private static final Boolean UPDATED_SPENT_TO_ADDRESS = true;

    @Autowired
    private OutputRepository outputRepository;

    @Autowired
    private OutputMapper outputMapper;

    @Autowired
    private OutputService outputService;

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

    private MockMvc restOutputMockMvc;

    private Output output;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OutputResource outputResource = new OutputResource(outputService);
        this.restOutputMockMvc = MockMvcBuilders.standaloneSetup(outputResource)
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
    public static Output createEntity(EntityManager em) {
        Output output = new Output()
            .n(DEFAULT_N)
            .value(DEFAULT_VALUE)
            .address(DEFAULT_ADDRESS)
            .txIndex(DEFAULT_TX_INDEX)
            .script(DEFAULT_SCRIPT)
            .spent(DEFAULT_SPENT)
            .spentToAddress(DEFAULT_SPENT_TO_ADDRESS);
        return output;
    }

    @Before
    public void initTest() {
        output = createEntity(em);
    }

    @Test
    @Transactional
    public void createOutput() throws Exception {
        int databaseSizeBeforeCreate = outputRepository.findAll().size();

        // Create the Output
        OutputDTO outputDTO = outputMapper.toDto(output);
        restOutputMockMvc.perform(post("/api/outputs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outputDTO)))
            .andExpect(status().isCreated());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeCreate + 1);
        Output testOutput = outputList.get(outputList.size() - 1);
        assertThat(testOutput.getN()).isEqualTo(DEFAULT_N);
        assertThat(testOutput.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testOutput.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOutput.getTxIndex()).isEqualTo(DEFAULT_TX_INDEX);
        assertThat(testOutput.getScript()).isEqualTo(DEFAULT_SCRIPT);
        assertThat(testOutput.isSpent()).isEqualTo(DEFAULT_SPENT);
        assertThat(testOutput.isSpentToAddress()).isEqualTo(DEFAULT_SPENT_TO_ADDRESS);
    }

    @Test
    @Transactional
    public void createOutputWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = outputRepository.findAll().size();

        // Create the Output with an existing ID
        output.setId(1L);
        OutputDTO outputDTO = outputMapper.toDto(output);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOutputMockMvc.perform(post("/api/outputs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outputDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOutputs() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList
        restOutputMockMvc.perform(get("/api/outputs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(output.getId().intValue())))
            .andExpect(jsonPath("$.[*].n").value(hasItem(DEFAULT_N)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].txIndex").value(hasItem(DEFAULT_TX_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].script").value(hasItem(DEFAULT_SCRIPT.toString())))
            .andExpect(jsonPath("$.[*].spent").value(hasItem(DEFAULT_SPENT.booleanValue())))
            .andExpect(jsonPath("$.[*].spentToAddress").value(hasItem(DEFAULT_SPENT_TO_ADDRESS.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getOutput() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get the output
        restOutputMockMvc.perform(get("/api/outputs/{id}", output.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(output.getId().intValue()))
            .andExpect(jsonPath("$.n").value(DEFAULT_N))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.txIndex").value(DEFAULT_TX_INDEX.intValue()))
            .andExpect(jsonPath("$.script").value(DEFAULT_SCRIPT.toString()))
            .andExpect(jsonPath("$.spent").value(DEFAULT_SPENT.booleanValue()))
            .andExpect(jsonPath("$.spentToAddress").value(DEFAULT_SPENT_TO_ADDRESS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOutput() throws Exception {
        // Get the output
        restOutputMockMvc.perform(get("/api/outputs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOutput() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        int databaseSizeBeforeUpdate = outputRepository.findAll().size();

        // Update the output
        Output updatedOutput = outputRepository.findById(output.getId()).get();
        // Disconnect from session so that the updates on updatedOutput are not directly saved in db
        em.detach(updatedOutput);
        updatedOutput
            .n(UPDATED_N)
            .value(UPDATED_VALUE)
            .address(UPDATED_ADDRESS)
            .txIndex(UPDATED_TX_INDEX)
            .script(UPDATED_SCRIPT)
            .spent(UPDATED_SPENT)
            .spentToAddress(UPDATED_SPENT_TO_ADDRESS);
        OutputDTO outputDTO = outputMapper.toDto(updatedOutput);

        restOutputMockMvc.perform(put("/api/outputs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outputDTO)))
            .andExpect(status().isOk());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeUpdate);
        Output testOutput = outputList.get(outputList.size() - 1);
        assertThat(testOutput.getN()).isEqualTo(UPDATED_N);
        assertThat(testOutput.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOutput.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOutput.getTxIndex()).isEqualTo(UPDATED_TX_INDEX);
        assertThat(testOutput.getScript()).isEqualTo(UPDATED_SCRIPT);
        assertThat(testOutput.isSpent()).isEqualTo(UPDATED_SPENT);
        assertThat(testOutput.isSpentToAddress()).isEqualTo(UPDATED_SPENT_TO_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingOutput() throws Exception {
        int databaseSizeBeforeUpdate = outputRepository.findAll().size();

        // Create the Output
        OutputDTO outputDTO = outputMapper.toDto(output);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutputMockMvc.perform(put("/api/outputs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outputDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOutput() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        int databaseSizeBeforeDelete = outputRepository.findAll().size();

        // Delete the output
        restOutputMockMvc.perform(delete("/api/outputs/{id}", output.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Output.class);
        Output output1 = new Output();
        output1.setId(1L);
        Output output2 = new Output();
        output2.setId(output1.getId());
        assertThat(output1).isEqualTo(output2);
        output2.setId(2L);
        assertThat(output1).isNotEqualTo(output2);
        output1.setId(null);
        assertThat(output1).isNotEqualTo(output2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutputDTO.class);
        OutputDTO outputDTO1 = new OutputDTO();
        outputDTO1.setId(1L);
        OutputDTO outputDTO2 = new OutputDTO();
        assertThat(outputDTO1).isNotEqualTo(outputDTO2);
        outputDTO2.setId(outputDTO1.getId());
        assertThat(outputDTO1).isEqualTo(outputDTO2);
        outputDTO2.setId(2L);
        assertThat(outputDTO1).isNotEqualTo(outputDTO2);
        outputDTO1.setId(null);
        assertThat(outputDTO1).isNotEqualTo(outputDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(outputMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(outputMapper.fromId(null)).isNull();
    }
}
