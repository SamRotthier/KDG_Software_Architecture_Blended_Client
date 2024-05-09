# V1

## Pre coaching

### Geschatte Progress (in procent): 30%

### Status

_Waar sta je globaal? Wat loopt goed en minder goed? Hoe verloopt de samenwerking? Wie heeft globaal welke delen van de applicatie uitgewerkt? (details progress kunnen we bekijken op het issue board)_

**Waar sta je globaal?**

Globaal gezien staan we niet waar we wouden staan. Wegens onvoorziene omstandigheden op het werk, hadden we beiden onvoldoende tijd om ons te focussen op de opdracht. Sam heeft veel wachtdiensten (24/7) en nieuwe klanten die vaak belden tijdens deze wachtdiensten. Bij Matthias een senior collega gestopt die hij moet vervangen. Hierdoor was er enorm veel knowledge transfer dat verwerkt diende te worden. Dit zijn enkele reden waarom we een stuk later zijn kunnen starten dan gehoopt.

Vanaf eind april hadden we eindelijk tijd om goed en wel aan de opdracht te beginnen. We zijn er van overtuigd dat we in mei nog steeds de opgelopen achterstand kunnen inhalen.

**Wat loopt goed en minder goed?**

De hele opstart van het project liep niet goed. Er kroop enorm veel tijd in het bestuderen van Spring en hoe dit juist werkt. Ondanks onze ervaring in Java, was de manier van werken toch even aanpassen. De opgenomen theorielessen hielpen ons om de concepten beter te begrijpen, maar het was nog steeds een drempel om de linken te leggen tussen de concepten. Alsook hoe dit juist praktisch moest worden uitgewerkt. Daarnaast hebben we ook veel tijd (+/- 20u) gestoken in het opzetten van de database met Docker. Momenteel zoeken we nog uit hoe we dit over 3 applicaties heen goed kunnen laten werken.

**Hoe verloopt de samenwerking?**

Om aan de opdracht te starten hebben we elke keer nauw samengewerkt via Teams calls. Nu we eindelijk goed op weg zijn, verdelen we het werk terwijl we bellen. We helpen elkaar met vragen tijdens dat ieders aan een eigen US bezig.


**Wie heeft globaal welke delen van de applicatie uitgewerkt?**

Aangezien we elke keer via Teams werken, hebben we globaal gezien aan dezelfde delen gewerkt mbv screensharing. In Git zal je een commit van 1 persoon zien, maar in werkelijkheid werd deze door beide uitgewerkt en/of gereviewd op hetzelfde moment.

### Stories
/
_(enkel voor stories die speciale aandacht vergen)_

### Quality
We moeten voor de uitgewerkte US nog test classes maken om zo de kwaliteit te garanderen. Verder moeten we nog kijken of de gebruikte attributen allemaal vereist of dat er nog andere moeten worden toegevoegd. 

We hebben al zo goed mogelijk geprobeerd om rekening te houden met de design patterns.

### Vragen

1) We hebben geprobeerd Docker te gebruiken voor de database. Dit is gelukt voor API Clients. Maar hoe kunnen we dit op dezelfde port gebruiken voor alle 3 de API's?
2) Hoe kunnen we query'en op de database zonder code (SQL PostGres)? Momenteel is het moeilijk te zien hoe de database is opgebouwd en wat er reeds in zit (zonder API calls)


## Post coaching

### Feedback

_(in te vullen na gesprek)_

# V2

## Pre coaching

### Geschatte Progress (in procent): XX%

### Status

_Waar sta je globaal? Wat loopt goed en minder goed? Hoe verloopt de samenwerking? Wie heeft globaal welke delen van de applicatie uitgewerkt? (details progress kunnen we bekijken op het issue board)_

### Stories

_(enkel voor stories die speciale aandacht vergen)_

### Quality

_Acties (refactorings,...) die nog gepland staan om de kwaliteit van je project te verhogen (maak hiervoor issue(s) aan!): [issue nummer]: toelichting_

### Vragen

_eventuele vragen voor je coach_

## Post coaching

### Feedback

Eerst en vooral moeten we meer gebruik maken van de GitLab functionaliteiten. Zo kunnen we bijvoorbeeld een extra tag/ lane toevoegen voor de issues die in progress zijn en nog getest moeten worden.

Over het algemeen was er nog niet veel feedback doordat onze opdracht nog niet ver gevorderd was. Echter de delen die al gecodeerd zijn, vond de docent goed en volgens de juiste principes. Om toch zeker te zijn van onze code, mogen we de komende weken nog de vraag stellen om door de code te gaan. Verder werd ons ook aangeraden om niet teveel tijd te verliezen met Docker, en eerder te focussen op de andere functionaliteiten.

#### In detail:  

**Clients**
- clients applicatie accountdto error handling: voor de @Valid annotatie dient er geen extra controle te gebeuren op de bindingresult (if functie).
- ProductService: illegal argument exception. Hierbij beter een custom exception maken en in de controller opvangen
- OrderController: annotatie @Controller => @RestController
    - RestController aangezien het een REST API betreft. Controller wordt gebruikt bij bv. MVC waar je met een frontend werkt (zoals in de Bakery API)
- Bij OrderService benaming lijkt op Dotnet benaming. Juiste semantiek toepassen. 

**Bakery**
- ProductController: annotatie @RestController => @Controller  

**Warehouse**  
- Geen templates folder nodig
- @Valid notatie (zie clients opmerking)