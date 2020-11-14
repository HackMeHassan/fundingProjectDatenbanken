<html>
<head><title>Projekt Suche</title>

<body>
<form name="user" action="search" method="post">
    Titel: <input type="text" name="projektname" /> <br/>
    <input type="submit" value="Suche" />
</form>
<br/>
<h1>***************************</h1>
<br/>
<h1>Suchergebnisse</h1>
<br/>
<div>${error}</div>
<br/>
<div>
    <#list aufprojekte as projekt>
        <li>Titel:<a href="/view_project?kennung=${projekt.kennung}">${projekt.titel}</a></li>
        <li>Von:<a href="/view_profile?email=${projekt.ersteller}">${projekt.ersteller}</a></li>
        <li>Aktuell:${projekt.finanzierungslimit}</li>
        <li>Status:${projekt.status}</li>
        <li>***********************************</li>
    </#list>
</div>
<br/>
<a href="/view_main"><button>Zurück zur Hauptseite</button></a>
<br/>
</body>
</html>