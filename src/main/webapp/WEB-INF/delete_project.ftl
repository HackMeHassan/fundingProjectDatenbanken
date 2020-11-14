<html>
<head><title>Projekt Löschen</title>

<body>
<div><h1>${error}</h1></div>
<br/>

<br/>
<form name="user" action="delete_project?kennung=${tashere}" method="post">

    Löschen?<br/>
    <input type="radio" name="group" value="yes"/>YES<br/>
    <input type="radio" name="group" value="no"/>NO<br/>
    <input type="submit" value="antworten"/>
</form>
<a href="/view_main"><button>Zurück zur Hauptseite</button></a>
</body>
</html>