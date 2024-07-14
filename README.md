<p align="center">
   <img width="450" src="assets/hephaestus-logo.png" >
</p>

## What does this project do?

TODO: provide short introduction to what the project does.

## Table of contents

- [Requirements](#requirements)
- [Usage](#usage)
- [Important addresses](#important-addresses)
- [Possible problems](#possible-problems)

## Requirements

* IDE
* Java 17
* Docker & Compose
* Docker Compose

## Usage

1. Start Docker
2. Set Maven Profile to `docker` 
   * a. Click  `Reload All Maven Projects` 
   * b. Click `Generate Sources and Update Folders For All Projects`
   * c. Run `package` for `hephaestus-api`
<p align="center">
   <img src="assets/maven-profile.jpg">
</p>

3. Docker images will be created in your local daemon, it will take some time
4. Open a terminal in the root folder (`hephaestus-api`) and start all containers:
```bash
docker-compose up -d
```
6. All backend services should be up and running.

## Important URLs

- [Zipkin server](http://localhost:9411) - `localhost:9411`

## Possible problems

* The microservice `washing-machine` may fail to recognize `QWashingMachine` (object generated by Querydsl).

To fix open the Project tab, right click on `washing-machine` module and select Maven, `Generate Sources And Update Folders`
