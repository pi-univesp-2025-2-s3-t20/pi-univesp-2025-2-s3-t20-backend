package com.univesp.pi.s3t20.integration;

import com.univesp.pi.s3t20.model.FormaPagamento;
import com.univesp.pi.s3t20.repository.FormaPagamentoRepository;
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
public class FormaPagamentoResourceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        formaPagamentoRepository.deleteAll();
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void testListarTodasFormasPagamento() {
        // Criar algumas formas de pagamento de teste
        FormaPagamento forma1 = new FormaPagamento();
        forma1.setIdPagamento("PAG001");
        forma1.setFormaPagamento("Cartão de Crédito");
        forma1.setIsActive(true);
        formaPagamentoRepository.save(forma1);

        FormaPagamento forma2 = new FormaPagamento();
        forma2.setIdPagamento("PAG002");
        forma2.setFormaPagamento("PIX");
        forma2.setIsActive(true);
        formaPagamentoRepository.save(forma2);
        
        ResponseEntity<FormaPagamento[]> response = restTemplate.getForEntity(baseUrl + "/formas-pagamento", FormaPagamento[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void testCriarFormaPagamento() {
        Map<String, Object> formaPagamentoDTO = new HashMap<>();
        formaPagamentoDTO.put("formaPagamento", "Boleto");
        formaPagamentoDTO.put("isActive", true);

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/formas-pagamento", formaPagamentoDTO, Map.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Boleto", response.getBody().get("formaPagamento"));
        assertTrue((Boolean) response.getBody().get("isActive"));
    }

    @Test
    void testCriarFormaPagamentoComCodigoDuplicado() {
        // Criar forma de pagamento existente
        FormaPagamento formaExistente = new FormaPagamento();
        formaExistente.setIdPagamento("PAG001");
        formaExistente.setFormaPagamento("Cartão de Crédito");
        formaExistente.setIsActive(true);
        formaPagamentoRepository.save(formaExistente);

        // Tentar criar com mesmo código usando DTO - mas o código deve ser gerado automaticamente
        Map<String, Object> formaDuplicadaDTO = new HashMap<>();
        formaDuplicadaDTO.put("formaPagamento", "Cartão de Débito");
        formaDuplicadaDTO.put("isActive", true);

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/formas-pagamento", formaDuplicadaDTO, Map.class);
        
        // Deve criar com sucesso pois o código será gerado automaticamente
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cartão de Débito", response.getBody().get("formaPagamento"));
        // Verificar se um código único foi gerado
        assertNotNull(response.getBody().get("idPagamento"));
        assertNotEquals("PAG001", response.getBody().get("idPagamento"));
    }

    @Test
    void testBuscarFormaPagamentoPorId() {
        FormaPagamento forma = new FormaPagamento();
        forma.setIdPagamento("PAG001");
        forma.setFormaPagamento("Cartão de Crédito");
        forma.setIsActive(true);
        forma = formaPagamentoRepository.save(forma);

        ResponseEntity<FormaPagamento> response = restTemplate.getForEntity(baseUrl + "/formas-pagamento/" + forma.getId(), FormaPagamento.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PAG001", response.getBody().getIdPagamento());
        assertEquals("Cartão de Crédito", response.getBody().getFormaPagamento());
    }

    @Test
    void testBuscarFormaPagamentoPorIdInexistente() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/formas-pagamento/999", String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testBuscarFormaPagamentoPorCodigo() {
        FormaPagamento forma = new FormaPagamento();
        forma.setIdPagamento("PAG001");
        forma.setFormaPagamento("Cartão de Crédito");
        forma.setIsActive(true);
        formaPagamentoRepository.save(forma);

        ResponseEntity<FormaPagamento> response = restTemplate.getForEntity(baseUrl + "/formas-pagamento/codigo/PAG001", FormaPagamento.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PAG001", response.getBody().getIdPagamento());
    }

    @Test
    void testBuscarFormaPagamentoPorCodigoInexistente() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/formas-pagamento/codigo/INEXISTENTE", String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAtualizarFormaPagamento() {
        FormaPagamento forma = new FormaPagamento();
        forma.setIdPagamento("PAG001");
        forma.setFormaPagamento("Cartão de Crédito");
        forma.setIsActive(true);
        forma = formaPagamentoRepository.save(forma);

        Map<String, Object> formaPagamentoDTO = new HashMap<>();
        formaPagamentoDTO.put("formaPagamento", "Cartão de Débito");
        formaPagamentoDTO.put("isActive", false);

        ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl + "/formas-pagamento/" + forma.getId(),
            org.springframework.http.HttpMethod.PUT,
            new org.springframework.http.HttpEntity<>(formaPagamentoDTO),
            Map.class
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cartão de Débito", response.getBody().get("formaPagamento"));
        assertFalse((Boolean) response.getBody().get("isActive"));
        // Verificar se o código único foi preservado
        assertEquals("PAG001", response.getBody().get("idPagamento"));
    }

    @Test
    void testAtualizarFormaPagamentoInexistente() {
        Map<String, Object> formaPagamentoDTO = new HashMap<>();
        formaPagamentoDTO.put("formaPagamento", "Dinheiro");
        formaPagamentoDTO.put("isActive", true);

        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/formas-pagamento/999",
            org.springframework.http.HttpMethod.PUT,
            new org.springframework.http.HttpEntity<>(formaPagamentoDTO),
            String.class
        );
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeletarFormaPagamento() {
        FormaPagamento forma = new FormaPagamento();
        forma.setIdPagamento("PAG001");
        forma.setFormaPagamento("Cartão de Crédito");
        forma.setIsActive(true);
        forma = formaPagamentoRepository.save(forma);

        ResponseEntity<Void> response = restTemplate.exchange(
            baseUrl + "/formas-pagamento/" + forma.getId(),
            org.springframework.http.HttpMethod.DELETE,
            null,
            Void.class
        );
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        
        // Verificar se foi deletado
        ResponseEntity<String> getResponse = restTemplate.getForEntity(baseUrl + "/formas-pagamento/" + forma.getId(), String.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testDeletarFormaPagamentoInexistente() {
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/formas-pagamento/999",
            org.springframework.http.HttpMethod.DELETE,
            null,
            String.class
        );
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testContarFormasPagamento() {
        // Criar algumas formas de pagamento
        FormaPagamento forma1 = new FormaPagamento();
        forma1.setIdPagamento("PAG001");
        forma1.setFormaPagamento("Cartão de Crédito");
        forma1.setIsActive(true);
        formaPagamentoRepository.save(forma1);

        FormaPagamento forma2 = new FormaPagamento();
        forma2.setIdPagamento("PAG002");
        forma2.setFormaPagamento("PIX");
        forma2.setIsActive(true);
        formaPagamentoRepository.save(forma2);

        ResponseEntity<Long> response = restTemplate.getForEntity(baseUrl + "/formas-pagamento/count", Long.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody());
    }
}
