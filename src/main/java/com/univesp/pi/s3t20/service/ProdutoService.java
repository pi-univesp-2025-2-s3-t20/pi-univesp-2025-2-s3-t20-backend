package com.univesp.pi.s3t20.service;

import com.univesp.pi.s3t20.model.Produto;
import com.univesp.pi.s3t20.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public Optional<Produto> buscarPorCodigo(String codigo) {
        return produtoRepository.findByIdProduto(codigo);
    }

    public List<Produto> buscarPorCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria);
    }

    public Produto criar(Produto produto) {
        // Gerar código único se não fornecido
        if (produto.getIdProduto() == null || produto.getIdProduto().trim().isEmpty()) {
            produto.setIdProduto(gerarCodigoProduto());
        }
        
        // Verificar se já existe um produto com o mesmo idProduto
        if (buscarPorCodigo(produto.getIdProduto()).isPresent()) {
            return null;
        }
        
        produto.setCreatedAt(java.time.LocalDateTime.now());
        produto.setUpdatedAt(java.time.LocalDateTime.now());
        return produtoRepository.save(produto);
    }
    
    private String gerarCodigoProduto() {
        long count = produtoRepository.count();
        return String.format("PROD%03d", count + 1);
    }

    public Optional<Produto> atualizar(Long id, Produto produtoAtualizado) {
        Optional<Produto> produtoOpt = produtoRepository.findById(id);
        if (produtoOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Produto produto = produtoOpt.get();
        // Preservar o código único existente se não fornecido
        if (produtoAtualizado.getIdProduto() != null && !produtoAtualizado.getIdProduto().trim().isEmpty()) {
            produto.setIdProduto(produtoAtualizado.getIdProduto());
        }
        produto.setProduto(produtoAtualizado.getProduto());
        produto.setCategoria(produtoAtualizado.getCategoria());
        produto.setPedidoMinimo(produtoAtualizado.getPedidoMinimo());
        produto.setCustoUnitario(produtoAtualizado.getCustoUnitario());
        produto.setPrecoSugerido(produtoAtualizado.getPrecoSugerido());
        produto.setCentoPreco(produtoAtualizado.getCentoPreco());
        produto.setUpdatedAt(java.time.LocalDateTime.now());
        
        return Optional.of(produtoRepository.save(produto));
    }

    public boolean deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            return false;
        }
        
        produtoRepository.deleteById(id);
        return true;
    }

    public Long contar() {
        return produtoRepository.count();
    }

    public List<String> listarCategorias() {
        return produtoRepository.findDistinctCategorias();
    }
}
