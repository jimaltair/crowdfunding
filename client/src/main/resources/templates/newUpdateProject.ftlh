<#import "spring.ftl" as spring/>
<!doctype html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <title>Изменение данных проекта</title>
</head>
<body>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
        integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/bootstrap/css/font-awesome.min.css"/>
<link rel="stylesheet" href="/bootstrap/css/bootstrap.2.min.css"/>

<style>
    .error {
        color: red;
    }
    body {
        background-image: url("/img/bg-1.png");
    }

    #main {
        background-color: #f2f2f2;
        padding: 20px;
        -webkit-border-radius: 4px;
        -moz-border-radius: 4px;
        -ms-border-radius: 4px;
        -o-border-radius: 4px;
        border-radius: 4px;
        border-bottom: 4px solid #ddd;
    }

    #real-estates-detail #author img {
        -webkit-border-radius: 100%;
        -moz-border-radius: 100%;
        -ms-border-radius: 100%;
        -o-border-radius: 100%;
        border-radius: 100%;
        border: 5px solid #ecf0f1;
        margin-bottom: 10px;
    }

    #real-estates-detail .sosmed-author i.fa {
        width: 30px;
        height: 30px;
        border: 2px solid #bdc3c7;
        color: #bdc3c7;
        padding-top: 6px;
        margin-top: 10px;
    }

    .panel-default .panel-heading {
        background-color: #fff;
    }

    #real-estates-detail .slides li img {
        height: 450px;
    }
</style>

<!-- Navigation bar -->
<nav class="navbar navbar-dark bg-dark navbar-static-top navbar-expand-sm">
    <!-- Links -->
    <ul class="navbar-nav mr-auto">
        <li class="nav-item"><a class="nav-link" href="/">Главная</a></li>
        <li class="nav-item"><a class="nav-link" href="/clients">Профиль</a></li>
        <li class="nav-item"><a class="nav-link" href="/logout">Выход</a></li>
    </ul>
</nav>

<!-- Container body -->
<div class="wrapper fadeInDown">
    <div id="formContent">
        <br>
        <div>
            <strong>Введите новые данные::</strong>
        </div>
        <!-- Login Form -->
        <form action="/projects/update/${id}" method="post" enctype="multipart/form-data">
            <@spring.formInput 'projectForm.title' 'id="title"'/>
            <br>
            <@spring.showErrors '<br>' 'error' />
            <@spring.formInput 'projectForm.description' 'id="description"'/>
            <br>
            <@spring.showErrors '<br>' 'error' />
            <@spring.formHiddenInput 'projectForm.moneyGoal'  'id="moneyGoal" '/>
            <br>
            <@spring.showErrors '<br>' 'error' />
            <@spring.formInput 'projectForm.finishDate' 'id="finishDate"' 'date'/>
            <br>
            <@spring.showErrors '<br>' 'error' />
            <#if dateMustBeFutureError??>
                <br>
                <span class="error">Дата должна быть из будущего</span>
            </#if>
            <input id="clientId" type="hidden"  name="clientId">
            <div>
                <strong>Загрузите изображение для проекта:</strong>
            </div>
            <input type="file" name="file">
            <#if imageProcessingError??>
                <br>
                <span class="error">Не удалось обработать изображение</span>
            </#if>
            <br>
            <input type="submit" class="fadeIn fourth" value="Сохранить">
        </form>
    </div>
</div>
</body>
</html>
