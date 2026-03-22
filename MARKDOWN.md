# Restorani Reserveerimissüsteem - CGI 2026 proovitöö  (Estonian)

## Eeltingimused käivitamiseks

- Java 25 SDK
- Node v24

## Käivitamine

> NB! Vaata, eks midagi muud samal ajal nendel portidel ei töötaks, muidu see ei lähe tööle.

```bash
git clone https://github.com/Rbr4t/CGI-proov
cd CGI-proov
```

## Docker 

```bash
sudo docker-compose up --build
```

## Otse kaustast

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm i
npm run dev
```

Kasutajaliides asub aadressil: <a>localhost:5000</a><br>
Päringuid tehakse vastu porti **8080**<br>
Andmebaasi sisu saab näha: http://localhost:8080/h2-console

```bash
JDBC URL: jdbc:h2:mem:restaurant
Username: sa
Password: (tühi)
```
-> `CONNECT` <br>

Koodijupid, millega näeb andmebaasi sisu:

```SQL
SELECT * FROM reservation; -- kõik reserveeringud
SELECT * FROM restaurant_tables; -- kõik lauad restoranis
```

## Arhitektuur

Projekt on liigitatud 2 kausta
- backend - kogu serveri loogika
    - `controller/` - REST API endpointid
    - `service/` - Reserveerimise ja soovitamise loogika
    - `repo/` - Andmebaasi päringute liidesed
    - `model/` - Andmebaaside mudelid (Table & Reservation)
    - lisaks on andmebaasi populeerimiseks **DataLoader.java**
- frontend - kogu visuaalne pool ja kliendi poolne loogika
    - `components/` - Sisaldab eraldi funktsionaalsusi
    - `lib/` - sisaldab API suhtlemiseks vajalikke funktsioone ja TypeScripti tüüpide klasse
    - `assets/` - Projektiga kaasa tulnud meedia (favicon ja logod)



### Tehnoloogiad
- **Backend:** Java 25, Spring Boot, H2 (andmebaas)
- **Frontend:** Svelte 5, TypeScript, Vite

## Funktsionaalsused

- saab **broneerida** lauda sobivale peale klikkides
- rakendus **soovitab** laudu ette antud kriteeriumite järgi 
- saab **minna ajas edasi/tagasi** filtrite all oleva vidina abil
- implementeeritud on lähedal olevate **laudade liitmine**, kuid see töötab ainult 2 lauaga ja väga suurte gruppidega see ei tööta
- rakendus genereerib esmasel käivitamisel juhuslikud broneeringud
- **lihtne** ja loogiline disain


### Soovitusalgoritm

Algoritm töötab kahes etapis:
1. **Üksiku laua soovitus**
Kõigepealt filtreeritakse välja lauad mis ei sobi - laud peab olema vaba valitud ajal, mahutama seltskonna ja vastama valitud tsoonile. Seejärel arvutatakse igale sobivale lauale skoor:

- Suuruse sobivus: mida vähem tühje toole jääb, seda kõrgem skoor.

> 2-liikmelist seltskonda ei paigutata 8-kohalisele lauale kui 2-kohaline on vaba

- Eelistused: kui laud vastab kliendi eelistusele (aknakoht, vaikne nurk, ligipääsetav), lisatakse skoorile punkte

> Lauad tagastatakse skoori järgi sorteerituna: parim soovitus on esimene. Kasutajaliides näitab kõiki laudu, mis ta arvab et on sobilik kliendile.

2. Laudade paarisoovitus
Kui ükski üksik laud ei mahuta seltskonda, otsib algoritm kahte kõrvuti asuvat lauda. "Kõrvuti" tähendab et laudade vahemaa on alla 250 ühiku. 
> Parim paar valitakse kahe kriteeriumi järgi: lauad on üksteisele võimalikult lähedal ja kokku jääb võimalikult vähe tühje toole.

## Ajakulu ja töö käik

> Mainin ära, et **kasutasin projekti koostamisel Claude.ai koodiagenti**. Ma ei teinud copy-paste, vaid lugesin ise ka läbi genereeritud koodi, parandasin vajadusel. <br>

> Otsustasin seda teed minna aja kokkuhoiu põhjustel. Mul on varem kogemust veebirakenduste loomisel ja kõike ise tehes kulub palju aega. Lisaks polnud ma varem Claude-i proovinud ja tahtsin teada, mis see hype on.

>Lasin tal genereerida osa kasutajaliidesest (stiil, elemendid, mingil määral loogika), server side jaoks küsisin talt küsimusi, kuid koodi kirjutasin ikkagi mõtlesin välja enamusti ise.

Projektiga alustasin 15. märts. Hakkasin pihta **Spring Booti** põgusa õppimisega, vaatasin paar videot ja käisin üle ka selle ametliku dokumentatsiooni. Tegin ühe näidisprojekti läbi, et saada natukene rohkem kogemust Java Spring Bootiga ja H2 andmebaasiga. Projekti üles seadistamine, lõin vastavad andmebaasi mudelid, liidesed, DataLoader *dummy* andmete jaoks.
> ajakulu umbes 6 tundi

Jätkasin 16. märts - koostasin Reserveeringute ja Soovitamise jaoks loogika. Koostasin REST API endpointid. POST ja GET päringute jaoks. Testisin manuaalselt funktsionaalsusi kasutades **Postmani**.<br>
Hakkasin pihta frontendi loomisega. Otsustasin **Svelte** kasuks, kuna varem seda kasutanud ja meeldib rohkem kui React. Projektis otsustasin kasutada **TypeScripti**, kuna eelmise proovitöö jaoks juba kasutasin seda ja tahtsin rohkem kogemust. Build ja dev tööriistaks valisin **Vite**. Koostasin kasutajaliidese laudade nägemise jaoks, filtreerimise jaoks ja otsustasin ka teha vidina ajas edasi/tagasi liikumise jaoks - minu arust oli see kõige loogilisem ja lihtsam viis kliendil laudasi sirvida. Projekt oli suuresti funktsionaalne ja panin veidikeseks ajaks pausile.
> ajakulu umbes 9 tundi

Vahepeal jäi vahe sisse - tegelesin kooliga

Jätk 22. märts - ühtlustasin stiili. Implementeerisin juurde ka laudade liitmise kuni 2 laua jaoks korraga. Mitmed bug fixid. Aja sünkroniseerimine filtri ja `TimeController`-i vahel. README kirjutamine.
> ajakulu umbes 5 tundi

## Mis oli keeruline
Projekt ei valmistanud suuri raskusi, kuna ma olen varem sarnaseid rakendusi(vt GitHubi teisi projekte). Ainult kuna Java on mulle suhteliselt uus keel, siis süntaks ja lahenduse implementeerimine võttis kõige rohkem aega. Mured said lahenduse koodiagendiga ja tavalise veebiotsingutega (koodiagent oli märgatavalt suure koormuse all paar korda arendamise jooksul). <br>

Ma tahtsin teha juurde ka admin lehe ja teha disain ilusaks, aga lõpuks ikkagi polnud aega seda juurde panna.

Kokkuvõtlikult, oli päris lõbus projekt. Väga palju oleks olnud, mida oleks saanud edasi veel arendada. 

Autor: Robert Koor
