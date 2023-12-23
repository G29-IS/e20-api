
<h1 align="center">
  <br>
  <a href="https://e-20.net"><img src="assets/logo.png" alt="E-20" width="200"></a>
  <br>
api
  <br>
</h1>

<h4 align="center">REST api per <a href="https://e-20.net" target="_blank">E-20</a>.</h4>

<p align="center">
  <a href="https://dl.circleci.com/status-badge/redirect/gh/G29-IS/e20-api/tree/main"><img src="https://dl.circleci.com/status-badge/img/gh/G29-IS/e20-api/tree/main.svg?style=svg&circle-token=4729243b7b647b23465f18f1f2a6a7411909b1d4"></a>
</p>

## Esecuzione in locale e development
Questa api richiede i seguenti servizi:
- PostgreSQL
- Redis
- Prometheus
- Grafana

Un Docker compose file per scopi di development locale è disponibile nella directory, alla root del progetto, chiamata `docker-compose-dev.yml`  
Esso crea tutti i servizi richiesti con un eventuale web dashboard:

| Service    |  username  |    password    | endpoint       | web dashboard                           |    
|------------|:----------:|:--------------:|:---------------|:----------------------------------------|    
| PostgreSQL | e20DevUser | e20DevPassword | localhost:5432 | [localhost:8081](http://localhost:8081) |    
| Redis      |            |                | localhost:6379 | [localhost:8082](http://localhost:8082) |    
| Prometheus |            |                | localhost:9090 |                                         |
| Grafana    |   admin    |     admin      |                | [localhost:3000](http://localhost:3000) |

Un file `.env` per lo sviluppo si trova in `/env/.env.development`, copialo nella root directory e modifica le variabili necessarie.

> Per utilizzare la web dashboard di Postgres
> inserisci nel campo `Server` il nome dato al Docker container di Postgres,
> se stai usando il file `docker-compose-dev.yml` esso sarà `index-postgres`.
> 
> Il database di default è `e20devdb`  

### Swagger UI
Swagger è disponibile all'url [localhost:$PORT/swagger](http://localhost:8080/swagger)  

### Grafana
Attraverso grafana è possibile visualizzare le metriche sull'utilizzo delle API di e-20 ed il carico del relativo server.  
Per farlo bisogna configurare un `data source`, queste api utilizzano Prometheus.

##### Sicurezza
Quando di connetti alla [dashboard Grafana](http://localhost:3000) ti verrà chiesto di cambiare la password!  
Per lo sviluppo locale non è un problema tenere `admin` come password, ma è importante settare una password sicura in ambiente di production o in ogni modo pubblici.

##### Aggiungere Prometheus come data source
Dalla homepage della dashboard di Grafana, clicca `Add your first datasource` e seleziona Prometheus, oppure seleziona `Connections > Add new connection > Prometheus` e clicca `Add new datasource`.  

Scegli un `name` (indifferente), specifica l'url, con il setup di docker compose dev dovrebbe essere `http://localhost:9090`, scorri in fondo alla pagina e clicca `Save & Test`  

##### Creare una dashboard per il monitoraggio delle api
Ci sono diverse dashboard già esistenti che si possono usare.  
Una dashboard fatta abbastanza bene è [questa qui](https://grafana.com/grafana/dashboards/4701-jvm-micrometer/)  

Per crearla clicca nell'hamburger menu a sinistra della home di Grafana, poi clicca `Dashboards` e successivamente `New > New dashboard` in alto a destra, poi `Import dashboard` ed inserisci il seguente ID a sinistra del pulsante `Load`: `4701`  