# Hotel API

Cette app est le rendu d'un TP d'initiation aux API

## Installation

Aucune, déjà déployée sur https://hotel-web-app-h2.herokuapp.com/

## Usage

3 requêtes utilisées pour réalisé le TP  :
- GET : https://hotel-web-app-h2.herokuapp.com/clients?start=0&size=3 
- GET : https://hotel-web-app-h2.herokuapp.com/dcf129f1-a2f9-47dc-8265-1d844244b192
- POST : https://hotel-web-app-h2.herokuapp.com/reservations
  body  {
  "dateDebut" : "2019-10-01",
  "dateFin" : "2019-10-10",
  "clientId" : "dcf129f1-a2f9-47dc-8265-1d844244b192", 
  "chambres" : [
  "754e6f53-e8f5-4976-9fd2-95e6a427ef1c",
  "43793061-f70b-44b9-a855-adc66a2efb9f"
  ]
}
