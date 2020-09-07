package dev.hotel.web;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hotel.entite.Client;
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
	public Optional<Client> getClient(@PathVariable String uIdClient) {
		return clientRepository.findById(UUID.fromString(uIdClient));
	}
}
