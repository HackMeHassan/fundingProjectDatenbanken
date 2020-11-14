<html>
<head><title>Projekt Kommentar</title>

<body>
<#list projekte as projekt>
    <h2>${projekt.titel}</h2>
</#list>

<form name="user" action="new_comment?kennung=${tashere}" method="post">
    <div>
    <h3>${error}</h3><br/>
            <input type="text" name="explanation"/><br/>
        <input type="radio" name="version" value="Anonym"/>Anonym kommentieren?<br/>
    <input type="submit" value="Kommentar hinzufügen" /><br/>
    </div>
</form>
<a href="/view_main"><button>Zurück zur Hauptseite</button></a>
</body>
</html>