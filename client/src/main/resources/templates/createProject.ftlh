<!DOCTYPE html>
<#import "spring.ftl" as spring/>
<head>
    <meta charset="UTF-8">
    <title>Создание проекта</title>
</head>
<body>
<h1>Создайте новый проект</h1>
<h2>Пожалуйста, введи необходимые данные:</h2>
<form action="/projects/create" method="post" enctype="multipart/form-data">
    <label for="title">Введите название проекта:</label>
    <input id="title" name="title" placeholder="Название">
    <br>
    <label for="description">Введите описание проекта:</label>
    <input id="description" name="description" placeholder="Описание">
    <br>
    <label for="moneyGoal">Введите необходимую сумму денег для сбора:</label>
    <input id="moneyGoal" name="moneyGoal" placeholder="Цель">
    <br>
    <label for="finishDate">Введите дату завершения проекта:</label>
    <input id="finishDate" type="datetime-local"  name="finishDate" placeholder="Дата">
    <input id="clientId" type="hidden"  name="clientId">
    <h3>Пожалуйста, загрузите изображение для проекта:</h3>
    <input type="file" name="file">
    <br>
    <input type="submit" value="Создать">
</form>
</body>
</html>