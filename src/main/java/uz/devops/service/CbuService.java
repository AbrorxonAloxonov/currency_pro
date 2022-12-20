package uz.devops.service;



import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.devops.domain.Currency;
import uz.devops.repository.CurrencyRepository;
import uz.devops.service.dto.ResponseDto;
import uz.devops.service.mapper.CurrencyMapper;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class CbuService implements CommonService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    public CbuService(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    @Override
    public ResponseDto<List<Currency>> getCurrencies(Instant date) {
        List<Currency> list;
        if (date != null) {
            list = currencyRepository.findByDate(date);
        } else {
            list = currencyRepository.findAll();
        }
        log.debug("check if list is empty or empty or not");
        if (!list.isEmpty()) {
            log.debug("list is not empty");
            return new ResponseDto(true, "currencies", list);
        }
        log.debug("list is empty");
        return new ResponseDto<>(false, "not data");
    }

    @Override
    public BankName getBankName() {
        return BankName.CBU;
    }
}
