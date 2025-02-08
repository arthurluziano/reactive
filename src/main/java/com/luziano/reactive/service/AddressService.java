package com.luziano.reactive.service;

import com.luziano.reactive.client.ViaCepClient;
import com.luziano.reactive.exception.CepNotFoundException;
import com.luziano.reactive.model.Address;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressService.class);
    private final ViaCepClient viaCepClient;

    public Mono<Address> getAddress(String zipCode) {
        return Mono.just(zipCode)
                .doOnNext(it -> log.info("Getting address from zipCode \"{}\" - ({})", zipCode, LocalDateTime.now()))
                .flatMap(viaCepClient::getAddress)
                .doOnError(it -> Mono.error(CepNotFoundException::new));
    }
}
