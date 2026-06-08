package pl.mruczekprogramista.data;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SprayRepository
        extends
            JpaRepository<Spray, Integer>,
            JpaSpecificationExecutor<Spray> {

    @Transactional
    void deleteById(Long id);
}
