package com.univesp.pi.s3t20.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.univesp.pi.s3t20.model.Cliente;
import com.univesp.pi.s3t20.repository.ClienteRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClienteResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();
    }

    private Cliente criarCliente(String codigo, String nome, String bairro, String cidade, String tipo) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(codigo);
        cliente.setNomeCliente(nome);
        cliente.setBairro(bairro);
        cliente.setCidade(cidade);
        cliente.setTipoCliente(tipo);
        return clienteRepository.save(cliente);
    }

    @Test
    @WithMockUser
    void testListarTodosClientes() throws Exception {
        criarCliente("TEST001", "Cliente Teste 1", "Centro", "São Paulo", "PF");
        criarCliente("TEST002", "Cliente Teste 2", "Zona Sul", "Rio de Janeiro", "PJ");

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCriarCliente() throws Exception {
        Map<String, Object> clienteDTO = new HashMap<>();
        clienteDTO.put("nomeCliente", "Novo Cliente");
        clienteDTO.put("bairro", "Centro");
        clienteDTO.put("cidade", "São Paulo");
        clienteDTO.put("tipoCliente", "Pessoa Física");

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeCliente", is("Novo Cliente")));
    }

    @Test
    @WithMockUser
    void testBuscarClientePorIdInexistente() throws Exception {
        mockMvc.perform(get("/clientes/999"))
                .andExpect(status().isNotFound());
    }
}
