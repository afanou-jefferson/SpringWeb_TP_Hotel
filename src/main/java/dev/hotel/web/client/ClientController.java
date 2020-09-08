package dev.hotel.web.client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hotel.entite.Client;
import dev.hotel.service.ClientService;

@RestController
@RequestMapping("clients") // Equivaux à path "/clients"
public class ClientController {

	private ClientService clientService;

	// Ici on autowire pas car c'est une mauvaise pratique, il faut mettre un
	// constructeur
	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	// GET /clients
	// @RequestMapping(method = RequestMethod.GET, path = "clients")
	@GetMapping
	public List<Client> listerClients(@RequestParam Integer start, @RequestParam Integer size) {
		return clientService.listerClients(start, size);
	}

	// @RequestMapping(method = RequestMethod.GET, path = "clients/{uuid}")
	@GetMapping("/{uuid}")
	public ResponseEntity<?> getClientUUID(@PathVariable UUID uuid) {

		Optional<Client> optClient = clientService.recupererClient(uuid);

		if (optClient.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(optClient);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Veuillez entrer un autre identifiant client");
		}
	}

	// POST /clients
	@PostMapping
	public ResponseEntity<?> postClient(@RequestBody @Valid CreerClientRequestDto client,
			BindingResult resultatValidation) {
		if (resultatValidation.hasErrors()) {
			return ResponseEntity.badRequest().body("Erreur");
		}
		return ResponseEntity
				.ok(new CreerClientResponseDto(clientService.creerNouveauClient(client.getNom(), client.getPrenoms())));
	}

}
