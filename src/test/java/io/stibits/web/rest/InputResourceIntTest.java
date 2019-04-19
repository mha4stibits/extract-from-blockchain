package io.stibits.web.rest;

import io.stibits.BlockChainApp;

import io.stibits.domain.Input;
import io.stibits.repository.InputRepository;
import io.stibits.service.InputService;
import io.stibits.service.dto.InputDTO;
import io.stibits.service.mapper.InputMapper;
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
 * Test class for the InputResource REST controller.
 *
 * @see InputResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlockChainApp.class)
public class InputResourceIntTest {

    private static final Long DEFAULT_SEQUENCE = 1L;
    private static final Long UPDATED_SEQUENCE = 2L;

    private static final String DEFAULT_SCRIPT_SIGNATURE = "AAAAAAAAAA";
    private static final String UPDATED_SCRIPT_SIGNATURE = "BBBBBBBBBB";

    @Autowired
    private InputRepository inputRepository;

    @Autowired
    private InputMapper inputMapper;

    @Autowired
    private InputService inputService;

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

    private MockMvc restInputMockMvc;

    private Input input;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InputResource inputResource = new InputResource(inputService);
        this.restInputMockMvc = MockMvcBuilders.standaloneSetup(inputResource)
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
    public static Input createEntity(EntityManager em) {
        Input input = new Input()
            .sequence(DEFAULT_SEQUENCE)
            .scriptSignature(DEFAULT_SCRIPT_SIGNATURE);
        return input;
    }

    @Before
    public void initTest() {
        input = createEntity(em);
    }

    @Test
    @Transactional
    public void createInput() throws Exception {
        int databaseSizeBeforeCreate = inputRepository.findAll().size();

        // Create the Input
        InputDTO inputDTO = inputMapper.toDto(input);
        restInputMockMvc.perform(post("/api/inputs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inputDTO)))
            .andExpect(status().isCreated());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeCreate + 1);
        Input testInput = inputList.get(inputList.size() - 1);
        assertThat(testInput.getSequence()).isEqualTo(DEFAULT_SEQUENCE);
        assertThat(testInput.getScriptSignature()).isEqualTo(DEFAULT_SCRIPT_SIGNATURE);
    }

    @Test
    @Transactional
    public void createInputWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inputRepository.findAll().size();

        // Create the Input with an existing ID
        input.setId(1L);
        InputDTO inputDTO = inputMapper.toDto(input);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInputMockMvc.perform(post("/api/inputs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inputDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInputs() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList
        restInputMockMvc.perform(get("/api/inputs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(input.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequence").value(hasItem(DEFAULT_SEQUENCE.intValue())))
            .andExpect(jsonPath("$.[*].scriptSignature").value(hasItem(DEFAULT_SCRIPT_SIGNATURE.toString())));
    }
    
    @Test
    @Transactional
    public void getInput() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get the input
        restInputMockMvc.perform(get("/api/inputs/{id}", input.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(input.getId().intValue()))
            .andExpect(jsonPath("$.sequence").value(DEFAULT_SEQUENCE.intValue()))
            .andExpect(jsonPath("$.scriptSignature").value(DEFAULT_SCRIPT_SIGNATURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInput() throws Exception {
        // Get the input
        restInputMockMvc.perform(get("/api/inputs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInput() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        int databaseSizeBeforeUpdate = inputRepository.findAll().size();

        // Update the input
        Input updatedInput = inputRepository.findById(input.getId()).get();
        // Disconnect from session so that the updates on updatedInput are not directly saved in db
        em.detach(updatedInput);
        updatedInput
            .sequence(UPDATED_SEQUENCE)
            .scriptSignature(UPDATED_SCRIPT_SIGNATURE);
        InputDTO inputDTO = inputMapper.toDto(updatedInput);

        restInputMockMvc.perform(put("/api/inputs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inputDTO)))
            .andExpect(status().isOk());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeUpdate);
        Input testInput = inputList.get(inputList.size() - 1);
        assertThat(testInput.getSequence()).isEqualTo(UPDATED_SEQUENCE);
        assertThat(testInput.getScriptSignature()).isEqualTo(UPDATED_SCRIPT_SIGNATURE);
    }

    @Test
    @Transactional
    public void updateNonExistingInput() throws Exception {
        int databaseSizeBeforeUpdate = inputRepository.findAll().size();

        // Create the Input
        InputDTO inputDTO = inputMapper.toDto(input);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInputMockMvc.perform(put("/api/inputs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inputDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInput() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        int databaseSizeBeforeDelete = inputRepository.findAll().size();

        // Delete the input
        restInputMockMvc.perform(delete("/api/inputs/{id}", input.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Input.class);
        Input input1 = new Input();
        input1.setId(1L);
        Input input2 = new Input();
        input2.setId(input1.getId());
        assertThat(input1).isEqualTo(input2);
        input2.setId(2L);
        assertThat(input1).isNotEqualTo(input2);
        input1.setId(null);
        assertThat(input1).isNotEqualTo(input2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InputDTO.class);
        InputDTO inputDTO1 = new InputDTO();
        inputDTO1.setId(1L);
        InputDTO inputDTO2 = new InputDTO();
        assertThat(inputDTO1).isNotEqualTo(inputDTO2);
        inputDTO2.setId(inputDTO1.getId());
        assertThat(inputDTO1).isEqualTo(inputDTO2);
        inputDTO2.setId(2L);
        assertThat(inputDTO1).isNotEqualTo(inputDTO2);
        inputDTO1.setId(null);
        assertThat(inputDTO1).isNotEqualTo(inputDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(inputMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(inputMapper.fromId(null)).isNull();
    }
}
