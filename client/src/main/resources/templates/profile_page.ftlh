<#import "spring.ftl" as spring/>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Профиль</title>
    <style>
        .error {
            color: red;
        }
    </style>
</head>
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
<link rel="stylesheet" href="/bootstrap/css/bootstrap.min.css"/>
<link rel="stylesheet" href="/bootstrap/css/font-awesome.min.css"/>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>

<style>
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

<div class="wrapper fadeInDown">
    <div id="main">
        <div class="row" id="real-estates-detail">
            <div class="col-lg-4 col-md-4 col-xs-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <header class="panel-title">
                            <div class="text-center">
                                <strong>Пользователь сайта</strong>
                            </div>
                        </header>
                    </div>
                    <div class="panel-body">
                        <div class="text-center" id="author">
                            <#if clientDto.image??>
                                <img src="/clients/image/${clientDto.image.id}" width="200" height="200"/>
                            </#if>
                            <h3>${clientDto.firstName} ${clientDto.lastName}</h3>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-8 col-xs-12 col-lg-8">
                <div class="panel">
                    <div class="panel-body">
                        <ul id="myTab" class="nav nav-pills">
                            <li class=""><a href="#detail" data-toggle="tab">О пользователе</a></li>
                            <li class=""><a href="#own_projects" data-toggle="tab">Свои проекты</a></li>
                            <li class=""><a href="#account" data-toggle="tab">Кошелёк</a></li>
                            <li class=""><a href="#change_profile" data-toggle="tab">Изменить данные</a></li>
                        </ul>
                        <div id="myTabContent" class="tab-content">
                            <hr>
                            <div class="tab-pane fade active in" id="detail">
                                <h4>Данные профиля:</h4>
                                <table class="table table-th-block">
                                    <tbody>
                                    <tr>
                                        <td class="active">Имя:</td>
                                        <td>${clientDto.firstName}</td>
                                    </tr>
                                    <tr>
                                        <td class="active">Фамилия:</td>
                                        <td>${clientDto.lastName}</td>
                                    </tr>
                                    <tr>
                                        <td class="active">Страна:</td>
                                        <td>${clientDto.country}</td>
                                    </tr>
                                    <tr>
                                        <td class="active">Город:</td>
                                        <td>${clientDto.city}</td>
                                    </tr>
                                    <tr>
                                        <td class="active">Почта:</td>
                                        <td>${clientDto.email}</td>
                                    </tr>
                                    <tr>
                                        <td class="active">Кошелёк:</td>
                                        <td>${clientDto.sumAccount}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <#----------------------------------->
                            <div class="tab-pane fade" id="own_projects">
                                <button type="button" onclick="location.href='/projects/create'" class="btn btn-success">
                                    Создать новый проект
                                </button>
                                <h4>Данные по своим проектам: </h4>
                                <table class="table table-th-block">
                                    <tbody>
                                    <tr>
                                        <td>Название</td>
                                        <td>Цель</td>
                                        <td>Собрано</td>
                                        <td>Количество участников</td>
                                        <td>Статус проекта</td>
                                    </tr>
                                    <#list projectDtos as project>
                                        <tr>
                                            <td> <a href="../projects/${project.id}">${project.title}</a></td>
                                            <td>${project.moneyGoal}</td>
                                            <td>${project.moneyCollected}</td>
                                            <td>${project.contributorsCount}</td>
                                            <td>${project.status}</td>
                                        </tr>
                                    </#list>
                                    </tbody>
                                </table>
                            </div>
                            <div class="tab-pane fade" id="account">
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <td>Баланс</td>
                                        <td class="active">${clientDto.sumAccount}</td>
                                    </tr>
                                    </thead>
                                </table>
                                <form role="form" class="form-inline" action="/operation/top_up" method="post" enctype="multipart/form-data">
                                    <div class="form-group">
                                        <input name="client_id" type="hidden" class="form-control" value="${clientDto.id}"/>
                                        <label class="control-label col-sm-4">Пополнить баланс</label>
                                        <input name="sum" type="text" class="form-control" placeholder="введите сумму"/>
                                    </div>
                                    <div class="form-group">
                                        <input type="submit" class="btn btn-info" value="Пополнить"/>
                                    </div>
                                </form>
                            </div>
                            <br>
                            <div class="tab-pane fade" id="change_profile">
                                <form role="form" class="form-horizontal" action="/clients/${clientDto.id}" method="post" enctype="multipart/form-data">
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Имя</label>
                                        <div class="col-sm-10">
                                            <@spring.formInput 'clientForm.firstName' 'id="firstName" class="form-control"'/>
                                            <@spring.showErrors '<br>' 'error' />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Фамилия</label>
                                        <div class="col-sm-10">
                                            <@spring.formInput 'clientForm.lastName' 'id="lastName" class="form-control"'/>
                                            <@spring.showErrors '<br>' 'error' />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Страна</label>
                                        <div class="col-sm-10">
                                            <@spring.formInput 'clientForm.country' 'id="country" class="form-control"'/>
                                            <@spring.showErrors '<br>' 'error' />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Город</label>
                                        <div class="col-sm-10">
                                            <@spring.formInput 'clientForm.city' 'id="city" class="form-control"'/>
                                            <@spring.showErrors '<br>' 'error' />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Почта</label>
                                        <div class="col-sm-10">
                                            <td>${clientDto.email}</td>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Фото профиля:</label>
                                        <input type="file" name="file">
                                        <#if imageProcessingError??>
                                            <br>
                                            <span class="error">Не удалось обработать изображение</span>
                                        </#if>
                                        <div class="form-group">
                                            <div class="col-sm-offset-2 col-sm-10">
                                                <br>
                                                <input type="submit" class="btn btn-info" value="Сохранить"/>
                                            </div>
                                        </div>
                                    </div>
<#--                                </form>-->
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div><!-- /.main -->
</div><!-- /.container -->
<body>
<script>
    $(document).ready(function(){
        <#if isUpdating??>
            $('.nav-pills a[href="#change_profile"]').tab('show');
        </#if>
    });
</script>
</body>
</html>