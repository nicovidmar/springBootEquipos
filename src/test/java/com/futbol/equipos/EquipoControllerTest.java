package com.futbol.equipos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.futbol.equipos.entity.Equipo;
import com.futbol.equipos.request.EquipoRequest;
import com.futbol.equipos.service.EquipoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.hamcrest.CoreMatchers;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class EquipoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EquipoService equipoService;

    @Autowired
    private ObjectMapper objectMapper;

    Equipo equipo1 = new Equipo();
    Equipo equipo2 = new Equipo();

    @BeforeEach
    void init() {
        equipo1.setNombre("Real Madrid");
        equipo1.setLiga("La Liga");
        equipo1.setPais("España");

        equipo2.setNombre("Dux Fc");
        equipo2.setLiga("Primera Division");
        equipo2.setPais("Argentina");

    }

    @DisplayName("Test para método GET del Controller")
    @Test
    void testGetEquipoById() throws Exception {
        when(equipoService.findById(1L)).thenReturn(equipo1);

        ResultActions response = mockMvc.perform(get("/equipos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(equipo1)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.nombre", CoreMatchers.is(equipo1.getNombre())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.liga", CoreMatchers.is(equipo1.getLiga())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.pais", CoreMatchers.is(equipo1.getPais())));
    }

    @DisplayName("Test para método POST del Controller")
    @Test
    void testCreateEquipo() throws Exception {
        given(equipoService.save(ArgumentMatchers.any(Equipo.class))).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/equipos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(equipo1)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.nombre", CoreMatchers.is(equipo1.getNombre())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.liga", CoreMatchers.is(equipo1.getLiga())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.pais", CoreMatchers.is(equipo1.getPais())));
    }

    @DisplayName("Test para método PUT del Controller")
    @Test
    void testUpdateEquipo() throws Exception {
        when(equipoService.updateEquipo(ArgumentMatchers.anyLong(), ArgumentMatchers.any(EquipoRequest.class))).thenReturn(equipo2);

        ResultActions response = mockMvc.perform(put("/equipos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(equipo2)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.nombre", CoreMatchers.is(equipo2.getNombre())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.liga", CoreMatchers.is(equipo2.getLiga())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.pais", CoreMatchers.is(equipo2.getPais())));
    }

    @DisplayName("Test para método DELETE del Controller")
    @Test
    void testDeleteEquipo() throws Exception {
        doNothing().when(equipoService).deleteById(1L);

        ResultActions response = mockMvc.perform(delete("/equipos/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
