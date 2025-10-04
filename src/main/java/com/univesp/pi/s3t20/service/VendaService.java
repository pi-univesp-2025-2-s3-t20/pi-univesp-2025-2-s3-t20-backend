package com.univesp.pi.s3t20.service;

import com.univesp.pi.s3t20.model.Venda;
import com.univesp.pi.s3t20.model.Produto;
import com.univesp.pi.s3t20.model.Cliente;
import com.univesp.pi.s3t20.model.FormaPagamento;
import com.univesp.pi.s3t20.repository.VendaRepository;
import com.univesp.pi.s3t20.repository.ProdutoRepository;
import com.univesp.pi.s3t20.repository.ClienteRepository;
import com.univesp.pi.s3t20.repository.FormaPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    public List<Venda> listarTodos() {
        return vendaRepository.findAll();
    }

    public Optional<Venda> buscarPorId(Long id) {
        return vendaRepository.findById(id);
    }

    public Optional<Venda> buscarPorCodigo(String codigo) {
        return vendaRepository.findByIdVenda(codigo);
    }

    public List<Venda> buscarPorData(LocalDate data) {
        return vendaRepository.findByData(data);
    }

    public List<Venda> buscarPorCliente(Long clienteId) {
        return vendaRepository.findByClienteId(clienteId);
    }

    public List<Venda> buscarPorProduto(Long produtoId) {
        return vendaRepository.findByProdutoId(produtoId);
    }

    public List<Venda> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return vendaRepository.findByDataBetween(dataInicio, dataFim);
    }

    public Optional<Venda> criar(Venda venda) {
        // Gerar código único se não fornecido
        if (venda.getIdVenda() == null || venda.getIdVenda().trim().isEmpty()) {
            venda.setIdVenda(gerarCodigoVenda());
        }
        
        // Calcular receita total se não fornecida
        if (venda.getReceitaTotal() == null && venda.getQuantidade() != null && venda.getPrecoUnitario() != null) {
            venda.setReceitaTotal(venda.getPrecoUnitario().multiply(java.math.BigDecimal.valueOf(venda.getQuantidade())));
        }
        
        // Validar se as entidades relacionadas existem
        if (venda.getProduto() != null && venda.getProduto().getId() != null) {
            Optional<Produto> produtoOpt = produtoRepository.findById(venda.getProduto().getId());
            if (produtoOpt.isEmpty()) {
                return Optional.empty();
            }
            venda.setProduto(produtoOpt.get());
        }

        if (venda.getCliente() != null && venda.getCliente().getId() != null) {
            Optional<Cliente> clienteOpt = clienteRepository.findById(venda.getCliente().getId());
            if (clienteOpt.isEmpty()) {
                return Optional.empty();
            }
            venda.setCliente(clienteOpt.get());
        }

        if (venda.getFormaPagamento() != null && venda.getFormaPagamento().getId() != null) {
            Optional<FormaPagamento> formaPagamentoOpt = formaPagamentoRepository.findById(venda.getFormaPagamento().getId());
            if (formaPagamentoOpt.isEmpty()) {
                return Optional.empty();
            }
            venda.setFormaPagamento(formaPagamentoOpt.get());
        }

        venda.setCreatedAt(java.time.LocalDateTime.now());
        venda.setUpdatedAt(java.time.LocalDateTime.now());
        return Optional.of(vendaRepository.save(venda));
    }
    
    private String gerarCodigoVenda() {
        long count = vendaRepository.count();
        return String.format("VEN%03d", count + 1);
    }

    public Optional<Venda> atualizar(Long id, Venda vendaAtualizada) {
        Optional<Venda> vendaOpt = vendaRepository.findById(id);
        if (vendaOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Venda venda = vendaOpt.get();
        // Preservar o código único existente se não fornecido
        if (vendaAtualizada.getIdVenda() != null && !vendaAtualizada.getIdVenda().trim().isEmpty()) {
            venda.setIdVenda(vendaAtualizada.getIdVenda());
        }
        venda.setData(vendaAtualizada.getData());
        venda.setQuantidade(vendaAtualizada.getQuantidade());
        venda.setPrecoUnitario(vendaAtualizada.getPrecoUnitario());
        
        // Calcular receita total se não fornecida
        if (vendaAtualizada.getReceitaTotal() == null && vendaAtualizada.getQuantidade() != null && vendaAtualizada.getPrecoUnitario() != null) {
            venda.setReceitaTotal(vendaAtualizada.getPrecoUnitario().multiply(java.math.BigDecimal.valueOf(vendaAtualizada.getQuantidade())));
        } else {
            venda.setReceitaTotal(vendaAtualizada.getReceitaTotal());
        }
        
        venda.setUpdatedAt(java.time.LocalDateTime.now());
        
        return Optional.of(vendaRepository.save(venda));
    }

    public boolean deletar(Long id) {
        if (!vendaRepository.existsById(id)) {
            return false;
        }
        
        vendaRepository.deleteById(id);
        return true;
    }

    public Long contar() {
        return vendaRepository.count();
    }

    public VendaResumo obterResumo() {
        Long totalVendas = vendaRepository.count();
        List<Venda> vendas = vendaRepository.findAll();
        
        double receitaTotal = vendas.stream()
            .mapToDouble(v -> v.getReceitaTotal().doubleValue())
            .sum();
        
        return new VendaResumo(totalVendas, receitaTotal);
    }

    public VendaResumo obterResumoPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        List<Venda> vendas = buscarPorPeriodo(dataInicio, dataFim);
        
        Long totalVendas = (long) vendas.size();
        double receitaTotal = vendas.stream()
            .mapToDouble(v -> v.getReceitaTotal().doubleValue())
            .sum();
        
        return new VendaResumo(totalVendas, receitaTotal);
    }

    public static class VendaResumo {
        public Long totalVendas;
        public Double receitaTotal;

        public VendaResumo(Long totalVendas, Double receitaTotal) {
            this.totalVendas = totalVendas;
            this.receitaTotal = receitaTotal;
        }
    }
}
