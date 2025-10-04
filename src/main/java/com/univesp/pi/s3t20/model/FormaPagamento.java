package com.univesp.pi.s3t20.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "formas_pagamento")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Entidade que representa uma forma de pagamento")
public class FormaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único da forma de pagamento (gerado automaticamente)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "id_pagamento", nullable = false, length = 10, unique = true)
    @Schema(description = "Código único da forma de pagamento (gerado automaticamente)", example = "PAG001", accessMode = Schema.AccessMode.READ_ONLY)
    private String idPagamento;

    @Column(name = "forma_pagamento", nullable = false, length = 50)
    @Schema(description = "Descrição da forma de pagamento", example = "Cartão de Crédito", required = true)
    private String formaPagamento;

    @OneToMany(mappedBy = "formaPagamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @Schema(description = "Lista de vendas com esta forma de pagamento", hidden = true)
    private List<Venda> vendas;

    @Column(name = "is_active")
    @Schema(description = "Indica se a forma de pagamento está ativa (gerado automaticamente)", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean isActive = true;

    @Column(name = "created_at")
    @Schema(description = "Data e hora de criação do registro (gerado automaticamente)", accessMode = Schema.AccessMode.READ_ONLY)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Data e hora da última atualização do registro (gerado automaticamente)", accessMode = Schema.AccessMode.READ_ONLY)
    private java.time.LocalDateTime updatedAt;

    // Constructors
    public FormaPagamento() {}

    public FormaPagamento(String idPagamento, String formaPagamento) {
        this.idPagamento = idPagamento;
        this.formaPagamento = formaPagamento;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdPagamento() {
        return idPagamento;
    }

    // idPagamento é gerado automaticamente - setter removido para manter somente leitura
    // Método público para uso interno dos serviços (não deve ser usado externamente)
    public void setIdPagamento(String idPagamento) {
        this.idPagamento = idPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
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
