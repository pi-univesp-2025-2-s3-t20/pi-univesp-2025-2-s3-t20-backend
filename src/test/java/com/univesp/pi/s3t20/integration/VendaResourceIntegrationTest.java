package com.univesp.pi.s3t20.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.univesp.pi.s3t20.model.Cliente;
import com.univesp.pi.s3t20.model.FormaPagamento;
import com.univesp.pi.s3t20.model.Produto;
import com.univesp.pi.s3t20.model.Venda;
import com.univesp.pi.s3t20.repository.ClienteRepository;
import com.univesp.pi.s3t20.repository.FormaPagamentoRepository;
import com.univesp.pi.s3t20.repository.ProdutoRepository;
import com.univesp.pi.s3t20.repository.VendaRepository;
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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class VendaResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Produto produtoTeste;
    private Cliente clienteTeste;
    private FormaPagamento formaPagamentoTeste;

    @BeforeEach
    void setUp() {
        vendaRepository.deleteAll();
        produtoRepository.deleteAll();
        clienteRepository.deleteAll();
        formaPagamentoRepository.deleteAll();

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

    private Venda criarVenda(String codigo, int quantidade, BigDecimal preco) {
        Venda venda = new Venda();
        venda.setIdVenda(codigo);
        venda.setData(LocalDate.now());
        venda.setQuantidade(quantidade);
        venda.setPrecoUnitario(preco);
        venda.setReceitaTotal(preco.multiply(new BigDecimal(quantidade)));
        venda.setProduto(produtoTeste);
        venda.setCliente(clienteTeste);
        venda.setFormaPagamento(formaPagamentoTeste);
        return vendaRepository.save(venda);
    }

    @Test
    @WithMockUser
    void testListarTodasVendas() throws Exception {
        criarVenda("VENDA001", 2, new BigDecimal("80.00"));
        criarVenda("VENDA002", 1, new BigDecimal("100.00"));

        mockMvc.perform(get("/vendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCriarVenda() throws Exception {
        Map<String, Object> vendaDTO = new HashMap<>();
        vendaDTO.put("data", LocalDate.now().toString());
        vendaDTO.put("produtoId", produtoTeste.getId());
        vendaDTO.put("quantidade", 3);
        vendaDTO.put("precoUnitario", 90.00);
        vendaDTO.put("clienteId", clienteTeste.getId());
        vendaDTO.put("formaPagamentoId", formaPagamentoTeste.getId());

        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantidade", is(3)));
    }

    @Test
    @WithMockUser
    void testBuscarVendaPorId() throws Exception {
        Venda venda = criarVenda("VENDA001", 2, new BigDecimal("80.00"));

        mockMvc.perform(get("/vendas/" + venda.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idVenda", is("VENDA001")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeletarVenda() throws Exception {
        Venda venda = criarVenda("VENDA001", 2, new BigDecimal("80.00"));

        mockMvc.perform(delete("/vendas/" + venda.getId()))
                .andExpect(status().isNoContent());
    }
}
