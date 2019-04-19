package io.stibits.web.rest;
import info.blockchain.api.blockexplorer.BlockExplorer;
import info.blockchain.api.blockexplorer.entity.*;
import io.stibits.service.BlockService;
import io.stibits.service.InputService;
import io.stibits.service.OutputService;
import io.stibits.service.TransactionService;
import io.stibits.service.dto.InputDTO;
import io.stibits.service.dto.OutputDTO;
import io.stibits.service.dto.TransactionDTO;
import io.stibits.web.rest.errors.BadRequestAlertException;
import io.stibits.web.rest.util.HeaderUtil;
import io.stibits.service.dto.BlockDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * REST controller for managing Block.
 */
@RestController
@RequestMapping("/api")
public class BlockResource {

    private final Logger log = LoggerFactory.getLogger(BlockResource.class);

    private static final String ENTITY_NAME = "block";

    private final BlockService blockService;

    private final TransactionService txService;

    private final OutputService outputService;

    private final InputService inputService;

    public BlockResource(BlockService blockService,TransactionService txService,OutputService outputService,InputService inputService) {
        this.blockService = blockService;
        this.txService = txService;
        this.outputService=outputService;
        this.inputService = inputService;
    }

    /**
     * POST  /blocks : Create a new block.
     *
     * @param blockDTO the blockDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new blockDTO, or with status 400 (Bad Request) if the block has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/blocks")
    public ResponseEntity<BlockDTO> createBlock(@RequestBody BlockDTO blockDTO) throws URISyntaxException {
        log.debug("REST request to save Block : {}", blockDTO);
        if (blockDTO.getId() != null) {
            throw new BadRequestAlertException("A new block cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlockDTO result = blockService.save(blockDTO);
        return ResponseEntity.created(new URI("/api/blocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /blocks : Updates an existing block.
     *
     * @param blockDTO the blockDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated blockDTO,
     * or with status 400 (Bad Request) if the blockDTO is not valid,
     * or with status 500 (Internal Server Error) if the blockDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/blocks")
    public ResponseEntity<BlockDTO> updateBlock(@RequestBody BlockDTO blockDTO) throws URISyntaxException {
        log.debug("REST request to update Block : {}", blockDTO);
        if (blockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BlockDTO result = blockService.save(blockDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, blockDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /blocks : get all the blocks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of blocks in body
     */
    @GetMapping("/blocks")
    public ResponseEntity<BlockDTO> getAllBlocks() throws Exception{
        log.debug("REST request to get all Blocks");
        Optional<BlockDTO> blockDTO = blockService.findOne(1L   );
        return ResponseUtil.wrapOrNotFound(blockDTO);
    }

    @GetMapping("/blockchain")
    public ResponseEntity<BlockDTO> getBlockchain() throws Exception{
        log.debug("REST request to get all Blocks");
        synch();
        Optional<BlockDTO> blockDTO = blockService.findOne(1L   );
        return ResponseUtil.wrapOrNotFound(blockDTO);
    }

    public void synch() throws Exception {
        // instantiate a block explorer
        BlockExplorer blockExplorer = new BlockExplorer();

        //List<UnspentOutput> list = blockExplorer.getUnspentOutputs("miUmB85Ewt6cEgcZwm9zu3tECmj8YjzZfh");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date startDate = sdf.parse("01-01-2009 00:00:00");
        Date endDate = sdf.parse("31-12-2009 00:00:00");

        Calendar calendar = Calendar.getInstance();
        //Setting the Calendar date and time to the given date and time
        calendar.setTime(endDate);
        long tsEnd = calendar.getTimeInMillis();
        calendar.setTime(startDate);

        while(calendar.getTimeInMillis()<=tsEnd){

            List<SimpleBlock> listBlock = blockExplorer.getBlocks(calendar.getTimeInMillis()/1000);

            System.out.println("====================== TOTAL BLOCK IN THIS DAY  ==============>>> "+listBlock.size()+"<<<<===============");

            Iterator<SimpleBlock> iter = listBlock.iterator();

            while(iter.hasNext()){
                SimpleBlock simpleBlock = iter.next();
                Block block = blockExplorer.getBlock(simpleBlock.getHash());
                BlockDTO savedBlock = blockService.save(convertBlockTODto(block));
                // Iterate over transactions.
                Iterator<Transaction> listTx = block.getTransactions().iterator();
                while(listTx.hasNext()){
                    Transaction tx = listTx.next();
                    TransactionDTO currentTx = txService.save(convertTxToDto(tx,savedBlock.getId()));
                    // Iterate over Output
                    Iterator<Output> listOutput = tx.getOutputs().iterator();
                    while(listOutput.hasNext()){
                        Output out = listOutput.next();
                        OutputDTO output = outputService.save(convertToDto(out,currentTx.getId()));
                    }
                    // Iterate over Input
                    Iterator<Input> listInput = tx.getInputs().iterator();
                    while(listInput.hasNext()){
                        Input inp = listInput.next();
                        Long previourOutputIndex = inp.getPreviousOutput()!=null?outputService.findByScriptAndNAndTxIndex(inp.getPreviousOutput().getScript(),inp.getPreviousOutput().getN(),inp.getPreviousOutput().getTxIndex()).get().getId():null;

                        InputDTO input = inputService.save(convertToDto(inp,currentTx.getId(),previourOutputIndex));

                    }

                }



            }

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

    }

    private static BlockDTO convertBlockTODto(Block block){
        BlockDTO dto = new BlockDTO();
        dto.setBits(block.getBits());
        dto.setFees(block.getBits());
        dto.setHash(block.getHash());
        dto.setHeight(block.getHeight());
        dto.setIndex(block.getIndex());
        dto.setMainChain(block.isMainChain());
        dto.setMerkleRoot(block.getMerkleRoot());
        dto.setNonce(block.getNonce());
        dto.setPreviousBlockHash(block.getPreviousBlockHash());
        dto.setReceivedTime(block.getReceivedTime());
        dto.setRelayedBy(block.getRelayedBy());
        dto.setSize(block.getSize());
        dto.setTime(block.getTime());
        dto.setVersion(block.getVersion());
        return dto;
    }

    private static TransactionDTO convertTxToDto(Transaction tx,Long idBlock){
        TransactionDTO txDto = new TransactionDTO();

        txDto.setBlockHeight(tx.getBlockHeight());
        txDto.setBlockId(idBlock);
        txDto.setDoubleSpend(tx.isDoubleSpend());
        txDto.setHash(tx.getHash());
        txDto.setIndex(tx.getIndex());
        txDto.setLockTime(tx.getLockTime());
        txDto.setRelayedBy(tx.getRelayedBy());
        txDto.setSize(tx.getSize());
        txDto.setTime(tx.getTime());
        txDto.setVersion(tx.getVersion());

        return txDto;

    }

    private static OutputDTO convertToDto(Output out,Long idTx){
        OutputDTO outDto = new OutputDTO();

        outDto.setN(out.getN());
        outDto.setValue(out.getValue());
        outDto.setAddress(out.getAddress());
        outDto.setTxIndex(out.getTxIndex());
        outDto.setScript(out.getScript());
        outDto.setSpent(out.isSpent());
        outDto.setSpentToAddress(out.isSpentToAddress());
        outDto.setTransactionId(idTx);

        return outDto;

    }


    private static InputDTO convertToDto(Input inp,Long idTx,Long txIndex){
        InputDTO inDto = new InputDTO();

        inDto.setSequence(inp.getSequence());
        inDto.setScriptSignature(inp.getScriptSignature());
        inDto.setPreviousOutputId(txIndex);
        inDto.setTransactionId(idTx);
        return inDto;

    }

    /**
     * GET  /blocks/:id : get the "id" block.
     *
     * @param id the id of the blockDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the blockDTO, or with status 404 (Not Found)
     */
    @GetMapping("/blocks/{id}")
    public ResponseEntity<BlockDTO> getBlock(@PathVariable Long id) {
        log.debug("REST request to get Block : {}", id);
        Optional<BlockDTO> blockDTO = blockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(blockDTO);
    }

    /**
     * DELETE  /blocks/:id : delete the "id" block.
     *
     * @param id the id of the blockDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/blocks/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        log.debug("REST request to delete Block : {}", id);
        blockService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
