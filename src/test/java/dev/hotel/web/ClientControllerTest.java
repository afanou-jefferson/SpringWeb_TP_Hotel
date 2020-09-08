package dev.hotel.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.hotel.entite.Client;
import dev.hotel.service.ClientService;
import dev.hotel.web.client.ClientController;

@WebMvcTest(ClientController.class) // Controller à tester
public class ClientControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ClientService clientService;

	@Test
	public void testListerClientsAvec2Clients() throws Exception {
		//
		Client c1 = new Client();
		c1.setNom("Caillou");
		c1.setPrenoms("Pierre");

		Client c2 = new Client();
		c2.setNom("Doe");
		c2.setPrenoms("John");

		when(clientService.listerClients(10, 20)).thenReturn(Arrays.asList(c1, c2));
		// GET /clients
		mockMvc.perform(MockMvcRequestBuilders.get("/clients?start=10&size=20"))
				.andExpect(MockMvcResultMatchers.jsonPath("[0].nom").value("Caillou"))
				.andExpect(MockMvcResultMatchers.jsonPath("[0].prenoms").value("Pierre"))
				.andExpect(MockMvcResultMatchers.jsonPath("[1].nom").value("Doe"));
	}

	@Test
	public void testerGetClientIDValide() throws Exception {

		Client c1 = new Client();
		c1.setNom("Caillou");
		c1.setPrenoms("Pierre");
		Optional<Client> optionalClient = Optional.of(c1); // Client extends Base entite donc UUID généré
															// randomly
		when(clientService.recupererClient(c1.getUuid())).thenReturn(optionalClient);

		// Get /clients/UUID
		mockMvc.perform(MockMvcRequestBuilders.get("/clients/{uuid}", c1.getUuid()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.nom").value("Caillou"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.prenoms").value("Pierre"));
	}

	@Test
	public void testerGetClientIdNonValide() throws Exception {
		Client c1 = new Client();
		mockMvc.perform(MockMvcRequestBuilders.get("/clients/{uuid}", c1.getUuid()))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void testPostClientValide() throws Exception {
		Client c1 = new Client();
		c1.setNom("Caillou");
		c1.setPrenoms("Pierre");

		when(clientService.creerNouveauClient("Caillou", "Pierre")).thenReturn(new Client("Caillou", "Pierre"));

		mockMvc.perform(MockMvcRequestBuilders.post("/clients").content(asJsonString(c1))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.nom").exists());

	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
