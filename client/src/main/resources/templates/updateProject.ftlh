<!DOCTYPE html>
<#import "spring.ftl" as spring/>
<head>
    <meta charset="UTF-8">
    <title>Изменение данных проекта</title>
</head>
<body>
<h1>Изменение данных проекта</h1>
<h2>Пожалуйста, введите новые данные:</h2>
<<<<<<< HEAD
<form action="/projects/update" method="post" enctype="multipart/form-data">
    <label for="title">Новое название проекта:</label>
    <input id="title" name="title" placeholder="Название">
    <br>
    <label for="description">Новое описание проекта:</label>
    <input id="description" name="description" placeholder="Описание">
    <br>
    <label for="moneyGoal">Введите необходимую сумму денег для сбора:</label>
    <input id="moneyGoal" name="moneyGoal" placeholder="Цель">
    <br>
    <label for="finishDate">Новая дата завершения проекта:</label>
    <input id="finishDate" type="datetime-local"  name="finishDate" placeholder="Дата">
=======
<form action="/projects/update/${id}" method="post" enctype="multipart/form-data">
    <label for="title">Новое название проекта:</label>
    <input id="title" name="title" value="${project_title}">
    <br>
    <label for="description">Новое описание проекта:</label>
    <input id="description" name="description" value="${project_description}">
    <br>
    <label for="moneyGoal">Введите необходимую сумму денег для сбора:</label>
    <input id="moneyGoal" name="moneyGoal" value="${project_money_goal}">
    <br>
    <label for="finishDate">Новая дата завершения проекта:</label>
<<<<<<<<< Temporary merge branch 1
    <input id="finishDate" type="date"  name="finishDate" value=${finish_date}>
>>>>>>> origin/dev
=========
    <input id="finishDate" type="date"  name="finishDate" value="${finish_date}">
>>>>>>>>> Temporary merge branch 2
    <h3>Пожалуйста, загрузите изображение для проекта:</h3>
    <input type="file" name="file">
    <br>
    <input type="submit" value="Сохранить">
</form>
</body>
<<<<<<< HEAD
</html>
=======
</html>
>>>>>>> origin/dev
