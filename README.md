<h2>Albacross Task</h2>
<h3> English version </h3>

solution was distributed into two branches: (master and extended-solution)
- master branch includes only mandatory scope of solution,
- extended-solution branch includes two points from optional scope,  
- on extended-solution branch we have possibility to test how data generator works without relational db 


first step is to clone repository
invoking from terminal:
>git clone https://github.com/kglinski/albacross.git <PATH_TO__FOLDER>

mandatory scope:
>
> git checkout master

optional scope:
>
> git checkout extended-solution

data generator without postgres-db
> (being on extended-solution branch)
>
>git checkout 45c2f07  


In order to start the application you need to open it in developer environment (Intellij) then open Main.scala class, click using right mouse button inside class' body and select RUN 

<h4> branch extended solution: </h4>

In order to achieve expected results on extended-solution branch (optional scope) it's needed to create local PostgreSql database as described below 

- PostgreSql installation 
> https://www.postgresqltutorial.com/install-postgresql-linux/


- create User `postgres` with passsword `postgres`

> CREATE USER postgres WITH PASSWORD 'postgres';
> https://kb.objectrocket.com/postgresql/how-to-use-the-postgres-create-user-command-1449

* it's recommended to install pgAdmin 3 (or higher) also

- create local database with name `ab-cross` on default port `5432`
> expected path to database should look like below:
>
>jdbc:postgresql://localhost:5432/ab-cross

- run db script (resources/db-schema/database.sql) using pgAdmin

ATTENTION:
- once program finishes you need to delete entities from db
> DELETE * FROM ip_scope

----

<h2>Albacross Task</h2>
<h3>Wersja Polska</h3>

rozwiazanie zostalo podzielone na dwa branche: (master oraz extended-solution)  
- na branchu master mamy tylko obowiazkowy zakres zadania,  
- na branchu extended-solution mamy zrealizowane dwa punkty z zakresu opcjonalnego  
- na branchu extended-solution rowniez mamy mozliwosc cofniecia sie do zakresu zadania z generatorem (bez obslugi bazy relacyjnej)

pierwszym krokiem, ktory nalezy wykonac jest sklonowanie repozytorium
wywolujac z terminala:
>git clone https://github.com/kglinski/albacross.git <SCIEZKA_DO_FOLDERU>

obowiazkowy zakres zadania:
>
> git checkout master

opcjonalney zakres zadania:
>
> git checkout extended-solution

obsluga samego generatora bez bazy danych
> (bedac na branchu extended-solution)
>
>git checkout 45c2f07  


----

Aby uruchomic aplikacje nalezy otworzyc ja w srodowisku developerskim Intellij przejsc do klasy Main nastepnie po kliknieciu prawym przyciskiem myszy wewnatrz ciala tej klasy wybrac RUN 

<h4> branch extended solution: </h4>

W celu uzyskania oczekiwanego dzialania na branchu extended-solution (zakres dodatkowy) nalezy zbudowac lokalna baze danych postgres w podanych ponizej krokach:

- instalacja postgreSql 
> https://www.postgresqltutorial.com/install-postgresql-linux/

- utworzenie usera postgres, o hasle postgres z terminala
> CREATE USER postgres WITH PASSWORD 'postgres';
> https://kb.objectrocket.com/postgresql/how-to-use-the-postgres-create-user-command-1449

* najlepiej zainstalowac takze pgAdmin w wersji 3 lub wyzszej

- utworzenie lokalnej bazy danych o nazwie `ab-cross` na domyslnym porcie `5432`
> docelowo nasza sciezka do bazy danych powinna wygladac w ten sposob:
>jdbc:postgresql://localhost:5432/ab-cross

- wykonaj bazodanowy skrypt (resources/db-schema/database.sql) uzywajac pgAdmin

UWAGA:
- gdy program ukonczy dzialanie usun encje z bazy danych
> DELETE * FROM ip_scope

