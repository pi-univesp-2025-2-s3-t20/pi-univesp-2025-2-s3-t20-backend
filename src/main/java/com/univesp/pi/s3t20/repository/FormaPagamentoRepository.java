package com.univesp.pi.s3t20.repository;

import com.univesp.pi.s3t20.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {
    
    Optional<FormaPagamento> findByIdPagamento(String idPagamento);
}
