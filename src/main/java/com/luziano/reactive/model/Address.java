package com.luziano.reactive.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    @JsonProperty("cep")
    private String zipCode;

    @JsonProperty("logradouro")
    private String street;

    @JsonProperty("complemento")
    private String complement;

    @JsonProperty("bairro")
    private String neighbourhood;

    @JsonProperty("localidade")
    private String city;

    @JsonProperty("uf")
    private String state;
}
