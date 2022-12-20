package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Currency;
import uz.devops.service.dto.CurrencyDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * Mapper for the entity {@link Currency} and its DTO {@link CurrencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CurrencyMapper extends EntityMapper<CurrencyDTO, Currency> {
    @Override
    @Mapping(target = "date", source = "date", qualifiedByName = "parseToInstant")
    Currency toEntity(CurrencyDTO dto);
    @Named("parseToInstant")
    default Instant parseToInstant(String data){
        String pattern = "dd.MM.yyyy";
        try {
            Date date = new SimpleDateFormat(pattern).parse(data);
            return date.toInstant();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

//    @Mapping(target = "date", source = "date", qualifiedByName = "parseToInstant")
//    Instant toEntity(CurrencyDTO dto);
//    @Named("parseToInstant")
//    default Instant parseToInstant(String data){
//        String pattern = "dd.MM.yyyy";
//        Date date = null;
//        try {
//            date = new SimpleDateFormat(pattern).parse(data);
//            return date.toInstant();
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @Override
//    @Mapping(target = "date", source = "date", dateFormat = "dd.MM.yyyy")
//    CurrencyDTO toDto(Currency entity);
}
