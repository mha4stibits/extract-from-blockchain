package io.stibits.repository;

import io.stibits.domain.Input;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Input entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InputRepository extends JpaRepository<Input, Long> {

}
