package uz.devops.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.devops.domain.Currency;
import uz.devops.repository.CurrencyRepository;
import uz.devops.service.CurrencyQueryService;
import uz.devops.service.CurrencyService;
import uz.devops.service.CurrencyServiceResolvers;
import uz.devops.service.criteria.CurrencyCriteria;
import uz.devops.service.dto.CurrencyDTO;
import uz.devops.service.dto.CurrencyRequestDTO;
import uz.devops.service.dto.ResponseDto;
import uz.devops.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.devops.domain.Currency}.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyResource.class);

    private static final String ENTITY_NAME = "currency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurrencyService currencyService;

    private final CurrencyRepository currencyRepository;

    private final CurrencyQueryService currencyQueryService;
    private final CurrencyServiceResolvers currencyServiceResolvers;


    /**
     * {@code POST  /currencies} : Create a new currency.
     *
     //* @param currencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencyDTO, or with status {@code 400 (Bad Request)} if the currency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PostMapping("/currencies")
    public ResponseDto<List<CurrencyDTO>> createCurrency() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        log.debug("Information is being fetched from Cbu.uz site");
        List<CurrencyDTO> currencyDtos = restTemplate
            .exchange("https://cbu.uz/oz/arkhiv-kursov-valyut/json/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CurrencyDTO>>() {}
            ).getBody();
        log.debug("transferred to the service for storage");
        currencyDtos = currencyService.saveAll(currencyDtos);
        if (currencyDtos == null){
            log.debug("Not save");
            return new ResponseDto<>(false,"not saved");
        }
        log.debug("save");
        return new ResponseDto<>(true,"save",currencyDtos);
    }

    /**
     * {@code PUT  /currencies/:id} : Updates an existing currency.
     *
     * @param id the id of the currencyDTO to save.
     * @param currencyDTO the currencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyDTO,
     * or with status {@code 400 (Bad Request)} if the currencyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/currencies/{id}")
    public ResponseEntity<CurrencyDTO> updateCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CurrencyDTO currencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Currency : {}, {}", id, currencyDTO);
        if (currencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CurrencyDTO result = currencyService.update(currencyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, currencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /currencies/:id} : Partial updates given fields of an existing currency, field will ignore if it is null
     *
     * @param id the id of the currencyDTO to save.
     * @param currencyDTO the currencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyDTO,
     * or with status {@code 400 (Bad Request)} if the currencyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the currencyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the currencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/currencies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CurrencyDTO> partialUpdateCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CurrencyDTO currencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Currency partially : {}, {}", id, currencyDTO);
        if (currencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CurrencyDTO> result = currencyService.partialUpdate(currencyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, currencyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /currencies} : get all the currencies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencies in body.
     */
    @GetMapping("/currencies")
    public ResponseEntity<List<CurrencyDTO>> getAllCurrencies(
        CurrencyCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Currencies by criteria: {}", criteria);
        Page<CurrencyDTO> page = currencyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /currencies/count} : count all the currencies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/currencies/count")
    public ResponseEntity<Long> countCurrencies(CurrencyCriteria criteria) {
        log.debug("REST request to count Currencies by criteria: {}", criteria);
        return ResponseEntity.ok().body(currencyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /currencies/:id} : get the "id" currency.
     *
     * @param id the id of the currencyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currencies/{id}")
    public ResponseEntity<CurrencyDTO> getCurrency(@PathVariable Long id) {
        log.debug("REST request to get Currency : {}", id);
        Optional<CurrencyDTO> currencyDTO = currencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currencyDTO);
    }
    @GetMapping("/currenciesCcy/{ccy}")
    public ResponseDto<List<CurrencyDTO>> getAllCurrenciesByBankName(@PathVariable String ccy){
        log.debug("Search by ccy");
        return currencyServiceResolvers.findByCcy(ccy);
    }
    @GetMapping("/currencyByRequestDto")
    public ResponseDto<List<Currency>> getAllByCurrency(@RequestBody CurrencyRequestDTO currencyRequestDTO){
        log.debug("start resolveGetCurrencyService");
        return currencyServiceResolvers.resolveGetCurrencyService(currencyRequestDTO);
    }

    /**
     * {@code DELETE  /currencies/:id} : delete the "id" currency.
     *
     * @param id the id of the currencyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/currencies/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        log.debug("REST request to delete Currency : {}", id);
        currencyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
