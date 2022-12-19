package uz.devops.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.devops.IntegrationTest;
import uz.devops.domain.Currency;
import uz.devops.repository.CurrencyRepository;
import uz.devops.service.dto.CurrencyDTO;
import uz.devops.service.mapper.CurrencyMapper;

/**
 * Integration tests for the {@link CurrencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CurrencyResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CCY = "AAAAAAAAAA";
    private static final String UPDATED_CCY = "BBBBBBBBBB";

    private static final String DEFAULT_CCY_NM_RU = "AAAAAAAAAA";
    private static final String UPDATED_CCY_NM_RU = "BBBBBBBBBB";

    private static final String DEFAULT_CCY_NM_UZ = "AAAAAAAAAA";
    private static final String UPDATED_CCY_NM_UZ = "BBBBBBBBBB";

    private static final String DEFAULT_CCY_NM_UZC = "AAAAAAAAAA";
    private static final String UPDATED_CCY_NM_UZC = "BBBBBBBBBB";

    private static final String DEFAULT_CCY_NM_EN = "AAAAAAAAAA";
    private static final String UPDATED_CCY_NM_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NOMINAL = "AAAAAAAAAA";
    private static final String UPDATED_NOMINAL = "BBBBBBBBBB";

    private static final String DEFAULT_RATE = "AAAAAAAAAA";
    private static final String UPDATED_RATE = "BBBBBBBBBB";

    private static final String DEFAULT_DIFF = "AAAAAAAAAA";
    private static final String UPDATED_DIFF = "BBBBBBBBBB";

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/currencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCurrencyMockMvc;

    private Currency currency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createEntity(EntityManager em) {
        Currency currency = new Currency()
            .code(DEFAULT_CODE)
            .ccy(DEFAULT_CCY)
            .ccyNmRU(DEFAULT_CCY_NM_RU)
            .ccyNmUZ(DEFAULT_CCY_NM_UZ)
            .ccyNmUZC(DEFAULT_CCY_NM_UZC)
            .ccyNmEN(DEFAULT_CCY_NM_EN)
            .nominal(DEFAULT_NOMINAL)
            .rate(DEFAULT_RATE)
            .diff(DEFAULT_DIFF)
            .date(DEFAULT_DATE);
        return currency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createUpdatedEntity(EntityManager em) {
        Currency currency = new Currency()
            .code(UPDATED_CODE)
            .ccy(UPDATED_CCY)
            .ccyNmRU(UPDATED_CCY_NM_RU)
            .ccyNmUZ(UPDATED_CCY_NM_UZ)
            .ccyNmUZC(UPDATED_CCY_NM_UZC)
            .ccyNmEN(UPDATED_CCY_NM_EN)
            .nominal(UPDATED_NOMINAL)
            .rate(UPDATED_RATE)
            .diff(UPDATED_DIFF)
            .date(UPDATED_DATE);
        return currency;
    }

    @BeforeEach
    public void initTest() {
        currency = createEntity(em);
    }

    @Test
    @Transactional
    void createCurrency() throws Exception {
        int databaseSizeBeforeCreate = currencyRepository.findAll().size();
        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);
        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isCreated());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate + 1);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCurrency.getCcy()).isEqualTo(DEFAULT_CCY);
        assertThat(testCurrency.getCcyNmRU()).isEqualTo(DEFAULT_CCY_NM_RU);
        assertThat(testCurrency.getCcyNmUZ()).isEqualTo(DEFAULT_CCY_NM_UZ);
        assertThat(testCurrency.getCcyNmUZC()).isEqualTo(DEFAULT_CCY_NM_UZC);
        assertThat(testCurrency.getCcyNmEN()).isEqualTo(DEFAULT_CCY_NM_EN);
        assertThat(testCurrency.getNominal()).isEqualTo(DEFAULT_NOMINAL);
        assertThat(testCurrency.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testCurrency.getDiff()).isEqualTo(DEFAULT_DIFF);
        assertThat(testCurrency.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createCurrencyWithExistingId() throws Exception {
        // Create the Currency with an existing ID
        currency.setId(1L);
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        int databaseSizeBeforeCreate = currencyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCurrencies() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].ccy").value(hasItem(DEFAULT_CCY)))
            .andExpect(jsonPath("$.[*].ccyNmRU").value(hasItem(DEFAULT_CCY_NM_RU)))
            .andExpect(jsonPath("$.[*].ccyNmUZ").value(hasItem(DEFAULT_CCY_NM_UZ)))
            .andExpect(jsonPath("$.[*].ccyNmUZC").value(hasItem(DEFAULT_CCY_NM_UZC)))
            .andExpect(jsonPath("$.[*].ccyNmEN").value(hasItem(DEFAULT_CCY_NM_EN)))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL)))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE)))
            .andExpect(jsonPath("$.[*].diff").value(hasItem(DEFAULT_DIFF)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    void getCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get the currency
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL_ID, currency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(currency.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.ccy").value(DEFAULT_CCY))
            .andExpect(jsonPath("$.ccyNmRU").value(DEFAULT_CCY_NM_RU))
            .andExpect(jsonPath("$.ccyNmUZ").value(DEFAULT_CCY_NM_UZ))
            .andExpect(jsonPath("$.ccyNmUZC").value(DEFAULT_CCY_NM_UZC))
            .andExpect(jsonPath("$.ccyNmEN").value(DEFAULT_CCY_NM_EN))
            .andExpect(jsonPath("$.nominal").value(DEFAULT_NOMINAL))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE))
            .andExpect(jsonPath("$.diff").value(DEFAULT_DIFF))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE));
    }

    @Test
    @Transactional
    void getNonExistingCurrency() throws Exception {
        // Get the currency
        restCurrencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency
        Currency updatedCurrency = currencyRepository.findById(currency.getId()).get();
        // Disconnect from session so that the updates on updatedCurrency are not directly saved in db
        em.detach(updatedCurrency);
        updatedCurrency
            .code(UPDATED_CODE)
            .ccy(UPDATED_CCY)
            .ccyNmRU(UPDATED_CCY_NM_RU)
            .ccyNmUZ(UPDATED_CCY_NM_UZ)
            .ccyNmUZC(UPDATED_CCY_NM_UZC)
            .ccyNmEN(UPDATED_CCY_NM_EN)
            .nominal(UPDATED_NOMINAL)
            .rate(UPDATED_RATE)
            .diff(UPDATED_DIFF)
            .date(UPDATED_DATE);
        CurrencyDTO currencyDTO = currencyMapper.toDto(updatedCurrency);

        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCurrency.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testCurrency.getCcyNmRU()).isEqualTo(UPDATED_CCY_NM_RU);
        assertThat(testCurrency.getCcyNmUZ()).isEqualTo(UPDATED_CCY_NM_UZ);
        assertThat(testCurrency.getCcyNmUZC()).isEqualTo(UPDATED_CCY_NM_UZC);
        assertThat(testCurrency.getCcyNmEN()).isEqualTo(UPDATED_CCY_NM_EN);
        assertThat(testCurrency.getNominal()).isEqualTo(UPDATED_NOMINAL);
        assertThat(testCurrency.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testCurrency.getDiff()).isEqualTo(UPDATED_DIFF);
        assertThat(testCurrency.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency
            .ccy(UPDATED_CCY)
            .ccyNmUZ(UPDATED_CCY_NM_UZ)
            .ccyNmUZC(UPDATED_CCY_NM_UZC)
            .rate(UPDATED_RATE)
            .date(UPDATED_DATE);

        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCurrency.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testCurrency.getCcyNmRU()).isEqualTo(DEFAULT_CCY_NM_RU);
        assertThat(testCurrency.getCcyNmUZ()).isEqualTo(UPDATED_CCY_NM_UZ);
        assertThat(testCurrency.getCcyNmUZC()).isEqualTo(UPDATED_CCY_NM_UZC);
        assertThat(testCurrency.getCcyNmEN()).isEqualTo(DEFAULT_CCY_NM_EN);
        assertThat(testCurrency.getNominal()).isEqualTo(DEFAULT_NOMINAL);
        assertThat(testCurrency.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testCurrency.getDiff()).isEqualTo(DEFAULT_DIFF);
        assertThat(testCurrency.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency
            .code(UPDATED_CODE)
            .ccy(UPDATED_CCY)
            .ccyNmRU(UPDATED_CCY_NM_RU)
            .ccyNmUZ(UPDATED_CCY_NM_UZ)
            .ccyNmUZC(UPDATED_CCY_NM_UZC)
            .ccyNmEN(UPDATED_CCY_NM_EN)
            .nominal(UPDATED_NOMINAL)
            .rate(UPDATED_RATE)
            .diff(UPDATED_DIFF)
            .date(UPDATED_DATE);

        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCurrency.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testCurrency.getCcyNmRU()).isEqualTo(UPDATED_CCY_NM_RU);
        assertThat(testCurrency.getCcyNmUZ()).isEqualTo(UPDATED_CCY_NM_UZ);
        assertThat(testCurrency.getCcyNmUZC()).isEqualTo(UPDATED_CCY_NM_UZC);
        assertThat(testCurrency.getCcyNmEN()).isEqualTo(UPDATED_CCY_NM_EN);
        assertThat(testCurrency.getNominal()).isEqualTo(UPDATED_NOMINAL);
        assertThat(testCurrency.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testCurrency.getDiff()).isEqualTo(UPDATED_DIFF);
        assertThat(testCurrency.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, currencyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(currencyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeDelete = currencyRepository.findAll().size();

        // Delete the currency
        restCurrencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, currency.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
