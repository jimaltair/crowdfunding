<!DOCTYPE html>
<#import "spring.ftl" as spring/>
<head>
    <meta charset="UTF-8">
    <title>Изменение данных проекта</title>
    <style>
        .error {
            color: red;
        }
    </style>
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
    <@spring.formInput 'projectForm.title' 'id="title"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <label for="description">Новое описание проекта:</label>
    <@spring.formInput 'projectForm.description' 'id="description"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <label for="moneyGoal">Введите необходимую сумму денег для сбора:</label>
    <@spring.formInput 'projectForm.moneyGoal' 'id="moneyGoal"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <label for="finishDate">Новая дата завершения проекта:</label>
<<<<<<<<< Temporary merge branch 1
<<<<<<<<< Temporary merge branch 1
    <input id="finishDate" type="date"  name="finishDate" value=${finish_date}>
>>>>>>> origin/dev
=========
    <input id="finishDate" type="date"  name="finishDate" value="${finish_date}">
>>>>>>>>> Temporary merge branch 2
=========
    <@spring.formInput 'projectForm.finishDate' 'id="finishDate"' 'date'/>
    <@spring.showErrors '<br>' 'error' />
    <#if dateMustBeFutureError??>
        <br>
        <span class="error">Дата должна быть из будущего</span>
    </#if>
>>>>>>>>> Temporary merge branch 2
    <h3>Пожалуйста, загрузите изображение для проекта:</h3>
    <input type="file" name="file">
    <#if imageProcessingError??>
        <br>
        <span class="error">Не удалось обработать изображение</span>
    </#if>
    <br>
    <input type="submit" value="Сохранить">
</form>
</body>
<<<<<<< HEAD
</html>
=======
</html>
>>>>>>> origin/dev
