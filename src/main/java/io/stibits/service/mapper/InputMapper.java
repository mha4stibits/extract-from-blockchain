package io.stibits.service.mapper;

import io.stibits.domain.*;
import io.stibits.service.dto.InputDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Input and its DTO InputDTO.
 */
@Mapper(componentModel = "spring", uses = {TransactionMapper.class, OutputMapper.class})
public interface InputMapper extends EntityMapper<InputDTO, Input> {

    @Mapping(source = "transaction.id", target = "transactionId")
    @Mapping(source = "previousOutput.id", target = "previousOutputId")
    InputDTO toDto(Input input);

    @Mapping(source = "transactionId", target = "transaction")
    @Mapping(source = "previousOutputId", target = "previousOutput")
    Input toEntity(InputDTO inputDTO);

    default Input fromId(Long id) {
        if (id == null) {
            return null;
        }
        Input input = new Input();
        input.setId(id);
        return input;
    }
}
