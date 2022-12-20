package uz.devops.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.devops.domain.Currency;
import uz.devops.repository.CurrencyRepository;
import uz.devops.service.dto.CurrencyDTO;
import uz.devops.service.dto.CurrencyRequestDTO;
import uz.devops.service.dto.ResponseDto;
import uz.devops.service.mapper.CurrencyMapper;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceResolvers implements CommonService{

    @Autowired
    private final List<CommonService> commonService;

    private final Map<BankName, CommonService> currencyServiceMap;
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    @PostConstruct
    void collectCurrencyServices() {
        for (CommonService commonService : commonService) {
            log.debug("Start collecting currency services");
            currencyServiceMap.put(commonService.getBankName(),commonService);
        }
    }

    CommonService getAvailableService(BankName bankName) {
        log.debug("Request to get available service with bankName: {}", bankName);
        return currencyServiceMap.get(bankName);
    }

    public ResponseDto<List<Currency>> resolveGetCurrencyService(CurrencyRequestDTO currencyRequestDTO) {
        BankName bankName;
        Currency currency;
        try{
            bankName = BankName.valueOf(currencyRequestDTO.getName().toUpperCase());
            String date = currencyRequestDTO.getDate();
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setDate(date);
            log.debug("currencyDto toEntity");
            currency = currencyMapper.toEntity(currencyDTO);
        }catch (IllegalArgumentException e){
            return new ResponseDto<>(false, "There is no bank with such a name");
        }catch (NullPointerException e){
            return new ResponseDto<>(false,"data not available");
        }
        catch (Exception e){
            return new ResponseDto<>(false,"Unexpected error");
        }


        log.debug("start resolveGetCurrencyService method");
        if (!currencyServiceMap.containsKey(bankName)) {
            log.debug("no information found");
            return new ResponseDto<>(false, "no information found");
        }

        var responseDto = getAvailableService(bankName).getCurrencies(currency.getDate());
        if (responseDto == null) {
            log.debug("responseDto null");
            return new ResponseDto<>(false, "no information found");
        }
        return responseDto;
    }

    public ResponseDto<List<CurrencyDTO>> findByCcy(String ccy) {
        log.debug("start findByCcy");
        List<Currency> byCcy = currencyRepository.findByCcy(ccy);
        List<CurrencyDTO> DTO = currencyMapper.toDto(byCcy);
        log.debug("check list is empty or not");
        if (!DTO.isEmpty()) {
            log.debug("not empty");
            return new ResponseDto<>(true, ccy, DTO);
        }
        log.debug("empty");
        return new ResponseDto<>(false, "no information found");
    }

    @Override
    public ResponseDto<List<Currency>> getCurrencies(Instant date) {
        return null;
    }

    @Override
    public BankName getBankName() {
        return null;
    }
}
