package dev.hotel.web;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hotel.entite.Client;
import dev.hotel.exception.ClientNotFoundException;
import dev.hotel.exception.InvalidInputsException;
import dev.hotel.repository.ClientRepository;

@RestController
public class ClientController {

	private ClientRepository clientRepository;

	// Ici on autowire pas car c'est une mauvaise pratique, il faut mettre un
	// constructeur

	public ClientController(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	// GET /clients
	@RequestMapping(method = RequestMethod.GET, path = "clients")
	public List<Client> listerClients(@RequestParam Integer start, @RequestParam Integer size) {
		return clientRepository.findAll(PageRequest.of(start, size)).getContent();
	}

	// GET /clients
	@RequestMapping(method = RequestMethod.GET, path = "clients/{uIdClient}")
	public ResponseEntity<Optional<Client>> getClient(@PathVariable String uIdClient) {
		Optional<Client> client = clientRepository.findById(UUID.fromString(uIdClient));

		if (client.isPresent()) {
			return ResponseEntity.status(202).body(client);
		} else {
			throw new ClientNotFoundException("UUID recherchée non répertoriée."); // va être gérée par un handler
		}
	}

	// Permet de gérer les erreurs de clients non trouvé et catch les exception de
	// type ClientNotFoundException
	@ExceptionHandler(value = { ClientNotFoundException.class })
	public ResponseEntity<String> onClientNotFound(ClientNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("client non trouvé : " + exception.getMessage());
	}

	// POST /clients
	@PostMapping("/post")
	public ResponseEntity<Client> postClient(@RequestParam String nomClient, @RequestParam String prenomClient)
			throws InvalidInputsException {
		if (nomClient.length() < 2 || prenomClient.length() < 2) {
			throw new InvalidInputsException("Nom ou Prénom trop court, 2 caractères minimum.");
		} else {
			Client newClient = new Client(nomClient, prenomClient);
			clientRepository.save(newClient);
			return ResponseEntity.status(200).body(newClient);
		}
	}

	@ExceptionHandler(value = { InvalidInputsException.class })
	public ResponseEntity<String> onNotValidInputs(InvalidInputsException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Nom ou prénom saisi trop courts, 2 caractères minimum");
	}

}
