<html>
<head><title>Hello World</title>
 
<body>
  <form name="user" action="hello" method="post">
    Firstname: <input type="text" name="firstname" /> <br/>
    Lastname: <input type="text" name="lastname" /> <br/>
    Email: <input type="text" name="email" /> <br/>
    Explanation: <input type="text" name="explanation" /> <br/>
    <input type="submit" value="Save" />
  </form>
 
  <table class="datatable">
    <tr>
        <th>Firstname</th>  <th>Lastname</th>
      <th>Email-Address</th>
      <th>Some Explanation</th>
    </tr>
    <#list users as user>
    <tr>
        <td>${user.firstname}</td> <td>${user.lastname}</td>
      <td>${user.email}</td>
      <td>${user.explanation}</td>
    </tr>
    </#list>
  </table>
<a href="www.google.com"><button type="button" name="jetz">GOOGLE</button></a>
</body>
</html>