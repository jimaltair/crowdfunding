<!DOCTYPE html>
<#import "spring.ftl" as spring/>
<head>
    <meta charset="UTF-8">
    <title>Создание проекта</title>
    <style>
        .error {
            color: red;
        }
    </style>
</head>
<body>
<h1>Создайте новый проект</h1>
<h2>Пожалуйста, введи необходимые данные:</h2>
<form action="/projects/create" method="post" enctype="multipart/form-data">
    <label for="title">Введите название проекта:</label>
    <@spring.formInput 'projectForm.title' 'id="title" placeholder="Название"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <label for="description">Введите описание проекта:</label>
    <@spring.formInput 'projectForm.description' 'id="description" placeholder="Описание"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <label for="moneyGoal">Введите необходимую сумму денег для сбора:</label>
    <@spring.formInput 'projectForm.moneyGoal' 'id="moneyGoal" placeholder="Цель"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <label for="finishDate">Введите дату завершения проекта:</label>
    <@spring.formInput 'projectForm.finishDate' 'id="finishDate" placeholder="Дата"' 'date'/>
    <@spring.showErrors '<br>' 'error' />
    <#if dateMustBeFutureError??>
        <br>
        <span class="error">Дата должна быть из будущего</span>
    </#if>
    <input id="clientId" type="hidden"  name="clientId">
    <h3>Пожалуйста, загрузите изображение для проекта:</h3>
    <input type="file" name="file">
    <#if imageProcessingError??>
        <br>
        <span class="error">Не удалось обработать изображение</span>
    </#if>
    <br>
    <input type="submit" value="Создать">
</form>
</body>
</html>