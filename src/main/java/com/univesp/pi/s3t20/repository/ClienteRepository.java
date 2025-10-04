package com.univesp.pi.s3t20.repository;

import com.univesp.pi.s3t20.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByIdCliente(String idCliente);
    
    List<Cliente> findByCidade(String cidade);
    
    List<Cliente> findByBairro(String bairro);
    
    List<Cliente> findByTipoCliente(String tipoCliente);
    
    List<Cliente> findByNomeClienteContainingIgnoreCase(String nome);
    
    @Query("SELECT DISTINCT c.cidade FROM Cliente c ORDER BY c.cidade")
    List<String> findDistinctCidades();
    
    @Query("SELECT DISTINCT c.bairro FROM Cliente c ORDER BY c.bairro")
    List<String> findDistinctBairros();
    
    long countByTipoCliente(String tipoCliente);
}
