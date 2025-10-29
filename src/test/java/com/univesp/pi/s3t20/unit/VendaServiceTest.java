package com.univesp.pi.s3t20.unit;

import com.univesp.pi.s3t20.model.Cliente;
import com.univesp.pi.s3t20.model.FormaPagamento;
import com.univesp.pi.s3t20.model.Produto;
import com.univesp.pi.s3t20.model.Venda;
import com.univesp.pi.s3t20.repository.ClienteRepository;
import com.univesp.pi.s3t20.repository.FormaPagamentoRepository;
import com.univesp.pi.s3t20.repository.ProdutoRepository;
import com.univesp.pi.s3t20.repository.VendaRepository;
import com.univesp.pi.s3t20.service.VendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private FormaPagamentoRepository formaPagamentoRepository;

    @InjectMocks
    private VendaService vendaService;

    private Venda vendaTeste;
    private Produto produtoTeste;
    private Cliente clienteTeste;
    private FormaPagamento formaPagamentoTeste;

    @BeforeEach
    void setUp() {
        produtoTeste = new Produto();
        produtoTeste.setId(1L);
        produtoTeste.setIdProduto("PROD001");

        clienteTeste = new Cliente();
        clienteTeste.setId(1L);
        clienteTeste.setIdCliente("CLI001");

        formaPagamentoTeste = new FormaPagamento();
        formaPagamentoTeste.setId(1L);
        formaPagamentoTeste.setIdPagamento("PAG001");

        vendaTeste = new Venda();
        vendaTeste.setId(1L);
        vendaTeste.setIdVenda("VENDA001");
        vendaTeste.setData(LocalDate.now());
        vendaTeste.setProduto(produtoTeste);
        vendaTeste.setCliente(clienteTeste);
        vendaTeste.setFormaPagamento(formaPagamentoTeste);
        vendaTeste.setReceitaTotal(new BigDecimal("160.00"));
    }

    @Test
    void testListarTodos() {
        when(vendaRepository.findAll()).thenReturn(Collections.singletonList(vendaTeste));
        List<Venda> vendas = vendaService.listarTodos();
        assertEquals(1, vendas.size());
        verify(vendaRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        when(vendaRepository.findById(1L)).thenReturn(Optional.of(vendaTeste));
        Optional<Venda> venda = vendaService.buscarPorId(1L);
        assertTrue(venda.isPresent());
        assertEquals("VENDA001", venda.get().getIdVenda());
        verify(vendaRepository, times(1)).findById(1L);
    }

    @Test
    void testCriar() {
        when(produtoRepository.findById(any())).thenReturn(Optional.of(produtoTeste));
        when(clienteRepository.findById(any())).thenReturn(Optional.of(clienteTeste));
        when(formaPagamentoRepository.findById(any())).thenReturn(Optional.of(formaPagamentoTeste));
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaTeste);

        Venda novaVenda = new Venda();
        novaVenda.setProduto(produtoTeste);
        novaVenda.setCliente(clienteTeste);
        novaVenda.setFormaPagamento(formaPagamentoTeste);

        Optional<Venda> vendaCriada = vendaService.criar(novaVenda);

        assertTrue(vendaCriada.isPresent());
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }

    @Test
    void testCriarComProdutoInexistente() {
        when(produtoRepository.findById(any())).thenReturn(Optional.empty());

        Venda novaVenda = new Venda();
        novaVenda.setProduto(produtoTeste);

        Optional<Venda> vendaCriada = vendaService.criar(novaVenda);

        assertFalse(vendaCriada.isPresent());
        verify(vendaRepository, never()).save(any(Venda.class));
    }

    @Test
    void testAtualizar() {
        when(vendaRepository.findById(1L)).thenReturn(Optional.of(vendaTeste));
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaTeste);

        Venda dadosAtualizacao = new Venda();
        dadosAtualizacao.setQuantidade(10);

        Optional<Venda> vendaAtualizada = vendaService.atualizar(1L, dadosAtualizacao);

        assertTrue(vendaAtualizada.isPresent());
        assertEquals(10, vendaAtualizada.get().getQuantidade());
        verify(vendaRepository, times(1)).save(vendaTeste);
    }

    @Test
    void testDeletar() {
        when(vendaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(vendaRepository).deleteById(1L);

        boolean deletado = vendaService.deletar(1L);

        assertTrue(deletado);
        verify(vendaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testObterResumo() {
        // Mock a chamada para obter a lista de vendas (usado para calcular a receita)
        when(vendaRepository.findAll()).thenReturn(Collections.singletonList(vendaTeste));
        // Mock a chamada para obter a contagem total de vendas
        when(vendaRepository.count()).thenReturn(1L);

        VendaService.VendaResumo resumo = vendaService.obterResumo();

        assertEquals(1L, resumo.totalVendas);
        assertEquals(160.0, resumo.receitaTotal);

        // Verifica se os métodos corretos foram chamados
        verify(vendaRepository, times(1)).findAll();
        verify(vendaRepository, times(1)).count();
    }
}
