package com.futbol.equipos;

import static org.mockito.ArgumentMatchers.any;

import java.util.Collections;
import java.util.List;

import com.futbol.equipos.entity.Equipo;
import com.futbol.equipos.exception.CustomizableException;
import com.futbol.equipos.repository.EquipoRepository;
import com.futbol.equipos.request.EquipoRequest;
import com.futbol.equipos.security.AuthController;
import com.futbol.equipos.service.EquipoService;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ComponentScan(
    basePackages = {"com.futbol.equipos"}, // Escanea todo el proyecto
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX, 
        pattern = "com\\.futbol\\.equipos\\.security\\..*" // Excluye el paquete security
    )
)
class EquiposApplicationTests {

	@Mock
	AuthController auth;

	@Mock
    private EquipoRepository equipoRepository; // Mock del repositorio

    @InjectMocks
    private EquipoService equipoService; // Servicio con dependencia mockeada

    private List<Equipo> mockEquipos;

	private Equipo equipo;

    @BeforeEach
    void setUp() {
        mockEquipos = List.of(
            new Equipo(1L, "Real Madrid", "La Liga", "España"),
            new Equipo(2L, "FC Barcelona", "La Liga", "España")
        );

		equipo = mockEquipos.get(0);
    }

    @Test
    public void findAllTest() {
        when(equipoRepository.findAll()).thenReturn(mockEquipos); // Configura el mock

        List<Equipo> equipos = equipoService.findAll(); // Llama al servicio

        Assertions.assertEquals(mockEquipos, equipos); // Verifica el resultado
    }

    @Test
    public void findByIdTest() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo)); // Configura el mock

        Equipo equipoEncontrado = equipoService.findById(1L); // Llama al servicio

        Assertions.assertEquals(equipo, equipoEncontrado); // Verifica el resultado
    }

	/* Verifica que tire CustomizableException al buscar por ID inexistente */
	@Test
    public void findByIdTestErr() throws CustomizableException {
        when(equipoRepository.findById(100L)).thenReturn(Optional.empty()); // Configura el mock

        // Verifica que se lanza la excepción CustomizableException
		CustomizableException exception = Assertions.assertThrows(CustomizableException.class, () -> {
			equipoService.findById(100L); // Llama al método que debería lanzar la excepción
		});

        Assertions.assertEquals("Equipo no encontrado.", exception.getMessage());
    	Assertions.assertEquals(404, exception.getCodigo());
    }

	@Test
    public void findAllByNombreContainingTest() {
        when(equipoRepository.findAllByNombreContainingIgnoreCase("Real")).thenReturn(List.of(equipo)); // Configura el mock

		List<Equipo> equipos = equipoService.findAllByNombreContaining("Real"); // Llama al servicio

		Assertions.assertEquals(equipo, equipos.get(0)); // Verifica el resultado

    }

	@Test
    public void findAllByNombreContainingTestErr() throws CustomizableException {
		when(equipoRepository.findAllByNombreContainingIgnoreCase("Boca")).thenReturn(Collections.emptyList());

        // Verifica que se lanza la excepción CustomizableException
		CustomizableException exception = Assertions.assertThrows(CustomizableException.class, () -> {
			equipoService.findAllByNombreContaining("Boca"); 
		});

        Assertions.assertEquals("Equipo no encontrado.", exception.getMessage());
    	Assertions.assertEquals(404, exception.getCodigo());

    }

	@Test
	public void updateEquipoTest() {
		// Equipo con valores actualizados
		EquipoRequest equipoRequest = new EquipoRequest("Dux Fc", "Primera Division", "Argentina");

		// Equipo existente en el repositorio
		Equipo equipoExistente = new Equipo(1L, "Real Madrid", "La Liga", "España");

		// Configuración del mock para que encuentre el equipo por ID
		when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoExistente));

		// Configuración del mock para guardar el equipo actualizado
		when(equipoRepository.save(any(Equipo.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Llamada al servicio
		Equipo resultado = equipoService.updateEquipo(1L, equipoRequest);

		// Verifica el resultado
		//Assertions.assertTrue(resultado.isPresent());
		Assertions.assertEquals("Dux Fc", resultado.getNombre());
		Assertions.assertEquals("Primera Division", resultado.getLiga());
		Assertions.assertEquals("Argentina", resultado.getPais());
	}

	@Test
	public void updateEquipoTestErr() {
		// Equipo con valores actualizados
		EquipoRequest equipoRequest = new EquipoRequest("Dux Fc", "", "");

		// Simular que el repositorio encuentra el equipo
		when(equipoRepository.findById(1L)).thenReturn(Optional.of(new Equipo(1L, "Real Madrid", "La Liga", "España")));

		// Verificar que se lanza la excepción
		CustomizableException exception = Assertions.assertThrows(CustomizableException.class, () -> {
			equipoService.updateEquipo(1L, equipoRequest); // Enviar request con parametro vacío
		});

		Assertions.assertEquals("La solicitud es invalida", exception.getMensaje());
		Assertions.assertEquals(400, exception.getCodigo());
	}

	@Test
	public void saveTest() {
		// Configurar el mock del repositorio para devolver el mismo objeto
		when(equipoRepository.save(any(Equipo.class))).thenReturn(equipo);

		// Llamar al método del servicio
		Equipo resultado = equipoService.save(mockEquipos.get(0));

		// Verificar que el resultado es el esperado
		Assertions.assertEquals(equipo, resultado);
	}

	@Test
	public void deleteByIdTest() {
		Long id = 1L;

		// Ejecutar el método del servicio
		equipoService.deleteById(id);

		// Verificar que el método del repositorio fue llamado con el ID correcto
		verify(equipoRepository, times(1)).deleteById(id);
	}

}
