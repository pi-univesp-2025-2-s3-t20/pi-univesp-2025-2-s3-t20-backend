package com.univesp.pi.s3t20.unit;

import com.univesp.pi.s3t20.model.Cliente;
import com.univesp.pi.s3t20.repository.ClienteRepository;
import com.univesp.pi.s3t20.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ClienteServiceTest {

    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente clienteTeste;

    @BeforeEach
    void setUp() {
        // Limpar dados antes de cada teste
        clienteRepository.deleteAll();
        
        // Criar cliente de teste
        clienteTeste = new Cliente();
        clienteTeste.setIdCliente("CLI001");
        clienteTeste.setNomeCliente("Cliente Teste");
        clienteTeste.setBairro("Centro");
        clienteTeste.setCidade("São Paulo");
        clienteTeste.setTipoCliente("Pessoa Física");
        clienteTeste = clienteRepository.save(clienteTeste);
    }

    @Test
    void testListarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        assertNotNull(clientes);
        assertFalse(clientes.isEmpty());
        assertEquals(1, clientes.size());
    }

    @Test
    void testBuscarPorId() {
        Optional<Cliente> cliente = clienteService.buscarPorId(clienteTeste.getId());
        assertTrue(cliente.isPresent());
        assertEquals(clienteTeste.getIdCliente(), cliente.get().getIdCliente());
        assertEquals(clienteTeste.getNomeCliente(), cliente.get().getNomeCliente());
    }

    @Test
    void testBuscarPorIdCliente() {
        Optional<Cliente> cliente = clienteService.buscarPorIdCliente("CLI001");
        assertTrue(cliente.isPresent());
        assertEquals("CLI001", cliente.get().getIdCliente());
    }

    @Test
    void testCriar() {
        Cliente novoCliente = new Cliente();
        novoCliente.setIdCliente("CLI002");
        novoCliente.setNomeCliente("Novo Cliente");
        novoCliente.setBairro("Vila Nova");
        novoCliente.setCidade("Rio de Janeiro");
        novoCliente.setTipoCliente("Pessoa Jurídica");

        Optional<Cliente> clienteCriado = clienteService.criar(novoCliente);
        assertTrue(clienteCriado.isPresent());
        assertNotNull(clienteCriado.get().getId());
        assertEquals("CLI002", clienteCriado.get().getIdCliente());
    }

    @Test
    void testCriarComIdClienteDuplicado() {
        Cliente clienteDuplicado = new Cliente();
        clienteDuplicado.setIdCliente("CLI001"); // Mesmo ID do cliente existente
        clienteDuplicado.setNomeCliente("Cliente Duplicado");
        clienteDuplicado.setBairro("Centro");
        clienteDuplicado.setCidade("São Paulo");
        clienteDuplicado.setTipoCliente("Pessoa Física");

        Optional<Cliente> clienteCriado = clienteService.criar(clienteDuplicado);
        assertFalse(clienteCriado.isPresent());
    }

    @Test
    void testAtualizar() {
        clienteTeste.setNomeCliente("Cliente Atualizado");
        clienteTeste.setCidade("Brasília");

        Optional<Cliente> clienteAtualizado = clienteService.atualizar(clienteTeste.getId(), clienteTeste);
        assertTrue(clienteAtualizado.isPresent());
        assertEquals("Cliente Atualizado", clienteAtualizado.get().getNomeCliente());
        assertEquals("Brasília", clienteAtualizado.get().getCidade());
    }

    @Test
    void testDeletar() {
        boolean deletado = clienteService.deletar(clienteTeste.getId());
        assertTrue(deletado);

        Optional<Cliente> clienteDeletado = clienteService.buscarPorId(clienteTeste.getId());
        assertFalse(clienteDeletado.isPresent());
    }

    @Test
    void testContar() {
        Long count = clienteService.contar();
        assertEquals(1L, count);
    }
}