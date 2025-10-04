package com.univesp.pi.s3t20.resource;

import com.univesp.pi.s3t20.dto.VendaDTO;
import com.univesp.pi.s3t20.dto.VendaResponseDTO;
import com.univesp.pi.s3t20.model.Venda;
import com.univesp.pi.s3t20.service.MapperService;
import com.univesp.pi.s3t20.service.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vendas")
@Tag(name = "Vendas", description = "API para gerenciamento de vendas")
public class VendaResource {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private MapperService mapperService;

    @GetMapping
    @Operation(summary = "Listar todas as vendas", description = "Retorna uma lista com todas as vendas cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista de vendas retornada com sucesso",
            content = @Content(schema = @Schema(implementation = VendaResponseDTO.class)))
    public List<VendaResponseDTO> listarVendas() {
        return vendaService.listarTodos().stream()
                .map(mapperService::toVendaResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar venda por ID", description = "Retorna uma venda específica pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = VendaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada")
    })
    public ResponseEntity<VendaResponseDTO> buscarVenda(
            @Parameter(description = "ID da venda", required = true, example = "1")
            @PathVariable Long id) {
        Optional<Venda> venda = vendaService.buscarPorId(id);
        if (venda.isPresent()) {
            return ResponseEntity.ok(mapperService.toVendaResponseDTO(venda.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/data/{data}")
    @Operation(summary = "Buscar vendas por data", description = "Retorna uma lista de vendas de uma data específica")
    @ApiResponse(responseCode = "200", description = "Lista de vendas da data retornada com sucesso",
            content = @Content(schema = @Schema(implementation = VendaResponseDTO.class)))
    public List<VendaResponseDTO> buscarPorData(
            @Parameter(description = "Data das vendas", required = true, example = "2024-01-15")
            @PathVariable LocalDate data) {
        return vendaService.buscarPorData(data).stream()
                .map(mapperService::toVendaResponseDTO)
                .toList();
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Buscar vendas por cliente", description = "Retorna uma lista de vendas de um cliente específico")
    @ApiResponse(responseCode = "200", description = "Lista de vendas do cliente retornada com sucesso",
            content = @Content(schema = @Schema(implementation = VendaResponseDTO.class)))
    public List<VendaResponseDTO> buscarPorCliente(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @PathVariable Long clienteId) {
        return vendaService.buscarPorCliente(clienteId).stream()
                .map(mapperService::toVendaResponseDTO)
                .toList();
    }

    @GetMapping("/produto/{produtoId}")
    @Operation(summary = "Buscar vendas por produto", description = "Retorna uma lista de vendas de um produto específico")
    @ApiResponse(responseCode = "200", description = "Lista de vendas do produto retornada com sucesso",
            content = @Content(schema = @Schema(implementation = VendaResponseDTO.class)))
    public List<VendaResponseDTO> buscarPorProduto(
            @Parameter(description = "ID do produto", required = true, example = "1")
            @PathVariable Long produtoId) {
        return vendaService.buscarPorProduto(produtoId).stream()
                .map(mapperService::toVendaResponseDTO)
                .toList();
    }

    @GetMapping("/periodo")
    @Operation(summary = "Buscar vendas por período", description = "Retorna uma lista de vendas em um período específico")
    @ApiResponse(responseCode = "200", description = "Lista de vendas do período retornada com sucesso",
            content = @Content(schema = @Schema(implementation = VendaResponseDTO.class)))
    public List<VendaResponseDTO> buscarPorPeriodo(
            @Parameter(description = "Data de início do período", required = true, example = "2024-01-01")
            @RequestParam LocalDate dataInicio,
            @Parameter(description = "Data de fim do período", required = true, example = "2024-01-31")
            @RequestParam LocalDate dataFim) {
        return vendaService.buscarPorPeriodo(dataInicio, dataFim).stream()
                .map(mapperService::toVendaResponseDTO)
                .toList();
    }

    @PostMapping
    @Operation(summary = "Criar nova venda", description = "Cria uma nova venda no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venda criada com sucesso",
                    content = @Content(schema = @Schema(implementation = VendaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<VendaResponseDTO> criarVenda(
            @Parameter(description = "Dados da venda a ser criada", required = true)
            @RequestBody VendaDTO vendaDTO) {
        try {
            Venda venda = mapperService.toVenda(vendaDTO);
            Optional<Venda> vendaCriada = vendaService.criar(venda);
            if (vendaCriada.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(mapperService.toVendaResponseDTO(vendaCriada.get()));
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar venda", description = "Atualiza os dados de uma venda existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = VendaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<VendaResponseDTO> atualizarVenda(
            @Parameter(description = "ID da venda a ser atualizada", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Novos dados da venda", required = true)
            @RequestBody VendaDTO vendaDTO) {
        try {
            Venda venda = mapperService.toVenda(vendaDTO);
            Optional<Venda> vendaAtualizada = vendaService.atualizar(id, venda);
            if (vendaAtualizada.isPresent()) {
                return ResponseEntity.ok(mapperService.toVendaResponseDTO(vendaAtualizada.get()));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar venda", description = "Remove uma venda do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venda deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada")
    })
    public ResponseEntity<Void> deletarVenda(
            @Parameter(description = "ID da venda a ser deletada", required = true, example = "1")
            @PathVariable Long id) {
        boolean deletado = vendaService.deletar(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/resumo")
    @Operation(summary = "Obter resumo de vendas", description = "Retorna um resumo geral das vendas")
    @ApiResponse(responseCode = "200", description = "Resumo de vendas retornado com sucesso")
    public VendaService.VendaResumo obterResumo() {
        return vendaService.obterResumo();
    }

    @GetMapping("/resumo/periodo")
    @Operation(summary = "Obter resumo de vendas por período", description = "Retorna um resumo das vendas em um período específico")
    @ApiResponse(responseCode = "200", description = "Resumo de vendas do período retornado com sucesso")
    public VendaService.VendaResumo obterResumoPorPeriodo(
            @Parameter(description = "Data de início do período", required = true, example = "2024-01-01")
            @RequestParam LocalDate dataInicio,
            @Parameter(description = "Data de fim do período", required = true, example = "2024-01-31")
            @RequestParam LocalDate dataFim) {
        return vendaService.obterResumoPorPeriodo(dataInicio, dataFim);
    }

    @GetMapping("/count")
    @Operation(summary = "Contar vendas", description = "Retorna o número total de vendas cadastradas")
    @ApiResponse(responseCode = "200", description = "Contagem de vendas retornada com sucesso")
    public Long contar() {
        return vendaService.contar();
    }
}
