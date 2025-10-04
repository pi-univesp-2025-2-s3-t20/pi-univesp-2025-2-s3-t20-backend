package com.univesp.pi.s3t20.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "clientes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Entidade que representa um cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do cliente (gerado automaticamente)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "id_cliente", nullable = false, length = 10, unique = true)
    @Schema(description = "Código único do cliente (gerado automaticamente)", example = "CLI001", accessMode = Schema.AccessMode.READ_ONLY)
    private String idCliente;

    @Column(name = "nome_cliente", nullable = false, length = 255)
    @Schema(description = "Nome do cliente", example = "João Silva", required = true)
    private String nomeCliente;

    @Column(name = "bairro", length = 100)
    @Schema(description = "Bairro do cliente", example = "Centro")
    private String bairro;

    @Column(name = "cidade", length = 100)
    @Schema(description = "Cidade do cliente", example = "São Paulo")
    private String cidade;

    @Column(name = "tipo_cliente", nullable = false, length = 20)
    @Schema(description = "Tipo do cliente", example = "Pessoa Física", required = true)
    private String tipoCliente;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @Schema(description = "Lista de vendas do cliente", hidden = true)
    private List<Venda> vendas;

    @Column(name = "is_active")
    @Schema(description = "Indica se o cliente está ativo (gerado automaticamente)", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean isActive = true;

    @Column(name = "created_at")
    @Schema(description = "Data e hora de criação do registro (gerado automaticamente)", accessMode = Schema.AccessMode.READ_ONLY)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Data e hora da última atualização do registro (gerado automaticamente)", accessMode = Schema.AccessMode.READ_ONLY)
    private java.time.LocalDateTime updatedAt;

    // Constructors
    public Cliente() {}

    public Cliente(String idCliente, String nomeCliente, String bairro, String cidade, String tipoCliente) {
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.bairro = bairro;
        this.cidade = cidade;
        this.tipoCliente = tipoCliente;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    // idCliente é gerado automaticamente - setter removido para manter somente leitura
    // Método público para uso interno dos serviços (não deve ser usado externamente)
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
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
