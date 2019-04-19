package io.stibits.repository;

import io.stibits.domain.Output;
import io.stibits.service.dto.OutputDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Output entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OutputRepository extends JpaRepository<Output, Long> {

    Optional<Output> findByScriptAndNAndTxIndex(String script,Integer n,Long txIndex);

}
