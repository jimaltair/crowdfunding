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
    <@spring.formInput 'projectForm.finishDate' 'id="finishDate"' 'date'/>
    <@spring.showErrors '<br>' 'error' />
    <h3>Пожалуйста, загрузите изображение для проекта:</h3>
    <input type="file" name="file">
    <br>
    <input type="submit" value="Сохранить">
</form>
</body>
</html>