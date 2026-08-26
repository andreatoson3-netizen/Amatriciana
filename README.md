CLASSI MODIFICATE DA UN DESIGN CLASS MODEL ALL'ALTRO 

[ ] Stats:Toltooperative come variabile(per seguire il design class model), aggiunto il metodo multiply

[ ] Tick:classe eliminata(nel design class model non c'era), inoltre in CitYStatecambiato Tick currTick in int currTick, appunto per evitare di dover fare altra classe

[ ] CityState: indicato currTick come int currTick, e messo una nuova variabile private Grid grid per avere il metodo getter utile a Jackson

[ ] City: implementata con i metodi del design class model ed aggiunto il metodo getCityState per restituire lo stato della città

[ ] GameController:la vecchia CityMajor, tutti i metodi implementati, aggiunto saveGame e getCity. Se ok, bisogna modificare il design class model

[ ] Grid: ok, sistemato problema di getCell, dove nell'if c'era "if(x<=0 &&..." al posto di "if(x>=0 &&...."

[ ] Cell:sistemata con aggiunta di un nuovo metodo isOperative, come nel design class model e rinominata da Block a Cell

[ ] Building:sistemata, cambiato di nuovo Block e Cell

[ ] Infrastructure:ok, sistemata come Building 

[ ] Residential:tolta variabile operative, sistemata e adattata alla nuova Building e al nuovo design class model

[ ] Commercial:come Residential

[ ] Factory come Residential

[ ] PowerPlant come sopra

[ ] Park come sopra

[ ] Road come sopra

[ ] CellFactory: classe totalmente nuova e aggiunta nel codice 

[ ] CityPersistenceManager: implementata con i suoi metodi del design class model

[ ] CityPolicyStrategy ok, modificata

[ ] EnvironmentalTax ok, scritta per la prima volta e coerente con la sua interfaccia CityPolicyStrategy

[ ] IndustrialExpansion ok, scritta per la prima volta e coerente con la sua interfaccia 

[ ] CityObserver ok, scritta per l prima volta sotto forma di interfaccia 

[ ] DashboardView nel punto in cui il programma è avviato nel main o GameController bidogna creare un istanza della dashboard e aggiungerla come osservatore allo stato della città, quindi come es. DashboardView dashboard = new DashBoardView(); e poi gameController.getCity().getCityState().addOberver(dashboard)



Io uso intellij come ide 
