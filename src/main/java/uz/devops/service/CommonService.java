package uz.devops.service;

import uz.devops.domain.Currency;
import uz.devops.service.dto.ResponseDto;

import java.time.Instant;
import java.util.List;

public interface CommonService {
    ResponseDto<List<Currency>> getCurrencies(Instant date);

    BankName getBankName();
}
