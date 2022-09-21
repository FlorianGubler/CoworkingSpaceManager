# M223 Coworkingspace Manager

API documentation: http://localhost:8080/swagger-ui/index.html  
H2 Console: http://localhost:8080/h2-console/  
GitHub Repo: https://github.com/FlorianGubler/m223

<h3>Wichtige Pfäde:</h3>  
- docs/ (Planung & Diagramme)
- http-requests/ (Requests - Keys müssen jede 24h erneuert werden in Env File) 
- src/main/java/ressources/data.sql (Testdaten SQL - Wird beim Start automatisch ausgeführt)
- Tests unter src/test/java/

<h3>Anleitung Applikationsstart</h3>
1. Projekt als Maven Project importieren & Dependencys laden
2. Main Klasse starten: /src/main/java/com.github.floriangubler.coworkspacemgr.CoworkspaceManagerApplication

<h3>Infos</h3>
- Die bereits existierenden Benutzer (z.B. Admin) sind im data.sql File mit den Passwörtern zu finden.
- Rest-Endpoints sind in der Planung unter docs/3_Schnittstellenplanung.docx zu finden
- Die Datenbank ist im File database.h2.mv.db / database.h2.trac.db persistent gespeichert. (Werden beim Start automatisch erstellt)
