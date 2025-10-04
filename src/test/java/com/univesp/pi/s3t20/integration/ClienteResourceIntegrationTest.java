package com.univesp.pi.s3t20.integration;

import com.univesp.pi.s3t20.model.Cliente;
import com.univesp.pi.s3t20.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ClienteResourceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private ClienteRepository clienteRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void testListarTodosClientes() {
        // Criar alguns clientes de teste
        Cliente cliente1 = new Cliente();
        cliente1.setIdCliente("TEST001");
        cliente1.setNomeCliente("Cliente Teste 1");
        cliente1.setBairro("Centro");
        cliente1.setCidade("São Paulo");
        cliente1.setTipoCliente("PF");
        clienteRepository.save(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setIdCliente("TEST002");
        cliente2.setNomeCliente("Cliente Teste 2");
        cliente2.setBairro("Zona Sul");
        cliente2.setCidade("Rio de Janeiro");
        cliente2.setTipoCliente("PJ");
        clienteRepository.save(cliente2);
        
        ResponseEntity<Cliente[]> response = restTemplate.getForEntity(baseUrl + "/clientes", Cliente[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void testCriarCliente() {
        // Criar DTO para o cliente
        Map<String, Object> clienteDTO = new HashMap<>();
        clienteDTO.put("nomeCliente", "Novo Cliente");
        clienteDTO.put("bairro", "Centro");
        clienteDTO.put("cidade", "São Paulo");
        clienteDTO.put("tipoCliente", "Pessoa Física");

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/clientes", clienteDTO, Map.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Novo Cliente", response.getBody().get("nomeCliente"));
    }

    @Test
    void testBuscarClientePorIdInexistente() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/clientes/999", String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}