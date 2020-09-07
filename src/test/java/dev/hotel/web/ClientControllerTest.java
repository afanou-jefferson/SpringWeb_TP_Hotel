package dev.hotel.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.hotel.entite.Client;
import dev.hotel.repository.ClientRepository;

@WebMvcTest(ClientController.class) // Controller à tester
public class ClientControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ClientRepository clientRepository;

	@Test
	public void testListerClientsAvec2Clients() throws Exception {
		//
		Client c1 = new Client();
		c1.setNom("Caillou");
		c1.setPrenoms("Pierre");

		Client c2 = new Client();
		c2.setNom("Doe");
		c2.setPrenoms("John");

		when(clientRepository.findAll(PageRequest.of(10, 20))).thenReturn(new PageImpl<>(Arrays.asList(c1, c2)));

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
		Optional<Client> optionalClient = Optional.ofNullable(c1); // Client extends Base entite donc UUID généré
																	// randomly
		when(clientRepository.findById(c1.getUuid())).thenReturn(optionalClient);

		// Get /clients/UUID
		mockMvc.perform(MockMvcRequestBuilders.get("/clients/" + c1.getUuid()))
				.andExpect(MockMvcResultMatchers.jsonPath("nom").value("Caillou"))
				.andExpect(MockMvcResultMatchers.jsonPath("prenoms").value("Pierre"));
	}

	@Test
	public void testerGetClientIdNonValide() throws Exception {
		Client c1 = new Client();
		mockMvc.perform(MockMvcRequestBuilders.get("/clients/" + c1.getUuid())).andExpect(status().is4xxClientError());
	}

	@Test
	public void testPostClientValide() throws Exception {
		Client c1 = new Client();
		c1.setNom("Caillou");
		c1.setPrenoms("Pierre");

		when(clientRepository.save(c1)).thenReturn(c1);

		// post?nomClient=x&prenomClient=y
		mockMvc.perform(MockMvcRequestBuilders.get("/post?nomClient=Caillou&prenomClient=Pierre"))
				.andExpect(MockMvcResultMatchers.jsonPath("nom").value("Caillou"))
				.andExpect(MockMvcResultMatchers.jsonPath("prenoms").value("Pierre"));
	}

}
