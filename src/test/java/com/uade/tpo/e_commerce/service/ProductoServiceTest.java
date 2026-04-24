package com.uade.tpo.e_commerce.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.uade.tpo.e_commerce.dto.ProductoRequestDTO;
import com.uade.tpo.e_commerce.dto.ProductoResponseDTO;
import com.uade.tpo.e_commerce.dto.ProductoUpdateDTO;
import com.uade.tpo.e_commerce.exception.PrecioNegativoException;
import com.uade.tpo.e_commerce.exception.ProductoNotFoundException;
import com.uade.tpo.e_commerce.model.Producto;
import com.uade.tpo.e_commerce.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void saveProducto_rechazaPrecioNegativo() {
        ProductoRequestDTO request = new ProductoRequestDTO("Nombre", "Desc", -1.0, 5);

        assertThatThrownBy(() -> productoService.saveProducto(request))
                .isInstanceOf(PrecioNegativoException.class);
    }

    @Test
    void updateProducto_lanzaSiNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        ProductoUpdateDTO request = new ProductoUpdateDTO(10.0, 5);

        assertThatThrownBy(() -> productoService.updateProducto(99L, request))
                .isInstanceOf(ProductoNotFoundException.class);
    }

    @Test
    void saveProducto_devuelveResponse() {
        ProductoRequestDTO request = new ProductoRequestDTO("Nombre", "Desc", 10.0, 5);
        Producto saved = Producto.builder()
                .id(1L)
                .nombre("Nombre")
                .descripcion("Desc")
                .precio(10.0)
                .stock(5)
                .build();

        when(productoRepository.save(any(Producto.class))).thenReturn(saved);

        ProductoResponseDTO response = productoService.saveProducto(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNombre()).isEqualTo("Nombre");
    }
}
