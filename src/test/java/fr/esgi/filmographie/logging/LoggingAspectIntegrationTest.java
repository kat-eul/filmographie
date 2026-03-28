package fr.esgi.filmographie.logging;

import fr.esgi.filmographie.role.RoleService;
import fr.esgi.filmographie.role.dto.RoleDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.extension.ExtendWith;

@SpringBootTest()
@TestPropertySource(properties = {
        "logging.level.fr.esgi.filmographie=DEBUG",
        "logging.aspect.slow-threshold-ms=200"
})
@ExtendWith(OutputCaptureExtension.class)
@AutoConfigureMockMvc
class LoggingAspectIntegrationTest {

    @Autowired
    RoleService roleService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void shouldLogHttpInAndHttpOutForControllerCalls(CapturedOutput output) throws Exception {
        final var roleToCreate = RoleDTO.builder().name("Director").build();

        final var createResponse = mockMvc.perform(post("/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        final RoleDTO created = objectMapper.readValue(
                createResponse.getResponse().getContentAsString(),
                RoleDTO.class
        );

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();

        final String logs = output.getOut() + output.getErr();
        assertThat(logs)
                .contains("http-in")
                .contains("http-out")
                .contains("class=RoleController")
                .contains("method=create");
    }


    @Test
    void shouldLogStartAndEndForServiceMethod(CapturedOutput output) {
        roleService.create(RoleDTO.builder().name("Director").build());

        final String logs = output.getOut() + output.getErr();
        assertThat(logs)
                .contains(" start class=RoleService method=create")
                .contains(" end class=RoleService method=create");
    }

    @Test
    void shouldLogErrorWhenExceptionIsThrown(CapturedOutput output) throws Throwable {
        final var aspect = new LoggingAspect(200);

        final ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        final MethodSignature sig = mock(MethodSignature.class);

        doReturn(sig).when(pjp).getSignature();
        doReturn(new Object()).when(pjp).getTarget();
        doThrow(new RuntimeException("boom")).when(pjp).proceed();

        doReturn("create").when(sig).getName();
        doReturn("fr.esgi.filmographie.role.RoleService").when(sig).getDeclaringTypeName();
        doReturn(fr.esgi.filmographie.role.RoleService.class).when(sig).getDeclaringType();

        assertThatThrownBy(() -> aspect.logAround(pjp))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("boom");

        assertThat(output.getOut() + output.getErr())
                .contains(" error ")
                .contains(" exType=RuntimeException");

        verify(pjp).proceed();
    }
}
