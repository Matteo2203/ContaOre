# ContaOre

App Android in Java per tenere a portata di mano gli orari di lavoro settimanali e le ore totali di ogni settimana, con un'interfaccia in stile pixel/videogame.

Progetto personale nato come esercizio di sviluppo Android: gli orari arrivano ogni settimana via chat sotto forma di foto, e l'app li raccoglie in un posto solo, in ordine, insieme alle ore lavorate.

> Stato: in sviluppo.

## Screenshot

<img src="screenshots/lista.png" alt="Lista delle settimane" width="260"> <img src="screenshots/aggiungi.png" alt="Aggiunta di una settimana" width="260">

<!-- Quando hai lo screenshot della foto a schermo intero, salvalo come screenshots/foto.png e togli i marcatori di commento qui sotto.

<img src="screenshots/foto.png" alt="Foto a schermo intero" width="260">

-->

## Funzioni

- **Aggiunta di una settimana**: scegli il lunedì con un selettore di date, la foto dell'orario dalla galleria e le ore totali.
- **Lista ordinata**: le settimane compaiono dalla più recente alla più vecchia, con l'etichetta del tipo "1ª settimana di OTTOBRE" e le ore.
- **Foto a schermo intero**: toccando una riga si apre la foto dell'orario, con zoom a due dita.
- **Eliminazione con conferma**: pressione lunga su una riga, poi conferma nel dialog.
- **Controlli sugli input**: l'app non salva se mancano data, foto o ore, oppure se le ore non sono un numero. Il selettore accetta solo i lunedì.
- **Stile pixel**: font Press Start 2P, palette retro, bottoni squadrati e schede con bordo.

## Tecnologie

- Java
- Android Studio, minSdk 26
- Room (SQLite) per il salvataggio locale
- RecyclerView con Adapter personalizzato
- Tre schermate: lista (`MainActivity`), aggiunta (`AddEntryActivity`), foto a schermo intero (`FotoActivity`)
- [PhotoView](https://github.com/Baseflow/PhotoView) per lo zoom della foto

## Come funziona

- La foto scelta dalla galleria viene copiata nella memoria interna dell'app, così non dipende dai permessi della galleria.
- La data è salvata nel formato `anno-mese-giorno` (per esempio `2026-10-05`), che permette di ordinare le settimane con una semplice query.
- Il salvataggio e la lettura del database avvengono in thread separati dal thread principale.

## Come provarla

1. Clona il repository.
2. Apri il progetto in Android Studio e attendi la sincronizzazione di Gradle.
3. Avvia un emulatore (o collega un telefono) e premi Run.

L'app funziona solo in locale: non usa internet e nessun dato lascia il dispositivo.

## Limiti noti e idee future

- Le settimane a cavallo di due mesi non vengono spezzate: le ore contano per intero per il mese del lunedì. Questo può non coincidere con il conteggio di una busta paga, che va dal primo all'ultimo giorno del mese.
- Manca ancora il riepilogo mensile delle ore.
- Quando elimini una settimana, il file della foto resta nella memoria dell'app.
- Testi dell'interfaccia scritti direttamente nei layout, da spostare in `strings.xml`.
