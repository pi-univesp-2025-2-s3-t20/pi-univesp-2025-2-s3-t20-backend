package com.univesp.pi.s3t20.unit;

import com.univesp.pi.s3t20.model.Venda;
import com.univesp.pi.s3t20.model.Produto;
import com.univesp.pi.s3t20.model.Cliente;
import com.univesp.pi.s3t20.model.FormaPagamento;
import com.univesp.pi.s3t20.repository.VendaRepository;
import com.univesp.pi.s3t20.repository.ProdutoRepository;
import com.univesp.pi.s3t20.repository.ClienteRepository;
import com.univesp.pi.s3t20.repository.FormaPagamentoRepository;
import com.univesp.pi.s3t20.service.VendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class VendaServiceTest {

    @Autowired
    private VendaService vendaService;
    
    @Autowired
    private VendaRepository vendaRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    private Venda vendaTeste;
    private Produto produtoTeste;
    private Cliente clienteTeste;
    private FormaPagamento formaPagamentoTeste;

    @BeforeEach
    void setUp() {
        // Limpar dados antes de cada teste
        vendaRepository.deleteAll();
        produtoRepository.deleteAll();
        clienteRepository.deleteAll();
        formaPagamentoRepository.deleteAll();
        
        // Criar entidades de teste
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

        vendaTeste = new Venda();
        vendaTeste.setIdVenda("VENDA001");
        vendaTeste.setData(LocalDate.now());
        vendaTeste.setQuantidade(2);
        vendaTeste.setPrecoUnitario(new BigDecimal("80.00"));
        vendaTeste.setReceitaTotal(new BigDecimal("160.00"));
        vendaTeste.setProduto(produtoTeste);
        vendaTeste.setCliente(clienteTeste);
        vendaTeste.setFormaPagamento(formaPagamentoTeste);
        vendaTeste = vendaRepository.save(vendaTeste);
    }

    @Test
    void testListarTodos() {
        List<Venda> vendas = vendaService.listarTodos();
        assertNotNull(vendas);
        assertFalse(vendas.isEmpty());
        assertEquals(1, vendas.size());
    }

    @Test
    void testBuscarPorId() {
        Optional<Venda> venda = vendaService.buscarPorId(vendaTeste.getId());
        assertTrue(venda.isPresent());
        assertEquals(vendaTeste.getIdVenda(), venda.get().getIdVenda());
        assertEquals(vendaTeste.getData(), venda.get().getData());
    }

    @Test
    void testBuscarPorCodigo() {
        Optional<Venda> venda = vendaService.buscarPorCodigo("VENDA001");
        assertTrue(venda.isPresent());
        assertEquals("VENDA001", venda.get().getIdVenda());
    }

    @Test
    void testBuscarPorData() {
        List<Venda> vendas = vendaService.buscarPorData(LocalDate.now());
        assertNotNull(vendas);
        assertEquals(1, vendas.size());
        assertEquals(LocalDate.now(), vendas.get(0).getData());
    }

    @Test
    void testBuscarPorCliente() {
        List<Venda> vendas = vendaService.buscarPorCliente(clienteTeste.getId());
        assertNotNull(vendas);
        assertEquals(1, vendas.size());
        assertEquals(clienteTeste.getId(), vendas.get(0).getCliente().getId());
    }

    @Test
    void testBuscarPorProduto() {
        List<Venda> vendas = vendaService.buscarPorProduto(produtoTeste.getId());
        assertNotNull(vendas);
        assertEquals(1, vendas.size());
        assertEquals(produtoTeste.getId(), vendas.get(0).getProduto().getId());
    }

    @Test
    void testBuscarPorPeriodo() {
        LocalDate dataInicio = LocalDate.now().minusDays(1);
        LocalDate dataFim = LocalDate.now().plusDays(1);
        
        List<Venda> vendas = vendaService.buscarPorPeriodo(dataInicio, dataFim);
        assertNotNull(vendas);
        assertEquals(1, vendas.size());
    }

    @Test
    void testCriar() {
        Venda novaVenda = new Venda();
        novaVenda.setIdVenda("VENDA002");
        novaVenda.setData(LocalDate.now());
        novaVenda.setQuantidade(3);
        novaVenda.setPrecoUnitario(new BigDecimal("100.00"));
        novaVenda.setReceitaTotal(new BigDecimal("300.00"));
        novaVenda.setProduto(produtoTeste);
        novaVenda.setCliente(clienteTeste);
        novaVenda.setFormaPagamento(formaPagamentoTeste);

        Optional<Venda> vendaCriada = vendaService.criar(novaVenda);
        assertTrue(vendaCriada.isPresent());
        assertNotNull(vendaCriada.get().getId());
        assertEquals("VENDA002", vendaCriada.get().getIdVenda());
        assertNotNull(vendaCriada.get().getCreatedAt());
        assertNotNull(vendaCriada.get().getUpdatedAt());
    }

    @Test
    void testCriarComProdutoInexistente() {
        Venda vendaComProdutoInexistente = new Venda();
        vendaComProdutoInexistente.setIdVenda("VENDA003");
        vendaComProdutoInexistente.setData(LocalDate.now());
        vendaComProdutoInexistente.setQuantidade(1);
        vendaComProdutoInexistente.setPrecoUnitario(new BigDecimal("50.00"));
        vendaComProdutoInexistente.setReceitaTotal(new BigDecimal("50.00"));
        
        Produto produtoInexistente = new Produto();
        produtoInexistente.setId(999L);
        vendaComProdutoInexistente.setProduto(produtoInexistente);
        vendaComProdutoInexistente.setCliente(clienteTeste);
        vendaComProdutoInexistente.setFormaPagamento(formaPagamentoTeste);

        Optional<Venda> vendaCriada = vendaService.criar(vendaComProdutoInexistente);
        assertFalse(vendaCriada.isPresent());
    }

    @Test
    void testCriarComClienteInexistente() {
        Venda vendaComClienteInexistente = new Venda();
        vendaComClienteInexistente.setIdVenda("VENDA004");
        vendaComClienteInexistente.setData(LocalDate.now());
        vendaComClienteInexistente.setQuantidade(1);
        vendaComClienteInexistente.setPrecoUnitario(new BigDecimal("50.00"));
        vendaComClienteInexistente.setReceitaTotal(new BigDecimal("50.00"));
        vendaComClienteInexistente.setProduto(produtoTeste);
        
        Cliente clienteInexistente = new Cliente();
        clienteInexistente.setId(999L);
        vendaComClienteInexistente.setCliente(clienteInexistente);
        vendaComClienteInexistente.setFormaPagamento(formaPagamentoTeste);

        Optional<Venda> vendaCriada = vendaService.criar(vendaComClienteInexistente);
        assertFalse(vendaCriada.isPresent());
    }

    @Test
    void testCriarComFormaPagamentoInexistente() {
        Venda vendaComFormaPagamentoInexistente = new Venda();
        vendaComFormaPagamentoInexistente.setIdVenda("VENDA005");
        vendaComFormaPagamentoInexistente.setData(LocalDate.now());
        vendaComFormaPagamentoInexistente.setQuantidade(1);
        vendaComFormaPagamentoInexistente.setPrecoUnitario(new BigDecimal("50.00"));
        vendaComFormaPagamentoInexistente.setReceitaTotal(new BigDecimal("50.00"));
        vendaComFormaPagamentoInexistente.setProduto(produtoTeste);
        vendaComFormaPagamentoInexistente.setCliente(clienteTeste);
        
        FormaPagamento formaPagamentoInexistente = new FormaPagamento();
        formaPagamentoInexistente.setId(999L);
        vendaComFormaPagamentoInexistente.setFormaPagamento(formaPagamentoInexistente);

        Optional<Venda> vendaCriada = vendaService.criar(vendaComFormaPagamentoInexistente);
        assertFalse(vendaCriada.isPresent());
    }

    @Test
    void testAtualizar() {
        vendaTeste.setQuantidade(5);
        vendaTeste.setPrecoUnitario(new BigDecimal("90.00"));
        vendaTeste.setReceitaTotal(new BigDecimal("450.00"));

        Optional<Venda> vendaAtualizada = vendaService.atualizar(vendaTeste.getId(), vendaTeste);
        assertTrue(vendaAtualizada.isPresent());
        assertEquals(5, vendaAtualizada.get().getQuantidade());
        assertEquals(new BigDecimal("90.00"), vendaAtualizada.get().getPrecoUnitario());
        assertEquals(new BigDecimal("450.00"), vendaAtualizada.get().getReceitaTotal());
        assertNotNull(vendaAtualizada.get().getUpdatedAt());
    }

    @Test
    void testAtualizarVendaInexistente() {
        Venda vendaInexistente = new Venda();
        vendaInexistente.setIdVenda("VENDA999");
        vendaInexistente.setData(LocalDate.now());
        vendaInexistente.setQuantidade(1);
        vendaInexistente.setPrecoUnitario(new BigDecimal("50.00"));
        vendaInexistente.setReceitaTotal(new BigDecimal("50.00"));

        Optional<Venda> vendaAtualizada = vendaService.atualizar(999L, vendaInexistente);
        assertFalse(vendaAtualizada.isPresent());
    }

    @Test
    void testDeletar() {
        boolean deletado = vendaService.deletar(vendaTeste.getId());
        assertTrue(deletado);

        Optional<Venda> vendaDeletada = vendaService.buscarPorId(vendaTeste.getId());
        assertFalse(vendaDeletada.isPresent());
    }

    @Test
    void testDeletarVendaInexistente() {
        boolean deletado = vendaService.deletar(999L);
        assertFalse(deletado);
    }

    @Test
    void testContar() {
        Long count = vendaService.contar();
        assertEquals(1L, count);
    }

    @Test
    void testObterResumo() {
        VendaService.VendaResumo resumo = vendaService.obterResumo();
        assertNotNull(resumo);
        assertEquals(1L, resumo.totalVendas);
        assertEquals(160.0, resumo.receitaTotal);
    }

    @Test
    void testObterResumoPorPeriodo() {
        LocalDate dataInicio = LocalDate.now().minusDays(1);
        LocalDate dataFim = LocalDate.now().plusDays(1);
        
        VendaService.VendaResumo resumo = vendaService.obterResumoPorPeriodo(dataInicio, dataFim);
        assertNotNull(resumo);
        assertEquals(1L, resumo.totalVendas);
        assertEquals(160.0, resumo.receitaTotal);
    }

    @Test
    void testObterResumoPorPeriodoSemVendas() {
        LocalDate dataInicio = LocalDate.now().plusDays(10);
        LocalDate dataFim = LocalDate.now().plusDays(20);
        
        VendaService.VendaResumo resumo = vendaService.obterResumoPorPeriodo(dataInicio, dataFim);
        assertNotNull(resumo);
        assertEquals(0L, resumo.totalVendas);
        assertEquals(0.0, resumo.receitaTotal);
    }
}
