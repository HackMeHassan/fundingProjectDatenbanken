<html>
<head><title>Projekt Spende</title>

<body>
<#list projekte as projekt>
<h2>${projekt.titel}</h2>
</#list>
<form name="user" action="new_project_fund?kennung=${tashere}" method="post">
    <div>
        <h3>${error}</h3><br/>
    Spendenbetrag (EUR): <input type="text" name="spendenbetrag" /><br/>

    <input type="radio" name="version" value="Anonym"/>Anonym spenden?<br/>
    <input type="submit" value="Spenden" />
    </div>
</form>
<a href="/view_main"><button>Zurück zur Hauptseite</button></a>
</body>
</html>