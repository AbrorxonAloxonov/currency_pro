package uz.devops.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import uz.devops.domain.*; // for static metamodels
import uz.devops.domain.Currency;
import uz.devops.repository.CurrencyRepository;
import uz.devops.service.criteria.CurrencyCriteria;
import uz.devops.service.dto.CurrencyDTO;
import uz.devops.service.mapper.CurrencyMapper;

/**
 * Service for executing complex queries for {@link Currency} entities in the database.
 * The main input is a {@link CurrencyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CurrencyDTO} or a {@link Page} of {@link CurrencyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CurrencyQueryService extends QueryService<Currency> {

    private final Logger log = LoggerFactory.getLogger(CurrencyQueryService.class);

    private final CurrencyRepository currencyRepository;

    private final CurrencyMapper currencyMapper;

    public CurrencyQueryService(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    /**
     * Return a {@link List} of {@link CurrencyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CurrencyDTO> findByCriteria(CurrencyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Currency> specification = createSpecification(criteria);
        return currencyMapper.toDto(currencyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CurrencyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CurrencyDTO> findByCriteria(CurrencyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Currency> specification = createSpecification(criteria);
        return currencyRepository.findAll(specification, page).map(currencyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CurrencyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Currency> specification = createSpecification(criteria);
        return currencyRepository.count(specification);
    }

    /**
     * Function to convert {@link CurrencyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Currency> createSpecification(CurrencyCriteria criteria) {
        Specification<Currency> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Currency_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Currency_.code));
            }
            if (criteria.getCcy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCcy(), Currency_.ccy));
            }
            if (criteria.getCcyNmRU() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCcyNmRU(), Currency_.ccyNmRU));
            }
            if (criteria.getCcyNmUZ() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCcyNmUZ(), Currency_.ccyNmUZ));
            }
            if (criteria.getCcyNmUZC() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCcyNmUZC(), Currency_.ccyNmUZC));
            }
            if (criteria.getCcyNmEN() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCcyNmEN(), Currency_.ccyNmEN));
            }
            if (criteria.getNominal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNominal(), Currency_.nominal));
            }
            if (criteria.getRate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRate(), Currency_.rate));
            }
            if (criteria.getDiff() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDiff(), Currency_.diff));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Currency_.date));
            }
        }
        return specification;
    }
}
