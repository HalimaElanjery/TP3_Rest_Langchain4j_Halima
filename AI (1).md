# IA.md — Utilisation de l'intelligence artificielle

## 1. Contexte

Dans le cadre du TP3 « Application REST avec LangChain4j », j'ai utilisé ChatGPT comme aide ponctuelle lorsque je rencontrais une difficulté que je n'arrivais pas à résoudre seule dans un temps raisonnable.

L'objectif n'était pas seulement de copier une solution. J'ai demandé des explications, puis j'ai intégré les modifications dans mon projet, lancé l'application sur Payara et vérifié les résultats avec des tests REST et avec l'interface HTML.

---

# Utilisation 1 — Problème d'accès à l'API REST

## Problème rencontré

Après le déploiement de l'application sur Payara, l'accès à l'URL REST retournait une erreur HTTP 404 Not Found.

L'application apparaissait pourtant comme déployée et activée dans Payara.

## Raison de l'utilisation de l'IA

Je ne trouvais pas immédiatement l'origine de l'erreur. J'ai donc utilisé l'IA pour vérifier la structure de mon application REST et comprendre comment identifier le bon chemin d'accès à la ressource.

## Prompt utilisé

> « J'ai un HTTP Status 404 - Not Found avec mon application REST Jakarta REST sur Payara. L'application est bien déployée. Voici mon code et mon URL. Aide-moi à trouver le problème et explique-moi étape par étape quoi vérifier. »

## Réponse de l'IA — extrait pertinent

L'IA m'a proposé de vérifier notamment :
- que l'application était bien activée dans Payara ;
- le context root de l'application ;
- le chemin de l'application REST (`/api`) ;
- l'annotation `@Path` de la ressource ;
- la combinaison finale des différents chemins.

L'IA m'a notamment expliqué qu'une URL REST finale est construite à partir du context root, du chemin défini par `@ApplicationPath` et du `@Path` de la ressource.

## Ce que j'ai appris

J'ai compris qu'une erreur 404 ne signifie pas forcément que le serveur ne fonctionne pas. Il faut vérifier chaque partie de l'URL.

Dans mon projet, le chemin final est construit ainsi :

```text
http://localhost:8080/
tp3-rest-langchain4j-halima-1.0-SNAPSHOT/
api/
guide/
lieu/{ville_ou_pays}
```

J'ai aussi appris à vérifier le context root dans Payara avant de modifier inutilement mon code Java.

---

# Utilisation 2 — Format exact de la réponse JSON

## Problème rencontré

Mon record Java utilisait des noms de propriétés Java en camelCase :

```text
lieu
endroitsAVisiter
prixMoyenRepas
```

Mais le format attendu dans le TP était :

```text
ville_ou_pays
endroits_a_visiter
prix_moyen_repas
```

Je voulais donc conserver des noms Java compréhensibles tout en obtenant exactement les noms demandés dans le JSON.

## Raison de l'utilisation de l'IA

Je ne savais pas comment demander à Jakarta JSON-B de produire ces noms JSON différents des noms des composants du record.

## Prompt utilisé

> « Le JSON retourné par mon application n'a pas exactement les noms demandés par le professeur. J'ai `lieu`, `endroitsAVisiter` et `prixMoyenRepas`, mais je dois obtenir `ville_ou_pays`, `endroits_a_visiter` et `prix_moyen_repas`. Comment corriger cela proprement avec mon record Java ? »

## Réponse de l'IA — extrait pertinent

L'IA m'a expliqué que JSON-B permet de personnaliser le nom d'une propriété avec `@JsonbProperty`.

Elle m'a proposé notamment :

```java
@JsonbProperty("ville_ou_pays")
String lieu
```

et de faire la même chose pour les deux autres propriétés.

Elle m'a également proposé `@JsonbPropertyOrder` pour contrôler l'ordre des propriétés dans le JSON.

## Modification réalisée

Dans `InfosTouristiques.java`, j'ai ajouté :

```java
@JsonbPropertyOrder({
        "ville_ou_pays",
        "endroits_a_visiter",
        "prix_moyen_repas"
})
```

et :

```java
@JsonbProperty("ville_ou_pays")
String lieu
```

```java
@JsonbProperty("endroits_a_visiter")
List<String> endroitsAVisiter
```

```java
@JsonbProperty("prix_moyen_repas")
String prixMoyenRepas
```

## Vérification

Après modification, j'ai testé l'API et obtenu par exemple :

```json
{
  "endroits_a_visiter": [
    "Place Jemaa el-Fna",
    "Jardin Majorelle",
    "Mosquée Hassan II",
    "Médina de Fès"
  ],
  "prix_moyen_repas": "60 MAD",
  "ville_ou_pays": "Maroc"
}
```

## Ce que j'ai appris

J'ai compris la différence entre le nom d'une propriété dans mon modèle Java et son nom dans le JSON produit par JSON-B.

J'ai surtout retenu que `@JsonbProperty` permet de contrôler explicitement le nom JSON sans devoir renommer mes variables Java.

---

# Utilisation 3 — Réalisation du bonus : nombre dynamique d'endroits

## Problème rencontré

Dans la première version de mon application, le nombre d'endroits à visiter était fixé à 2.

Le bonus demandait de permettre à l'utilisateur de choisir ce nombre.

## Raison de l'utilisation de l'IA

Je comprenais le principe du paramètre HTTP, mais je voulais savoir comment faire passer correctement cette valeur depuis l'URL jusqu'au prompt LangChain4j.

## Prompt utilisé

> « Il reste la partie bonus. Le nombre d'endroits à visiter peut être indiqué par l'utilisateur au lieu d'être fixé à 2. Je veux réaliser tout le bonus. Donne-moi les étapes de réalisation et explique-moi comment modifier la ressource REST et le prompt LangChain4j. »

## Réponse de l'IA — extrait pertinent

L'IA m'a expliqué qu'il était possible d'utiliser un paramètre de requête avec :

```java
@QueryParam("nb") @DefaultValue("2") int nb
```

Puis de transmettre cette valeur au client LangChain4j :

```java
llmClient.envoyerRequete(villeOuPays, nb);
```

Elle m'a également expliqué qu'il fallait utiliser la variable `nb` dans le prompt pour demander au modèle le nombre correspondant d'endroits.

## Modification réalisée

Dans la ressource REST :

```java
@QueryParam("nb") @DefaultValue("2") int nb
```

Dans le prompt :

```text
"endroits_a_visiter" contient les {{nb}} principaux endroits à visiter dans {{lieu}}.
```

## Tests réalisés

Sans paramètre :

```text
/api/guide/lieu/Paris
```

Le nombre par défaut est 2.

Avec `nb=3` :

```text
/api/guide/lieu/Tokyo?nb=3
```

La réponse contenait 3 endroits.

Avec `nb=4` :

```text
/api/guide/lieu/Maroc?nb=4
```

La réponse contenait 4 endroits.

## Ce que j'ai appris

J'ai compris comment un paramètre de requête HTTP peut être récupéré dans une ressource Jakarta REST avec `@QueryParam`.

J'ai aussi compris que la valeur doit ensuite être transmise à la couche qui appelle le modèle de langage et utilisée dans le prompt.

---

# Utilisation 4 — Réalisation du bonus : interface HTML

## Problème rencontré

Le deuxième bonus demandait de créer une page HTML permettant d'utiliser l'API REST.

Je devais faire communiquer une page HTML avec mon application déployée sur Payara, récupérer la réponse JSON et afficher les résultats.

## Raison de l'utilisation de l'IA

Je connaissais les bases de HTML et JavaScript, mais je ne savais pas comment construire correctement la requête vers mon endpoint REST et traiter la réponse JSON.

## Prompt utilisé

> « Headers CORS : à faire. Création de guide.html : à faire. Communication HTML → REST : à faire. Affichage des résultats HTML : à faire. Aide-moi à réaliser toutes ces étapes pour le bonus. »

## Réponse de l'IA — extrait pertinent

L'IA m'a proposé de créer un fichier :

```text
html-test/guide.html
```

avec :
- un champ pour saisir une ville ou un pays ;
- un champ pour choisir le nombre d'endroits ;
- un bouton pour lancer la requête ;
- un `XMLHttpRequest` pour appeler l'API ;
- `JSON.parse()` pour transformer la réponse en objet JavaScript ;
- l'affichage des propriétés reçues dans la page.

Elle m'a également expliqué que, lorsque la page HTML est ouverte séparément du serveur REST, il peut être nécessaire d'autoriser la communication avec l'API avec un en-tête CORS.

## Modifications réalisées

Dans la ressource REST, j'ai ajouté :

```java
responseBuilder.header("Access-Control-Allow-Origin", "*");
```

J'ai également ajouté des en-têtes pour éviter les réponses mises en cache pendant les tests :

```java
responseBuilder.header(
        "Cache-Control",
        "no-store, no-cache, must-revalidate, max-age=0"
);
responseBuilder.header("Pragma", "no-cache");
responseBuilder.header("Expires", "0");
```

Dans `guide.html`, l'URL de l'API est construite avec le lieu et le nombre demandé :

```text
http://localhost:8080/tp3-rest-langchain4j-halima-1.0-SNAPSHOT/api/guide/lieu/{ville}?nb={nb}
```

La réponse est ensuite convertie avec :

```javascript
var data = JSON.parse(xhr.responseText);
```

et les résultats sont affichés avec :

```javascript
data.ville_ou_pays
data.endroits_a_visiter
data.prix_moyen_repas
```

La liste des endroits est affichée avec :

```javascript
data.endroits_a_visiter.join(", ")
```

## Ce que j'ai appris

J'ai compris le principe d'une communication entre une page web et une API REST.

J'ai appris qu'une page HTML peut envoyer une requête HTTP à l'API, recevoir du JSON, transformer ce JSON en objet JavaScript avec `JSON.parse()` et utiliser les propriétés de cet objet pour modifier le contenu de la page.

J'ai également compris le rôle de CORS lorsqu'une page et une API ne sont pas servies exactement depuis la même origine.

---

# Utilisation 5 — Documentation et vérification finale

## Problème rencontré

Après avoir terminé les fonctionnalités, je voulais vérifier que mon code était correctement documenté et que les éléments demandés dans le TP étaient bien présents.

## Raison de l'utilisation de l'IA

Je voulais éviter d'oublier une partie du TP, notamment la Javadoc, les tests et les éléments du bonus.

## Prompt utilisé

> « Est-ce que j'ai fait tout le TP ? Il reste la partie bonus. Vérifie ce qu'il manque et aide-moi à terminer toutes les parties demandées, puis à vérifier les tests, la Javadoc et GitHub. »

## Réponse de l'IA — extrait pertinent

L'IA m'a indiqué de vérifier :
- le fonctionnement de l'API REST ;
- le format JSON ;
- le paramètre dynamique `nb` ;
- l'interface HTML ;
- la Javadoc ;
- les tests ;
- le dépôt GitHub et les commits.

Elle m'a aussi proposé une Javadoc pour la méthode REST :

```java
/**
 * Retourne les informations touristiques d'une ville ou d'un pays.
 *
 * @param villeOuPays nom de la ville ou du pays à rechercher
 * @param nb nombre d'endroits à visiter (2 par défaut)
 * @return réponse JSON contenant le lieu, les endroits à visiter
 *         et le prix moyen d'un repas
 */
```

## Ce que j'ai appris

J'ai compris que la vérification finale d'un TP ne consiste pas seulement à vérifier que l'application démarre.

Il faut également vérifier :
- le comportement attendu ;
- les différents cas de test ;
- le format des réponses ;
- la documentation ;
- les fonctionnalités du bonus ;
- et la présence des fichiers nécessaires dans le dépôt.

---

# 6. Bilan personnel

L'utilisation de l'IA m'a principalement aidée à comprendre des problèmes précis que je n'arrivais pas à résoudre seule rapidement.

Les principales notions que j'ai retenues sont :

- la construction d'une URL Jakarta REST à partir du context root, de `@ApplicationPath` et de `@Path` ;
- la personnalisation des propriétés JSON avec JSON-B ;
- l'utilisation de `@QueryParam` et `@DefaultValue` ;
- le passage d'une valeur de la requête HTTP jusqu'au prompt LangChain4j ;
- la communication entre une page HTML et une API REST ;
- le principe de CORS ;
- l'utilisation de `JSON.parse()` et l'affichage des données JSON en JavaScript ;
- l'importance des tests et de la documentation.

L'IA a donc été utilisée comme un outil d'aide à la compréhension et au débogage, et non uniquement pour obtenir du code. Après chaque modification, j'ai intégré le code dans mon projet, exécuté l'application et vérifié le résultat.

