package uz.devops.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import uz.devops.service.criteria.CurrencyCriteria;
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

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
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
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getCurrenciesByIdFiltering() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        Long id = currency.getId();

        defaultCurrencyShouldBeFound("id.equals=" + id);
        defaultCurrencyShouldNotBeFound("id.notEquals=" + id);

        defaultCurrencyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCurrencyShouldNotBeFound("id.greaterThan=" + id);

        defaultCurrencyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCurrencyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code equals to DEFAULT_CODE
        defaultCurrencyShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the currencyList where code equals to UPDATED_CODE
        defaultCurrencyShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code in DEFAULT_CODE or UPDATED_CODE
        defaultCurrencyShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the currencyList where code equals to UPDATED_CODE
        defaultCurrencyShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code is not null
        defaultCurrencyShouldBeFound("code.specified=true");

        // Get all the currencyList where code is null
        defaultCurrencyShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code contains DEFAULT_CODE
        defaultCurrencyShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the currencyList where code contains UPDATED_CODE
        defaultCurrencyShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code does not contain DEFAULT_CODE
        defaultCurrencyShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the currencyList where code does not contain UPDATED_CODE
        defaultCurrencyShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccy equals to DEFAULT_CCY
        defaultCurrencyShouldBeFound("ccy.equals=" + DEFAULT_CCY);

        // Get all the currencyList where ccy equals to UPDATED_CCY
        defaultCurrencyShouldNotBeFound("ccy.equals=" + UPDATED_CCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccy in DEFAULT_CCY or UPDATED_CCY
        defaultCurrencyShouldBeFound("ccy.in=" + DEFAULT_CCY + "," + UPDATED_CCY);

        // Get all the currencyList where ccy equals to UPDATED_CCY
        defaultCurrencyShouldNotBeFound("ccy.in=" + UPDATED_CCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccy is not null
        defaultCurrencyShouldBeFound("ccy.specified=true");

        // Get all the currencyList where ccy is null
        defaultCurrencyShouldNotBeFound("ccy.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccy contains DEFAULT_CCY
        defaultCurrencyShouldBeFound("ccy.contains=" + DEFAULT_CCY);

        // Get all the currencyList where ccy contains UPDATED_CCY
        defaultCurrencyShouldNotBeFound("ccy.contains=" + UPDATED_CCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccy does not contain DEFAULT_CCY
        defaultCurrencyShouldNotBeFound("ccy.doesNotContain=" + DEFAULT_CCY);

        // Get all the currencyList where ccy does not contain UPDATED_CCY
        defaultCurrencyShouldBeFound("ccy.doesNotContain=" + UPDATED_CCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmRUIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmRU equals to DEFAULT_CCY_NM_RU
        defaultCurrencyShouldBeFound("ccyNmRU.equals=" + DEFAULT_CCY_NM_RU);

        // Get all the currencyList where ccyNmRU equals to UPDATED_CCY_NM_RU
        defaultCurrencyShouldNotBeFound("ccyNmRU.equals=" + UPDATED_CCY_NM_RU);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmRUIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmRU in DEFAULT_CCY_NM_RU or UPDATED_CCY_NM_RU
        defaultCurrencyShouldBeFound("ccyNmRU.in=" + DEFAULT_CCY_NM_RU + "," + UPDATED_CCY_NM_RU);

        // Get all the currencyList where ccyNmRU equals to UPDATED_CCY_NM_RU
        defaultCurrencyShouldNotBeFound("ccyNmRU.in=" + UPDATED_CCY_NM_RU);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmRUIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmRU is not null
        defaultCurrencyShouldBeFound("ccyNmRU.specified=true");

        // Get all the currencyList where ccyNmRU is null
        defaultCurrencyShouldNotBeFound("ccyNmRU.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmRUContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmRU contains DEFAULT_CCY_NM_RU
        defaultCurrencyShouldBeFound("ccyNmRU.contains=" + DEFAULT_CCY_NM_RU);

        // Get all the currencyList where ccyNmRU contains UPDATED_CCY_NM_RU
        defaultCurrencyShouldNotBeFound("ccyNmRU.contains=" + UPDATED_CCY_NM_RU);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmRUNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmRU does not contain DEFAULT_CCY_NM_RU
        defaultCurrencyShouldNotBeFound("ccyNmRU.doesNotContain=" + DEFAULT_CCY_NM_RU);

        // Get all the currencyList where ccyNmRU does not contain UPDATED_CCY_NM_RU
        defaultCurrencyShouldBeFound("ccyNmRU.doesNotContain=" + UPDATED_CCY_NM_RU);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmUZIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmUZ equals to DEFAULT_CCY_NM_UZ
        defaultCurrencyShouldBeFound("ccyNmUZ.equals=" + DEFAULT_CCY_NM_UZ);

        // Get all the currencyList where ccyNmUZ equals to UPDATED_CCY_NM_UZ
        defaultCurrencyShouldNotBeFound("ccyNmUZ.equals=" + UPDATED_CCY_NM_UZ);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmUZIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmUZ in DEFAULT_CCY_NM_UZ or UPDATED_CCY_NM_UZ
        defaultCurrencyShouldBeFound("ccyNmUZ.in=" + DEFAULT_CCY_NM_UZ + "," + UPDATED_CCY_NM_UZ);

        // Get all the currencyList where ccyNmUZ equals to UPDATED_CCY_NM_UZ
        defaultCurrencyShouldNotBeFound("ccyNmUZ.in=" + UPDATED_CCY_NM_UZ);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmUZIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmUZ is not null
        defaultCurrencyShouldBeFound("ccyNmUZ.specified=true");

        // Get all the currencyList where ccyNmUZ is null
        defaultCurrencyShouldNotBeFound("ccyNmUZ.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmUZContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmUZ contains DEFAULT_CCY_NM_UZ
        defaultCurrencyShouldBeFound("ccyNmUZ.contains=" + DEFAULT_CCY_NM_UZ);

        // Get all the currencyList where ccyNmUZ contains UPDATED_CCY_NM_UZ
        defaultCurrencyShouldNotBeFound("ccyNmUZ.contains=" + UPDATED_CCY_NM_UZ);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmUZNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmUZ does not contain DEFAULT_CCY_NM_UZ
        defaultCurrencyShouldNotBeFound("ccyNmUZ.doesNotContain=" + DEFAULT_CCY_NM_UZ);

        // Get all the currencyList where ccyNmUZ does not contain UPDATED_CCY_NM_UZ
        defaultCurrencyShouldBeFound("ccyNmUZ.doesNotContain=" + UPDATED_CCY_NM_UZ);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmUZCIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmUZC equals to DEFAULT_CCY_NM_UZC
        defaultCurrencyShouldBeFound("ccyNmUZC.equals=" + DEFAULT_CCY_NM_UZC);

        // Get all the currencyList where ccyNmUZC equals to UPDATED_CCY_NM_UZC
        defaultCurrencyShouldNotBeFound("ccyNmUZC.equals=" + UPDATED_CCY_NM_UZC);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmUZCIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmUZC in DEFAULT_CCY_NM_UZC or UPDATED_CCY_NM_UZC
        defaultCurrencyShouldBeFound("ccyNmUZC.in=" + DEFAULT_CCY_NM_UZC + "," + UPDATED_CCY_NM_UZC);

        // Get all the currencyList where ccyNmUZC equals to UPDATED_CCY_NM_UZC
        defaultCurrencyShouldNotBeFound("ccyNmUZC.in=" + UPDATED_CCY_NM_UZC);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmUZCIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmUZC is not null
        defaultCurrencyShouldBeFound("ccyNmUZC.specified=true");

        // Get all the currencyList where ccyNmUZC is null
        defaultCurrencyShouldNotBeFound("ccyNmUZC.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmUZCContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmUZC contains DEFAULT_CCY_NM_UZC
        defaultCurrencyShouldBeFound("ccyNmUZC.contains=" + DEFAULT_CCY_NM_UZC);

        // Get all the currencyList where ccyNmUZC contains UPDATED_CCY_NM_UZC
        defaultCurrencyShouldNotBeFound("ccyNmUZC.contains=" + UPDATED_CCY_NM_UZC);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmUZCNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmUZC does not contain DEFAULT_CCY_NM_UZC
        defaultCurrencyShouldNotBeFound("ccyNmUZC.doesNotContain=" + DEFAULT_CCY_NM_UZC);

        // Get all the currencyList where ccyNmUZC does not contain UPDATED_CCY_NM_UZC
        defaultCurrencyShouldBeFound("ccyNmUZC.doesNotContain=" + UPDATED_CCY_NM_UZC);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmENIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmEN equals to DEFAULT_CCY_NM_EN
        defaultCurrencyShouldBeFound("ccyNmEN.equals=" + DEFAULT_CCY_NM_EN);

        // Get all the currencyList where ccyNmEN equals to UPDATED_CCY_NM_EN
        defaultCurrencyShouldNotBeFound("ccyNmEN.equals=" + UPDATED_CCY_NM_EN);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmENIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmEN in DEFAULT_CCY_NM_EN or UPDATED_CCY_NM_EN
        defaultCurrencyShouldBeFound("ccyNmEN.in=" + DEFAULT_CCY_NM_EN + "," + UPDATED_CCY_NM_EN);

        // Get all the currencyList where ccyNmEN equals to UPDATED_CCY_NM_EN
        defaultCurrencyShouldNotBeFound("ccyNmEN.in=" + UPDATED_CCY_NM_EN);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmENIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmEN is not null
        defaultCurrencyShouldBeFound("ccyNmEN.specified=true");

        // Get all the currencyList where ccyNmEN is null
        defaultCurrencyShouldNotBeFound("ccyNmEN.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmENContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmEN contains DEFAULT_CCY_NM_EN
        defaultCurrencyShouldBeFound("ccyNmEN.contains=" + DEFAULT_CCY_NM_EN);

        // Get all the currencyList where ccyNmEN contains UPDATED_CCY_NM_EN
        defaultCurrencyShouldNotBeFound("ccyNmEN.contains=" + UPDATED_CCY_NM_EN);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCcyNmENNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where ccyNmEN does not contain DEFAULT_CCY_NM_EN
        defaultCurrencyShouldNotBeFound("ccyNmEN.doesNotContain=" + DEFAULT_CCY_NM_EN);

        // Get all the currencyList where ccyNmEN does not contain UPDATED_CCY_NM_EN
        defaultCurrencyShouldBeFound("ccyNmEN.doesNotContain=" + UPDATED_CCY_NM_EN);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNominalIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where nominal equals to DEFAULT_NOMINAL
        defaultCurrencyShouldBeFound("nominal.equals=" + DEFAULT_NOMINAL);

        // Get all the currencyList where nominal equals to UPDATED_NOMINAL
        defaultCurrencyShouldNotBeFound("nominal.equals=" + UPDATED_NOMINAL);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNominalIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where nominal in DEFAULT_NOMINAL or UPDATED_NOMINAL
        defaultCurrencyShouldBeFound("nominal.in=" + DEFAULT_NOMINAL + "," + UPDATED_NOMINAL);

        // Get all the currencyList where nominal equals to UPDATED_NOMINAL
        defaultCurrencyShouldNotBeFound("nominal.in=" + UPDATED_NOMINAL);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNominalIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where nominal is not null
        defaultCurrencyShouldBeFound("nominal.specified=true");

        // Get all the currencyList where nominal is null
        defaultCurrencyShouldNotBeFound("nominal.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByNominalContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where nominal contains DEFAULT_NOMINAL
        defaultCurrencyShouldBeFound("nominal.contains=" + DEFAULT_NOMINAL);

        // Get all the currencyList where nominal contains UPDATED_NOMINAL
        defaultCurrencyShouldNotBeFound("nominal.contains=" + UPDATED_NOMINAL);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNominalNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where nominal does not contain DEFAULT_NOMINAL
        defaultCurrencyShouldNotBeFound("nominal.doesNotContain=" + DEFAULT_NOMINAL);

        // Get all the currencyList where nominal does not contain UPDATED_NOMINAL
        defaultCurrencyShouldBeFound("nominal.doesNotContain=" + UPDATED_NOMINAL);
    }

    @Test
    @Transactional
    void getAllCurrenciesByRateIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where rate equals to DEFAULT_RATE
        defaultCurrencyShouldBeFound("rate.equals=" + DEFAULT_RATE);

        // Get all the currencyList where rate equals to UPDATED_RATE
        defaultCurrencyShouldNotBeFound("rate.equals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByRateIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where rate in DEFAULT_RATE or UPDATED_RATE
        defaultCurrencyShouldBeFound("rate.in=" + DEFAULT_RATE + "," + UPDATED_RATE);

        // Get all the currencyList where rate equals to UPDATED_RATE
        defaultCurrencyShouldNotBeFound("rate.in=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where rate is not null
        defaultCurrencyShouldBeFound("rate.specified=true");

        // Get all the currencyList where rate is null
        defaultCurrencyShouldNotBeFound("rate.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByRateContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where rate contains DEFAULT_RATE
        defaultCurrencyShouldBeFound("rate.contains=" + DEFAULT_RATE);

        // Get all the currencyList where rate contains UPDATED_RATE
        defaultCurrencyShouldNotBeFound("rate.contains=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByRateNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where rate does not contain DEFAULT_RATE
        defaultCurrencyShouldNotBeFound("rate.doesNotContain=" + DEFAULT_RATE);

        // Get all the currencyList where rate does not contain UPDATED_RATE
        defaultCurrencyShouldBeFound("rate.doesNotContain=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByDiffIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where diff equals to DEFAULT_DIFF
        defaultCurrencyShouldBeFound("diff.equals=" + DEFAULT_DIFF);

        // Get all the currencyList where diff equals to UPDATED_DIFF
        defaultCurrencyShouldNotBeFound("diff.equals=" + UPDATED_DIFF);
    }

    @Test
    @Transactional
    void getAllCurrenciesByDiffIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where diff in DEFAULT_DIFF or UPDATED_DIFF
        defaultCurrencyShouldBeFound("diff.in=" + DEFAULT_DIFF + "," + UPDATED_DIFF);

        // Get all the currencyList where diff equals to UPDATED_DIFF
        defaultCurrencyShouldNotBeFound("diff.in=" + UPDATED_DIFF);
    }

    @Test
    @Transactional
    void getAllCurrenciesByDiffIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where diff is not null
        defaultCurrencyShouldBeFound("diff.specified=true");

        // Get all the currencyList where diff is null
        defaultCurrencyShouldNotBeFound("diff.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByDiffContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where diff contains DEFAULT_DIFF
        defaultCurrencyShouldBeFound("diff.contains=" + DEFAULT_DIFF);

        // Get all the currencyList where diff contains UPDATED_DIFF
        defaultCurrencyShouldNotBeFound("diff.contains=" + UPDATED_DIFF);
    }

    @Test
    @Transactional
    void getAllCurrenciesByDiffNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where diff does not contain DEFAULT_DIFF
        defaultCurrencyShouldNotBeFound("diff.doesNotContain=" + DEFAULT_DIFF);

        // Get all the currencyList where diff does not contain UPDATED_DIFF
        defaultCurrencyShouldBeFound("diff.doesNotContain=" + UPDATED_DIFF);
    }

    @Test
    @Transactional
    void getAllCurrenciesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where date equals to DEFAULT_DATE
        defaultCurrencyShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the currencyList where date equals to UPDATED_DATE
        defaultCurrencyShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where date in DEFAULT_DATE or UPDATED_DATE
        defaultCurrencyShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the currencyList where date equals to UPDATED_DATE
        defaultCurrencyShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where date is not null
        defaultCurrencyShouldBeFound("date.specified=true");

        // Get all the currencyList where date is null
        defaultCurrencyShouldNotBeFound("date.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCurrencyShouldBeFound(String filter) throws Exception {
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
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
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCurrencyShouldNotBeFound(String filter) throws Exception {
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
