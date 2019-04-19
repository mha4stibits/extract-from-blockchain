package io.stibits.service.mapper;

import io.stibits.domain.*;
import io.stibits.service.dto.OutputDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Output and its DTO OutputDTO.
 */
@Mapper(componentModel = "spring", uses = {TransactionMapper.class})
public interface OutputMapper extends EntityMapper<OutputDTO, Output> {

    @Mapping(source = "transaction.id", target = "transactionId")
    OutputDTO toDto(Output output);

    @Mapping(source = "transactionId", target = "transaction")
    Output toEntity(OutputDTO outputDTO);

    default Output fromId(Long id) {
        if (id == null) {
            return null;
        }
        Output output = new Output();
        output.setId(id);
        return output;
    }
}
