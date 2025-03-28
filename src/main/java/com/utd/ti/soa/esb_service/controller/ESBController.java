package com.utd.ti.soa.esb_service.controller;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
// Dependencias para realizar la construccion de las peticiones get y post
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;    
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.utd.ti.soa.esb_service.model.Client;
import com.utd.ti.soa.esb_service.model.User;
import org.springframework.http.HttpStatus;
import com.utd.ti.soa.esb_service.utils.Auth;

@RestController
@RequestMapping("/api/v1/esb")
public class ESBController {
    private final WebClient webClient = WebClient.create();
    private final Auth auth = new Auth(); // Instanciar `Auth`

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody User user,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    System.out.println("Request Body: " + user);
    System.out.println("Token recibido: " + token);
        // System.out.println("Request Body: " + user);
    
        // Validar el token
    if (!auth.validateToken(token)) {
        return ResponseEntity.status(401)
                .body("Token invalido o expirado");
    }
        try {
            String response = webClient.post()
                .uri("http://users.railway.internal:3001/api/user/")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> System.out.println("Error: " + error.getMessage()))
                .block();

            return ResponseEntity.ok(response);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error en la solicitud: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<String> getUsers(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Token recibido: " + token);
        // Método para obtener todos los usuarios
    
    if (!auth.validateToken(token)) {
        return ResponseEntity.status(401)
                .body("Token invalido o expirado");
    }

        try {
            String response = webClient.get()
                .uri("https://users-production-ec69.up.railway.app/api/user/all")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> System.out.println("Error: " + error.getMessage()))
                .block();

            return ResponseEntity.ok(response);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error en la solicitud: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }

     @PutMapping("/updateUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, 
        @RequestBody User user, 
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        
        System.out.println("Token recibido: " + token);
        System.out.println("Datos a actualizar: " + user);

        // Validar el token
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }

        try {
            String response = webClient.patch()
                .uri("http://users.railway.internal:3001/api/user/:id" + id)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(user))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class))
                .doOnError(error -> System.out.println("Error: " + error.getMessage()))
                .block();

            return ResponseEntity.ok(response);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error en la solicitud: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }

    // Endpoint para dar de baja lógica a un usuario
    @PatchMapping("/delete/{id}")
    public ResponseEntity<String> changeUserStatus(@PathVariable String id, 
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        
        System.out.println("Token recibido: " + token);
        System.out.println("Dando de baja al usuario con ID: " + id);

        // Validar el token
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }

        try {
            String response = webClient.patch()
                .uri("http://users.railway.internal:3001/api/user/:id" + id)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class))
                .doOnError(error -> System.out.println("Error: " + error.getMessage()))
                .block();

            return ResponseEntity.ok(response);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error en la solicitud: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }

    @PostMapping("/createClient")
    public ResponseEntity<String> createClient(@RequestBody Client client,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    System.out.println("Request Body: " + client);
    System.out.println("Token recibido: " + token);
        // System.out.println("Request Body: " + user);
    
        // Validar el token
    if (!auth.validateToken(token)) {
        return ResponseEntity.status(401)
                .body("Token invalido o expirado");
    }
        try {
            String response = webClient.post()
                .uri("http://clients.railway.internal:3001/api/clients/createClient")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(client))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class))
                .doOnError(error -> System.out.println("Error: " + error.getMessage()))
                .block();

            return ResponseEntity.ok(response);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error en la solicitud: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }
}
