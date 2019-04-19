package io.stibits.web.rest;

import io.stibits.BlockChainApp;

import io.stibits.domain.Block;
import io.stibits.repository.BlockRepository;
import io.stibits.service.BlockService;
import io.stibits.service.InputService;
import io.stibits.service.OutputService;
import io.stibits.service.TransactionService;
import io.stibits.service.dto.BlockDTO;
import io.stibits.service.mapper.BlockMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static io.stibits.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BlockResource REST controller.
 *
 * @see BlockResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlockChainApp.class)
public class BlockResourceIntTest {

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final String DEFAULT_PREVIOUS_BLOCK_HASH = "AAAAAAAAAA";
    private static final String UPDATED_PREVIOUS_BLOCK_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_MERKLE_ROOT = "AAAAAAAAAA";
    private static final String UPDATED_MERKLE_ROOT = "BBBBBBBBBB";

    private static final Long DEFAULT_BITS = 1L;
    private static final Long UPDATED_BITS = 2L;

    private static final Long DEFAULT_FEES = 1L;
    private static final Long UPDATED_FEES = 2L;

    private static final Long DEFAULT_NONCE = 1L;
    private static final Long UPDATED_NONCE = 2L;

    private static final Long DEFAULT_SIZE = 1L;
    private static final Long UPDATED_SIZE = 2L;

    private static final Long DEFAULT_INDEX = 1L;
    private static final Long UPDATED_INDEX = 2L;

    private static final Long DEFAULT_RECEIVED_TIME = 1L;
    private static final Long UPDATED_RECEIVED_TIME = 2L;

    private static final String DEFAULT_RELAYED_BY = "AAAAAAAAAA";
    private static final String UPDATED_RELAYED_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_HEIGHT = 1L;
    private static final Long UPDATED_HEIGHT = 2L;

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final Long DEFAULT_TIME = 1L;
    private static final Long UPDATED_TIME = 2L;

    private static final Boolean DEFAULT_MAIN_CHAIN = false;
    private static final Boolean UPDATED_MAIN_CHAIN = true;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private BlockService blockService;

    @Autowired
    private TransactionService txService;

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

    private MockMvc restBlockMockMvc;

    private Block block;


    private  OutputService outputService;

    private  InputService inputService;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BlockResource blockResource = new BlockResource(blockService,txService,outputService,inputService);
        this.restBlockMockMvc = MockMvcBuilders.standaloneSetup(blockResource)
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
    public static Block createEntity(EntityManager em) {
        Block block = new Block()
            .version(DEFAULT_VERSION)
            .previousBlockHash(DEFAULT_PREVIOUS_BLOCK_HASH)
            .merkleRoot(DEFAULT_MERKLE_ROOT)
            .bits(DEFAULT_BITS)
            .fees(DEFAULT_FEES)
            .nonce(DEFAULT_NONCE)
            .size(DEFAULT_SIZE)
            .index(DEFAULT_INDEX)
            .receivedTime(DEFAULT_RECEIVED_TIME)
            .relayedBy(DEFAULT_RELAYED_BY)
            .height(DEFAULT_HEIGHT)
            .hash(DEFAULT_HASH)
            .time(DEFAULT_TIME)
            .mainChain(DEFAULT_MAIN_CHAIN);
        return block;
    }

    @Before
    public void initTest() {
        block = createEntity(em);
    }

    @Test
    @Transactional
    public void createBlock() throws Exception {
        int databaseSizeBeforeCreate = blockRepository.findAll().size();

        // Create the Block
        BlockDTO blockDTO = blockMapper.toDto(block);
        restBlockMockMvc.perform(post("/api/blocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isCreated());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeCreate + 1);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testBlock.getPreviousBlockHash()).isEqualTo(DEFAULT_PREVIOUS_BLOCK_HASH);
        assertThat(testBlock.getMerkleRoot()).isEqualTo(DEFAULT_MERKLE_ROOT);
        assertThat(testBlock.getBits()).isEqualTo(DEFAULT_BITS);
        assertThat(testBlock.getFees()).isEqualTo(DEFAULT_FEES);
        assertThat(testBlock.getNonce()).isEqualTo(DEFAULT_NONCE);
        assertThat(testBlock.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testBlock.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testBlock.getReceivedTime()).isEqualTo(DEFAULT_RECEIVED_TIME);
        assertThat(testBlock.getRelayedBy()).isEqualTo(DEFAULT_RELAYED_BY);
        assertThat(testBlock.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testBlock.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testBlock.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testBlock.isMainChain()).isEqualTo(DEFAULT_MAIN_CHAIN);
    }

    @Test
    @Transactional
    public void createBlockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = blockRepository.findAll().size();

        // Create the Block with an existing ID
        block.setId(1L);
        BlockDTO blockDTO = blockMapper.toDto(block);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlockMockMvc.perform(post("/api/blocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBlocks() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList
        restBlockMockMvc.perform(get("/api/blocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(block.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].previousBlockHash").value(hasItem(DEFAULT_PREVIOUS_BLOCK_HASH.toString())))
            .andExpect(jsonPath("$.[*].merkleRoot").value(hasItem(DEFAULT_MERKLE_ROOT.toString())))
            .andExpect(jsonPath("$.[*].bits").value(hasItem(DEFAULT_BITS.intValue())))
            .andExpect(jsonPath("$.[*].fees").value(hasItem(DEFAULT_FEES.intValue())))
            .andExpect(jsonPath("$.[*].nonce").value(hasItem(DEFAULT_NONCE.intValue())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].receivedTime").value(hasItem(DEFAULT_RECEIVED_TIME.intValue())))
            .andExpect(jsonPath("$.[*].relayedBy").value(hasItem(DEFAULT_RELAYED_BY.toString())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].mainChain").value(hasItem(DEFAULT_MAIN_CHAIN.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get the block
        restBlockMockMvc.perform(get("/api/blocks/{id}", block.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(block.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.previousBlockHash").value(DEFAULT_PREVIOUS_BLOCK_HASH.toString()))
            .andExpect(jsonPath("$.merkleRoot").value(DEFAULT_MERKLE_ROOT.toString()))
            .andExpect(jsonPath("$.bits").value(DEFAULT_BITS.intValue()))
            .andExpect(jsonPath("$.fees").value(DEFAULT_FEES.intValue()))
            .andExpect(jsonPath("$.nonce").value(DEFAULT_NONCE.intValue()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.intValue()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX.intValue()))
            .andExpect(jsonPath("$.receivedTime").value(DEFAULT_RECEIVED_TIME.intValue()))
            .andExpect(jsonPath("$.relayedBy").value(DEFAULT_RELAYED_BY.toString()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.intValue()))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.mainChain").value(DEFAULT_MAIN_CHAIN.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBlock() throws Exception {
        // Get the block
        restBlockMockMvc.perform(get("/api/blocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Update the block
        Block updatedBlock = blockRepository.findById(block.getId()).get();
        // Disconnect from session so that the updates on updatedBlock are not directly saved in db
        em.detach(updatedBlock);
        updatedBlock
            .version(UPDATED_VERSION)
            .previousBlockHash(UPDATED_PREVIOUS_BLOCK_HASH)
            .merkleRoot(UPDATED_MERKLE_ROOT)
            .bits(UPDATED_BITS)
            .fees(UPDATED_FEES)
            .nonce(UPDATED_NONCE)
            .size(UPDATED_SIZE)
            .index(UPDATED_INDEX)
            .receivedTime(UPDATED_RECEIVED_TIME)
            .relayedBy(UPDATED_RELAYED_BY)
            .height(UPDATED_HEIGHT)
            .hash(UPDATED_HASH)
            .time(UPDATED_TIME)
            .mainChain(UPDATED_MAIN_CHAIN);
        BlockDTO blockDTO = blockMapper.toDto(updatedBlock);

        restBlockMockMvc.perform(put("/api/blocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isOk());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testBlock.getPreviousBlockHash()).isEqualTo(UPDATED_PREVIOUS_BLOCK_HASH);
        assertThat(testBlock.getMerkleRoot()).isEqualTo(UPDATED_MERKLE_ROOT);
        assertThat(testBlock.getBits()).isEqualTo(UPDATED_BITS);
        assertThat(testBlock.getFees()).isEqualTo(UPDATED_FEES);
        assertThat(testBlock.getNonce()).isEqualTo(UPDATED_NONCE);
        assertThat(testBlock.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testBlock.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testBlock.getReceivedTime()).isEqualTo(UPDATED_RECEIVED_TIME);
        assertThat(testBlock.getRelayedBy()).isEqualTo(UPDATED_RELAYED_BY);
        assertThat(testBlock.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testBlock.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testBlock.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testBlock.isMainChain()).isEqualTo(UPDATED_MAIN_CHAIN);
    }

    @Test
    @Transactional
    public void updateNonExistingBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Create the Block
        BlockDTO blockDTO = blockMapper.toDto(block);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockMockMvc.perform(put("/api/blocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeDelete = blockRepository.findAll().size();

        // Delete the block
        restBlockMockMvc.perform(delete("/api/blocks/{id}", block.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Block.class);
        Block block1 = new Block();
        block1.setId(1L);
        Block block2 = new Block();
        block2.setId(block1.getId());
        assertThat(block1).isEqualTo(block2);
        block2.setId(2L);
        assertThat(block1).isNotEqualTo(block2);
        block1.setId(null);
        assertThat(block1).isNotEqualTo(block2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlockDTO.class);
        BlockDTO blockDTO1 = new BlockDTO();
        blockDTO1.setId(1L);
        BlockDTO blockDTO2 = new BlockDTO();
        assertThat(blockDTO1).isNotEqualTo(blockDTO2);
        blockDTO2.setId(blockDTO1.getId());
        assertThat(blockDTO1).isEqualTo(blockDTO2);
        blockDTO2.setId(2L);
        assertThat(blockDTO1).isNotEqualTo(blockDTO2);
        blockDTO1.setId(null);
        assertThat(blockDTO1).isNotEqualTo(blockDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(blockMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(blockMapper.fromId(null)).isNull();
    }
}
