package io.stibits.service.impl;

import io.stibits.service.BlockService;
import io.stibits.domain.Block;
import io.stibits.repository.BlockRepository;
import io.stibits.service.dto.BlockDTO;
import io.stibits.service.mapper.BlockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Block.
 */
@Service
@Transactional
public class BlockServiceImpl implements BlockService {

    private final Logger log = LoggerFactory.getLogger(BlockServiceImpl.class);

    private final BlockRepository blockRepository;

    private final BlockMapper blockMapper;

    public BlockServiceImpl(BlockRepository blockRepository, BlockMapper blockMapper) {
        this.blockRepository = blockRepository;
        this.blockMapper = blockMapper;
    }

    /**
     * Save a block.
     *
     * @param blockDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BlockDTO save(BlockDTO blockDTO) {
        log.debug("Request to save Block : {}", blockDTO);
        Block block = blockMapper.toEntity(blockDTO);
        block = blockRepository.save(block);
        return blockMapper.toDto(block);
    }

    /**
     * Get all the blocks.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<BlockDTO> findAll() {
        log.debug("Request to get all Blocks");
        return blockRepository.findAll().stream()
            .map(blockMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one block by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BlockDTO> findOne(Long id) {
        log.debug("Request to get Block : {}", id);
        return blockRepository.findById(id)
            .map(blockMapper::toDto);
    }

    /**
     * Delete the block by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Block : {}", id);
        blockRepository.deleteById(id);
    }
}
