package com.univesp.pi.s3t20.service;

import com.univesp.pi.s3t20.model.FormaPagamento;
import com.univesp.pi.s3t20.repository.FormaPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FormaPagamentoService {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    public List<FormaPagamento> listarTodos() {
        return formaPagamentoRepository.findAll();
    }

    public Optional<FormaPagamento> buscarPorId(Long id) {
        return formaPagamentoRepository.findById(id);
    }

    public Optional<FormaPagamento> buscarPorCodigo(String codigo) {
        return formaPagamentoRepository.findByIdPagamento(codigo);
    }

    public Optional<FormaPagamento> criar(FormaPagamento formaPagamento) {
        // Gerar código único se não fornecido
        if (formaPagamento.getIdPagamento() == null || formaPagamento.getIdPagamento().trim().isEmpty()) {
            formaPagamento.setIdPagamento(gerarCodigoFormaPagamento());
        }
        
        // Verificar se já existe uma forma de pagamento com o mesmo idPagamento
        if (buscarPorCodigo(formaPagamento.getIdPagamento()).isPresent()) {
            return Optional.empty();
        }
        
        formaPagamento.setCreatedAt(java.time.LocalDateTime.now());
        formaPagamento.setUpdatedAt(java.time.LocalDateTime.now());
        return Optional.of(formaPagamentoRepository.save(formaPagamento));
    }
    
    private String gerarCodigoFormaPagamento() {
        long count = formaPagamentoRepository.count();
        return String.format("PAG%03d", count + 1);
    }

    public Optional<FormaPagamento> atualizar(Long id, FormaPagamento formaPagamentoAtualizada) {
        Optional<FormaPagamento> formaPagamentoOpt = formaPagamentoRepository.findById(id);
        if (formaPagamentoOpt.isEmpty()) {
            return Optional.empty();
        }
        
        FormaPagamento formaPagamento = formaPagamentoOpt.get();
        // Preservar o código único existente se não fornecido
        if (formaPagamentoAtualizada.getIdPagamento() != null && !formaPagamentoAtualizada.getIdPagamento().trim().isEmpty()) {
            formaPagamento.setIdPagamento(formaPagamentoAtualizada.getIdPagamento());
        }
        formaPagamento.setFormaPagamento(formaPagamentoAtualizada.getFormaPagamento());
        formaPagamento.setIsActive(formaPagamentoAtualizada.getIsActive());
        formaPagamento.setUpdatedAt(java.time.LocalDateTime.now());
        
        return Optional.of(formaPagamentoRepository.save(formaPagamento));
    }

    public boolean deletar(Long id) {
        if (!formaPagamentoRepository.existsById(id)) {
            return false;
        }
        
        formaPagamentoRepository.deleteById(id);
        return true;
    }

    public Long contar() {
        return formaPagamentoRepository.count();
    }
}
