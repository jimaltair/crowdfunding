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
    <input id="title" name="title" value="${project_title}">
    <@spring.formInput 'projectForm.title' 'id="title"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <label for="description">Новое описание проекта:</label>
    <input id="description" name="description" value="${project_description}">
    <@spring.formInput 'projectForm.description' 'id="description"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <label for="moneyGoal">Введите необходимую сумму денег для сбора:</label>
    <input id="moneyGoal" name="moneyGoal" value="${project_money_goal}">
    <@spring.formInput 'projectForm.moneyGoal' 'id="moneyGoal"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <label for="finishDate">Новая дата завершения проекта:</label>
    <input id="finishDate" type="date"  name="finishDate" value="${finish_date}">
    <@spring.formInput 'projectForm.finishDate' 'id="finishDate"' 'date'/>
    <@spring.showErrors '<br>' 'error' />
    <#if dateMustBeFutureError??>
        <br>
        <span class="error">Дата должна быть из будущего</span>
    </#if>
    <h3>Пожалуйста, загрузите изображение для проекта:</h3>
    <input type="file" name="file">
    <#if imageProcessingError??>
        <br>
        <span class="error">Не удалось обработать изображение</span>
    </#if>
    <br>
    <input type="submit" value="Сохранить">
</form>