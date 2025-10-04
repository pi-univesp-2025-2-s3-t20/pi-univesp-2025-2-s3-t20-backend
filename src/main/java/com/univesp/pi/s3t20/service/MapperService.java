package com.univesp.pi.s3t20.service;

import com.univesp.pi.s3t20.dto.*;
import com.univesp.pi.s3t20.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MapperService {

    // Cliente mappings
    public ClienteDTO toClienteDTO(Cliente cliente) {
        if (cliente == null) return null;
        
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setIdCliente(cliente.getIdCliente());
        dto.setNomeCliente(cliente.getNomeCliente());
        dto.setBairro(cliente.getBairro());
        dto.setCidade(cliente.getCidade());
        dto.setTipoCliente(cliente.getTipoCliente());
        dto.setIsActive(cliente.getIsActive());
        dto.setCreatedAt(cliente.getCreatedAt());
        dto.setUpdatedAt(cliente.getUpdatedAt());
        return dto;
    }

    public Cliente toCliente(ClienteDTO dto) {
        if (dto == null) return null;
        
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        // idCliente será gerado automaticamente pelo serviço se não fornecido
        if (dto.getIdCliente() != null && !dto.getIdCliente().trim().isEmpty()) {
            cliente.setIdCliente(dto.getIdCliente());
        }
        cliente.setNomeCliente(dto.getNomeCliente());
        cliente.setBairro(dto.getBairro());
        cliente.setCidade(dto.getCidade());
        cliente.setTipoCliente(dto.getTipoCliente());
        return cliente;
    }

    // Produto mappings
    public ProdutoDTO toProdutoDTO(Produto produto) {
        if (produto == null) return null;
        
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setIdProduto(produto.getIdProduto());
        dto.setProduto(produto.getProduto());
        dto.setCategoria(produto.getCategoria());
        dto.setPedidoMinimo(produto.getPedidoMinimo());
        dto.setCustoUnitario(produto.getCustoUnitario());
        dto.setPrecoSugerido(produto.getPrecoSugerido());
        dto.setCentoPreco(produto.getCentoPreco());
        dto.setIsActive(produto.getIsActive());
        dto.setCreatedAt(produto.getCreatedAt());
        dto.setUpdatedAt(produto.getUpdatedAt());
        return dto;
    }

    public Produto toProduto(ProdutoDTO dto) {
        if (dto == null) return null;
        
        Produto produto = new Produto();
        produto.setId(dto.getId());
        // idProduto será gerado automaticamente pelo serviço se não fornecido
        if (dto.getIdProduto() != null && !dto.getIdProduto().trim().isEmpty()) {
            produto.setIdProduto(dto.getIdProduto());
        }
        produto.setProduto(dto.getProduto());
        produto.setCategoria(dto.getCategoria());
        produto.setPedidoMinimo(dto.getPedidoMinimo());
        produto.setCustoUnitario(dto.getCustoUnitario());
        produto.setPrecoSugerido(dto.getPrecoSugerido());
        produto.setCentoPreco(dto.getCentoPreco());
        return produto;
    }

    // FormaPagamento mappings
    public FormaPagamentoDTO toFormaPagamentoDTO(FormaPagamento formaPagamento) {
        if (formaPagamento == null) return null;
        
        FormaPagamentoDTO dto = new FormaPagamentoDTO();
        dto.setId(formaPagamento.getId());
        dto.setIdPagamento(formaPagamento.getIdPagamento());
        dto.setFormaPagamento(formaPagamento.getFormaPagamento());
        dto.setIsActive(formaPagamento.getIsActive());
        dto.setCreatedAt(formaPagamento.getCreatedAt());
        dto.setUpdatedAt(formaPagamento.getUpdatedAt());
        return dto;
    }

    public FormaPagamento toFormaPagamento(FormaPagamentoDTO dto) {
        if (dto == null) return null;
        
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(dto.getId());
        // idPagamento será gerado automaticamente pelo serviço se não fornecido
        if (dto.getIdPagamento() != null && !dto.getIdPagamento().trim().isEmpty()) {
            formaPagamento.setIdPagamento(dto.getIdPagamento());
        }
        formaPagamento.setFormaPagamento(dto.getFormaPagamento());
        formaPagamento.setIsActive(dto.getIsActive());
        return formaPagamento;
    }

    // Venda mappings
    public VendaDTO toVendaDTO(Venda venda) {
        if (venda == null) return null;
        
        VendaDTO dto = new VendaDTO();
        dto.setId(venda.getId());
        dto.setIdVenda(venda.getIdVenda());
        dto.setData(venda.getData());
        dto.setProdutoId(venda.getProduto() != null ? venda.getProduto().getId() : null);
        dto.setQuantidade(venda.getQuantidade());
        dto.setPrecoUnitario(venda.getPrecoUnitario());
        dto.setReceitaTotal(venda.getReceitaTotal());
        dto.setClienteId(venda.getCliente() != null ? venda.getCliente().getId() : null);
        dto.setFormaPagamentoId(venda.getFormaPagamento() != null ? venda.getFormaPagamento().getId() : null);
        dto.setIsActive(venda.getIsActive());
        dto.setCreatedAt(venda.getCreatedAt());
        dto.setUpdatedAt(venda.getUpdatedAt());
        return dto;
    }

    public Venda toVenda(VendaDTO dto) {
        if (dto == null) return null;
        
        Venda venda = new Venda();
        venda.setId(dto.getId());
        // idVenda será gerado automaticamente pelo serviço se não fornecido
        if (dto.getIdVenda() != null && !dto.getIdVenda().trim().isEmpty()) {
            venda.setIdVenda(dto.getIdVenda());
        }
        venda.setData(dto.getData());
        venda.setQuantidade(dto.getQuantidade());
        venda.setPrecoUnitario(dto.getPrecoUnitario());
        // receitaTotal será calculada automaticamente pelo serviço
        
        // Criar referências para as entidades relacionadas
        if (dto.getProdutoId() != null) {
            Produto produto = new Produto();
            produto.setId(dto.getProdutoId());
            venda.setProduto(produto);
        }
        
        if (dto.getClienteId() != null) {
            Cliente cliente = new Cliente();
            cliente.setId(dto.getClienteId());
            venda.setCliente(cliente);
        }
        
        if (dto.getFormaPagamentoId() != null) {
            FormaPagamento formaPagamento = new FormaPagamento();
            formaPagamento.setId(dto.getFormaPagamentoId());
            venda.setFormaPagamento(formaPagamento);
        }
        
        return venda;
    }

    public VendaResponseDTO toVendaResponseDTO(Venda venda) {
        if (venda == null) return null;
        
        VendaResponseDTO dto = new VendaResponseDTO();
        dto.setId(venda.getId());
        dto.setIdVenda(venda.getIdVenda());
        dto.setData(venda.getData());
        dto.setProduto(toProdutoDTO(venda.getProduto()));
        dto.setQuantidade(venda.getQuantidade());
        dto.setPrecoUnitario(venda.getPrecoUnitario());
        dto.setReceitaTotal(venda.getReceitaTotal());
        dto.setCliente(toClienteDTO(venda.getCliente()));
        dto.setFormaPagamento(toFormaPagamentoDTO(venda.getFormaPagamento()));
        dto.setIsActive(venda.getIsActive());
        dto.setCreatedAt(venda.getCreatedAt());
        dto.setUpdatedAt(venda.getUpdatedAt());
        return dto;
    }

    // Método para calcular receita total
    public BigDecimal calcularReceitaTotal(Integer quantidade, BigDecimal precoUnitario) {
        if (quantidade == null || precoUnitario == null) {
            return BigDecimal.ZERO;
        }
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
