<!DOCTYPE html>
<#import "spring.ftl" as spring/>
<head>
    <meta charset="UTF-8">
    <title>Изменение данных проекта</title>
</head>
<#--<script>-->
<#--    const element = document.getElementById('finishDate');-->
<#--    element.valueAsNumber = Date.now()-(new Date()).getTimezoneOffset()*60000;-->
<#--<script/>-->
<body>
<h1>Изменение данных проекта</h1>
<h2>Пожалуйста, введите новые данные:</h2>
<form action="/projects/update/${id}" method="post" enctype="multipart/form-data">
    <label for="title">Новое название проекта:</label>
    <input id="title" name="title" value=${project_title}>
    <br>
    <label for="description">Новое описание проекта:</label>
    <input id="description" name="description" value=${project_description}>
    <br>
    <label for="moneyGoal">Введите необходимую сумму денег для сбора:</label>
    <input id="moneyGoal" name="moneyGoal" value=${project_money_goal}>
    <br>
    <label for="finishDate">Новая дата завершения проекта:</label>
    <input id="finishDate" type="date"  name="finishDate" value="2021-12-31">
    <h3>Пожалуйста, загрузите изображение для проекта:</h3>
    <input type="file" name="file">
    <br>
    <input type="submit" value="Сохранить">
</form>
</body>
</html>