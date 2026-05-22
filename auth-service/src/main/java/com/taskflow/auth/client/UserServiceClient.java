package com.taskflow.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.UUID;

// Chamado após registro para criar o perfil no user-service.
// Se o user-service estiver fora do ar, o registro falha — isso é intencional:
// um usuário sem perfil não conseguiria usar o sistema de qualquer forma.
@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserServiceClient {

    @PostMapping("/api/internal/users")
    void createProfile(@RequestBody Map<String, Object> request);
}
