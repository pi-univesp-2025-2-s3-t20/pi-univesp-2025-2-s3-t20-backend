package com.univesp.pi.s3t20.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vendas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Entidade que representa uma venda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único da venda (gerado automaticamente)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "id_venda", nullable = false, length = 10, unique = true)
    @Schema(description = "Código único da venda (gerado automaticamente)", example = "VEN001", accessMode = Schema.AccessMode.READ_ONLY)
    private String idVenda;

    @Column(name = "data", nullable = false)
    @Schema(description = "Data da venda", example = "2024-01-15", required = true)
    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    @Schema(description = "Produto vendido", required = true)
    private Produto produto;

    @Column(name = "quantidade", nullable = false)
    @Schema(description = "Quantidade vendida", example = "2", required = true)
    private Integer quantidade;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Preço unitário do produto", example = "25.90", required = true)
    private BigDecimal precoUnitario;

    @Column(name = "receita_total", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Receita total da venda (calculada automaticamente)", example = "51.80", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal receitaTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @Schema(description = "Cliente que fez a compra", required = true)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forma_pagamento_id", nullable = false)
    @Schema(description = "Forma de pagamento utilizada", required = true)
    private FormaPagamento formaPagamento;

    @Column(name = "is_active")
    @Schema(description = "Indica se a venda está ativa (gerado automaticamente)", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean isActive = true;

    @Column(name = "created_at")
    @Schema(description = "Data e hora de criação do registro (gerado automaticamente)", accessMode = Schema.AccessMode.READ_ONLY)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Data e hora da última atualização do registro (gerado automaticamente)", accessMode = Schema.AccessMode.READ_ONLY)
    private java.time.LocalDateTime updatedAt;

    // Constructors
    public Venda() {}

    public Venda(String idVenda, LocalDate data, Produto produto, Integer quantidade, 
                 BigDecimal precoUnitario, BigDecimal receitaTotal, Cliente cliente, 
                 FormaPagamento formaPagamento) {
        this.idVenda = idVenda;
        this.data = data;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.receitaTotal = receitaTotal;
        this.cliente = cliente;
        this.formaPagamento = formaPagamento;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdVenda() {
        return idVenda;
    }

    // idVenda é gerado automaticamente - setter removido para manter somente leitura
    // Método público para uso interno dos serviços (não deve ser usado externamente)
    public void setIdVenda(String idVenda) {
        this.idVenda = idVenda;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public BigDecimal getReceitaTotal() {
        return receitaTotal;
    }

    // receitaTotal é calculada automaticamente - setter removido para manter somente leitura
    // Método público para uso interno dos serviços (não deve ser usado externamente)
    public void setReceitaTotal(BigDecimal receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    // isActive, createdAt e updatedAt são gerenciados automaticamente - setters removidos para manter somente leitura
    // Métodos públicos para uso interno dos serviços (não devem ser usados externamente)
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public java.time.LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
