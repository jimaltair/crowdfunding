<!DOCTYPE html>
<#import "spring.ftl" as spring/>
<head>
    <meta charset="UTF-8">
    <title>Create Project</title>
</head>
<body>
<h1>Create your project</h1>
<h2>Please enter your data:</h2>
<form action="/projects/create" method="post">
    <label for="title">Enter title of your project</label>
    <input id="title" name="title" placeholder="Title">
    <br>
    <label for="description">Enter description of your project:</label>
    <input id="description" name="description" placeholder="Description">
    <br>
    <input type="file" name="image">
    <input type="submit" value="Create">
</form>
</body>
</html>