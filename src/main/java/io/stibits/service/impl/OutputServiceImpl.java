package io.stibits.service.impl;

import io.stibits.service.OutputService;
import io.stibits.domain.Output;
import io.stibits.repository.OutputRepository;
import io.stibits.service.dto.OutputDTO;
import io.stibits.service.mapper.OutputMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Output.
 */
@Service
@Transactional
public class OutputServiceImpl implements OutputService {

    private final Logger log = LoggerFactory.getLogger(OutputServiceImpl.class);

    private final OutputRepository outputRepository;

    private final OutputMapper outputMapper;

    public OutputServiceImpl(OutputRepository outputRepository, OutputMapper outputMapper) {
        this.outputRepository = outputRepository;
        this.outputMapper = outputMapper;
    }

    /**
     * Save a output.
     *
     * @param outputDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OutputDTO save(OutputDTO outputDTO) {
        log.debug("Request to save Output : {}", outputDTO);
        Output output = outputMapper.toEntity(outputDTO);
        output = outputRepository.save(output);
        return outputMapper.toDto(output);
    }

    /**
     * Get all the outputs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<OutputDTO> findAll() {
        log.debug("Request to get all Outputs");
        return outputRepository.findAll().stream()
            .map(outputMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one output by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OutputDTO> findOne(Long id) {
        log.debug("Request to get Output : {}", id);
        return outputRepository.findById(id)
            .map(outputMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OutputDTO> findByScriptAndNAndTxIndex(String script,Integer n,Long txIndex) {
        log.debug("Request to get Output : {}"+ script);
        return outputRepository.findByScriptAndNAndTxIndex(script,n,txIndex)
            .map(outputMapper::toDto);
    }

    /**
     * Delete the output by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Output : {}", id);
        outputRepository.deleteById(id);
    }
}
