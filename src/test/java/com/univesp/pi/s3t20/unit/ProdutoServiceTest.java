package com.univesp.pi.s3t20.unit;

import com.univesp.pi.s3t20.model.Produto;
import com.univesp.pi.s3t20.repository.ProdutoRepository;
import com.univesp.pi.s3t20.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProdutoServiceTest {

    @Autowired
    private ProdutoService produtoService;
    
    @Autowired
    private ProdutoRepository produtoRepository;

    private Produto produtoTeste;

    @BeforeEach
    void setUp() {
        // Limpar dados antes de cada teste
        produtoRepository.deleteAll();
        
        // Criar produto de teste
        produtoTeste = new Produto();
        produtoTeste.setIdProduto("PROD001");
        produtoTeste.setProduto("Produto Teste");
        produtoTeste.setCategoria("Eletrônicos");
        produtoTeste.setPedidoMinimo(10);
        produtoTeste.setCustoUnitario(new BigDecimal("50.00"));
        produtoTeste.setPrecoSugerido(new BigDecimal("80.00"));
        produtoTeste.setCentoPreco(new BigDecimal("75.00"));
        produtoTeste = produtoRepository.save(produtoTeste);
    }

    @Test
    void testListarTodos() {
        List<Produto> produtos = produtoService.listarTodos();
        assertNotNull(produtos);
        assertFalse(produtos.isEmpty());
        assertEquals(1, produtos.size());
    }

    @Test
    void testBuscarPorId() {
        Optional<Produto> produto = produtoService.buscarPorId(produtoTeste.getId());
        assertTrue(produto.isPresent());
        assertEquals(produtoTeste.getIdProduto(), produto.get().getIdProduto());
        assertEquals(produtoTeste.getProduto(), produto.get().getProduto());
    }

    @Test
    void testBuscarPorCodigo() {
        Optional<Produto> produto = produtoService.buscarPorCodigo("PROD001");
        assertTrue(produto.isPresent());
        assertEquals("PROD001", produto.get().getIdProduto());
    }

    @Test
    void testBuscarPorCategoria() {
        // Criar outro produto na mesma categoria
        Produto produto2 = new Produto();
        produto2.setIdProduto("PROD002");
        produto2.setProduto("Produto 2");
        produto2.setCategoria("Eletrônicos");
        produto2.setPedidoMinimo(5);
        produto2.setCustoUnitario(new BigDecimal("30.00"));
        produto2.setPrecoSugerido(new BigDecimal("50.00"));
        produto2.setCentoPreco(new BigDecimal("45.00"));
        produtoRepository.save(produto2);

        List<Produto> produtos = produtoService.buscarPorCategoria("Eletrônicos");
        assertNotNull(produtos);
        assertEquals(2, produtos.size());
    }

    @Test
    void testCriar() {
        Produto novoProduto = new Produto();
        novoProduto.setIdProduto("PROD003");
        novoProduto.setProduto("Novo Produto");
        novoProduto.setCategoria("Roupas");
        novoProduto.setPedidoMinimo(20);
        novoProduto.setCustoUnitario(new BigDecimal("25.00"));
        novoProduto.setPrecoSugerido(new BigDecimal("40.00"));
        novoProduto.setCentoPreco(new BigDecimal("35.00"));

        Produto produtoCriado = produtoService.criar(novoProduto);
        assertNotNull(produtoCriado);
        assertNotNull(produtoCriado.getId());
        assertEquals("PROD003", produtoCriado.getIdProduto());
        assertNotNull(produtoCriado.getCreatedAt());
        assertNotNull(produtoCriado.getUpdatedAt());
    }

    @Test
    void testCriarComIdProdutoDuplicado() {
        Produto produtoDuplicado = new Produto();
        produtoDuplicado.setIdProduto("PROD001"); // Mesmo ID do produto existente
        produtoDuplicado.setProduto("Produto Duplicado");
        produtoDuplicado.setCategoria("Eletrônicos");
        produtoDuplicado.setPedidoMinimo(10);
        produtoDuplicado.setCustoUnitario(new BigDecimal("50.00"));
        produtoDuplicado.setPrecoSugerido(new BigDecimal("80.00"));
        produtoDuplicado.setCentoPreco(new BigDecimal("75.00"));

        Produto produtoCriado = produtoService.criar(produtoDuplicado);
        assertNull(produtoCriado);
    }

    @Test
    void testAtualizar() {
        produtoTeste.setProduto("Produto Atualizado");
        produtoTeste.setCategoria("Informática");
        produtoTeste.setPedidoMinimo(15);
        produtoTeste.setCustoUnitario(new BigDecimal("60.00"));
        produtoTeste.setPrecoSugerido(new BigDecimal("90.00"));
        produtoTeste.setCentoPreco(new BigDecimal("85.00"));

        Optional<Produto> produtoAtualizado = produtoService.atualizar(produtoTeste.getId(), produtoTeste);
        assertTrue(produtoAtualizado.isPresent());
        assertEquals("Produto Atualizado", produtoAtualizado.get().getProduto());
        assertEquals("Informática", produtoAtualizado.get().getCategoria());
        assertEquals(15, produtoAtualizado.get().getPedidoMinimo());
        assertEquals(new BigDecimal("60.00"), produtoAtualizado.get().getCustoUnitario());
        assertNotNull(produtoAtualizado.get().getUpdatedAt());
    }

    @Test
    void testAtualizarProdutoInexistente() {
        Produto produtoInexistente = new Produto();
        produtoInexistente.setIdProduto("PROD999");
        produtoInexistente.setProduto("Produto Inexistente");
        produtoInexistente.setCategoria("Teste");
        produtoInexistente.setPedidoMinimo(1);
        produtoInexistente.setCustoUnitario(new BigDecimal("10.00"));
        produtoInexistente.setPrecoSugerido(new BigDecimal("15.00"));
        produtoInexistente.setCentoPreco(new BigDecimal("12.00"));

        Optional<Produto> produtoAtualizado = produtoService.atualizar(999L, produtoInexistente);
        assertFalse(produtoAtualizado.isPresent());
    }

    @Test
    void testDeletar() {
        boolean deletado = produtoService.deletar(produtoTeste.getId());
        assertTrue(deletado);

        Optional<Produto> produtoDeletado = produtoService.buscarPorId(produtoTeste.getId());
        assertFalse(produtoDeletado.isPresent());
    }

    @Test
    void testDeletarProdutoInexistente() {
        boolean deletado = produtoService.deletar(999L);
        assertFalse(deletado);
    }

    @Test
    void testContar() {
        Long count = produtoService.contar();
        assertEquals(1L, count);
    }

    @Test
    void testListarCategorias() {
        // Criar produtos com categorias diferentes
        Produto produto2 = new Produto();
        produto2.setIdProduto("PROD002");
        produto2.setProduto("Produto 2");
        produto2.setCategoria("Roupas");
        produto2.setPedidoMinimo(5);
        produto2.setCustoUnitario(new BigDecimal("30.00"));
        produto2.setPrecoSugerido(new BigDecimal("50.00"));
        produto2.setCentoPreco(new BigDecimal("45.00"));
        produtoRepository.save(produto2);

        List<String> categorias = produtoService.listarCategorias();
        assertNotNull(categorias);
        assertEquals(2, categorias.size());
        assertTrue(categorias.contains("Eletrônicos"));
        assertTrue(categorias.contains("Roupas"));
    }
}
