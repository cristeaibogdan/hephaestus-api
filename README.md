<div align="center">
   <img width="450" src="assets/hephaestus-logo.png" >
</div>

<div align="center">

   [Requirements](#requirements) | 
   [Usage](#usage) | 
   [Important URLs](#important-urls) |
   [Possible problems](#possible-problems)

</div>

## What does this project do?
`Hephaestus` is a personal project developed by **HARServices** designed to help users make informed decisions about household appliances. The application evaluates the condition and damage of the appliance and provides tailored recommendations on what actions to take.

The project consists of two components:
- Frontend built with Angular 
- Backend developed using Java ( **you are here 📍** )

## Requirements
* IDE
* Java 17
* Docker
* Docker Compose

## Usage
1. Clone the repository:
```bash
git clone https://github.com/cristeaibogdan/hephaestus-api.git
```
2. Start Docker
3. Set Maven Profile to `docker` 

   a. Click  `Reload All Maven Projects` 

   b. Click `Generate Sources and Update Folders For All Projects`

   c. Run `package` for `hephaestus-api`
<p align="center">
   <img src="assets/maven-profile.jpg">
</p>

4. Docker images will be created in your local daemon (takes some time to build all of them...)
5. Open a terminal in the root folder (`hephaestus-api`) and start all containers:
```bash
docker-compose up -d
```
6. All backend services should be up and running.

## Important URLs
- [Pgadmin](http://localhost:5050) - `localhost:5050`
  1. Use `postgres` to login
  2. Register new server
  3. On the Connection tab use `postgres` for Host name/address, username and password

- [Zipkin server](http://localhost:9411) - `localhost:9411`

## Possible problems
* The microservice `washing-machine` may fail to recognize `QWashingMachine` (object generated by Querydsl).      
  * To fix open the Project tab, right click on `washing-machine` module and select Maven, `Generate Sources And Update Folders`

<div align="center">
   <img src="assets/QWashingMachine.gif" >
</div>

***