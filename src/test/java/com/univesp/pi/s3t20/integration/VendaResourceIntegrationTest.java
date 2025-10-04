package com.univesp.pi.s3t20.integration;

import com.univesp.pi.s3t20.model.Venda;
import com.univesp.pi.s3t20.model.Produto;
import com.univesp.pi.s3t20.model.Cliente;
import com.univesp.pi.s3t20.model.FormaPagamento;
import com.univesp.pi.s3t20.repository.VendaRepository;
import com.univesp.pi.s3t20.repository.ProdutoRepository;
import com.univesp.pi.s3t20.repository.ClienteRepository;
import com.univesp.pi.s3t20.repository.FormaPagamentoRepository;
import com.univesp.pi.s3t20.service.VendaService;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class VendaResourceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private VendaRepository vendaRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    private String baseUrl;
    private Produto produtoTeste;
    private Cliente clienteTeste;
    private FormaPagamento formaPagamentoTeste;

    @BeforeEach
    void setUp() {
        vendaRepository.deleteAll();
        produtoRepository.deleteAll();
        clienteRepository.deleteAll();
        formaPagamentoRepository.deleteAll();
        baseUrl = "http://localhost:" + port;
        
        // Criar entidades de teste
        produtoTeste = new Produto();
        produtoTeste.setIdProduto("PROD001");
        produtoTeste.setProduto("Produto Teste");
        produtoTeste.setCategoria("Eletrônicos");
        produtoTeste.setPedidoMinimo(10);
        produtoTeste.setCustoUnitario(new BigDecimal("50.00"));
        produtoTeste.setPrecoSugerido(new BigDecimal("80.00"));
        produtoTeste.setCentoPreco(new BigDecimal("75.00"));
        produtoTeste = produtoRepository.save(produtoTeste);

        clienteTeste = new Cliente();
        clienteTeste.setIdCliente("CLI001");
        clienteTeste.setNomeCliente("Cliente Teste");
        clienteTeste.setBairro("Centro");
        clienteTeste.setCidade("São Paulo");
        clienteTeste.setTipoCliente("Pessoa Física");
        clienteTeste = clienteRepository.save(clienteTeste);

        formaPagamentoTeste = new FormaPagamento();
        formaPagamentoTeste.setIdPagamento("PAG001");
        formaPagamentoTeste.setFormaPagamento("Cartão de Crédito");
        formaPagamentoTeste.setIsActive(true);
        formaPagamentoTeste = formaPagamentoRepository.save(formaPagamentoTeste);
    }

    @Test
    void testListarTodasVendas() {
        // Criar algumas vendas de teste
        Venda venda1 = new Venda();
        venda1.setIdVenda("VENDA001");
        venda1.setData(LocalDate.now());
        venda1.setQuantidade(2);
        venda1.setPrecoUnitario(new BigDecimal("80.00"));
        venda1.setReceitaTotal(new BigDecimal("160.00"));
        venda1.setProduto(produtoTeste);
        venda1.setCliente(clienteTeste);
        venda1.setFormaPagamento(formaPagamentoTeste);
        vendaRepository.save(venda1);

        Venda venda2 = new Venda();
        venda2.setIdVenda("VENDA002");
        venda2.setData(LocalDate.now());
        venda2.setQuantidade(1);
        venda2.setPrecoUnitario(new BigDecimal("100.00"));
        venda2.setReceitaTotal(new BigDecimal("100.00"));
        venda2.setProduto(produtoTeste);
        venda2.setCliente(clienteTeste);
        venda2.setFormaPagamento(formaPagamentoTeste);
        vendaRepository.save(venda2);
        
        ResponseEntity<Venda[]> response = restTemplate.getForEntity(baseUrl + "/vendas", Venda[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void testCriarVenda() {
        Map<String, Object> vendaDTO = new HashMap<>();
        vendaDTO.put("data", LocalDate.now().toString());
        vendaDTO.put("produtoId", produtoTeste.getId());
        vendaDTO.put("quantidade", 3);
        vendaDTO.put("precoUnitario", 90.00);
        vendaDTO.put("clienteId", clienteTeste.getId());
        vendaDTO.put("formaPagamentoId", formaPagamentoTeste.getId());

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/vendas", vendaDTO, Map.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().get("quantidade"));
    }

    @Test
    void testCriarVendaComProdutoInexistente() {
        Venda vendaComProdutoInexistente = new Venda();
        vendaComProdutoInexistente.setIdVenda("VENDA004");
        vendaComProdutoInexistente.setData(LocalDate.now());
        vendaComProdutoInexistente.setQuantidade(1);
        vendaComProdutoInexistente.setPrecoUnitario(new BigDecimal("50.00"));
        vendaComProdutoInexistente.setReceitaTotal(new BigDecimal("50.00"));
        
        Produto produtoInexistente = new Produto();
        produtoInexistente.setId(999L);
        vendaComProdutoInexistente.setProduto(produtoInexistente);
        
        Cliente clienteRef = new Cliente();
        clienteRef.setId(clienteTeste.getId());
        vendaComProdutoInexistente.setCliente(clienteRef);
        
        FormaPagamento formaPagamentoRef = new FormaPagamento();
        formaPagamentoRef.setId(formaPagamentoTeste.getId());
        vendaComProdutoInexistente.setFormaPagamento(formaPagamentoRef);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/vendas", vendaComProdutoInexistente, String.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testBuscarVendaPorId() {
        Venda venda = new Venda();
        venda.setIdVenda("VENDA001");
        venda.setData(LocalDate.now());
        venda.setQuantidade(2);
        venda.setPrecoUnitario(new BigDecimal("80.00"));
        venda.setReceitaTotal(new BigDecimal("160.00"));
        venda.setProduto(produtoTeste);
        venda.setCliente(clienteTeste);
        venda.setFormaPagamento(formaPagamentoTeste);
        venda = vendaRepository.save(venda);

        ResponseEntity<Venda> response = restTemplate.getForEntity(baseUrl + "/vendas/" + venda.getId(), Venda.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("VENDA001", response.getBody().getIdVenda());
        assertEquals(2, response.getBody().getQuantidade());
    }

    @Test
    void testBuscarVendaPorIdInexistente() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/vendas/999", String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testBuscarVendasPorData() {
        LocalDate dataTeste = LocalDate.now();
        
        Venda venda1 = new Venda();
        venda1.setIdVenda("VENDA001");
        venda1.setData(dataTeste);
        venda1.setQuantidade(2);
        venda1.setPrecoUnitario(new BigDecimal("80.00"));
        venda1.setReceitaTotal(new BigDecimal("160.00"));
        venda1.setProduto(produtoTeste);
        venda1.setCliente(clienteTeste);
        venda1.setFormaPagamento(formaPagamentoTeste);
        vendaRepository.save(venda1);

        Venda venda2 = new Venda();
        venda2.setIdVenda("VENDA002");
        venda2.setData(dataTeste.plusDays(1));
        venda2.setQuantidade(1);
        venda2.setPrecoUnitario(new BigDecimal("100.00"));
        venda2.setReceitaTotal(new BigDecimal("100.00"));
        venda2.setProduto(produtoTeste);
        venda2.setCliente(clienteTeste);
        venda2.setFormaPagamento(formaPagamentoTeste);
        vendaRepository.save(venda2);

        ResponseEntity<Venda[]> response = restTemplate.getForEntity(baseUrl + "/vendas/data/" + dataTeste, Venda[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
        assertEquals(dataTeste, response.getBody()[0].getData());
    }

    @Test
    void testBuscarVendasPorCliente() {
        Venda venda1 = new Venda();
        venda1.setIdVenda("VENDA001");
        venda1.setData(LocalDate.now());
        venda1.setQuantidade(2);
        venda1.setPrecoUnitario(new BigDecimal("80.00"));
        venda1.setReceitaTotal(new BigDecimal("160.00"));
        venda1.setProduto(produtoTeste);
        venda1.setCliente(clienteTeste);
        venda1.setFormaPagamento(formaPagamentoTeste);
        vendaRepository.save(venda1);

        ResponseEntity<Venda[]> response = restTemplate.getForEntity(baseUrl + "/vendas/cliente/" + clienteTeste.getId(), Venda[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
        assertEquals(clienteTeste.getId(), response.getBody()[0].getCliente().getId());
    }

    @Test
    void testBuscarVendasPorProduto() {
        Venda venda1 = new Venda();
        venda1.setIdVenda("VENDA001");
        venda1.setData(LocalDate.now());
        venda1.setQuantidade(2);
        venda1.setPrecoUnitario(new BigDecimal("80.00"));
        venda1.setReceitaTotal(new BigDecimal("160.00"));
        venda1.setProduto(produtoTeste);
        venda1.setCliente(clienteTeste);
        venda1.setFormaPagamento(formaPagamentoTeste);
        vendaRepository.save(venda1);

        ResponseEntity<Venda[]> response = restTemplate.getForEntity(baseUrl + "/vendas/produto/" + produtoTeste.getId(), Venda[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
        assertEquals(produtoTeste.getId(), response.getBody()[0].getProduto().getId());
    }

    @Test
    void testBuscarVendasPorPeriodo() {
        LocalDate dataInicio = LocalDate.now();
        LocalDate dataFim = LocalDate.now().plusDays(2);
        
        Venda venda1 = new Venda();
        venda1.setIdVenda("VENDA001");
        venda1.setData(dataInicio);
        venda1.setQuantidade(2);
        venda1.setPrecoUnitario(new BigDecimal("80.00"));
        venda1.setReceitaTotal(new BigDecimal("160.00"));
        venda1.setProduto(produtoTeste);
        venda1.setCliente(clienteTeste);
        venda1.setFormaPagamento(formaPagamentoTeste);
        vendaRepository.save(venda1);

        Venda venda2 = new Venda();
        venda2.setIdVenda("VENDA002");
        venda2.setData(dataFim.plusDays(1)); // Fora do período
        venda2.setQuantidade(1);
        venda2.setPrecoUnitario(new BigDecimal("100.00"));
        venda2.setReceitaTotal(new BigDecimal("100.00"));
        venda2.setProduto(produtoTeste);
        venda2.setCliente(clienteTeste);
        venda2.setFormaPagamento(formaPagamentoTeste);
        vendaRepository.save(venda2);

        String url = baseUrl + "/vendas/periodo?dataInicio=" + dataInicio + "&dataFim=" + dataFim;
        ResponseEntity<Venda[]> response = restTemplate.getForEntity(url, Venda[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
    }

    @Test
    void testAtualizarVenda() {
        Venda venda = new Venda();
        venda.setIdVenda("VENDA001");
        venda.setData(LocalDate.now());
        venda.setQuantidade(2);
        venda.setPrecoUnitario(new BigDecimal("80.00"));
        venda.setReceitaTotal(new BigDecimal("160.00"));
        venda.setProduto(produtoTeste);
        venda.setCliente(clienteTeste);
        venda.setFormaPagamento(formaPagamentoTeste);
        venda = vendaRepository.save(venda);

        Map<String, Object> vendaDTO = new HashMap<>();
        vendaDTO.put("data", LocalDate.now().toString());
        vendaDTO.put("produtoId", produtoTeste.getId());
        vendaDTO.put("quantidade", 5);
        vendaDTO.put("precoUnitario", 90.00);
        vendaDTO.put("clienteId", clienteTeste.getId());
        vendaDTO.put("formaPagamentoId", formaPagamentoTeste.getId());

        ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl + "/vendas/" + venda.getId(),
            org.springframework.http.HttpMethod.PUT,
            new org.springframework.http.HttpEntity<>(vendaDTO),
            Map.class
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().get("quantidade"));
    }

    @Test
    void testAtualizarVendaInexistente() {
        Venda vendaInexistente = new Venda();
        vendaInexistente.setIdVenda("VENDA999");
        vendaInexistente.setData(LocalDate.now());
        vendaInexistente.setQuantidade(1);
        vendaInexistente.setPrecoUnitario(new BigDecimal("50.00"));
        vendaInexistente.setReceitaTotal(new BigDecimal("50.00"));

        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/vendas/999",
            org.springframework.http.HttpMethod.PUT,
            new org.springframework.http.HttpEntity<>(vendaInexistente),
            String.class
        );
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeletarVenda() {
        Venda venda = new Venda();
        venda.setIdVenda("VENDA001");
        venda.setData(LocalDate.now());
        venda.setQuantidade(2);
        venda.setPrecoUnitario(new BigDecimal("80.00"));
        venda.setReceitaTotal(new BigDecimal("160.00"));
        venda.setProduto(produtoTeste);
        venda.setCliente(clienteTeste);
        venda.setFormaPagamento(formaPagamentoTeste);
        venda = vendaRepository.save(venda);

        ResponseEntity<Void> response = restTemplate.exchange(
            baseUrl + "/vendas/" + venda.getId(),
            org.springframework.http.HttpMethod.DELETE,
            null,
            Void.class
        );
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        
        // Verificar se foi deletado
        ResponseEntity<String> getResponse = restTemplate.getForEntity(baseUrl + "/vendas/" + venda.getId(), String.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testDeletarVendaInexistente() {
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/vendas/999",
            org.springframework.http.HttpMethod.DELETE,
            null,
            String.class
        );
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testObterResumo() {
        // Criar algumas vendas
        Venda venda1 = new Venda();
        venda1.setIdVenda("VENDA001");
        venda1.setData(LocalDate.now());
        venda1.setQuantidade(2);
        venda1.setPrecoUnitario(new BigDecimal("80.00"));
        venda1.setReceitaTotal(new BigDecimal("160.00"));
        venda1.setProduto(produtoTeste);
        venda1.setCliente(clienteTeste);
        venda1.setFormaPagamento(formaPagamentoTeste);
        vendaRepository.save(venda1);

        Venda venda2 = new Venda();
        venda2.setIdVenda("VENDA002");
        venda2.setData(LocalDate.now());
        venda2.setQuantidade(1);
        venda2.setPrecoUnitario(new BigDecimal("100.00"));
        venda2.setReceitaTotal(new BigDecimal("100.00"));
        venda2.setProduto(produtoTeste);
        venda2.setCliente(clienteTeste);
        venda2.setFormaPagamento(formaPagamentoTeste);
        vendaRepository.save(venda2);

        ResponseEntity<VendaService.VendaResumo> response = restTemplate.getForEntity(baseUrl + "/vendas/resumo", VendaService.VendaResumo.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody().totalVendas);
        assertEquals(260.0, response.getBody().receitaTotal);
    }

    @Test
    void testObterResumoPorPeriodo() {
        LocalDate dataInicio = LocalDate.now();
        LocalDate dataFim = LocalDate.now().plusDays(1);
        
        Venda venda1 = new Venda();
        venda1.setIdVenda("VENDA001");
        venda1.setData(dataInicio);
        venda1.setQuantidade(2);
        venda1.setPrecoUnitario(new BigDecimal("80.00"));
        venda1.setReceitaTotal(new BigDecimal("160.00"));
        venda1.setProduto(produtoTeste);
        venda1.setCliente(clienteTeste);
        venda1.setFormaPagamento(formaPagamentoTeste);
        vendaRepository.save(venda1);

        String url = baseUrl + "/vendas/resumo/periodo?dataInicio=" + dataInicio + "&dataFim=" + dataFim;
        ResponseEntity<VendaService.VendaResumo> response = restTemplate.getForEntity(url, VendaService.VendaResumo.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().totalVendas);
        assertEquals(160.0, response.getBody().receitaTotal);
    }

    @Test
    void testContarVendas() {
        // Criar algumas vendas
        Venda venda1 = new Venda();
        venda1.setIdVenda("VENDA001");
        venda1.setData(LocalDate.now());
        venda1.setQuantidade(2);
        venda1.setPrecoUnitario(new BigDecimal("80.00"));
        venda1.setReceitaTotal(new BigDecimal("160.00"));
        venda1.setProduto(produtoTeste);
        venda1.setCliente(clienteTeste);
        venda1.setFormaPagamento(formaPagamentoTeste);
        vendaRepository.save(venda1);

        Venda venda2 = new Venda();
        venda2.setIdVenda("VENDA002");
        venda2.setData(LocalDate.now());
        venda2.setQuantidade(1);
        venda2.setPrecoUnitario(new BigDecimal("100.00"));
        venda2.setReceitaTotal(new BigDecimal("100.00"));
        venda2.setProduto(produtoTeste);
        venda2.setCliente(clienteTeste);
        venda2.setFormaPagamento(formaPagamentoTeste);
        vendaRepository.save(venda2);

        ResponseEntity<Long> response = restTemplate.getForEntity(baseUrl + "/vendas/count", Long.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody());
    }
}
