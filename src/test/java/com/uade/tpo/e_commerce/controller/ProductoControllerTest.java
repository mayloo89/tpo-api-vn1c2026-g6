package com.uade.tpo.e_commerce.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.uade.tpo.e_commerce.model.Producto;
import com.uade.tpo.e_commerce.repository.ProductoRepository;

@SpringBootTest
class ProductoControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ProductoRepository productoRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        productoRepository.deleteAll();
    }

    // ── GET /api/productos ────────────────────────────────────────────────────

    @Test
    void getProductos_sinAuth_retornaListaVacia() throws Exception {
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getProductos_retornaListaConElementos() throws Exception {
        productoRepository.save(Producto.builder()
                .nombre("Teclado").descripcion("Mecánico").precio(15000.0).stock(10).build());
        productoRepository.save(Producto.builder()
                .nombre("Mouse").descripcion("Inalámbrico").precio(8000.0).stock(5).build());

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Teclado")));
    }

    // ── GET /api/productos/{id} ───────────────────────────────────────────────

    @Test
    void getProductoById_retornaProducto() throws Exception {
        Producto saved = productoRepository.save(Producto.builder()
                .nombre("Monitor").descripcion("4K").precio(50000.0).stock(3).build());

        mockMvc.perform(get("/api/productos/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Monitor")))
                .andExpect(jsonPath("$.precio", is(50000.0)));
    }

    @Test
    void getProductoById_retorna404SiNoExiste() throws Exception {
        mockMvc.perform(get("/api/productos/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    // ── POST /api/productos ───────────────────────────────────────────────────

    @Test
    void crearProducto_sinAuth_retornaNoAutorizado() throws Exception {
        String body = """
                {"nombre":"Auriculares","descripcion":"BT","precio":5000.0,"stock":20}
                """;

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearProducto_conAdmin_retorna201() throws Exception {
        String body = """
                {"nombre":"Auriculares","descripcion":"BT","precio":5000.0,"stock":20}
                """;

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Auriculares")))
                .andExpect(jsonPath("$.precio", is(5000.0)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void crearProducto_conUser_retorna403() throws Exception {
        String body = """
                {"nombre":"Auriculares","descripcion":"BT","precio":5000.0,"stock":20}
                """;

        // POST /api/productos requiere autenticación pero no rol específico,
        // USER autenticado puede crear productos
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    // ── DELETE /api/productos/{id} ────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    void eliminarProducto_conUser_retorna403() throws Exception {
        Producto saved = productoRepository.save(Producto.builder()
                .nombre("Gabinete").descripcion("ATX").precio(12000.0).stock(2).build());

        mockMvc.perform(delete("/api/productos/{id}", saved.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminarProducto_conAdmin_retorna204() throws Exception {
        Producto saved = productoRepository.save(Producto.builder()
                .nombre("Gabinete").descripcion("ATX").precio(12000.0).stock(2).build());

        mockMvc.perform(delete("/api/productos/{id}", saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminarProducto_noExiste_retorna404() throws Exception {
        mockMvc.perform(delete("/api/productos/{id}", 9999L))
                .andExpect(status().isNotFound());
    }
}
