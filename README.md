# P6-Full-Stack-reseau-dev

Application full-stack **MDD (Monde de Dév)** : back-end Java/Spring Boot + front-end Angular.

## Prérequis

- Java 11
- Maven (le wrapper `mvnw` est fourni)
- Node.js / npm
- MySQL en écoute sur le port 3306

## Back

Le back-end est une API REST Spring Boot située dans le dossier `back/`.

### Base de données

L'application se connecte à une base MySQL `mdd_db` (créée automatiquement au démarrage
grâce au paramètre `createDatabaseIfNotExist=true`). La configuration se trouve dans
`back/src/main/resources/application.properties`.

### Démarrer le serveur

Depuis le dossier `back/` :

```
./mvnw spring-boot:run
```

L'API démarre sur `http://localhost:8080/`.

## Front

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.3.

Don't forget to install your node_modules before starting (`npm install`).

### Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

### Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

### Where to start

As you may have seen if you already started the app, a simple home page containing a logo, a title and a button is available. If you take a look at its code (in the `home.component.html`) you will see that an external UI library is already configured in the project.

This library is `@angular/material`, it's one of the most famous in the angular ecosystem. As you can see on their docs (https://material.angular.io/), it contains a lot of highly customizable components that will help you design your interfaces quickly.

Note: I recommend to use material however it's not mandatory, if you prefer you can get rid of it.

Good luck!
