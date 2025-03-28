package com.utd.ti.soa.esb_service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
// Esto es para que funcione el fokin token
@NoArgsConstructor  // Asegura que haya un constructor vacío


public class Client {
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String birthDate;
    private String address;
}
