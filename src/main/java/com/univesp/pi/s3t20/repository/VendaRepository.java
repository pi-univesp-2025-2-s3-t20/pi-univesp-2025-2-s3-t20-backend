package com.univesp.pi.s3t20.repository;

import com.univesp.pi.s3t20.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    
    Optional<Venda> findByIdVenda(String idVenda);
    
    List<Venda> findByData(LocalDate data);
    
    List<Venda> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);
    
    List<Venda> findByClienteId(Long clienteId);
    
    List<Venda> findByProdutoId(Long produtoId);
    
    List<Venda> findByFormaPagamentoId(Long formaPagamentoId);
    
    @Query("SELECT v FROM Venda v WHERE v.cliente.cidade = ?1")
    List<Venda> findByClienteCidade(String cidade);
    
    @Query("SELECT v FROM Venda v WHERE v.produto.categoria = ?1")
    List<Venda> findByProdutoCategoria(String categoria);
}
