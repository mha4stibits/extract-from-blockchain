package io.stibits.web.rest;
import io.stibits.service.OutputService;
import io.stibits.web.rest.errors.BadRequestAlertException;
import io.stibits.web.rest.util.HeaderUtil;
import io.stibits.service.dto.OutputDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Output.
 */
@RestController
@RequestMapping("/api")
public class OutputResource {

    private final Logger log = LoggerFactory.getLogger(OutputResource.class);

    private static final String ENTITY_NAME = "output";

    private final OutputService outputService;

    public OutputResource(OutputService outputService) {
        this.outputService = outputService;
    }

    /**
     * POST  /outputs : Create a new output.
     *
     * @param outputDTO the outputDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new outputDTO, or with status 400 (Bad Request) if the output has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/outputs")
    public ResponseEntity<OutputDTO> createOutput(@RequestBody OutputDTO outputDTO) throws URISyntaxException {
        log.debug("REST request to save Output : {}", outputDTO);
        if (outputDTO.getId() != null) {
            throw new BadRequestAlertException("A new output cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OutputDTO result = outputService.save(outputDTO);
        return ResponseEntity.created(new URI("/api/outputs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /outputs : Updates an existing output.
     *
     * @param outputDTO the outputDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated outputDTO,
     * or with status 400 (Bad Request) if the outputDTO is not valid,
     * or with status 500 (Internal Server Error) if the outputDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/outputs")
    public ResponseEntity<OutputDTO> updateOutput(@RequestBody OutputDTO outputDTO) throws URISyntaxException {
        log.debug("REST request to update Output : {}", outputDTO);
        if (outputDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OutputDTO result = outputService.save(outputDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, outputDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /outputs : get all the outputs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of outputs in body
     */
    @GetMapping("/outputs")
    public List<OutputDTO> getAllOutputs() {
        log.debug("REST request to get all Outputs");
        return outputService.findAll();
    }

    /**
     * GET  /outputs/:id : get the "id" output.
     *
     * @param id the id of the outputDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the outputDTO, or with status 404 (Not Found)
     */
    @GetMapping("/outputs/{id}")
    public ResponseEntity<OutputDTO> getOutput(@PathVariable Long id) {
        log.debug("REST request to get Output : {}", id);
        Optional<OutputDTO> outputDTO = outputService.findOne(id);
        return ResponseUtil.wrapOrNotFound(outputDTO);
    }

    /**
     * DELETE  /outputs/:id : delete the "id" output.
     *
     * @param id the id of the outputDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/outputs/{id}")
    public ResponseEntity<Void> deleteOutput(@PathVariable Long id) {
        log.debug("REST request to delete Output : {}", id);
        outputService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
