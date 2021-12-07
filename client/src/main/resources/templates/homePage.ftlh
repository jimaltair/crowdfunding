<!DOCTYPE html>
<#import "spring.ftl" as spring/>
<head>
    <meta charset="UTF-8">
    <title>КРАУДФАНДИНГ ДЛЯ ДИЗАЙНЕРОВ ВЕБ СТРАНИЦ </title>
</head>
<body>
<h1>СПИСОК ПРОЕКТОВ КОТОРЫЕ CONFIRMED</h1>
<table>
    <tr>
        <#list ListProject as projects>
        <p>Название: ${projects.getTitle()}
            Описание: ${projects.getDescription()}
            МОНЕЙ ГОЛ: ${projects.getMoneyGoal()}
            </#list>
    </tr>

</table>
</body>
</html>