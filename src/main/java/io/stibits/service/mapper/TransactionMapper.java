package io.stibits.service.mapper;

import io.stibits.domain.*;
import io.stibits.service.dto.TransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Transaction and its DTO TransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {BlockMapper.class})
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {

    @Mapping(source = "block.id", target = "blockId")
    TransactionDTO toDto(Transaction transaction);

    @Mapping(source = "blockId", target = "block")
    @Mapping(target = "outputs", ignore = true)
    @Mapping(target = "inputs", ignore = true)
    Transaction toEntity(TransactionDTO transactionDTO);

    default Transaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setId(id);
        return transaction;
    }
}
