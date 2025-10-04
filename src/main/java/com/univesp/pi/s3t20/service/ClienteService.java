package com.univesp.pi.s3t20.service;

import com.univesp.pi.s3t20.model.Cliente;
import com.univesp.pi.s3t20.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorCodigo(String codigo) {
        return clienteRepository.findByIdCliente(codigo);
    }

    public Optional<Cliente> buscarPorIdCliente(String idCliente) {
        return clienteRepository.findByIdCliente(idCliente);
    }

    public List<Cliente> buscarPorCidade(String cidade) {
        return clienteRepository.findByCidade(cidade);
    }

    public List<Cliente> buscarPorBairro(String bairro) {
        return clienteRepository.findByBairro(bairro);
    }

    public List<Cliente> buscarPorTipo(String tipo) {
        return clienteRepository.findByTipoCliente(tipo);
    }

    public List<Cliente> buscarPorTipoCliente(String tipoCliente) {
        return clienteRepository.findByTipoCliente(tipoCliente);
    }

    public Optional<Cliente> criar(Cliente cliente) {
        // Gerar código único se não fornecido
        if (cliente.getIdCliente() == null || cliente.getIdCliente().trim().isEmpty()) {
            cliente.setIdCliente(gerarCodigoCliente());
        }
        
        // Verificar se já existe um cliente com o mesmo idCliente
        if (buscarPorIdCliente(cliente.getIdCliente()).isPresent()) {
            return Optional.empty();
        }
        
        cliente.setCreatedAt(java.time.LocalDateTime.now());
        cliente.setUpdatedAt(java.time.LocalDateTime.now());
        return Optional.of(clienteRepository.save(cliente));
    }
    
    private String gerarCodigoCliente() {
        long count = clienteRepository.count();
        return String.format("CLI%03d", count + 1);
    }

    public Optional<Cliente> atualizar(Long id, Cliente clienteAtualizado) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Cliente cliente = clienteOpt.get();
        // Preservar o código único existente se não fornecido
        if (clienteAtualizado.getIdCliente() != null && !clienteAtualizado.getIdCliente().trim().isEmpty()) {
            cliente.setIdCliente(clienteAtualizado.getIdCliente());
        }
        cliente.setNomeCliente(clienteAtualizado.getNomeCliente());
        cliente.setBairro(clienteAtualizado.getBairro());
        cliente.setCidade(clienteAtualizado.getCidade());
        cliente.setTipoCliente(clienteAtualizado.getTipoCliente());
        cliente.setUpdatedAt(java.time.LocalDateTime.now());
        
        return Optional.of(clienteRepository.save(cliente));
    }

    public boolean deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            return false;
        }
        
        clienteRepository.deleteById(id);
        return true;
    }

    public Long contar() {
        return clienteRepository.count();
    }

    public List<String> listarCidades() {
        return clienteRepository.findDistinctCidades();
    }

    public List<String> listarBairros() {
        return clienteRepository.findDistinctBairros();
    }

    public Long contarPorTipo(String tipo) {
        return clienteRepository.countByTipoCliente(tipo);
    }

    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeClienteContainingIgnoreCase(nome);
    }
}
