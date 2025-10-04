package com.univesp.pi.s3t20.repository;

import com.univesp.pi.s3t20.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    Optional<Produto> findByIdProduto(String idProduto);
    
    List<Produto> findByCategoria(String categoria);
    
    List<Produto> findByProdutoContainingIgnoreCase(String produto);
    
    @Query("SELECT DISTINCT p.categoria FROM Produto p ORDER BY p.categoria")
    List<String> findDistinctCategorias();
}
