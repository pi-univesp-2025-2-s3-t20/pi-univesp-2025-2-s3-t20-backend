package com.univesp.pi.s3t20.integration;

import com.univesp.pi.s3t20.model.Produto;
import com.univesp.pi.s3t20.repository.ProdutoRepository;
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
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProdutoResourceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private ProdutoRepository produtoRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        produtoRepository.deleteAll();
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void testListarTodosProdutos() {
        // Criar alguns produtos de teste
        Produto produto1 = new Produto();
        produto1.setIdProduto("PROD001");
        produto1.setProduto("Produto 1");
        produto1.setCategoria("Eletrônicos");
        produto1.setPedidoMinimo(10);
        produto1.setCustoUnitario(new BigDecimal("50.00"));
        produto1.setPrecoSugerido(new BigDecimal("80.00"));
        produto1.setCentoPreco(new BigDecimal("75.00"));
        produtoRepository.save(produto1);

        Produto produto2 = new Produto();
        produto2.setIdProduto("PROD002");
        produto2.setProduto("Produto 2");
        produto2.setCategoria("Roupas");
        produto2.setPedidoMinimo(5);
        produto2.setCustoUnitario(new BigDecimal("30.00"));
        produto2.setPrecoSugerido(new BigDecimal("50.00"));
        produto2.setCentoPreco(new BigDecimal("45.00"));
        produtoRepository.save(produto2);
        
        ResponseEntity<Produto[]> response = restTemplate.getForEntity(baseUrl + "/produtos", Produto[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void testCriarProduto() {
        Map<String, Object> produtoDTO = new HashMap<>();
        produtoDTO.put("produto", "Novo Produto");
        produtoDTO.put("categoria", "Informática");
        produtoDTO.put("pedidoMinimo", 15);
        produtoDTO.put("custoUnitario", 100.00);
        produtoDTO.put("precoSugerido", 150.00);
        produtoDTO.put("centoPreco", 140.00);

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/produtos", produtoDTO, Map.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Novo Produto", response.getBody().get("produto"));
        assertEquals("Informática", response.getBody().get("categoria"));
    }

    @Test
    void testCriarProdutoComCodigoDuplicado() {
        // Criar produto existente
        Produto produtoExistente = new Produto();
        produtoExistente.setIdProduto("PROD001");
        produtoExistente.setProduto("Produto Existente");
        produtoExistente.setCategoria("Eletrônicos");
        produtoExistente.setPedidoMinimo(10);
        produtoExistente.setCustoUnitario(new BigDecimal("50.00"));
        produtoExistente.setPrecoSugerido(new BigDecimal("80.00"));
        produtoExistente.setCentoPreco(new BigDecimal("75.00"));
        produtoRepository.save(produtoExistente);

        // Tentar criar com mesmo código usando DTO - mas o código deve ser gerado automaticamente
        Map<String, Object> produtoDuplicadoDTO = new HashMap<>();
        produtoDuplicadoDTO.put("produto", "Produto Duplicado");
        produtoDuplicadoDTO.put("categoria", "Roupas");
        produtoDuplicadoDTO.put("pedidoMinimo", 5);
        produtoDuplicadoDTO.put("custoUnitario", 30.00);
        produtoDuplicadoDTO.put("precoSugerido", 50.00);
        produtoDuplicadoDTO.put("centoPreco", 45.00);

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/produtos", produtoDuplicadoDTO, Map.class);
        
        // Deve criar com sucesso pois o código será gerado automaticamente
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Produto Duplicado", response.getBody().get("produto"));
        // Verificar se um código único foi gerado
        assertNotNull(response.getBody().get("idProduto"));
        assertNotEquals("PROD001", response.getBody().get("idProduto"));
    }

    @Test
    void testBuscarProdutoPorId() {
        Produto produto = new Produto();
        produto.setIdProduto("PROD001");
        produto.setProduto("Produto Teste");
        produto.setCategoria("Eletrônicos");
        produto.setPedidoMinimo(10);
        produto.setCustoUnitario(new BigDecimal("50.00"));
        produto.setPrecoSugerido(new BigDecimal("80.00"));
        produto.setCentoPreco(new BigDecimal("75.00"));
        produto = produtoRepository.save(produto);

        ResponseEntity<Produto> response = restTemplate.getForEntity(baseUrl + "/produtos/" + produto.getId(), Produto.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PROD001", response.getBody().getIdProduto());
        assertEquals("Produto Teste", response.getBody().getProduto());
    }

    @Test
    void testBuscarProdutoPorIdInexistente() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/produtos/999", String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testBuscarProdutoPorCodigo() {
        Produto produto = new Produto();
        produto.setIdProduto("PROD001");
        produto.setProduto("Produto Teste");
        produto.setCategoria("Eletrônicos");
        produto.setPedidoMinimo(10);
        produto.setCustoUnitario(new BigDecimal("50.00"));
        produto.setPrecoSugerido(new BigDecimal("80.00"));
        produto.setCentoPreco(new BigDecimal("75.00"));
        produtoRepository.save(produto);

        ResponseEntity<Produto> response = restTemplate.getForEntity(baseUrl + "/produtos/codigo/PROD001", Produto.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PROD001", response.getBody().getIdProduto());
    }

    @Test
    void testBuscarProdutoPorCodigoInexistente() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/produtos/codigo/INEXISTENTE", String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testBuscarProdutosPorCategoria() {
        // Criar produtos de categorias diferentes
        Produto produto1 = new Produto();
        produto1.setIdProduto("PROD001");
        produto1.setProduto("Produto 1");
        produto1.setCategoria("Eletrônicos");
        produto1.setPedidoMinimo(10);
        produto1.setCustoUnitario(new BigDecimal("50.00"));
        produto1.setPrecoSugerido(new BigDecimal("80.00"));
        produto1.setCentoPreco(new BigDecimal("75.00"));
        produtoRepository.save(produto1);

        Produto produto2 = new Produto();
        produto2.setIdProduto("PROD002");
        produto2.setProduto("Produto 2");
        produto2.setCategoria("Eletrônicos");
        produto2.setPedidoMinimo(5);
        produto2.setCustoUnitario(new BigDecimal("30.00"));
        produto2.setPrecoSugerido(new BigDecimal("50.00"));
        produto2.setCentoPreco(new BigDecimal("45.00"));
        produtoRepository.save(produto2);

        Produto produto3 = new Produto();
        produto3.setIdProduto("PROD003");
        produto3.setProduto("Produto 3");
        produto3.setCategoria("Roupas");
        produto3.setPedidoMinimo(20);
        produto3.setCustoUnitario(new BigDecimal("25.00"));
        produto3.setPrecoSugerido(new BigDecimal("40.00"));
        produto3.setCentoPreco(new BigDecimal("35.00"));
        produtoRepository.save(produto3);

        ResponseEntity<Produto[]> response = restTemplate.getForEntity(baseUrl + "/produtos/categoria/Eletrônicos", Produto[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void testListarCategorias() {
        // Criar produtos de categorias diferentes
        Produto produto1 = new Produto();
        produto1.setIdProduto("PROD001");
        produto1.setProduto("Produto 1");
        produto1.setCategoria("Eletrônicos");
        produto1.setPedidoMinimo(10);
        produto1.setCustoUnitario(new BigDecimal("50.00"));
        produto1.setPrecoSugerido(new BigDecimal("80.00"));
        produto1.setCentoPreco(new BigDecimal("75.00"));
        produtoRepository.save(produto1);

        Produto produto2 = new Produto();
        produto2.setIdProduto("PROD002");
        produto2.setProduto("Produto 2");
        produto2.setCategoria("Roupas");
        produto2.setPedidoMinimo(5);
        produto2.setCustoUnitario(new BigDecimal("30.00"));
        produto2.setPrecoSugerido(new BigDecimal("50.00"));
        produto2.setCentoPreco(new BigDecimal("45.00"));
        produtoRepository.save(produto2);

        ResponseEntity<String[]> response = restTemplate.getForEntity(baseUrl + "/produtos/categorias", String[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
        assertTrue(java.util.Arrays.asList(response.getBody()).contains("Eletrônicos"));
        assertTrue(java.util.Arrays.asList(response.getBody()).contains("Roupas"));
    }

    @Test
    void testAtualizarProduto() {
        Produto produto = new Produto();
        produto.setIdProduto("PROD001");
        produto.setProduto("Produto Original");
        produto.setCategoria("Eletrônicos");
        produto.setPedidoMinimo(10);
        produto.setCustoUnitario(new BigDecimal("50.00"));
        produto.setPrecoSugerido(new BigDecimal("80.00"));
        produto.setCentoPreco(new BigDecimal("75.00"));
        produto = produtoRepository.save(produto);

        Map<String, Object> produtoDTO = new HashMap<>();
        produtoDTO.put("produto", "Produto Atualizado");
        produtoDTO.put("categoria", "Informática");
        produtoDTO.put("pedidoMinimo", 15);
        produtoDTO.put("custoUnitario", 60.00);
        produtoDTO.put("precoSugerido", 90.00);
        produtoDTO.put("centoPreco", 85.00);

        ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl + "/produtos/" + produto.getId(),
            org.springframework.http.HttpMethod.PUT,
            new org.springframework.http.HttpEntity<>(produtoDTO),
            Map.class
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Produto Atualizado", response.getBody().get("produto"));
        assertEquals("Informática", response.getBody().get("categoria"));
        assertEquals(15, response.getBody().get("pedidoMinimo"));
    }

    @Test
    void testAtualizarProdutoInexistente() {
        Map<String, Object> produtoDTO = new HashMap<>();
        produtoDTO.put("produto", "Produto Inexistente");
        produtoDTO.put("categoria", "Teste");
        produtoDTO.put("pedidoMinimo", 1);
        produtoDTO.put("custoUnitario", 10.00);
        produtoDTO.put("precoSugerido", 15.00);
        produtoDTO.put("centoPreco", 12.00);

        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/produtos/999",
            org.springframework.http.HttpMethod.PUT,
            new org.springframework.http.HttpEntity<>(produtoDTO),
            String.class
        );
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeletarProduto() {
        Produto produto = new Produto();
        produto.setIdProduto("PROD001");
        produto.setProduto("Produto Teste");
        produto.setCategoria("Eletrônicos");
        produto.setPedidoMinimo(10);
        produto.setCustoUnitario(new BigDecimal("50.00"));
        produto.setPrecoSugerido(new BigDecimal("80.00"));
        produto.setCentoPreco(new BigDecimal("75.00"));
        produto = produtoRepository.save(produto);

        ResponseEntity<Void> response = restTemplate.exchange(
            baseUrl + "/produtos/" + produto.getId(),
            org.springframework.http.HttpMethod.DELETE,
            null,
            Void.class
        );
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        
        // Verificar se foi deletado
        ResponseEntity<String> getResponse = restTemplate.getForEntity(baseUrl + "/produtos/" + produto.getId(), String.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testDeletarProdutoInexistente() {
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/produtos/999",
            org.springframework.http.HttpMethod.DELETE,
            null,
            String.class
        );
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testContarProdutos() {
        // Criar alguns produtos
        Produto produto1 = new Produto();
        produto1.setIdProduto("PROD001");
        produto1.setProduto("Produto 1");
        produto1.setCategoria("Eletrônicos");
        produto1.setPedidoMinimo(10);
        produto1.setCustoUnitario(new BigDecimal("50.00"));
        produto1.setPrecoSugerido(new BigDecimal("80.00"));
        produto1.setCentoPreco(new BigDecimal("75.00"));
        produtoRepository.save(produto1);

        Produto produto2 = new Produto();
        produto2.setIdProduto("PROD002");
        produto2.setProduto("Produto 2");
        produto2.setCategoria("Roupas");
        produto2.setPedidoMinimo(5);
        produto2.setCustoUnitario(new BigDecimal("30.00"));
        produto2.setPrecoSugerido(new BigDecimal("50.00"));
        produto2.setCentoPreco(new BigDecimal("45.00"));
        produtoRepository.save(produto2);

        ResponseEntity<Long> response = restTemplate.getForEntity(baseUrl + "/produtos/count", Long.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody());
    }
}
