package com.univesp.pi.s3t20.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.univesp.pi.s3t20.model.FormaPagamento;
import com.univesp.pi.s3t20.repository.FormaPagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
public class FormaPagamentoResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        formaPagamentoRepository.deleteAll();
    }

    private FormaPagamento criarFormaPagamento(String codigo, String nome, boolean isActive) {
        FormaPagamento forma = new FormaPagamento();
        forma.setIdPagamento(codigo);
        forma.setFormaPagamento(nome);
        forma.setIsActive(isActive);
        return formaPagamentoRepository.save(forma);
    }

    @Test
    @WithMockUser
    void testListarTodasFormasPagamento() throws Exception {
        criarFormaPagamento("PAG001", "Cartão de Crédito", true);
        criarFormaPagamento("PAG002", "PIX", true);

        mockMvc.perform(get("/formas-pagamento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCriarFormaPagamento() throws Exception {
        Map<String, Object> formaPagamentoDTO = new HashMap<>();
        formaPagamentoDTO.put("formaPagamento", "Boleto");
        formaPagamentoDTO.put("isActive", true);

        mockMvc.perform(post("/formas-pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formaPagamentoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.formaPagamento", is("Boleto")))
                .andExpect(jsonPath("$.isActive", is(true)));
    }

    @Test
    @WithMockUser
    void testBuscarFormaPagamentoPorId() throws Exception {
        FormaPagamento forma = criarFormaPagamento("PAG001", "Cartão de Crédito", true);

        mockMvc.perform(get("/formas-pagamento/" + forma.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPagamento", is("PAG001")));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAtualizarFormaPagamento() throws Exception {
        FormaPagamento forma = criarFormaPagamento("PAG001", "Cartão de Crédito", true);

        Map<String, Object> formaPagamentoDTO = new HashMap<>();
        formaPagamentoDTO.put("formaPagamento", "Cartão de Débito");
        formaPagamentoDTO.put("isActive", false);

        mockMvc.perform(put("/formas-pagamento/" + forma.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formaPagamentoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formaPagamento", is("Cartão de Débito")))
                .andExpect(jsonPath("$.isActive", is(false)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeletarFormaPagamento() throws Exception {
        FormaPagamento forma = criarFormaPagamento("PAG001", "A ser deletado", true);

        mockMvc.perform(delete("/formas-pagamento/" + forma.getId()))
                .andExpect(status().isNoContent());

        assertFalse(formaPagamentoRepository.findById(forma.getId()).isPresent());
    }

    @Test
    @WithMockUser
    void testContarFormasPagamento() throws Exception {
        criarFormaPagamento("PAG001", "Cartão de Crédito", true);
        criarFormaPagamento("PAG002", "PIX", true);

        mockMvc.perform(get("/formas-pagamento/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }
}
