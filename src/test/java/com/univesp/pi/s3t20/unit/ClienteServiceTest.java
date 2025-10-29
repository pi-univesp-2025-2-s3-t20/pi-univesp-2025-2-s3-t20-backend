package com.univesp.pi.s3t20.unit;

import com.univesp.pi.s3t20.model.Cliente;
import com.univesp.pi.s3t20.repository.ClienteRepository;
import com.univesp.pi.s3t20.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clienteTeste;

    @BeforeEach
    void setUp() {
        clienteTeste = new Cliente();
        clienteTeste.setId(1L);
        clienteTeste.setIdCliente("CLI001");
        clienteTeste.setNomeCliente("Cliente Teste");
        clienteTeste.setBairro("Centro");
        clienteTeste.setCidade("São Paulo");
        clienteTeste.setTipoCliente("Pessoa Física");
    }

    @Test
    void testListarTodos() {
        when(clienteRepository.findAll()).thenReturn(Collections.singletonList(clienteTeste));

        List<Cliente> clientes = clienteService.listarTodos();

        assertNotNull(clientes);
        assertFalse(clientes.isEmpty());
        assertEquals(1, clientes.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteTeste));

        Optional<Cliente> cliente = clienteService.buscarPorId(1L);

        assertTrue(cliente.isPresent());
        assertEquals("CLI001", cliente.get().getIdCliente());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorCodigo() {
        when(clienteRepository.findByIdCliente("CLI001")).thenReturn(Optional.of(clienteTeste));

        Optional<Cliente> cliente = clienteService.buscarPorCodigo("CLI001");

        assertTrue(cliente.isPresent());
        assertEquals("CLI001", cliente.get().getIdCliente());
        verify(clienteRepository, times(1)).findByIdCliente("CLI001");
    }

    @Test
    void testCriar() {
        when(clienteRepository.findByIdCliente("CLI002")).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteTeste);

        Cliente novoCliente = new Cliente();
        novoCliente.setIdCliente("CLI002");

        Optional<Cliente> clienteCriado = clienteService.criar(novoCliente);

        assertTrue(clienteCriado.isPresent());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testCriarComIdClienteDuplicado() {
        when(clienteRepository.findByIdCliente("CLI001")).thenReturn(Optional.of(clienteTeste));

        Cliente clienteDuplicado = new Cliente();
        clienteDuplicado.setIdCliente("CLI001");

        Optional<Cliente> clienteCriado = clienteService.criar(clienteDuplicado);

        assertFalse(clienteCriado.isPresent());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testAtualizar() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteTeste));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteTeste);

        Cliente clienteAtualizadoDados = new Cliente();
        clienteAtualizadoDados.setNomeCliente("Cliente Atualizado");

        Optional<Cliente> clienteAtualizado = clienteService.atualizar(1L, clienteAtualizadoDados);

        assertTrue(clienteAtualizado.isPresent());
        assertEquals("Cliente Atualizado", clienteAtualizado.get().getNomeCliente());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testDeletar() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        boolean deletado = clienteService.deletar(1L);

        assertTrue(deletado);
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testContar() {
        when(clienteRepository.count()).thenReturn(5L);

        Long count = clienteService.contar();

        assertEquals(5L, count);
        verify(clienteRepository, times(1)).count();
    }
}
