package io.stibits.service.mapper;

import io.stibits.domain.*;
import io.stibits.service.dto.BlockDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Block and its DTO BlockDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BlockMapper extends EntityMapper<BlockDTO, Block> {


    @Mapping(target = "transactions", ignore = true)
    Block toEntity(BlockDTO blockDTO);

    default Block fromId(Long id) {
        if (id == null) {
            return null;
        }
        Block block = new Block();
        block.setId(id);
        return block;
    }
}
