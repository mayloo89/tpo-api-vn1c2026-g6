package com.uade.tpo.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoItemResponseDTO {
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private Double precioUnitario;
}
