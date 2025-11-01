package com.univesp.pi.s3t20.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.univesp.pi.s3t20.model.Produto;
import com.univesp.pi.s3t20.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProdutoResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        produtoRepository.deleteAll();
    }

    private Produto criarProduto(String codigo, String nome, String categoria) {
        Produto produto = new Produto();
        produto.setIdProduto(codigo);
        produto.setProduto(nome);
        produto.setCategoria(categoria);
        produto.setPedidoMinimo(10);
        produto.setCustoUnitario(new BigDecimal("50.00"));
        produto.setPrecoSugerido(new BigDecimal("80.00"));
        produto.setCentoPreco(new BigDecimal("75.00"));
        return produtoRepository.save(produto);
    }

    @Test
    @WithMockUser
    void testListarTodosProdutos() throws Exception {
        criarProduto("PROD001", "Produto 1", "Eletrônicos");
        criarProduto("PROD002", "Produto 2", "Roupas");

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCriarProduto() throws Exception {
        Map<String, Object> produtoDTO = new HashMap<>();
        produtoDTO.put("produto", "Novo Produto");
        produtoDTO.put("categoria", "Informática");
        produtoDTO.put("pedidoMinimo", 15);
        produtoDTO.put("custoUnitario", 100.00);
        produtoDTO.put("precoSugerido", 150.00);
        produtoDTO.put("centoPreco", 140.00);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.produto", is("Novo Produto")))
                .andExpect(jsonPath("$.categoria", is("Informática")));
    }

    @Test
    @WithMockUser
    void testBuscarProdutoPorId() throws Exception {
        Produto produto = criarProduto("PROD001", "Produto Teste", "Eletrônicos");

        mockMvc.perform(get("/produtos/" + produto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProduto", is("PROD001")))
                .andExpect(jsonPath("$.produto", is("Produto Teste")));
    }

    @Test
    @WithMockUser
    void testBuscarProdutoPorIdInexistente() throws Exception {
        mockMvc.perform(get("/produtos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAtualizarProduto() throws Exception {
        Produto produto = criarProduto("PROD001", "Produto Original", "Eletrônicos");

        Map<String, Object> produtoDTO = new HashMap<>();
        produtoDTO.put("produto", "Produto Atualizado");
        produtoDTO.put("categoria", "Informática");
        produtoDTO.put("pedidoMinimo", 15);
        produtoDTO.put("custoUnitario", 60.00);
        produtoDTO.put("precoSugerido", 90.00);
        produtoDTO.put("centoPreco", 85.00);

        mockMvc.perform(put("/produtos/" + produto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produto", is("Produto Atualizado")))
                .andExpect(jsonPath("$.categoria", is("Informática")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeletarProduto() throws Exception {
        Produto produto = criarProduto("PROD001", "Produto a ser deletado", "Descartáveis");

        mockMvc.perform(delete("/produtos/" + produto.getId()))
                .andExpect(status().isNoContent());

        assertFalse(produtoRepository.findById(produto.getId()).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeletarProdutoInexistente() throws Exception {
        mockMvc.perform(delete("/produtos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testContarProdutos() throws Exception {
        criarProduto("PROD001", "Produto 1", "Eletrônicos");
        criarProduto("PROD002", "Produto 2", "Roupas");

        mockMvc.perform(get("/produtos/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }
}
