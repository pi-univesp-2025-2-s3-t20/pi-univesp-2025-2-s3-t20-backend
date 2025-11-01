package com.univesp.pi.s3t20.unit;

import com.univesp.pi.s3t20.model.FormaPagamento;
import com.univesp.pi.s3t20.repository.FormaPagamentoRepository;
import com.univesp.pi.s3t20.service.FormaPagamentoService;
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
public class FormaPagamentoServiceTest {

    @Mock
    private FormaPagamentoRepository formaPagamentoRepository;

    @InjectMocks
    private FormaPagamentoService formaPagamentoService;

    private FormaPagamento formaPagamentoTeste;

    @BeforeEach
    void setUp() {
        formaPagamentoTeste = new FormaPagamento();
        formaPagamentoTeste.setId(1L);
        formaPagamentoTeste.setIdPagamento("PAG001");
        formaPagamentoTeste.setFormaPagamento("Cartão de Crédito");
        formaPagamentoTeste.setIsActive(true);
    }

    @Test
    void testListarTodos() {
        when(formaPagamentoRepository.findAll()).thenReturn(Collections.singletonList(formaPagamentoTeste));
        List<FormaPagamento> formasPagamento = formaPagamentoService.listarTodos();
        assertEquals(1, formasPagamento.size());
        verify(formaPagamentoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        when(formaPagamentoRepository.findById(1L)).thenReturn(Optional.of(formaPagamentoTeste));
        Optional<FormaPagamento> formaPagamento = formaPagamentoService.buscarPorId(1L);
        assertTrue(formaPagamento.isPresent());
        assertEquals("PAG001", formaPagamento.get().getIdPagamento());
        verify(formaPagamentoRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorCodigo() {
        when(formaPagamentoRepository.findByIdPagamento("PAG001")).thenReturn(Optional.of(formaPagamentoTeste));
        Optional<FormaPagamento> formaPagamento = formaPagamentoService.buscarPorCodigo("PAG001");
        assertTrue(formaPagamento.isPresent());
        assertEquals("PAG001", formaPagamento.get().getIdPagamento());
        verify(formaPagamentoRepository, times(1)).findByIdPagamento("PAG001");
    }

    @Test
    void testCriar() {
        when(formaPagamentoRepository.findByIdPagamento("PAG002")).thenReturn(Optional.empty());
        when(formaPagamentoRepository.save(any(FormaPagamento.class))).thenReturn(formaPagamentoTeste);

        FormaPagamento novaFormaPagamento = new FormaPagamento();
        novaFormaPagamento.setIdPagamento("PAG002");

        Optional<FormaPagamento> formaPagamentoCriada = formaPagamentoService.criar(novaFormaPagamento);

        assertTrue(formaPagamentoCriada.isPresent());
        verify(formaPagamentoRepository, times(1)).save(any(FormaPagamento.class));
    }

    @Test
    void testCriarComIdPagamentoDuplicado() {
        when(formaPagamentoRepository.findByIdPagamento("PAG001")).thenReturn(Optional.of(formaPagamentoTeste));

        FormaPagamento formaPagamentoDuplicada = new FormaPagamento();
        formaPagamentoDuplicada.setIdPagamento("PAG001");

        Optional<FormaPagamento> formaPagamentoCriada = formaPagamentoService.criar(formaPagamentoDuplicada);

        assertFalse(formaPagamentoCriada.isPresent());
        verify(formaPagamentoRepository, never()).save(any(FormaPagamento.class));
    }

    @Test
    void testAtualizar() {
        when(formaPagamentoRepository.findById(1L)).thenReturn(Optional.of(formaPagamentoTeste));
        when(formaPagamentoRepository.save(any(FormaPagamento.class))).thenReturn(formaPagamentoTeste);

        FormaPagamento dadosAtualizacao = new FormaPagamento();
        dadosAtualizacao.setFormaPagamento("Cartão de Débito");

        Optional<FormaPagamento> formaPagamentoAtualizada = formaPagamentoService.atualizar(1L, dadosAtualizacao);

        assertTrue(formaPagamentoAtualizada.isPresent());
        assertEquals("Cartão de Débito", formaPagamentoAtualizada.get().getFormaPagamento());
        verify(formaPagamentoRepository, times(1)).save(formaPagamentoTeste);
    }

    @Test
    void testDeletar() {
        when(formaPagamentoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(formaPagamentoRepository).deleteById(1L);

        boolean deletado = formaPagamentoService.deletar(1L);

        assertTrue(deletado);
        verify(formaPagamentoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testContar() {
        when(formaPagamentoRepository.count()).thenReturn(3L);

        Long count = formaPagamentoService.contar();

        assertEquals(3L, count);
        verify(formaPagamentoRepository, times(1)).count();
    }
}
