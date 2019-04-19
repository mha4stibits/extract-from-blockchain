package io.stibits.service;

import io.stibits.service.dto.OutputDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Output.
 */
public interface OutputService {

    /**
     * Save a output.
     *
     * @param outputDTO the entity to save
     * @return the persisted entity
     */
    OutputDTO save(OutputDTO outputDTO);

    /**
     * Get all the outputs.
     *
     * @return the list of entities
     */
    List<OutputDTO> findAll();


    /**
     * Get the "id" output.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OutputDTO> findOne(Long id);

    Optional<OutputDTO> findByScriptAndNAndTxIndex(String script,Integer n,Long txIndex);

    /**
     * Delete the "id" output.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
