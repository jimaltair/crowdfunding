<#import "spring.ftl" as spring/>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>profile page</title>
</head>
<link rel="stylesheet" href="/bootstrap/css/bootstrap.min.css"/>
<link rel="stylesheet" href="/bootstrap/css/font-awesome.min.css"/>
<script src="/bootstrap/js/jquery-1.11.3.min.js"></script>
<script src="/bootstrap/js/bootstrap.min.js"></script>

<br><br><br>

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

<div class="container">
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
                            <img src="/img/avatar_vasiliy.jpg">
                            <h3>${clientDto.firstName} ${clientDto.lastName}</h3>
                            <p>${clientDto.about}</p>
                            <p class="sosmed-author">
                                <a href="#"><i class="fa fa-facebook" title="Facebook"></i></a>
                                <a href="#"><i class="fa fa-twitter" title="Twitter"></i></a>
                                <a href="#"><i class="fa fa-google-plus" title="Google Plus"></i></a>
                                <a href="#"><i class="fa fa-linkedin" title="Linkedin"></i></a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-8 col-xs-12 col-lg-8">
                <div class="panel">
                    <div class="panel-body">
                        <ul id="myTab" class="nav nav-pills">
                            <li class="active"><a href="#detail" data-toggle="tab">О пользователе</a></li>
                            <li class=""><a href="#own_projects" data-toggle="tab">Свои проекты</a></li>
                            <li class=""><a href="#sponsored_projects" data-toggle="tab">Поддержанные проекты</a></li>
                            <li class=""><a href="#account" data-toggle="tab">Кошелёк</a></li>
                            <li class=""><a href="#change_profile" data-toggle="tab">Изменить данные</a></li>
                        </ul>
                        <div id="myTabContent" class="tab-content">
                            <hr>
                            <div class="tab-pane fade active in" id="detail">
<#----------------------->
                                <h4>Данные профиля</h4>
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
                                        <td class="active">Запасная почта:</td>
                                        <td>${clientDto.secondEmail}</td>
                                    </tr>
                                    <tr>
                                        <td class="active">Поле с данными:</td>
                                        <td>данные пользователя</td>
                                    </tr>
                                    <tr>
                                        <td class="active">Поле с данными:</td>
                                        <td>данные пользователя</td>
                                    </tr>
                                    <tr>
                                        <td class="active">Кошелёк:</td>
                                        <td>300 p</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <#----------------------------------->
                            <div class="tab-pane fade" id="own_projects">
                                <h4>Типа свои проекты </h4>
                                <table class="table table-th-block">
                                    <tbody>
                                </table>
                            </div>
                            <div class="tab-pane fade" id="sponsored_projects">
                                <h4>Поддержанные проекты</h4>
                            </div>
                            <div class="tab-pane fade" id="account">
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <td>Баланс</td>
                                        <td class="active">300 ₽</td>
                                    </tr>
                                    </thead>
                                </table>
                                <form role="form" class="form-inline">
                                    <div class="form-group">
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
                                <form role="form" class="form-horizontal">
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Имя</label>
                                        <div class="col-sm-10">
                                            <input name="first_name" type="text" class="form-control"
                                                   placeholder="имя"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Фамилия</label>
                                        <div class="col-sm-10">
                                            <input name="last_name" type="text" class="form-control"
                                                   placeholder="фамилия"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Страна</label>
                                        <div class="col-sm-10">
                                            <input name="country" type="text" class="form-control"
                                                   placeholder="страна"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Город</label>
                                        <div class="col-sm-10">
                                            <input name="city" type="text" class="form-control"
                                                   placeholder="город"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Почта</label>
                                        <div class="col-sm-10">
                                            <input name="email" type="email" class="form-control" placeholder="почта"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Запасная почта</label>
                                        <div class="col-sm-10">
                                            <input name="reserve_email" type="email" class="form-control"
                                                   placeholder="запасная почта"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-sm-offset-2 col-sm-10">
                                            <input type="submit" class="btn btn-info" value="Сохранить"/>
                                        </div>
                                    </div>
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
</html>