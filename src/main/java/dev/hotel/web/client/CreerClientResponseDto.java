package dev.hotel.web.client;

import dev.hotel.entite.Client;

import java.util.UUID;

public class CreerClientResponseDto extends CreerClientRequestDto {

    private UUID uuid;

    public CreerClientResponseDto(Client client) {
        this.uuid = client.getUuid();
        this.setNom(client.getNom());
        this.setPrenoms(client.getPrenoms());
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
