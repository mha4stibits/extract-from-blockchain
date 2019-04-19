package io.stibits.web.rest;
import io.stibits.service.InputService;
import io.stibits.web.rest.errors.BadRequestAlertException;
import io.stibits.web.rest.util.HeaderUtil;
import io.stibits.service.dto.InputDTO;
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
 * REST controller for managing Input.
 */
@RestController
@RequestMapping("/api")
public class InputResource {

    private final Logger log = LoggerFactory.getLogger(InputResource.class);

    private static final String ENTITY_NAME = "input";

    private final InputService inputService;

    public InputResource(InputService inputService) {
        this.inputService = inputService;
    }

    /**
     * POST  /inputs : Create a new input.
     *
     * @param inputDTO the inputDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inputDTO, or with status 400 (Bad Request) if the input has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/inputs")
    public ResponseEntity<InputDTO> createInput(@RequestBody InputDTO inputDTO) throws URISyntaxException {
        log.debug("REST request to save Input : {}", inputDTO);
        if (inputDTO.getId() != null) {
            throw new BadRequestAlertException("A new input cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InputDTO result = inputService.save(inputDTO);
        return ResponseEntity.created(new URI("/api/inputs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inputs : Updates an existing input.
     *
     * @param inputDTO the inputDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inputDTO,
     * or with status 400 (Bad Request) if the inputDTO is not valid,
     * or with status 500 (Internal Server Error) if the inputDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/inputs")
    public ResponseEntity<InputDTO> updateInput(@RequestBody InputDTO inputDTO) throws URISyntaxException {
        log.debug("REST request to update Input : {}", inputDTO);
        if (inputDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InputDTO result = inputService.save(inputDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, inputDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inputs : get all the inputs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of inputs in body
     */
    @GetMapping("/inputs")
    public List<InputDTO> getAllInputs() {
        log.debug("REST request to get all Inputs");
        return inputService.findAll();
    }

    /**
     * GET  /inputs/:id : get the "id" input.
     *
     * @param id the id of the inputDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inputDTO, or with status 404 (Not Found)
     */
    @GetMapping("/inputs/{id}")
    public ResponseEntity<InputDTO> getInput(@PathVariable Long id) {
        log.debug("REST request to get Input : {}", id);
        Optional<InputDTO> inputDTO = inputService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inputDTO);
    }

    /**
     * DELETE  /inputs/:id : delete the "id" input.
     *
     * @param id the id of the inputDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/inputs/{id}")
    public ResponseEntity<Void> deleteInput(@PathVariable Long id) {
        log.debug("REST request to delete Input : {}", id);
        inputService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
