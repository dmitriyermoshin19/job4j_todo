<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
            integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
            integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
            crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
            integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
            crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <title>Задачи</title>
    <style>
        h6 {
            position: absolute;
            right: 400px;
            top: 200px;
        }

        #user {
            position: absolute;
            right: 400px;
            top: 10px;
        }
    </style>
    <script>
        function table(data) {
            let table = $('#table')
            data.forEach(el => {
                table.append('<tr>')
                table.append('<td>' + el.user.name + '</td>')
                table.append('<td>' + el.desc + '</td>')
                table.append('<td>' + el.created + '</td>')
                let result = '';
                let categories = el.categories;
                for (let i = 0; i < categories.length; i++) {
                    result += categories[i]['name'] + "\n";
                }
                table.append('<td>' + result + '</td>')
                if (el.done === false) {
                    table.append('<td>Надо выполнить' +
                        '<div class="row float-right" style="margin-right: 20px">' +
                        '<button type="reset" class="btn btn-primary" onclick="doTask(' + el.id + ')">Выполнить</button></div> </td>');
                } else if (el.done === true) {
                    table.append('<td>Выполнено</td>')
                }
                table.append('</tr>');
            })
        }
    </script>
</head>

<body>
<script>
    function getAllTasks() {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/TODO_list/showTasks',
            dataType: 'json'
        }).done(function (data) {
            table(data)
        });
    }
    window.onload = getAllTasks;

    function getActiveTasks() {
        $.ajax({
            type: 'Get',
            url: 'http://localhost:8080/TODO_list/activeTasks',
            dataType: 'json'
        }).done(function (data) {
            $('#table').html("")
            table(data)
        });
    }

    $(function checkbox() {
        $("#select").on("click", function () {
            if ($(this).is(":checked")) {
                $('#table').html("")
                getActiveTasks()
            } else {
                $('#table').html("")
                getAllTasks()
            }
        })
    });

    function checkFlag() {
        if ($('input:checkbox[name=check]').is(':checked')) {
            $('#table').html("")
            getActiveTasks();
        } else {
            $('#table').html("")
            getAllTasks()
        }
    }

    function addNewTask(text, ids) {
        if (text !== '') {
            $.ajax({
                type: 'Post',
                url: 'http://localhost:8080/TODO_list/addTask',
                data: {text: text, cIds : ids}
            }).done(function () {
                checkFlag()
                return true;
            });
        } else {
            alert("Пожалуйста, заполниете поле \'Новая задача\'")
            return false;
        }
    }

    function doTask(taskId) {
        $.ajax({
            type: 'Post',
            url: 'http://localhost:8080/TODO_list/doTask',
            data: {id: taskId}
        }).done(function () {
            checkFlag()
        });
    }

    $(document).ready(function () {
        $.ajax({
            type: 'Get',
            url: 'http://localhost:8080/TODO_list/currentUser',
            dataType: 'json'
        }).done(function (data) {
            $('#u').replaceWith('<div>' + data.name + ' | Выйти</div>')
        })
    })

    $(document).ready(function () {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/TODO_list/addTask",
            dataType: "json",
            success: function (data) {
                let categories = "";
                for (let i = 0; i < data.length; i++) {
                    categories += "<option value=" + data[i]['id'] + ">" + data[i]['name'] + "</option>";
                }
                $('#cIds').html(categories);
            }
        })
    })
</script>
<div class="row" id="user">
    <ul class="nav">
        <li class="nav-item">
            <a class="nav-link" href="index.jsp">
                <div class="container">
                    <div id="u" class="topright"></div>
                </div>
            </a>
        </li>
    </ul>
</div>
<div class="container">
    <div class="row pt-3">
        <table class="table table-bordered" style="background-color: azure">
            <thead>
            <tr>
                <h4>
                    Новая задача
                </h4>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>
                    <form>
                        <input type="text" id="text" style="width: 600px" placeholder="Введите описание..."/>
                        <button type="reset" class="btn btn-success" onclick="return addNewTask($('#text').val(), $('#cIds').val())">
                            Добавить задачу
                        </button>
                        <div class="col-sm-5">
                            <label for="cIds" style="font-weight: bold">Category</label>
                            <select class="custom-select" id="cIds" multiple></select>
                        </div>
                    </form>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

<div class="container">
    <div class="row pt-5">
        <h2>
            Список задач
        </h2>
        <h6>
            <form>
                только активные задачи <input type="checkbox" name="check" id="select"/>
            </form>
        </h6>

        <table class="table table-bordered" style="background-color: #f0ffff">
            <thead>
            <tr>
                <th>Автор</th>
                <th style="width: 300px">Задача</th>
                <th>Дата создания</th>
                <th>Категория</th>
                <th>Процесс выполнения</th>
            </tr>
            </thead>
            <tbody id="table"></tbody>
        </table>
    </div>
</div>
</body>
</html>