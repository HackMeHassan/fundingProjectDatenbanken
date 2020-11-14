<html>
<head>
    <title>Nutzer Profil</title>
</head>

<body>

<h1>Profil von&nbsp;${kontoinhab}</h1>
<br/>
<div>
        <li>Benutzername:${kontoinhabname}</li>
        <li>Anzahl erstellter Projekte:${anzahlerst}</li>
        <li>Anzahl unterstüzter Projekte:${anzahlunte}</li>
</div>
<br/>
<h1>*****************************</h1>
<h1>Erstellte Projekte</h1>
<br/>
<div>
    <#list aufprojekte as projekt>
        <li>Titel:<a href="/view_project?kennung=${projekt.kennung}">${projekt.titel}</a></li>
        <li>Aktuell:${projekt.finanzierungslimit}</li>
            <li>Status:${projekt.status}</li>
        <li>***********************************</li>
    </#list>
</div>
<br/>
<h1>*****************************</h1>
<br/>
<h1>Unterstüzte Projekte</h1>
<br/>
<div>
    <#list zuprojekte as projekt>
        <li>Titel:<a href="/view_project?kennung=${projekt.projektKennung}">${projekt.projektTitel}</a></li>
        <li>Limit:${projekt.limit}&nbsp;EUR</li>
        <li>Status:${projekt.statu}</li>
            <li>Gespendet:${projekt.gespendetBetrag}&nbsp;EUR</li>
        <li>***********************************</li>
    </#list>
</div>
<a href="/view_main"><button>Zurück zur Hauptseite</button></a>
</body>
</html>