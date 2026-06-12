package com.uade.tpo.e_commerce.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDTO {
    private Long id;
    private Long usuarioId;
    private String estado;
    private Double total;
    private List<PedidoItemResponseDTO> items;
}
