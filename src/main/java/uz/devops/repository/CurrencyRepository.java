package uz.devops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.devops.domain.Currency;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA repository for the Currency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long>, JpaSpecificationExecutor<Currency> {
    List<Currency> findByDate(Instant date);
    List<Currency> findByCcy(String ccy);
}
