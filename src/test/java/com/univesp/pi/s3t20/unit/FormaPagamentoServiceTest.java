package com.univesp.pi.s3t20.unit;

import com.univesp.pi.s3t20.model.FormaPagamento;
import com.univesp.pi.s3t20.repository.FormaPagamentoRepository;
import com.univesp.pi.s3t20.service.FormaPagamentoService;
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
public class FormaPagamentoServiceTest {

    @Autowired
    private FormaPagamentoService formaPagamentoService;
    
    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    private FormaPagamento formaPagamentoTeste;

    @BeforeEach
    void setUp() {
        // Limpar dados antes de cada teste
        formaPagamentoRepository.deleteAll();
        
        // Criar forma de pagamento de teste
        formaPagamentoTeste = new FormaPagamento();
        formaPagamentoTeste.setIdPagamento("PAG001");
        formaPagamentoTeste.setFormaPagamento("Cartão de Crédito");
        formaPagamentoTeste.setIsActive(true);
        formaPagamentoTeste = formaPagamentoRepository.save(formaPagamentoTeste);
    }

    @Test
    void testListarTodos() {
        List<FormaPagamento> formasPagamento = formaPagamentoService.listarTodos();
        assertNotNull(formasPagamento);
        assertFalse(formasPagamento.isEmpty());
        assertEquals(1, formasPagamento.size());
    }

    @Test
    void testBuscarPorId() {
        Optional<FormaPagamento> formaPagamento = formaPagamentoService.buscarPorId(formaPagamentoTeste.getId());
        assertTrue(formaPagamento.isPresent());
        assertEquals(formaPagamentoTeste.getIdPagamento(), formaPagamento.get().getIdPagamento());
        assertEquals(formaPagamentoTeste.getFormaPagamento(), formaPagamento.get().getFormaPagamento());
    }

    @Test
    void testBuscarPorCodigo() {
        Optional<FormaPagamento> formaPagamento = formaPagamentoService.buscarPorCodigo("PAG001");
        assertTrue(formaPagamento.isPresent());
        assertEquals("PAG001", formaPagamento.get().getIdPagamento());
    }

    @Test
    void testCriar() {
        FormaPagamento novaFormaPagamento = new FormaPagamento();
        novaFormaPagamento.setIdPagamento("PAG002");
        novaFormaPagamento.setFormaPagamento("PIX");
        novaFormaPagamento.setIsActive(true);

        Optional<FormaPagamento> formaPagamentoCriada = formaPagamentoService.criar(novaFormaPagamento);
        assertTrue(formaPagamentoCriada.isPresent());
        assertNotNull(formaPagamentoCriada.get().getId());
        assertEquals("PAG002", formaPagamentoCriada.get().getIdPagamento());
        assertNotNull(formaPagamentoCriada.get().getCreatedAt());
        assertNotNull(formaPagamentoCriada.get().getUpdatedAt());
    }

    @Test
    void testCriarComIdPagamentoDuplicado() {
        FormaPagamento formaPagamentoDuplicada = new FormaPagamento();
        formaPagamentoDuplicada.setIdPagamento("PAG001"); // Mesmo ID da forma de pagamento existente
        formaPagamentoDuplicada.setFormaPagamento("Boleto");
        formaPagamentoDuplicada.setIsActive(true);

        Optional<FormaPagamento> formaPagamentoCriada = formaPagamentoService.criar(formaPagamentoDuplicada);
        assertFalse(formaPagamentoCriada.isPresent());
    }

    @Test
    void testAtualizar() {
        formaPagamentoTeste.setFormaPagamento("Cartão de Débito");
        formaPagamentoTeste.setIsActive(false);

        Optional<FormaPagamento> formaPagamentoAtualizada = formaPagamentoService.atualizar(formaPagamentoTeste.getId(), formaPagamentoTeste);
        assertTrue(formaPagamentoAtualizada.isPresent());
        assertEquals("Cartão de Débito", formaPagamentoAtualizada.get().getFormaPagamento());
        assertFalse(formaPagamentoAtualizada.get().getIsActive());
        assertNotNull(formaPagamentoAtualizada.get().getUpdatedAt());
    }

    @Test
    void testAtualizarFormaPagamentoInexistente() {
        FormaPagamento formaPagamentoInexistente = new FormaPagamento();
        formaPagamentoInexistente.setIdPagamento("PAG999");
        formaPagamentoInexistente.setFormaPagamento("Dinheiro");
        formaPagamentoInexistente.setIsActive(true);

        Optional<FormaPagamento> formaPagamentoAtualizada = formaPagamentoService.atualizar(999L, formaPagamentoInexistente);
        assertFalse(formaPagamentoAtualizada.isPresent());
    }

    @Test
    void testDeletar() {
        boolean deletado = formaPagamentoService.deletar(formaPagamentoTeste.getId());
        assertTrue(deletado);

        Optional<FormaPagamento> formaPagamentoDeletada = formaPagamentoService.buscarPorId(formaPagamentoTeste.getId());
        assertFalse(formaPagamentoDeletada.isPresent());
    }

    @Test
    void testDeletarFormaPagamentoInexistente() {
        boolean deletado = formaPagamentoService.deletar(999L);
        assertFalse(deletado);
    }

    @Test
    void testContar() {
        Long count = formaPagamentoService.contar();
        assertEquals(1L, count);
    }
}
