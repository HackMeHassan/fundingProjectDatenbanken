<html>
<head>
    <title>Hauptseite</title>
</head>

<body>
<h1>ProjektFunder</h1>
<a href="/view_profile?email=${meinprof}"><button>Mein Profil</button></a>
<a href="/search"><button>suchen</button></a>
<a href="/"><button>neu loggen</button></a>
<h1>Offene Projekte</h1>
<br/>
<div>
    <#list aufprojekte as projekt>
            <li>Titel:<a href="/view_project?kennung=${projekt.kennung}">${projekt.titel}</a></li>
        <li>Von:<a href="/view_profile?email=${projekt.ersteller}">${projekt.ersteller}</a></li>
        <li>Aktuell:&nbsp;${projekt.finanzierungslimit}&nbsp;EUR</li>


            <li>***********************************</li>
    </#list>
</div>
<br/>
<h1>*****************************</h1>
<br/>
<h1>Abgeschlossene Projekte</h1>
<br/>
<div>
    <#list zuprojekte as projekt>
        <li>Titel:<a href="/view_project?kennung=${projekt.kennung}">${projekt.titel}</a></li>
        <li>Von:<a href="/view_profile?email=${projekt.ersteller}">${projekt.ersteller}</a></li>
        <li>Aktuell:&nbsp;${projekt.finanzierungslimit}&nbsp;EUR</li>


        <li>***********************************</li>
    </#list>
</div>
<br/>
<a href="/new_project"><button>Projekt erstellen</button></a>
</body>
</html>