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