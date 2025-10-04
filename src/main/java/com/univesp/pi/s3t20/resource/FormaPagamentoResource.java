package com.univesp.pi.s3t20.resource;

import com.univesp.pi.s3t20.dto.FormaPagamentoDTO;
import com.univesp.pi.s3t20.model.FormaPagamento;
import com.univesp.pi.s3t20.service.FormaPagamentoService;
import com.univesp.pi.s3t20.service.MapperService;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/formas-pagamento")
@Tag(name = "Formas de Pagamento", description = "API para gerenciamento de formas de pagamento")
public class FormaPagamentoResource {

    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @Autowired
    private MapperService mapperService;

    @GetMapping
    @Operation(summary = "Listar todas as formas de pagamento", description = "Retorna uma lista com todas as formas de pagamento cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista de formas de pagamento retornada com sucesso",
            content = @Content(schema = @Schema(implementation = FormaPagamentoDTO.class)))
    public List<FormaPagamentoDTO> listarFormasPagamento() {
        return formaPagamentoService.listarTodos().stream()
                .map(mapperService::toFormaPagamentoDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar forma de pagamento por ID", description = "Retorna uma forma de pagamento específica pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forma de pagamento encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = FormaPagamentoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada")
    })
    public ResponseEntity<FormaPagamentoDTO> buscarFormaPagamento(
            @Parameter(description = "ID da forma de pagamento", required = true, example = "1")
            @PathVariable Long id) {
        Optional<FormaPagamento> formaPagamento = formaPagamentoService.buscarPorId(id);
        if (formaPagamento.isPresent()) {
            return ResponseEntity.ok(mapperService.toFormaPagamentoDTO(formaPagamento.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar forma de pagamento por código", description = "Retorna uma forma de pagamento específica pelo seu código único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forma de pagamento encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = FormaPagamentoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada")
    })
    public ResponseEntity<FormaPagamentoDTO> buscarFormaPagamentoPorCodigo(
            @Parameter(description = "Código único da forma de pagamento", required = true, example = "PAG001")
            @PathVariable String codigo) {
        Optional<FormaPagamento> formaPagamento = formaPagamentoService.buscarPorCodigo(codigo);
        if (formaPagamento.isPresent()) {
            return ResponseEntity.ok(mapperService.toFormaPagamentoDTO(formaPagamento.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Criar nova forma de pagamento", description = "Cria uma nova forma de pagamento no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Forma de pagamento criada com sucesso",
                    content = @Content(schema = @Schema(implementation = FormaPagamentoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<FormaPagamentoDTO> criarFormaPagamento(
            @Parameter(description = "Dados da forma de pagamento a ser criada", required = true)
            @RequestBody FormaPagamentoDTO formaPagamentoDTO) {
        try {
            FormaPagamento formaPagamento = mapperService.toFormaPagamento(formaPagamentoDTO);
            Optional<FormaPagamento> formaPagamentoCriada = formaPagamentoService.criar(formaPagamento);
            if (formaPagamentoCriada.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(mapperService.toFormaPagamentoDTO(formaPagamentoCriada.get()));
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar forma de pagamento", description = "Atualiza os dados de uma forma de pagamento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forma de pagamento atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = FormaPagamentoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<FormaPagamentoDTO> atualizarFormaPagamento(
            @Parameter(description = "ID da forma de pagamento a ser atualizada", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Novos dados da forma de pagamento", required = true)
            @RequestBody FormaPagamentoDTO formaPagamentoDTO) {
        try {
            FormaPagamento formaPagamento = mapperService.toFormaPagamento(formaPagamentoDTO);
            Optional<FormaPagamento> formaPagamentoAtualizada = formaPagamentoService.atualizar(id, formaPagamento);
            if (formaPagamentoAtualizada.isPresent()) {
                return ResponseEntity.ok(mapperService.toFormaPagamentoDTO(formaPagamentoAtualizada.get()));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar forma de pagamento", description = "Remove uma forma de pagamento do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Forma de pagamento deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada")
    })
    public ResponseEntity<Void> deletarFormaPagamento(
            @Parameter(description = "ID da forma de pagamento a ser deletada", required = true, example = "1")
            @PathVariable Long id) {
        boolean deletado = formaPagamentoService.deletar(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Contar formas de pagamento", description = "Retorna o número total de formas de pagamento cadastradas")
    @ApiResponse(responseCode = "200", description = "Contagem de formas de pagamento retornada com sucesso")
    public Long contar() {
        return formaPagamentoService.contar();
    }
}